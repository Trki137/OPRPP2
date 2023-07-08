package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.math.Complex;
import hr.fer.zemris.java.math.ComplexPolynomial;
import hr.fer.zemris.java.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implement Newton calculations with ThreadPools
 *
 * @author Dean Trkulja
 * @version 1.0
 */

public class NewtonP1 {

    public static class Calculate implements Runnable {

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

        ComplexRootedPolynomial complexRootedPolynomial;

        ComplexPolynomial complexPolynomial;

        ComplexPolynomial firstDerivative;

        public Calculate(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax, int m, AtomicBoolean cancel, short[] data, ComplexRootedPolynomial complexRootedPolynomial, ComplexPolynomial complexPolynomial) {
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

        /**
         * Job that an instance of this class must do
         */
        public void run() {

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
    }

    public static class NewtonProducer implements IFractalProducer {
        private ExecutorService executor;

        private int numOfTracks;

        private final int numberOfWorkers;

        private final ComplexRootedPolynomial complexRootedPolynomial;

        private final ComplexPolynomial complexPolynomial;

        public NewtonProducer(ComplexRootedPolynomial complexRootedPolynomial, int numOfWorkers, int numOfTracks) {

            this.numberOfWorkers = numOfWorkers;
            this.numOfTracks = numOfTracks;
            this.complexRootedPolynomial = complexRootedPolynomial;
            this.complexPolynomial = complexRootedPolynomial.toComplexPolynom();

        }


        @Override
        public void setup() {
            executor = Executors.newFixedThreadPool(numberOfWorkers);
        }

        public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, IFractalResultObserver iFractalResultObserver, AtomicBoolean cancel) {

            if (numOfTracks > height) numOfTracks = height;

            System.out.println("Number of workers: " + numberOfWorkers);
            System.out.println("Number of tracks: " + numOfTracks);

            int m = 16 * 16;
            short[] data = new short[width * height];

            int numberOfYByTrack = height / numOfTracks;

            List<Runnable> jobs = new ArrayList<>();

            for (int i = 0; i < numOfTracks; i++) {
                int yMin = i * numberOfYByTrack;
                int yMax = (i + 1) * numberOfYByTrack - 1;

                if (i == numOfTracks - 1) yMax = height - 1;

                Calculate p = new Calculate(
                        reMin, reMax, imMin, imMax, width, height,
                        yMin, yMax, m, cancel, data,
                        complexRootedPolynomial, complexPolynomial
                );
                jobs.add(p);
            }

            List<Future<?>> futures = new ArrayList<>();
            for (Runnable job : jobs) {
                futures.add(executor.submit(job));
            }

            for (Future<?> f : futures) {
                while (true) {
                    try {
                        f.get();
                        break;
                    } catch (InterruptedException | ExecutionException ignored) {
                    }
                }
            }

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            iFractalResultObserver.acceptResult(data, (short) (complexPolynomial.order() + 1), requestNo);
        }

        @Override
        public void close() {
            executor.shutdown();
        }
    }

    /**
     * Starting point for our application
     * @throws IllegalArgumentException if arguments are not in valid format
     * @param args argument for our program , -w number or --workers=number and -t number or --tracks=number
     */
    public static void main(String[] args) {
        boolean secondMode = false;
        String past = "";
        int numOfThreads = 0;
        int numOfTracks = 0;

        for (String arg : args) {
            if (arg.equals("-w") | arg.equals("-t")) {
                secondMode = true;
                past = arg;
                continue;
            }
            if (secondMode) {
                if(!(past.equals("-w") || past.equals("-t"))) throw new IllegalArgumentException("Illegal argument used: "+ past);

                if (past.equals("-w")) {
                    if (numOfThreads != 0) throw new IllegalArgumentException("Can't define number of threads twice");
                    numOfThreads = Integer.parseInt(arg);
                }

                if (past.equals("-t")) {
                    if (numOfTracks != 0) throw new IllegalArgumentException("Can't define number of threads twice");
                    numOfTracks = Integer.parseInt(arg);
                }

                secondMode = false;
                continue;
            }
            if (arg.startsWith("-w") || arg.startsWith("--w")) {
                if (numOfThreads != 0)
                    throw new IllegalArgumentException("Can't define number of threads twice");

                if (arg.startsWith("-w"))
                    numOfThreads = Integer.parseInt(arg.substring(arg.indexOf(" ") + 1));
                else numOfThreads = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));
            }

            if (arg.startsWith("-t") || arg.startsWith("--t")) {
                if (numOfTracks != 0)
                    throw new IllegalArgumentException("Can't define number of tracks twice");

                if (arg.startsWith("-t"))
                    numOfTracks = Integer.parseInt(arg.substring(arg.indexOf(" ") + 1));
                else numOfTracks = Integer.parseInt(arg.substring(arg.indexOf("=") + 1));
            }
        }

        if (numOfThreads <= 0) numOfThreads = Runtime.getRuntime().availableProcessors();
        if (numOfTracks <= 0) numOfTracks = 4 * Runtime.getRuntime().availableProcessors();

        List<Complex> roots = Util.getRoots();

        System.out.println("Image of fractal will appear shortly. Thank you.");
        NewtonProducer newtonProducer = new NewtonProducer(new ComplexRootedPolynomial(Complex.ONE, roots.toArray(Complex[]::new)), numOfThreads, numOfTracks);
        FractalViewer.show(newtonProducer);
    }
}
