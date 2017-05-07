package com.beata.learnopengl.orthom;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.beata.learnopengl.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by beata on 2017/5/1.
 */
public class SenceRenderer implements GLSurfaceView.Renderer{

    GLSurfaceView glSurfaceView;
    private SixPointedStar[] stars = new SixPointedStar[6];

    public SenceRenderer(GLSurfaceView glSurfaceView){
        this.glSurfaceView = glSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        for(int i = 0 ; i < stars.length ; i++){
            stars[i] = new SixPointedStar(glSurfaceView, 0.2f, 0.5f, -1f *i);
        }
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        MatrixState.setProjectFrustum(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f, 1f, 50f);
        MatrixState.setCamera(0, 0, 3f, 0f, 0f, 0f, 0, 1f, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        for(SixPointedStar st : stars){
            st.drawSelf();
        }
    }

    public SixPointedStar[] getStars(){
        return stars;
    }
}
