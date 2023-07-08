package oprpp2.hw01.server;

import oprpp2.hw01.message.*;
import oprpp2.hw01.util.MessageUtil;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * This class represent server for our communication app
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class Main {

    /**
     * Starting buffer size
     */
    private static int BUFFER_SIZE = 4096;

    /**
     * Starting user id to assign
     */
    private static long UID;

    /**
     * Map of all users and its connections
     */
    private static final Map<Long, ServerTask> clientMap = new HashMap<>();
    /**
     * Map of all users with keys
     */
    private static final Map<Long, Long> userMap = new HashMap<>();

    /**
     * Starting point of out application
     * @param args expects 1 argument, port number
     * @throws SocketException
     * @throws IllegalArgumentException if args lenght is different from 1
     */
    public static void main(String[] args) throws SocketException {
        if (args.length != 1) {
            throw new IllegalArgumentException(String.format(
                    "Expected one argument but got %d, expected PORT number", args.length
            ));
        }
        UID = new Random().nextLong();

        int port = Integer.parseInt(args[0]);
        DatagramSocket dSocket = new DatagramSocket(null);
        dSocket.bind(new InetSocketAddress((InetAddress) null, port));

        System.out.println("Server open at port: "+ port);

        while (true) {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(
                    buffer, buffer.length
            );

            try {
                dSocket.receive(datagramPacket);
            } catch (IOException e) {
                continue;
            }

            if (datagramPacket.getLength() > BUFFER_SIZE) {
                System.out.println("Too much data sent. I am expanding my buffer 2 times..");
                BUFFER_SIZE *= 2;
                continue;
            }

            Message message = MessageUtil.convertToMessage(Arrays.copyOfRange(buffer, 0, datagramPacket.getLength()));
            if (Objects.isNull(message)) {
                System.out.println("Invalid message received.");
                continue;
            }


            switch (message.getMessageCode()) {
                case HELLO -> createNewThread(message, datagramPacket, dSocket);
                case BYE -> terminateThread(message);
                case OUTMSG -> sendResponse(message, datagramPacket, dSocket);
                case ACK -> storeAck((AckMessage) message);
                default -> {
                }
            }

        }

    }

    /**
     * Creates new thread for communication with client or send ack message again if client already registered
     * @param message instance of {@link HelloMessage}
     * @param packet instance of {@link DatagramPacket}
     * @param dSocket instance of {@link DatagramSocket}
     */
    private static void createNewThread(Message message, DatagramPacket packet, DatagramSocket dSocket) {
        HelloMessage helloMessage = (HelloMessage) message;

        System.out.printf("Paket je: Hello(%d,%s,%d)\n",helloMessage.getMessageId(),helloMessage.getName(),helloMessage.getKey());

        if (userMap.containsKey(((HelloMessage) message).getKey())) {
            sendAck(helloMessage, packet, dSocket);
            return;
        }else sendAck(helloMessage,packet,dSocket);


        ServerTask serverTask = new ServerTask(UID, helloMessage,dSocket,packet.getPort(),packet.getAddress(),helloMessage.getKey());

        userMap.put(helloMessage.getKey(),UID);
        clientMap.put(UID++,serverTask);

        Thread thread = new Thread(serverTask);
        thread.start();
    }

    /**
     * Send again ack message, if first ack message was lost in transmission,  for Hello message.
     * @param message instance of {@link Message}
     * @param recvPacket instance of {@link DatagramPacket}
     * @param dSocket instance of {@link DatagramSocket}
     */
    private static void sendAck(Message message, DatagramPacket recvPacket, DatagramSocket dSocket) {
        Long uid;

        if(message instanceof HelloMessage) uid = userMap.getOrDefault(((HelloMessage) message).getKey(),UID);
        else uid = ((OutMessage) message).getUID();


        AckMessage ackMessage = new AckMessage(message.getMessageId(), uid);
        byte[] ackMessageByte = MessageUtil.convertToMessageFormat(ackMessage);

        if (Objects.isNull(ackMessageByte)) {
            System.out.println("Invalid Hello packet");
            return;
        }

        DatagramPacket packet = new DatagramPacket(
                ackMessageByte, ackMessageByte.length
        );

        packet.setSocketAddress(new InetSocketAddress(recvPacket.getAddress(), recvPacket.getPort()));


        while (true) {
            try {
                dSocket.send(packet);
                System.out.println("Sent ACK message: "+ ackMessage);
                break;
            } catch (IOException e) {
                continue;
            }
        }

    }

    private static void terminateThread(Message message) {
        ByeMessage byeMessage = (ByeMessage) message;

        System.out.printf("Paket je: Bye(%d,%d)\n",byeMessage.getMessageId(),byeMessage.getUID());

        ServerTask serverTask = clientMap.get(byeMessage.getUID());
        serverTask.addToQueue(byeMessage);

        clientMap.remove(userMap.get(serverTask.getKey()));

    }

    /**
     * When server gets instance of {@link OutMessage} he calls this method that will send messages to all clients
     * @param message instance of {@link OutMessage} message to be sent to all clients
     */
    private static void sendResponse(Message message,DatagramPacket packet, DatagramSocket dSocket) {
        OutMessage outMessage = (OutMessage) message;
        System.out.printf("Paket je: OutMessage(%d,uid=%d, text='%s')\n",outMessage.getMessageId(),outMessage.getUID(), outMessage.getMessage());
        sendAck(message,packet,dSocket);

        String name = clientMap.get(outMessage.getUID()).getName();
        for(var client : clientMap.values()){
            client.sendMessage(outMessage, name);
        }

     }

    /**
     * Stores ack messages
     * @param ackMessage instance of {@link AckMessage}
     */

    private static void storeAck(AckMessage ackMessage) {
        System.out.printf("Paket je: Ack(%d,uid=%d)\n",ackMessage.getMessageId(),ackMessage.getUID());

        ServerTask serverTask = clientMap.get(ackMessage.getUID());
        serverTask.storeAckMessage(ackMessage);
    }
}
