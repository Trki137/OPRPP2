package hr.fer.zemris.java.scripting.exec;

import hr.fer.zemris.java.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;
public class ObjectMultistackTest {
    private ObjectMultistack multistack;

    @BeforeEach
    public void setup(){
        this.multistack = new ObjectMultistack();

        this.multistack.push("key1", new ValueWrapper(10));
        this.multistack.push("key1", new ValueWrapper(11));
        this.multistack.push("key1", new ValueWrapper(10.123));
        this.multistack.push("key2", new ValueWrapper("1"));
        this.multistack.push("key3", new ValueWrapper("4"));
        this.multistack.push("key3", new ValueWrapper("1.6E1"));
        this.multistack.push("key2", new ValueWrapper(null));
        this.multistack.push("key4", new ValueWrapper(-3));
    }

    @Test
    public void testIsEmptyTrue(){
        assertTrue(multistack.isEmpty("key5"));
    }
    @Test
    public void testIsEmptyFalse(){
        assertTrue(multistack.isEmpty("key5"));
    }

    @Test
    public void testPeekThrows(){
        assertThrows(EmptyStackException.class, () -> multistack.peek("key5"));
    }

    @Test
    public void testPopThrows(){
        assertThrows(EmptyStackException.class, () -> multistack.pop("key5"));
    }

    @Test
    public void testPeek(){
        assertEquals(10.123, multistack.peek("key1").getValue());
        assertNull(multistack.peek("key2").getValue());
        assertEquals("1.6E1", multistack.peek("key3").getValue());
        assertEquals(-3, multistack.peek("key4").getValue());
    }

    @Test
    public void testPop(){
        assertEquals(10.123,multistack.pop("key1").getValue());
        assertEquals(11,multistack.pop("key1").getValue());
        assertEquals(10,multistack.pop("key1").getValue());
        assertTrue(multistack.isEmpty("key1"));
        assertThrows(EmptyStackException.class, () -> multistack.pop("key1"));
    }

    @Test
    public void testPush(){
        multistack.push("key1", new ValueWrapper(23));
        assertEquals(23, multistack.peek("key1").getValue());

        multistack.push("key5", new ValueWrapper(12));
        assertEquals(12, multistack.peek("key5").getValue());

    }
}
