package oprpp2.hw01.message;

import oprpp2.hw01.enums.MessageType;

/**
 * This class represent message for acknowledging that other message has been received
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class AckMessage extends Message{
    /**
     * user id of person who sent message
     */
    private final long UID;

    /**
     * Constructor for creating instance of {@code AckMessage}
     * @param messageId id of message
     * @param UID user id
     */
    public AckMessage(long messageId, long UID) {
        super(MessageType.ACK, messageId);
        this.UID = UID;
    }

    /**
     * Getter for user id
     * @return user id
     */
    public long getUID() {
        return UID;
    }

    @Override
    public String toString() {
        return String.format("Message type: %s, Message id: %d, UID: %d",MessageType.ACK,getMessageId(),getUID());
    }
}
