package oprpp2.hw01.message;

import oprpp2.hw01.enums.MessageType;
/**
 * This class represent message that is sent from server to every other client
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class InMessage extends Message{
    /**
     * Name of the client that sent original message
     */
    private final String name;
    /**
     * Message that client sent to server
     */
    private final String message;

    /**
     * Creates new instance of {@code InMessage}
     * @param messageId id of message
     * @param name name of client that sent message
     * @param message message that client sent
     */
    public InMessage(long messageId, String name ,String message) {
        super(MessageType.INMSG, messageId);
        this.name = name;
        this.message = message;
    }

    /**
     * Getter for name
     * @return client name that sent message
     */

    public String getName() {
        return name;
    }

    /**
     * Getter for message
     *
     * @return message of client
     */

    public String getMessage() {
        return message;
    }
}
