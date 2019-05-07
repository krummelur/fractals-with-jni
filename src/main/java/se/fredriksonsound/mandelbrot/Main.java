package se.fredriksonsound.mandelbrot;

import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final int imageWidth = 100;
    private static final int imageHeight = 100;
    private static final double distanceToSearch = 1;
    private static final double fixPointX = 0;
    private static final double fixPointY = 0;
    private static final int MAX_ITERATIONS = 250;
    public static void main(String[] args) {

        int max = 0;
        boolean JNI = true;
        BufferedImage bi = new BufferedImage(imageWidth,imageHeight, BufferedImage.TYPE_INT_RGB);

            long startTime = System.currentTimeMillis();
        if(JNI) {
            NativeMandelbrot nMandelbrot = new NativeMandelbrot();
            int nativeResultArr[] = new int[imageWidth * imageHeight];

            nMandelbrot.mandelbrot_init(imageHeight, imageWidth, MAX_ITERATIONS);
            //nMandelbrot.render_to_memory(-0.15, 0.84, .001, .001, nativeResultArr);
            nMandelbrot.render_to_memory(0, 0, 4, 4, nativeResultArr);
            double runTime = System.currentTimeMillis() - startTime;
            System.out.println("Total time seconds: " + runTime / 1000);
            nMandelbrot.test();
            for (int i = 0; i < nativeResultArr.length; i++) {
                if (max < nativeResultArr[i])
                    max = nativeResultArr[i];
            }
            double mult = 255.0/(double)max;
            for (int j = 0; j < imageHeight; j++) {
                for (int i = 0; i < imageWidth; i++) {
                    int col = nativeResultArr[i + j * imageHeight];
                    col = col == MAX_ITERATIONS ? 0 : (int)(col*mult);
                    bi.setRGB(i, j, col);
                }
            }


            System.out.println("MAX: " + max);
        }
        else {




            bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            FractalSet fs = new Mandelbrot();

            double scalefactor = distanceToSearch * 2 / imageHeight;

            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    bi.setRGB(j, i, fs.iterationsBeforeEscape(new Complex(scalefactor * j - distanceToSearch + fixPointX,
                            scalefactor * i - distanceToSearch + fixPointY)));
                }
            }
        }

            try {
                ImageIO.write(bi, "png", new File("img\\test.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
