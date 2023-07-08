package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.math.Complex;
import hr.fer.zemris.java.math.ComplexPolynomial;
import hr.fer.zemris.java.math.ComplexRootedPolynomial;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implement Newton calculations with ForkJoinPool
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class NewtonP2 {

    /**
     * Number of tracks to divide our program
     */
    public volatile static int minTracks = 0;

    /**
     * Starting point for our application
     * @throws IllegalArgumentException if arguments are not in valid format
     * @param args argument for our program , -m number or --mintracks=number
     */

    public static void main(String[] args) {
        if(args.length == 1){
            if(args[0].contains("--mintracks")) minTracks = Integer.parseInt(args[0].substring(args[0].indexOf("=") + 1));
            else if (args[0].contains("-m")) minTracks = Integer.parseInt(args[0].split(" ")[1]);
            else throw new IllegalArgumentException("Invalid argument");
        }
        if(args.length == 2) {
            if(args[0].equals("-m")) minTracks = Integer.parseInt(args[1]);
            else throw new IllegalArgumentException("Invalid argument");
        }
        if(args.length > 2) throw new IllegalArgumentException("To many arguments");

        if(minTracks == 0) minTracks = 16;

        System.out.println(minTracks);

        List<Complex> roots = Util.getRoots();

        System.out.println("Image of fractal will appear shortly. Thank you.");
        NewtonProducer newtonProducer = new NewtonProducer(new ComplexRootedPolynomial(Complex.ONE, roots.toArray(Complex[]::new)),minTracks);
        FractalViewer.show(newtonProducer);
    }

    public static class Calculate extends RecursiveAction{

        private final double reMin;

        private final double reMax;

        private final double imMin;

        private final double imMax;

        private final int width;

        private final int height;

        private final int yMin;

        private final int yMax;

        private final int m;
        private final short[] data;
        private final AtomicBoolean cancel;

        private final ComplexRootedPolynomial complexRootedPolynomial;

        private final ComplexPolynomial complexPolynomial;

        private final ComplexPolynomial firstDerivative;


        public Calculate(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax , int m, AtomicBoolean cancel, short[] data, ComplexRootedPolynomial complexRootedPolynomial, ComplexPolynomial complexPolynomial) {
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMax = yMax;
            this.yMin = yMin;
            this.m = m;
            this.cancel = cancel;
            this.data = data;
            this.complexPolynomial = complexPolynomial;
            this.firstDerivative = complexPolynomial.derive();
            this.complexRootedPolynomial = complexRootedPolynomial;
        }

        private void computeDirect(){
            double rootTreshold = 1E-3;
            double convergenceTreshold = 0.002;
            int offset = yMin * width;

            for (int y = yMin; y <= yMax && !cancel.get(); y++) {

                for (int x = 0; x < width; x++) {

                    double cRe = x / (width - 1.0) * (reMax - reMin) + reMin;
                    double cIm = (height - 1.0 - y) / (height - 1.0) * (imMax - imMin) + imMin;
                    double module;
                    int iters = 0;

                    Complex zn = new Complex(cRe, cIm);

                    do {
                        Complex numerator = complexPolynomial.apply(zn);
                        Complex denominator = firstDerivative.apply(zn);

                        Complex znoId = zn;

                        Complex fraction = numerator.divide(denominator);

                        zn = zn.sub(fraction);
                        module = zn.sub(znoId).module();

                        iters++;
                    } while (module > convergenceTreshold && iters < m);

                    int index = complexRootedPolynomial.indexOfClosestRootFor(zn, rootTreshold);
                    data[offset++] = (short) (index + 1);
                }
            }
        }

        @Override
        protected void compute() {
            if(minTracks >= (yMax - yMin)){
                computeDirect();
                return;
            }

            int yMaxFirstHalf = ((yMin + yMax) / 2) ;

            Calculate calculate = new Calculate(
                    reMin, reMax, imMin, imMax, width, height,
                    yMin, yMaxFirstHalf, m, cancel, data,
                    complexRootedPolynomial, complexPolynomial);

            Calculate calculate1 = new Calculate(
                    reMin, reMax, imMin, imMax, width, height,
                    yMaxFirstHalf + 1, yMax, m, cancel, data,
                    complexRootedPolynomial, complexPolynomial);

            invokeAll(calculate,calculate1);

        }
    }

    public static class NewtonProducer implements IFractalProducer {
        private ForkJoinPool pool;
        private int minTracks;
        private final ComplexRootedPolynomial complexRootedPolynomial;

        private final ComplexPolynomial complexPolynomial;
        public NewtonProducer(ComplexRootedPolynomial complexRootedPolynomial, int minTracks){
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexRootedPolynomial.toComplexPolynom();
            this.minTracks = minTracks;
        }

        @Override
        public void setup() {
            pool = new ForkJoinPool();
        }

        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, IFractalResultObserver iFractalResultObserver, AtomicBoolean cancel){
            if(minTracks > height) minTracks = height;

            int m = 16 * 16;

            short[] data = new short[width * height];

            Calculate calculate = new Calculate(
                    reMin, reMax, imMin, imMax, width, height,
                    0, height - 1, m, cancel, data,
                    complexRootedPolynomial, complexPolynomial
            );

            pool.invoke(calculate);

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            iFractalResultObserver.acceptResult(data, (short)(complexPolynomial.order() + 1), requestNo);

        }


        @Override
        public void close() {
            pool.shutdown();
        }
    }
}
