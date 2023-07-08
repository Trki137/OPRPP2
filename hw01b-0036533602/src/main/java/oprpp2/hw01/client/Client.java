package oprpp2.hw01.client;

import oprpp2.hw01.enums.MessageType;
import oprpp2.hw01.message.*;
import oprpp2.hw01.util.MessageUtil;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class Client {

    /**
     * Maximum size of message that we can receive
     */
    private final int BUFFER_SIZE = 4096;
    /**
     * Size of ack message
     */
    private final int ACK_MESSAGE_SIZE = 18;
    /**
     * Max number of retransmissions
     */
    private final int MAX_RETRANSMISSION = 10;
    /**
     * Current message id
     */
    private final AtomicLong messageId = new AtomicLong(0);
    /**
     * User id for client
     */
    private AtomicLong UID = null;
    /**
     * Client username
     */
    private final String username;
    /**
     * Address of server
     */
    private final String hostName;
    /**
     * Server port
     */
    private final int port;
    /**
     * Socket used for this communication
     */
    private final DatagramSocket dSocket;
    /**
     * Queue for all messages that need to be processed
     */

    private final BlockingQueue<Message> queue;

    /**
     * Last message id that we received
     */
    private long lastInMessageId = -1;

    /**
     * Craetes an instance of {@code Client}
     * @param port server port
     * @param hostname server address
     * @param username client name
     * @throws SocketException
     */
    public Client(int port, String hostname, String username) throws SocketException {
        this.port = port;
        this.hostName = hostname;
        this.username = username;
        this.dSocket = new DatagramSocket();
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Initializes communication with server with Hello message
     * @return true if it was successful, else it returns false
     * @throws UnknownHostException
     * @throws SocketException
     */
    public boolean connectToServer() throws UnknownHostException, SocketException {
        HelloMessage helloMessage = new HelloMessage(messageId.get(), username, new Random().nextLong());

        byte[] helloData = MessageUtil.convertToMessageFormat(helloMessage);

        if (Objects.isNull(helloData)) {
            throw new NullPointerException("Hello data is null");
        }

        DatagramPacket packet = new DatagramPacket(
                helloData, helloData.length
        );

        packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(hostName), port));

        dSocket.setSoTimeout(5000);

        int numOfRetransmission = 0;


        while (numOfRetransmission < MAX_RETRANSMISSION) {
            numOfRetransmission++;

            try {
                dSocket.send(packet);
            } catch (IOException e) {
                continue;
            }

            byte[] recvBuffer = new byte[ACK_MESSAGE_SIZE];
            DatagramPacket recvPacket = new DatagramPacket(
                    recvBuffer, recvBuffer.length
            );

            try {
                dSocket.receive(recvPacket);
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout expired. Retransmission of packet...");
                continue;
            } catch (IOException e) {
                System.out.println("Couldn't send packet. Retransmission of packet...");
                continue;
            }

            Message recvMessage = MessageUtil.convertToMessage(recvBuffer);

            if (!recvMessage.getMessageCode().equals(MessageType.ACK)) {
                System.out.println("Invalid packet. Retransmission of packet...");
                continue;
            }

            AckMessage ackMessage = (AckMessage) recvMessage;

            UID = new AtomicLong(ackMessage.getUID());
            messageId.incrementAndGet();

            System.out.printf("Ack received. UID is %d\n", UID.get());
            return true;
        }


        return false;
    }

    /**
     * Creates a new thread that listens for new messages.
     * If message is an instance of {@link InMessage} then with {@code messageConsumer} we write message to GUI
     * @param messageConsumer instance of {@link Consumer} that will write text on GUI when we get instance of {@link InMessage}
     */
    public void listenForMessages(Consumer<Message> messageConsumer) {
        Thread thread = new Thread(() -> {
            while (true) {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(hostName), port));
                    dSocket.setSoTimeout(0);
                    dSocket.receive(packet);

                    Message recvMessage = MessageUtil.convertToMessage(Arrays.copyOfRange(buffer, 0, packet.getLength()));

                    switch (recvMessage.getMessageCode()) {
                        case ACK -> {
                            queue.add((AckMessage) recvMessage);
                        }
                        case INMSG -> {
                            InMessage inMessage = (InMessage) recvMessage;
                            if(lastInMessageId != inMessage.getMessageId())
                                messageConsumer.accept(inMessage);

                            lastInMessageId = inMessage.getMessageId();

                            AckMessage ackMessage = new AckMessage(inMessage.getMessageId(), UID.get());
                            byte[] data = MessageUtil.convertToMessageFormat(ackMessage);
                            DatagramPacket ackPacket = new DatagramPacket(data, data.length);
                            ackPacket.setSocketAddress(new InetSocketAddress(packet.getAddress(), packet.getPort()));

                            dSocket.send(ackPacket);
                        }
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        });

        thread.start();
    }

    /**
     * Send message to server
     * @param message message that needs to be sent
     * @throws UnknownHostException
     */
    public void sendMessage(String message) throws UnknownHostException {
        byte[] data = MessageUtil.convertToMessageFormat(new OutMessage(messageId.get(),  UID.get(),message));

        int numOfRetransmissions = 0;

        while (numOfRetransmissions < MAX_RETRANSMISSION) {
            numOfRetransmissions++;
            DatagramPacket packet = new DatagramPacket(data, data.length);
            packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(hostName), port));

            try {
                dSocket.send(packet);
            } catch (IOException e) {
                System.out.println("Couldn't send message.");
                continue;
            }

            try {
                Message recvMsg = queue.poll(5, TimeUnit.SECONDS);

                if(Objects.isNull(recvMsg)){
                    System.out.println("Timeout expired....Retransmission");
                    continue;
                }
                if (recvMsg.getMessageCode().equals(MessageType.ACK)){

                    messageId.incrementAndGet();
                    break;
                }else System.out.println("Message not ACK... I am sending again");

            } catch (InterruptedException e) {
                System.out.println("Timeout expired....Retransmission of packet");
                continue;
            }


        }

    }

    /**
     * Disconnects this client from server with bye message and closes socket
     */
    public void disconnectFromServer() {

        int numberOfRetransmissions = 0;
        while (numberOfRetransmissions < MAX_RETRANSMISSION) {

            try {
                numberOfRetransmissions++;
                sendBye();
            } catch (IOException e) {
                continue;
            }

            try{
                Message m = queue.poll(5, TimeUnit.SECONDS);
                if (!Objects.isNull(m) && m.getMessageCode().equals(MessageType.ACK)) break;
            }catch (InterruptedException e){
                continue;
            }


        }

        dSocket.close();
        System.exit(0);
    }

    /**
     * Creates instance of {@link ByeMessage} and sends it to server
     * @throws IOException
     */
    private void sendBye() throws IOException {
        ByeMessage byeMessage = new ByeMessage(messageId.get(),UID.get());

        byte[] byeMessageByte = MessageUtil.convertToMessageFormat(byeMessage);

        if(Objects.isNull(byeMessageByte)){
            return;
        }

        DatagramPacket datagramPacket = new DatagramPacket(byeMessageByte, byeMessageByte.length);
        datagramPacket.setSocketAddress(new InetSocketAddress(InetAddress.getByName(hostName),port));
        System.out.println("Bye message sent");
        dSocket.send(datagramPacket);
    }

}
