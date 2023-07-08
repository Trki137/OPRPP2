package hr.fer.zemris.java.scripting.exec;

import hr.fer.zemris.java.scripting.exec.ValueWrapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ValueMapperTest {

    @Test
    public void test(){
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper(null);
        v1.add(v2.getValue());
        assertEquals(0,v1.getValue());

        ValueWrapper v3 = new ValueWrapper("1.2E1");
        ValueWrapper v4 = new ValueWrapper(1);
        v3.add(v4.getValue());
        assertEquals(13.0, v3.getValue());

        ValueWrapper v5 = new ValueWrapper("12");
        ValueWrapper v6 = new ValueWrapper(1);
        v5.add(v6.getValue());
        assertEquals(13,v5.getValue());

        ValueWrapper v7 = new ValueWrapper("Ankica");
        ValueWrapper v8 = new ValueWrapper(1);
        assertThrows(RuntimeException.class, () -> v7.add(v8.getValue()));
    }

    @Test
    public void test2(){
        ValueWrapper v1 = new ValueWrapper(null);
        ValueWrapper v2 = new ValueWrapper("3.1");
        v1.add(v2.getValue());
        assertEquals(3.1,v1.getValue());

        ValueWrapper v3 = new ValueWrapper("3.2E2");
        v3.add(v2.getValue());
        assertEquals(323.1,v3.getValue());

        ValueWrapper v4 = new ValueWrapper(14.2);
        v2.add(v4.getValue());
        assertEquals(17.3,v2.getValue());

        ValueWrapper v5 = new ValueWrapper(2.1);
        v4.subtract(v5.getValue());
        assertEquals(12.1,v4.getValue());

        ValueWrapper v6 = new ValueWrapper(3);
        v6.multiply(v4.getValue());
        assertEquals(36.3,v6.getValue());

        ValueWrapper v7 = new ValueWrapper(3);
        ValueWrapper v8 = new ValueWrapper(9);
        v8.divide(v7.getValue());
        assertEquals(3,v8.getValue());

        assertEquals(0, v8.numCompare("3"));
        assertNotEquals(0,v8.numCompare(11));
    }

}
