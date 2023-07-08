package hr.fer.zemris.java.scripting.exec;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * ValueMapper is a wrapper for any type of data.
 * Arithmetic operation are meant for types of Integers, Doubles and Strings that represent either Double or Integer
 *
 * @author Dean Trkulja
 * @version 1.0
 */
public class ValueWrapper {

    /**
     * Value of {@link ValueWrapper}
     */
    private Object value;

    /**
     * A basic constructor that has one argument.
     *
     * @param value any Object, it can be null
     */

    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * Arithmetic operation add.
     * It will sum {@code incValue} and {@code value} and store in {@code value}
     * @param incValue Object - is type of Double,Integer or String but String that can convert to Integer or Double. It also can be null, but then it will be represented by 0.
     * @throws RuntimeException if {@code incValue} is of invalid type
     */
    public void add(Object incValue) {
        calculate(incValue, Double::sum);
    }

    /**
     * Arithmetic operation subtract.
     * It will subtract {@code decValue} and {@code value} and store in {@code value}
     * @param decValue Object - is type of Double,Integer or String but String that can convert to Integer or Double. It also can be null, but then it will be represented by 0.
     * @throws RuntimeException if {@code incValue} is of invalid type
     */

    public void subtract(Object decValue) {
        calculate(decValue, (x, y) -> x - y);
    }

    /**
     * Arithmetic operation multiply.
     * It will multiply {@code mulValue} and {@code value} and store in {@code value}
     * @param mulValue Object - is type of Double,Integer or String but String that can convert to Integer or Double. It also can be null, but then it will be represented by 0.
     * @throws RuntimeException if {@code incValue} is of invalid type
     */

    public void multiply(Object mulValue) {
        calculate(mulValue, (x, y) -> x * y);
    }

    /**
     * Arithmetic operation division.
     * It will divide {@code value} and {@code minValue} and store in {@code value}
     * @param divValue is type of Double,Integer or String but String that can convert to Integer or Double. It also can be null, but then it will be represented by 0.
     * @throws RuntimeException if {@code incValue} is of invalid type
     */
    public void divide(Object divValue) {
        calculate(divValue, (x, y) -> x / y);
    }

    /**
     * Compares {@code value} and {@code withValue}
     * @param withValue Object - is type of Double,Integer or String but String that can convert to Integer or Double. It also can be null, but then it will be represented by 0.
     * @return int - 0 if both are null or if they are same. Less then 0 if {@code value} is smaller than {@code withValue}. Bigger than 0 if {@code value} is bigger than {@code withValue}
     */

    public int numCompare(Object withValue) {
        if (Objects.isNull(value) && Objects.isNull(withValue)) return 0;
        if (withValue instanceof Integer && value instanceof Integer)
            return Integer.compare((Integer) value, (Integer) withValue);
        if (withValue instanceof Double && value instanceof Double)
            return Double.compare((Double) value, (Double) withValue);

        String first = convert(value);
        String second = convert(withValue);

        if ((first.contains(".") || first.toLowerCase().contains("e")) || ((second.contains(".") || second.toLowerCase().contains("e")))) {
            return Double.compare(Double.parseDouble(first), Double.parseDouble(second));
        }
        return Integer.compare(Integer.parseInt(first), Integer.parseInt(second));

    }

    /**
     * Getter for {@code value}
     * @return Object
     */

    public Object getValue() {
        return value;
    }

    /**
     * Setter for {@code value}
     * @param value Object
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if(value instanceof String) return (String)value;
        if(value instanceof Integer) return Integer.toString((Integer)value);
        if(value instanceof Double) return Double.toString((Double) value);
        return value.toString();
    }

    /**
     * Calculates an arithmetic operation and stores result in {@code value}
     * @param secondValue Object - is type of Double,Integer or String but String that can convert to Integer or Double. It also can be null, but then it will be represented by 0.
     * @param function BiFunction<Double,Double,Double> - calculates arithmetic operation on 2 Double type data and returns the result
     * @throws RuntimeException if {@code secondValue} or {@code value} can't be represented as Integer or Double
     **/

    private void calculate(Object secondValue, BiFunction<Double, Double, Double> function) {
        Objects.requireNonNull(function);

        if (Objects.isNull(value) && Objects.isNull(secondValue)) {
            this.value = 0;
            return;
        }
        if (secondValue instanceof Double && value instanceof Double) {
            this.value = function.apply((Double) value, (Double) secondValue);
            return;
        }
        if (secondValue instanceof Integer && value instanceof Integer) {
            Double res = function.apply(Double.valueOf((Integer) value), Double.valueOf((Integer) secondValue));
            this.value = res.intValue();
            return;
        }

        String first = convert(value);
        String second = convert(secondValue);

        if ((first.contains(".") || first.toLowerCase().contains("e")) || ((second.contains(".") || second.toLowerCase().contains("e")))) {
            this.value = function.apply(Double.parseDouble(first), Double.parseDouble(second));
            return;
        }

        Double res = function.apply(Double.valueOf(first), Double.valueOf(second));
        this.value = res.intValue();
    }

    /**
     * Converts Object to a String, Integer or a Double
     * @param value Object that we want to convert
     * @return String - casted from Object
     */
    private String convert(Object value) {
        if (Objects.isNull(value)) return Integer.toString(0);
        else if (value instanceof String) return (String) value;
        else if (value instanceof Integer) return Integer.toString((Integer) value);
        else return Double.toString((Double) value);
    }
}
