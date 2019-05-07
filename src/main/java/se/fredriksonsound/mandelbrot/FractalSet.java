package se.fredriksonsound.mandelbrot;


import org.apache.commons.math3.complex.Complex;

public interface FractalSet {
        /**
         * Determines if a point belongs to a set, and returns the number of iterations before it was shown to escape.
         * @param complex the point to examine
         * @return the number of iterations before the series is shown to diverge.
         */
        public int iterationsBeforeEscape(Complex complex);

}
