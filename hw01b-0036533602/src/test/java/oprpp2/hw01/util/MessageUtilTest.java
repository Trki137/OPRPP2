package oprpp2.hw01.util;

import static org.junit.jupiter.api.Assertions.*;

import oprpp2.hw01.message.*;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class MessageUtilTest {

    @Test
    public void testHelloMessage(){
        HelloMessage helloMessage = new HelloMessage(56,"Dean Trkulja",123412);

        HelloMessage m =(HelloMessage) MessageUtil.convertToMessage(Objects.requireNonNull(MessageUtil.convertToMessageFormat(helloMessage)));


        assertEquals(helloMessage.getName(),m.getName());
        assertEquals(helloMessage.getMessageId(),m.getMessageId());
        assertEquals(helloMessage.getKey(),m.getKey());
    }

    @Test
    public void testByeMessage(){
        ByeMessage byeMessage = new ByeMessage(12314,312312);

        ByeMessage test = (ByeMessage) MessageUtil.convertToMessage(Objects.requireNonNull(MessageUtil.convertToMessageFormat(byeMessage)));

        assertEquals(byeMessage.getMessageCode(),test.getMessageCode());
        assertEquals(byeMessage.getMessageId(),test.getMessageId());
        assertEquals(byeMessage.getUID(),test.getUID());
    }

    @Test
    public void testAckMessage(){
        AckMessage ackMessage = new AckMessage(3124123,3123123);

        AckMessage test = (AckMessage) MessageUtil.convertToMessage(Objects.requireNonNull(MessageUtil.convertToMessageFormat(ackMessage)));

        assertEquals(ackMessage.getMessageCode(),test.getMessageCode());
        assertEquals(ackMessage.getMessageId(),test.getMessageId());
        assertEquals(ackMessage.getUID(),test.getUID());

    }

    @Test
    public void testOutMessage(){
        OutMessage outMessage = new OutMessage(312312, 3123,"Hello there mate");

        OutMessage test = (OutMessage) MessageUtil.convertToMessage(Objects.requireNonNull(MessageUtil.convertToMessageFormat(outMessage)));

        assertEquals(outMessage.getMessageCode(),test.getMessageCode());
        assertEquals(outMessage.getMessageId(),test.getMessageId());
        assertEquals(outMessage.getUID(),test.getUID());
        assertEquals(outMessage.getMessage(), test.getMessage());

    }

    @Test
    public void testInMessage(){
        InMessage inMessage = new InMessage(312312,"Dean Trkulja","Hello there mate");

        InMessage test = (InMessage) MessageUtil.convertToMessage(Objects.requireNonNull(MessageUtil.convertToMessageFormat(inMessage)));

        assertEquals(inMessage.getMessageCode(),test.getMessageCode());
        assertEquals(inMessage.getMessageId(),test.getMessageId());
        assertEquals(inMessage.getName(),test.getName());
        assertEquals(inMessage.getMessage(), test.getMessage());

    }


}
