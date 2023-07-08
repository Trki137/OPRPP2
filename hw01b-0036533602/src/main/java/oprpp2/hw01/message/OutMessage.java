package oprpp2.hw01.message;

import oprpp2.hw01.enums.MessageType;
/**
 * This class represent message that has been sent from client to server
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class OutMessage extends Message{
    /**
     * id of user that sent message
     */
    private final long UID;

    /**
     * Message that user sent
     */
    private final String message;

    /**
     * Creates new instance of {@code OutMessage}
     * @param messageId id of message
     * @param message message that client sent
     * @param UID user id of client that sent message
     */
    public OutMessage(long messageId,long UID,String message) {
        super(MessageType.OUTMSG, messageId);
        this.UID = UID;
        this.message = message;
    }

    public long getUID() {
        return UID;
    }

    public String getMessage() {
        return message;
    }
}
