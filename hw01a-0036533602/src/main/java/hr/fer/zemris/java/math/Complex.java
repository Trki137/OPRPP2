package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Class that represent one complex numer
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class Complex {
  /**
   * Real part of complex number
   */
  private final double re;

  /**
   * Imaginary part of complex number
   */
  private final double im;

  /**
   * Constant complex number that is equal to 0
   */

  public static final Complex ZERO = new Complex(0,0);

  /**
   * Constant complex number with real part equal to 1 and imaginary to 0
   */
  public static final Complex ONE = new Complex(1,0);

  /**
   * Constant complex number with real part equal to -1 and imaginary to 0
   */
  public static final Complex ONE_NEG = new Complex(-1,0);

  /**
   * Constant complex number with real part equal to 0 and imaginary to 1
   */
  public static final Complex IM = new Complex(0,1);

  /**
   * Constant complex number with real part equal to 0 and imaginary to -1
   */

  public static final Complex IM_NEG = new Complex(0,-1);

  /**
   * Default constructor, creates complex number with real and imaginary part equal to 0
   */
  public Complex(){
    this(0,0);
  }

  /**
   * Constructor with 2 arguments
   *
   * @param re real part of complex number
   * @param im imaginary part of complex number
   */

  public Complex(double re, double im){
    this.re = re;
    this.im = im;
  }

  /**
   * Parses string <code>input</code> into a new Complex number.
   *
   * @param input String to be parsed
   * @return new <code>Complex</code> that is the result of the parsing <code>input</code>
   *
   * @throws NullPointerException if <code>input</code> is null
   * @throws IllegalArgumentException if <code>input</code> is in invalid format
   */

  public static Complex parse(String input) {
    Objects.requireNonNull(input, "Input can't be null");

    String[] inputParts = input.split(" ");

    if(inputParts.length == 1){
      double value;
      if(input.contains("-i")){
        if(input.length() == 2) return Complex.IM_NEG;
        value = -Double.parseDouble(input.substring(2));

      } else if(input.contains("i")){
        if(input.length() == 1) return Complex.IM;

        value = Double.parseDouble(input.substring(1));
      }else{
        value = Double.parseDouble(input);
      }

      if(value == 0) return Complex.ZERO;

      if(value == 1) return inputParts[0].contains("i") ? Complex.IM : Complex.ONE;

      if(value == -1) return inputParts[0].contains("i") ? Complex.IM_NEG : Complex.ONE_NEG;

      return inputParts[0].contains("i") ? new Complex(0,value) : new Complex(value,0);
    }

    if(inputParts.length != 3)
      throw new IllegalArgumentException("Expected format a + ib OR a - ib");

    double realValue = Double.parseDouble(inputParts[0]);
    double imaginaryValue;

    if(inputParts[2].length() == 1 && inputParts[2].equals("i")) imaginaryValue = 1;
    else imaginaryValue = Double.parseDouble(inputParts[2].substring(1));

    String sign = inputParts[1];
    boolean positive = sign.equals("+");

    imaginaryValue = positive ? imaginaryValue : -imaginaryValue;

    if(realValue == 0 && imaginaryValue == 0) return Complex.ZERO;

    if(realValue == 0){
      if(Double.compare(imaginaryValue,-1) == 0) return Complex.IM_NEG;
      if(Double.compare(imaginaryValue,1) == 0) return Complex.IM;
    }

    if(imaginaryValue == 0){
      if(Double.compare(realValue, -1) == 0) return Complex.ONE_NEG;
      if(Double.compare(realValue, 1) == 0) return Complex.ONE;
    }

    return new Complex(realValue, imaginaryValue);
  }

  /**
   * Calculates module of complex number
   *
   * @return double module of complex number
   */
  public double module(){
    return Math.hypot(re,im);
  }

  /**
   * Multiplies current complex number with <code>c</code>
   *
   * @param c instance of Complex
   * @return new <code>Complex</code> that is the result of the expression
   * @throws NullPointerException if <code>c</code> is null
   */

  public Complex multiply(Complex c){
    Objects.requireNonNull(c, "Complex number can't be null");

    double real = c.re * this.re - c.im * this.im;
    double imaginary = this.re * c.im + this.im * c.re;

    return new Complex(real, imaginary);
  }

  /**
   * Divides current complex number with <code>c</code>
   *
   * @param c instance of Complex
   * @return new <code>Complex</code> that is the result of the expression
   * @throws NullPointerException if <code>c</code> is null
   * @throws IllegalArgumentException if denominator is equal to 0
   */

  public Complex divide(Complex c){
    Objects.requireNonNull(c, "Complex number can't be null");

    double denominator = c.re * c.re + c.im * c.im;

    if(denominator == 0)
      throw new IllegalArgumentException("Denominator is equal to 0");

    double real = (this.re * c.re + this.im * c.im)/denominator;
    double imaginary = (this.im * c.re - this.re * c.im)/denominator;

    return new Complex(real, imaginary);
  }

  /**
   * Adds current complex number with <code>c</code>
   *
   * @param c instance of Complex
   * @return new <code>Complex</code> that is the result of the expression
   * @throws NullPointerException if <code>c</code> is null
   */

  public Complex add(Complex c){
    Objects.requireNonNull(c, "Complex number can't be null");

    return new Complex(this.re + c.re, this.im + c.im);
  }

  /**
   * Subtracts current complex number with <code>c</code>
   *
   * @param c instance of Complex
   * @return new <code>Complex</code> that is the result of the expression
   * @throws NullPointerException if <code>c</code> is null
   */

  public Complex sub(Complex c){
    Objects.requireNonNull(c, "Complex number can't be null");

    return  new Complex(this.re - c.re, this.im - c.im);
  }
  /**
   * Negates current complex number
   *
   * @return new <code>Complex</code> that is the result of the expression
   */
  public Complex negate(){
    return new Complex(-re, -im);
  }

  /**
   * Calculates the current complex number raised to the power of <code>n</code>.
   *
   * @param n exponent
   * @return new <code>Complex</code> that is the result of the expression
   * @throws IllegalArgumentException if <code>n</code> is smaller than 0
   */
  public Complex power(int n){
    if(n < 0) throw new IllegalArgumentException("n must be non-negative integer");

    double r = module();
    double angle = Math.atan2(im, re);


    double real = Math.pow(r,n) * Math.cos(n*angle);
    double imaginary = Math.pow(r,n) * Math.sin(n*angle);

    return new Complex(real, imaginary);
  }
  /**
   * Calculates the <code>n</code> -th complex roots of the current complex number.
   *
   * @param n degree of root
   * @return list of new <code>Complex</code> that is the result of the expression
   * @throws IllegalArgumentException if <code>n</code> is smaller than 0
   */
  public List<Complex> root(int n){

    if(n < 0) throw new IllegalArgumentException("n must be non-negative integer");

    List<Complex> complexRoots = new ArrayList<>();

    double r = module();
    double angle = Math.atan2(im, re);

    double rootR = Math.pow(r, 1./(double)n);

    for(int k = 0; k < n; k++){
      double numerator = angle + 2*k*Math.PI;
      double real = rootR*Math.cos(numerator / n);
      double imaginary = rootR*Math.sin(numerator/n);
      complexRoots.add(new Complex(real,imaginary));
    }

    return complexRoots;
  }

  /**
   * Getter for <code>Complex</code> imaginary number part
   *
   * @return value of imaginary part of this complex number
   */

  public double getIm() {
    return im;
  }


  /**
   * Getter for <code>Complex</code> real number part
   *
   * @return value of real part of this complex number
   */
  public double getRe() {
    return re;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();

    if(im == 0 && re != 0)
      sb.append(String.format("%.2f", re));

    if(im != 0 && re == 0){
      sb.append(String.format("%s %.2f",im < 0 ? "-":"",abs(im)));
    }

    if(im != 0 && re != 0){
      sb.append(String.format("%.2f %s %.2fi",re,im > 0 ? "+": "-",abs(im)));
    }


    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Complex complex = (Complex) o;
    double DELTA = 1E-8;
    return Math.abs(complex.re - this.re) < DELTA && Math.abs(complex.im - this.im) < DELTA;
  }

  @Override
  public int hashCode() {
    return Objects.hash(re, im);
  }
}
