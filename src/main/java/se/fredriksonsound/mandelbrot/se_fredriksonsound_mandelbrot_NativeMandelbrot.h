/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class se_fredriksonsound_mandelbrot_NativeMandelbrot */

#ifndef _Included_se_fredriksonsound_mandelbrot_NativeMandelbrot
#define _Included_se_fredriksonsound_mandelbrot_NativeMandelbrot
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     se_fredriksonsound_mandelbrot_NativeMandelbrot
 * Method:    mandelbrot_init
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_se_fredriksonsound_mandelbrot_NativeMandelbrot_mandelbrot_1init
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     se_fredriksonsound_mandelbrot_NativeMandelbrot
 * Method:    render_to_memory
 * Signature: (DDDD[I)V
 */
JNIEXPORT void JNICALL Java_se_fredriksonsound_mandelbrot_NativeMandelbrot_render_1to_1memory
  (JNIEnv *, jobject, jdouble, jdouble, jdouble, jdouble, jintArray);

/*
 * Class:     se_fredriksonsound_mandelbrot_NativeMandelbrot
 * Method:    test
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_se_fredriksonsound_mandelbrot_NativeMandelbrot_test
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
