package se.fredriksonsound.mandelbrot;

import org.apache.commons.math3.complex.Complex;

public class Mandelbrot implements FractalSet {
    private final double ESCAPE_LIMIT = 2.0;
    private final int MAXIMUM_ITERATIONS = 500;

    @Override
    public int iterationsBeforeEscape(Complex complexPoint) {
        double re = complexPoint.getReal();
        double im = complexPoint.getImaginary();
        int iterations = 0;
        double zr = re;
        double zi = im;
        double znr = 0;
        if (true) {

        //return (int)(re * re + im * im * 50);

        while (zr * zr + zi * zi < ESCAPE_LIMIT*ESCAPE_LIMIT && iterations < MAXIMUM_ITERATIONS) {
            //(zr + zi)(zr + zi) = zr(zr +zi) + zi(zr +zi) = zr*zr + zr*im + zi*zr +zi * zi =>
            // zr = zr*zr - zi*zi
            // zi = zr*im*2
            znr = zr;
            zr = zr * zr - zi * zi + re;
            zi = znr * zi * 2 + im;
            iterations++;
        }
            return MAXIMUM_ITERATIONS - iterations;

        } else {
            Complex complexResult = new Complex(complexPoint.getReal(), complexPoint.getImaginary());
            while (complexResult.abs() < ESCAPE_LIMIT && iterations < MAXIMUM_ITERATIONS) {
                complexResult = complexResult.pow(2).add(complexPoint);
                iterations++;

            }
            return MAXIMUM_ITERATIONS - iterations;
        }
    }
}
