package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * The <code>ComplexRootedPolynomial</code> class models a complex rooted polynomial in the form of f(z) = z*(z-z1)*...*(z-zn)
 * z is a constant, z1...zn are roots
 *@author Dean Trkulja
 * @version 1.0
 */

public class ComplexRootedPolynomial {

  /**
   * Constant of polynomial
   */
  private final Complex constant;

  /**
   * Roots of polynomial
   */
  private final Complex[] roots;

  /**
   * Constructor for creating new <code>ComplexRootedPolynomial</code>
   *
   * @param constant constant of polynomial
   * @param roots roots of polynomial
   */
  public ComplexRootedPolynomial(Complex constant, Complex ...roots){
    this.constant = constant;
    this.roots = Arrays.copyOf(roots,roots.length);
  }

  /**
   * Converts the current {@code ComplexRootedPolynomial} instance to <link> ComplexPolynomial</link> type.
   *
   * @return new <link>ComplexPolynomial</link> that is calculated from current <code>ComplexRootedPolynomial</code>
   */
  public ComplexPolynomial toComplexPolynom(){
    ComplexPolynomial polynomial = new ComplexPolynomial(constant);
    for(Complex complex: roots)
      polynomial = polynomial.multiply(new ComplexPolynomial(complex.negate(), Complex.ONE));

    return polynomial;
  }

  /**
   * Calculates current <code>ComplexRootedPolynomial</code> with given <code>z</code>
   *
   * @param z instance of <code>ComplexRootedPolynomial</code>
   * @return new <code>ComplexRootedPolynomial</code> that is the result of the expression
   * @throws NullPointerException if <code>z</code> is null
   */

  public Complex apply(Complex z){
    Objects.requireNonNull(z, "Given complex number can't be null");

    List<Complex> results = new ArrayList<>();

    for (Complex root: roots)
      results.add(z.sub(root));

    Complex result = constant;

    for(Complex complexNum: results)
      result = result.multiply(complexNum);

    return result;
  }

  /**
   * Finds the index of the closest root for the given complex number <code>z</code> that is within the given <code>threshold</code>
   * @param z other complex number
   * @param threshold max distance allowed between a certain root and the given <code>z</code>
   * @return index of the closest root that is within threshold. If there isn't any than -1
   */
  public int indexOfClosestRootFor(Complex z, double threshold){
    Objects.requireNonNull(z, "Given complex number can't be null");

    int index = -1;
    double minDistance = threshold;

    for(int i = 0; i < roots.length; i++){

      double distance = calculateDistance(z, roots[i]);

      if (distance < minDistance) {
        minDistance = distance;
        index = i;
      }
    }

      return index;
  }

  /**
   * Calculates distance between 2 complex numbers
   * @param z first complex number
   * @param complex second complex number
   * @return distance between <code>z</code> and <code>complex</code>
   */
  private double calculateDistance(Complex z, Complex complex) {
    double xAxis = Math.pow(z.getRe() - complex.getRe() , 2);
    double yAxis = Math.pow(z.getIm() - complex.getIm(), 2);
    double underRoot = xAxis + yAxis;

    return Math.sqrt(underRoot);
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if(constant.equals(Complex.ZERO))
      sb.append("f(z) = ");
    else if(constant.getRe() == 0 || constant.getIm() == 0)
      sb.append(String.format("f(z) = %s*", constant));
    else
      sb.append(String.format("f(z) = (%s)*", constant));

    for(Complex root: roots){

      if(root.getRe() == 0 && root.getIm() != 0){
        String sign = root.getIm() < 0 ? "+" : "-";
        sb.append(String.format("(z %s %.2f)*",sign,abs(root.getIm())));
      }
      if(root.getRe() != 0 && root.getIm() == 0) {
        String sign = root.getRe() < 0 ? "+" : "-";
        sb.append(String.format("(z %s %.2f)*",sign,abs(root.getRe())));
      }

      if(root.getRe() != 0 && root.getIm() != 0)
        sb.append(String.format("(z - (%s))*", root));
    }

    sb.delete(sb.length()-1,sb.length());

    return sb.toString();
  }
}
