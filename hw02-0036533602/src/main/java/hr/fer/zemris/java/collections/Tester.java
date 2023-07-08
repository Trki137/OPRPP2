package hr.fer.zemris.java.collections;

/**
 * Interface for testing some conditions
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public interface Tester {
    /**
     * Method for testing if some value satisfies condition
     *
     * @param obj Object that we test
     * @return boolean if condition is satisfied returns true or else false
     */
    boolean test(Object obj);
}
