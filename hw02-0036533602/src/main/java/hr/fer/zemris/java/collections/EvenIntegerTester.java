package hr.fer.zemris.java.collections;

/**
 * Class that implements Tester.
 * This class is only for excersise purpose
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class EvenIntegerTester implements Tester{
    /**
     * Checks if Integer value is even number.
     *
     * @param obj Object that we test
     * @return boolean if condition is satisfied returns true else false
     */
    public boolean test(Object obj) {
        if(!(obj instanceof Integer)) return false;

        Integer i = (Integer)obj;

        return i % 2 == 0;
    }

}
