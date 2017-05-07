package com.beata.learnopengl.orthom;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.beata.learnopengl.MatrixState;
import com.beata.learnopengl.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beata on 2017/5/1.
 */
public class SixPointedStar {

    public static float[] mProjMatrix = new float[16];
    public static float[] mVMatrix = new float[16];
    public static float[] mMMatrix = new float[16];
    public static float[] mMVPMatrix;
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    String mVertexShader;
    String mFragmentShader;

    FloatBuffer mVertexBuffer;
    FloatBuffer mColorBuffer;

    int vCount = 0;
    float yAngle = 0;
    float xAngle = 0;
    final float UNIT_SIZE = 1;

    public SixPointedStar(GLSurfaceView surfaceView, float r, float R, float z){
        initVertexData(R, r, z);
        initShader(surfaceView);
    }

    public void initVertexData(float R, float r, float z){
        List<Float> flist = new ArrayList<Float>();
        float temAngle = 360/6;
        float halfTempAngle = temAngle / 2;

        for(float angle = 0 ; angle < 360 ; angle+=temAngle){
            flist.add(0f);flist.add(0f);flist.add(z); //first vertex pos

            flist.add((float)(R * UNIT_SIZE * Math.cos(Math.toRadians(angle)))); //second x
            flist.add((float)(R * UNIT_SIZE * Math.sin(Math.toRadians(angle)))); //second y
            flist.add(z); //second z

            flist.add((float)(r * UNIT_SIZE * Math.cos(Math.toRadians(angle + halfTempAngle)))); //third x
            flist.add((float)(r * UNIT_SIZE * Math.sin(Math.toRadians(angle + halfTempAngle)))); //third y
            flist.add(z); //third z

            flist.add(0f);flist.add(0f);flist.add(z); //first vertex pos

            flist.add((float)(r * UNIT_SIZE * Math.cos(Math.toRadians(angle + halfTempAngle)))); //second x
            flist.add((float)(r * UNIT_SIZE * Math.sin(Math.toRadians(angle + halfTempAngle)))); //second y
            flist.add(z); //second z

            flist.add((float)(R * UNIT_SIZE * Math.cos(Math.toRadians(angle + temAngle)))); //third x
            flist.add((float)(R * UNIT_SIZE * Math.sin(Math.toRadians(angle + temAngle)))); //third y
            flist.add(z); //third z
        }

        vCount = flist.size() / 3;
        float[] vertexArray = new float[flist.size()];
        for(int i = 0 ; i < vCount ; i++){
            vertexArray[i * 3] = flist.get(i * 3);
            vertexArray[i * 3 + 1] = flist.get(i * 3 + 1);
            vertexArray[i * 3 + 2] = flist.get(i * 3 + 2);
        }
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertexArray.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);

        float[] colorArray = new float[vCount * 4];
        for(int i = 0 ; i < vCount ; i++){
            if(i % 3 == 0){
                colorArray[i * 4] = 1;
                colorArray[i * 4 + 1] = 1;
                colorArray[i * 4 + 2] = 1;
                colorArray[i * 4 + 3] = 0;
            }else{
                colorArray[i * 4] = 0.45f;
                colorArray[i * 4 + 1] = 0.75f;
                colorArray[i * 4 + 2] = 0.75f;
                colorArray[i * 4 + 3] = 0;
            }
        }
        ByteBuffer cbuffer = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbuffer.order(ByteOrder.nativeOrder());
        mColorBuffer = cbuffer.asFloatBuffer();
        mColorBuffer.put(colorArray);
        mColorBuffer.position(0);
    }

    public void initShader(GLSurfaceView surfaceView){
        mVertexShader = ShaderUtils.loadFromAssetsFile("vertex.vsh", surfaceView.getResources());
        mFragmentShader = ShaderUtils.loadFromAssetsFile("fragment.fsh", surfaceView.getResources());
        mProgram = ShaderUtils.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(){
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 0, 1);
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);
        Matrix.rotateM(mMMatrix, 0, yAngle, 0, 1, 0);
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0);
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glVertexAttribPointer(maColorHandle, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
