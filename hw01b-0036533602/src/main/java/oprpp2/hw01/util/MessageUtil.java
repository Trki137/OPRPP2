package oprpp2.hw01.util;

import oprpp2.hw01.enums.MessageType;
import oprpp2.hw01.message.*;

import java.io.*;

/**
 * Util class for converting type of message and its data to byte array
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class MessageUtil {

    /**
     * Constant byte for HELLO message
     */
    private static final byte HELLO = 1;
    /**
     * Constant byte for ACK message
     */
    private static final byte ACK = 2;
    /**
     * Constant byte for BYE message
     */
    private static final byte BYE = 3;
    /**
     * Constant byte for OUT_MESSAGE message
     */
    private static final byte OUT_MESSAGE = 4;
    /**
     * Constant byte for IN_MESSAGE message
     */
    private static final byte IN_MESSAGE = 5;

    /**
     * Converts byte array to convenient message type
     * @param data byte array
     * @return instance of Message or null if first byte is invalid
     */
    public static Message convertToMessage(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream dos = new DataInputStream(bis);
        try {
            byte type = dos.readByte();
            long messageId = dos.readLong();

            return switch (type) {
                case HELLO -> new HelloMessage(messageId,dos.readUTF(),dos.readLong());
                case ACK -> new AckMessage(messageId,dos.readLong());
                case BYE -> new ByeMessage(messageId,dos.readLong());
                case OUT_MESSAGE -> new OutMessage(messageId,dos.readLong(), dos.readUTF());
                case IN_MESSAGE -> new InMessage(messageId,dos.readUTF(),dos.readUTF());
                default -> null;
            };
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Converts instance of {@link Message} to byte array
     * @param message instance of {@link Message}
     * @return byte array
     */
    public static byte[] convertToMessageFormat(Message message) {
        try {
            return switch (message.getMessageCode()) {
                case HELLO -> getHelloFormat(message);
                case BYE, ACK -> getByeOrAckFormat(message);
                case OUTMSG -> getOutFormat(message);
                case INMSG -> getInFormat(message);
            };
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates byte array for class {@link InMessage}
     * @param message instance of {@link InMessage}
     * @return byte array that represent data of that class
     * @throws IOException
     */
    private static byte[] getInFormat(Message message) throws IOException {
        InMessage inMessage = (InMessage) message;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(IN_MESSAGE);
        dos.writeLong(inMessage.getMessageId());
        dos.writeUTF(inMessage.getName());
        dos.writeUTF(inMessage.getMessage());

        dos.close();
        return bos.toByteArray();
    }

    /**
     * Creates byte array for class {@link OutMessage}
     * @param message instance of {@link OutMessage}
     * @return byte array that represent data of that class
     * @throws IOException
     */
    private static byte[] getOutFormat(Message message) throws IOException {
        OutMessage outMessage = (OutMessage) message;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(OUT_MESSAGE);
        dos.writeLong(outMessage.getMessageId());
        dos.writeLong(outMessage.getUID());
        dos.writeUTF(outMessage.getMessage());

        dos.close();

        return bos.toByteArray();
    }

    /**
     * Creates byte array for class {@link AckMessage} or {@link ByeMessage}
     * @param message instance of {@link AckMessage} or {@link ByeMessage}
     * @return byte array that represent data for those classes
     * @throws IOException
     */
    private static byte[] getByeOrAckFormat(Message message) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(message.getMessageCode().equals(MessageType.BYE) ? BYE : ACK);
        dos.writeLong(message.getMessageId());
        dos.writeLong(message.getMessageCode().equals(MessageType.BYE) ? ((ByeMessage) message).getUID() : ((AckMessage) message).getUID());

        dos.close();

        return bos.toByteArray();
    }

    /**
     * Creates byte array for class {@link HelloMessage}
     * @param message instance of {@link HelloMessage}
     * @return byte array that represent data of that class
     * @throws IOException
     */
    private static byte[] getHelloFormat(Message message) throws IOException {
        HelloMessage helloMessage = (HelloMessage) message;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeByte(HELLO);
        dos.writeLong(helloMessage.getMessageId());
        dos.writeUTF(helloMessage.getName());
        dos.writeLong(helloMessage.getKey());

        dos.close();

        return bos.toByteArray();
    }

}
