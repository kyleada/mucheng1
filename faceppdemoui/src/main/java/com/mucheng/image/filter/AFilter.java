/*
 *
 * AFilter.java
 * 
 * Created by Wuwang on 2016/10/17
 */
package com.mucheng.image.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.mucheng.util.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Description:
 */
public abstract class AFilter {

    private Context mContext;
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int glHMatrix;

    private Bitmap mBitmap;

    private FloatBuffer bPos;
    private FloatBuffer bCoord;

    private int textureId;


    private float uXY;

    private String vertex;
    private String fragment;
    private float[] mViewMatrix=new float[16];
    private float[] mProjectMatrix=new float[16];
    private float[] mMVPMatrix=new float[16];

	//顶点坐标
    private final float[] sPos={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };

    //纹理坐标
    private final float[] sCoord={
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };

    public AFilter(Context context, String vertex, String fragment){
        this.mContext=context;
        this.vertex=vertex;
        this.fragment=fragment;
        ByteBuffer bb= ByteBuffer.allocateDirect(sPos.length*4);
        bb.order(ByteOrder.nativeOrder());
        bPos=bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc= ByteBuffer.allocateDirect(sCoord.length*4);
        cc.order(ByteOrder.nativeOrder());
        bCoord=cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

    public void setImageBuffer(int[] buffer,int width,int height){
        mBitmap= Bitmap.createBitmap(buffer,width,height, Bitmap.Config.RGB_565);
    }

    public void setBitmap(Bitmap bitmap){
        this.mBitmap=bitmap;
    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgram= ShaderUtils.createProgram(mContext.getResources(),vertex,fragment);
        glHPosition= GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate= GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture= GLES20.glGetUniformLocation(mProgram,"vTexture");
        glHMatrix= GLES20.glGetUniformLocation(mProgram,"vMatrix");
//        onDrawCreatedSet(mProgram);

		textureId=createTexture();
    }

	private final float[] modelMatrix = new float[16];


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        int w=mBitmap.getWidth();
        int h=mBitmap.getHeight();
        float sWH=w/(float)h;
        float sWidthHeight=width/(float)height;
        uXY=sWidthHeight;
        if(width>height){
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight*sWH,sWidthHeight*sWH, -1,1, 3, 5);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight/sWH,sWidthHeight/sWH, -1,1, 3, 5);
            }
        }else{
            if(sWH>sWidthHeight){
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1/sWidthHeight*sWH, 1/sWidthHeight*sWH,3, 5);
            }else{
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH/sWidthHeight, sWH/sWidthHeight,3, 5);
            }
        }
		preDrawMatrix(0f);

		//设置相机位置
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

	}

	public void setNeedMatrixFVary(boolean needMatrixFVary) {
		isNeedMatrixFVary = needMatrixFVary;
	}

	boolean isNeedMatrixFVary = false;

	private void preDrawMatrix(float translateX) {
		if(isNeedMatrixFVary){
			setIdentityM(modelMatrix, 0);
			scaleM(modelMatrix,0, 0.5f,0.5f, 0.5f);
			translateM(modelMatrix, 0,translateX, 0f, 0f);

			final float[] temp = new float[16];
			Matrix.multiplyMM(temp, 0, mViewMatrix, 0, modelMatrix, 0);
			//计算变换矩阵
			Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,temp,0);
		}else {
			Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
		}
	}


    public void onDrawFrame(GL10 gl, float translateX) {
		preDrawMatrix(translateX);

//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT| GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
//        onDrawSet();
;
        GLES20.glUniformMatrix4fv(glHMatrix,1,false,mMVPMatrix,0);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);

        GLES20.glVertexAttribPointer(glHPosition,2, GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(glHCoordinate,2, GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);

		GLES20.glDisableVertexAttribArray(glHPosition);
		GLES20.glDisableVertexAttribArray(glHCoordinate);
    }

    public abstract void onDrawSet();
    public abstract void onDrawCreatedSet(int mProgram);

    private int createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }


}
