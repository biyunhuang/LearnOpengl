package com.beata.learnopengl.orthom;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by beata on 2017/5/1.
 */
public class MySurfaceView extends GLSurfaceView{

    SenceRenderer mRenderer = null;

    private final float TOUCH_SCALE_FACTOR = 180f/320;
    private float mPreviousX;
    private float mPreviousY;

    public MySurfaceView(Context context){
        super(context);
        this.setEGLContextClientVersion(2);
        mRenderer = new SenceRenderer(this);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        float x = event.getX();

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                for(SixPointedStar star : mRenderer.getStars()){
                    star.yAngle += dx * TOUCH_SCALE_FACTOR;
                    star.xAngle += dy * TOUCH_SCALE_FACTOR;
                }
                break;
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
