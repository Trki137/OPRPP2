package oprpp2.hw01.server;

import oprpp2.hw01.enums.MessageType;
import oprpp2.hw01.message.*;
import oprpp2.hw01.util.MessageUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class represent communication between client and server
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class ServerTask implements Runnable {
    /**
     * Key from user when he initializes connection
     */
    private final Long key;
    /**
     * Messages to be processed
     */
    private final BlockingQueue<Message> queue;
    /**
     * All ack messages
     */

    private final List<AckMessage> ackMessagesList;

    /**
     * User id for this communication
     */
    private final Long UID;

    /**
     * Socket used by server
     */
    private final DatagramSocket dSocket;
    /**
     * Address of client
     */
    private final InetSocketAddress address;
    /**
     * Client name
     */
    private final String name;
    /**
     * Starting id of messages that server sends
     */
    private long messageIdSend = 1L;
    /**
     * Last received message id
     */
    private Long messageIdLastReceived = null;


    /**
     * Creates instance of {@code ServerTask}
     * @param UID user id which who we communicate
     * @param helloMessage instance of {@link HelloMessage}
     * @param dSocket socket used by server
     * @param port port used by client
     * @param address address used by client
     */
    public ServerTask(Long UID, HelloMessage helloMessage, DatagramSocket dSocket, int port, InetAddress address, Long key) {
        this.queue = new LinkedBlockingQueue<>();
        this.ackMessagesList = new LinkedList<>();
        this.UID = UID;
        this.dSocket = dSocket;
        this.address = new InetSocketAddress(address, port);
        this.name = helloMessage.getName();
        this.key = key;

        queue.add(helloMessage);
    }

    @Override
    public void run() {
        while (true) {
            Message m = null;
            try {
                m = queue.take();
            } catch (InterruptedException e) {
                continue;
            }

            if(m.getMessageCode().equals(MessageType.HELLO) ||
                    m.getMessageCode().equals(MessageType.BYE))
                sendAck(m);

            if(m.getMessageCode().equals(MessageType.BYE)) break;
        }
    }

    /**
     * Sends ack message
     * @param message instance of {@link Message}
     */
    private void sendAck(Message message) {
        long messageId = message.getMessageId();
        messageIdLastReceived = messageId;

        AckMessage ackMessage = new AckMessage(messageId, this.UID);
        byte[] buffer = MessageUtil.convertToMessageFormat(ackMessage);

        if (Objects.isNull(buffer)) {
            System.out.println("Invalid message format");
            return;
        }

        DatagramPacket datagramPacket = new DatagramPacket(
                buffer, buffer.length
        );

        datagramPacket.setSocketAddress(address);
        try {
            dSocket.send(datagramPacket);
        } catch (IOException e) {
        }
    }

    /**
     * Send message to a client
     * @param outMessage instance of {@link OutMessage}
     * @param byUser name of client that sent message
     */
    public void sendMessage(OutMessage outMessage,String byUser) {
        //sendAck(outMessage);
        InMessage inMessage = new InMessage(messageIdSend++, byUser, outMessage.getMessage());

        byte[] buffer = MessageUtil.convertToMessageFormat(inMessage);

        if (Objects.isNull(buffer)) {
            System.out.println("Invalid message");
            return;
        }

        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramPacket.setSocketAddress(address);

        try {
            dSocket.send(datagramPacket);
        } catch (IOException e) {
        }

    }

    /**
     * Adds message to {@code queue}
     * @param message instance of {@link Message}
     */
    public void addToQueue(Message message) {
         queue.add(message);
    }

    /**
     * Stores ack message in {@code ackMessagesList}
     * @param ackMessage
     */
    public void storeAckMessage(AckMessage ackMessage){
        ackMessagesList.add(ackMessage);
    }

    /**
     * Getter for name of client
     * @return name of client
     */

    public String getName() {
        return name;
    }

    /**
     * Getter for key
     * @return key from initialization step
     */
    public Long getKey() {
        return key;
    }
}
