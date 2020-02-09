package se.fredriksonsound.mandelbrot;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final int imageWidth = 1024;
    private static final int imageHeight = 1024;
    private static final double START_DISTANCE = 1;
    private static final double END_DISTANCE =   0.00000000000000005;
    private static double fixPointX = -1.0067581019642513;
    private static double fixPointY = 0.3112899872556565;
    private static final int MAX_ITERATIONS = 1024;
    private static final int NUM_FRAMES = 1;
    private static final double FRAME_ZOOM = 0.9;
    private static boolean JNI = true;

    public static void main(String[] args) {
        BufferedImage bi;
        if(JNI) {
            System.out.println("Rendering using JNI");
            NativeMandelbrot nMandelbrot = new NativeMandelbrot();
            int nativeResultArr[] = new int[imageWidth * imageHeight];

            //Initialize library
            nMandelbrot.mandelbrot_init(imageWidth, imageHeight, MAX_ITERATIONS);

            //Render results to java array
            int framesRendered = 0;
            double currentZoom = START_DISTANCE;
            while(framesRendered < NUM_FRAMES && currentZoom > END_DISTANCE) {
                currentZoom *= FRAME_ZOOM;
                bi = new BufferedImage(imageWidth,imageHeight, BufferedImage.TYPE_INT_RGB);

                long startTime = System.currentTimeMillis();
                nMandelbrot.render_to_memory(fixPointX, fixPointY, currentZoom, currentZoom*((double)imageHeight/imageWidth), nativeResultArr);
                double runTime = System.currentTimeMillis() - startTime;
                System.out.println("Total time seconds: " + runTime / 1000);
                int max = 0;
                int min = MAX_ITERATIONS;
                for (int i = 0; i < nativeResultArr.length; i++) {
                    if (max < nativeResultArr[i]) {
                        max = nativeResultArr[i];
                    }
                    if (min > nativeResultArr[i]) {
                        min = nativeResultArr[i];
                    }
                }

                //Color the image
                for (int j = 0; j < imageHeight; j++) {
                    for (int i = 0; i < imageWidth; i++) {
                        int col = nativeResultArr[i + j * imageWidth];
                        float hue = logInterpolateClamped(min, max, col);
                        float sat = 1.0f - (float)col/max;
                        float val = (float)(Math.sqrt(Math.sqrt(logInterpolateClamped(min-1, (float)min+25, col))));
                        //val = col == MAX_ITERATIONS ? 0 : val;
                        int rgb = Color.HSBtoRGB( hue, sat, val);
                        bi.setRGB(i, j, rgb);
                    }
                }
                System.out.println("MAX: " + max + " MAX: " + max + " FRAME: " + framesRendered + "/" + NUM_FRAMES + " ZOOM: " + currentZoom);
                saveImg(bi, (String.format("%04d", framesRendered++)));
            }
        }
        else {
            bi = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            FractalSet fs = new Mandelbrot();
            double scalefactor = START_DISTANCE * 2 / imageHeight;

            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    bi.setRGB(j, i, fs.iterationsBeforeEscape(new Complex(scalefactor * j - START_DISTANCE + fixPointX,
                            scalefactor * i - START_DISTANCE + fixPointY)));
                }
            }
        }
    }

    //Speedup rendering by skipping frames, then using scale to interpolate between them
    private static void interpolateImages(int numImages, int stride) {
        for (int i = 0; i < numImages; i++) {
            BufferedImage im = null;
            try {
                String fnStr = String.format("%06d", i * stride);
                im = ImageIO.read(new File("img\\frames\\interp\\" + fnStr + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            for (int j = 1; j < stride; j++) {
                BufferedImage sc = scaleImage(im, 1d / (1d - (1-FRAME_ZOOM)/stride * j));
                saveImg("img\\frames\\interp\\",sc, String.format("%06d", j + i * stride));
            }
        }
    }

    //Scale images for interpolation frames when making videos
    private static BufferedImage scaleImage(BufferedImage o, double scaleFactor) {
        BufferedImage scaledImage = new BufferedImage(o.getWidth(), o.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        AffineTransform xform = new AffineTransform();
        xform.setTransform(scaleFactor,0, 0, scaleFactor, -(scaleFactor*o.getWidth()-o.getWidth())/2, -(scaleFactor*o.getHeight()-o.getHeight())/2);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(o, xform, null);
        graphics2D.dispose();
        return scaledImage;
    }

    private static void saveImg (String path, BufferedImage img, String filename) {
        try {
            ImageIO.write(img, "png", new File(path + filename + ".png"));
            System.out.println("Saved: " + path + filename);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void saveImg (BufferedImage img, String filename) {
            String path = "img\\frames\\tmp\\" + filename ;
            saveImg(path, img, filename);
    }

    private static double linInterpolate(double minO, double maxO, double minI, double maxI, double r) {
        double rangeO = maxO-minO;
        double rangeI = maxI-minI;
        double distance = r-minI;
        return minO + rangeO * distance/rangeI;
    }

    private static float linInterpolateClamped(float min, float max, float r) {
        if(r <= min) return 0;
        if(r >= max) return 1;
        return (r-min)/(max-min);
    }

    private static float logInterpolateClamped(float min, float max, float r) {
        if(min >= r) return 0;
        if(r >= max) return 1;
        float x = (r-min)/(max-min);
        return (float)Math.log10(x/1.11111+0.1)+1;
    }
}