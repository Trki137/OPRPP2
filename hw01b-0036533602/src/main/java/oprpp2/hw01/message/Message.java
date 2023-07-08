package oprpp2.hw01.message;

import oprpp2.hw01.enums.MessageType;

/**
 *  Represents base class for messages used in communication
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public abstract class Message {
    /**
     * Type of message
     */
    private final MessageType messageCode;
    /**
     * Message id
     */
    private final long messageId;

    /**
     * Constructor that creates new message
     *
     * @param messageCode type of message
     * @param messageId starting message id
     */

    public Message(MessageType messageCode, long messageId) {
        this.messageCode = messageCode;
        this.messageId = messageId;
    }

    /**
     * Getter for message code
     * @return type of message
     */
    public MessageType getMessageCode() {
        return messageCode;
    }

    /**
     * Getter for message id
     * @return current message id
     */

    public long getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return String.format("Message type: %s,Message id: %d",messageCode,messageId);
    }
}
