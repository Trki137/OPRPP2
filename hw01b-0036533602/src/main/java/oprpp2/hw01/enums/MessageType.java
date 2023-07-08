package oprpp2.hw01.enums;

/**
 * This class represent message types used in communication
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public enum MessageType {
    /**
     * Represent first message in communication, used for establishing connection
     */
    HELLO,
    /**
     * Represents that message has been received
     */
    ACK,
    /**
     * Represents end of communication
     */
    BYE,
    /**
     * Represent message that client sent to server
     */
    OUTMSG,
    /**
     * Represent message that server sent to client
     */
    INMSG
}
