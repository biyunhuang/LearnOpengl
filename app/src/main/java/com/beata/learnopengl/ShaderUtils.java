package com.beata.learnopengl;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by beata on 2017/4/30.
 */
public class ShaderUtils {

    private static final String TAG = "GLES20_ERROR";

    public static int loadShader(int shaderType, String source){
        int shader = GLES20.glCreateShader(shaderType);

        if(shader != 0){
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);

            int[] compileid = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileid, 0);

            if(compileid[0] == 0){
                Log.e(TAG, "could not compile shader "+shaderType+":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if(vertexShader == 0){
            return 0;
        }

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if(fragmentShader == 0){
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if(program != 0){
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, fragmentShader);
            checkGlError("glAttachShader");

            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if(linkStatus[0] != GLES20.GL_TRUE){
                Log.e(TAG, "Could not link program : ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static void checkGlError(String op){
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e(TAG, op+": glError "+error);
            throw new RuntimeException(op + ": glError "+error);
        }
    }

    public static String loadFromAssetsFile(String fname, Resources resources){
        String result = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try{
            inputStream = resources.getAssets().open(fname);
            int ch = 0;
            baos = new ByteArrayOutputStream();

//            byte[] buffer = new byte[1024];
//            while ((ch = inputStream.read(buffer)) != -1){
//                baos.write(buffer, 0, buffer.length);
//            }

            while ((ch = inputStream.read()) != -1){
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();

            result = new String(buff, "utf-8");
            result = result.replaceAll("\\r\\n","\n");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(null != inputStream){
                    inputStream.close();
                }
                if(null != baos){
                    baos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return result;
    }
}
