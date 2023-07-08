package hr.fer.zemris.java.math;

import java.util.Arrays;
import java.util.Objects;

/**
 * The <code>ComplexPolynomial</code> class models a complex polynomial in the form of f(z)=zn*z^n+zn-1*z^n-1+...+z2*z^2+z1*z+z0.
 * z0 to zn represent coefficients alongside their respective powers of z.
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class ComplexPolynomial {
  /**
   * Array of complex coefficients
   */
  private final Complex[] factors;

  /**
   * Creates an instance of <code>ComplexPolynomial</code>
   *
   * @param factors array of complex coefficients
   * @throws NullPointerException if <code>factors</code> is null or any of coefficient is null
   */
  public ComplexPolynomial(Complex ...factors){
    Objects.requireNonNull(factors, "The given array can't be null");
    Arrays.stream(factors).forEach(factor -> Objects.requireNonNull(factor, "Complex root can't be null"));

    this.factors = Arrays.copyOf(factors,factors.length);
  }

  /**
   * Returns the order of the current polynomial.
   *
   * @return the order of the current polynomial.
   */
  public short order(){
    return (short)(factors.length - 1);
  }

  /**
   * Multiples current <code>ComplexPolynomial</code> with given <code>p</code> <code>ComplexPolynomial</code>
   *
   * @param p other <code>ComplexPolynomial</code> instance
   * @return new <code>ComplexPolynomial</code> that is the result of the expression
   * @throws NullPointerException if <code>p</code> is null
   */

  public ComplexPolynomial multiply(ComplexPolynomial p){
    Objects.requireNonNull(p, "Polynomial can't be null");

    Complex[] newFactors = new Complex[this.order() + p.order() + 1];

    for(int i = 0; i < p.factors.length; i++){
      for(int j = 0; j < this.factors.length; j++){

        Complex multiplyResult  = this.factors[j].multiply(p.factors[i]);

        if(newFactors[i+j] == null) newFactors[i+j] = multiplyResult;
        else newFactors[i+j] = newFactors[i+j].add(multiplyResult);

      }
    }

    return new ComplexPolynomial(newFactors);
  }

  /**
   * Derives current <code>ComplexPolynomial</code>
   *
   * @return new <code>ComplexPolynomial</code> that is the result of the expression
   */
  public ComplexPolynomial derive(){
    if(this.factors.length == 1) return new ComplexPolynomial(Complex.ZERO);

    Complex[] newFactors = new Complex[factors.length - 1];

    for(int i = 1; i < factors.length; i++){
      newFactors[i - 1] = factors[i].multiply(new Complex(i,0));
    }

    return new ComplexPolynomial(newFactors);
  }

  /**
   * Calculates current <code>ComplexPolynomial</code> with given <code>z</code>
   *
   * @param z instance of <code>ComplexPolynomial</code>
   * @return new <code>ComplexPolynomial</code> that is the result of the expression
   * @throws NullPointerException if <code>z</code> is null
   */
  public Complex apply(Complex z){
    Objects.requireNonNull(z,"Value can't be null");

    Complex result = null;

    for(int i = 0; i < factors.length; i++){
      Complex polynomValue = z.power(i);
      Complex multiplyValue = polynomValue.multiply(factors[i]);
      if(i == 0) {
        result = multiplyValue;
        continue;
      }

      result = result.add(multiplyValue);

    }

    return result;
  }

  public Complex[] getFactors() {
    return factors;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for(int i = factors.length - 1; i >= 0; i--) {
      sb.append("(").append(this.factors[i].toString()).append(")");
      if(i != 0) sb.append("*z^").append(i).append("+");
    }

    return sb.toString();
  }
}
