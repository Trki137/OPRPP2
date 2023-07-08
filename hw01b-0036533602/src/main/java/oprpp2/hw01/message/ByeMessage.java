package oprpp2.hw01.message;

import oprpp2.hw01.enums.MessageType;

/**
 * This class represent message for closing communication with server
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class ByeMessage extends Message{
    /**
     * User id
     */
    private final long UID;

    /**
     * Constructor for creating instance of {@code ByeMessage}
     * @param messageId message id
     * @param UID user id who sent this message
     */
    public ByeMessage(long messageId, long UID) {
        super(MessageType.BYE, messageId);
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
        return String.format("Message type: %s, Message id: %d, UID: %d",MessageType.BYE,getMessageId(),getUID());
    }
}
