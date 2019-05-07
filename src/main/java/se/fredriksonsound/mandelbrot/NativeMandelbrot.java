package se.fredriksonsound.mandelbrot;

public class NativeMandelbrot {

    public NativeMandelbrot() {
        LibraryUtil.loadDLL("mandelbrotlib.dll");

    }

    public native void mandelbrot_init(int w, int h, int m_iter);
    public native void render_to_memory(double init_re, double init_im, double searchWidth, double searchHeight, int[] inArr);
    public native void test();
}
