/**
 * Author: Li Lin
 * Class Name:
 * Date: 09/04/2014
 * Describe:
 */
package com.Li.vibrationtest;

/**
 * @author Linda
 *
 */
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class LayeredImageView extends ImageView 
{
    private final static String TAG = "LayeredImageView";

    ArrayList<Bitmap> mLayers;
    ArrayList<Matrix> mMatrix;
    float[] degrees;
    float centerX = 0;
    float centerY = 0;
    boolean isBtnUp = false;
    boolean isClockwise = true;
    Bitmap alteredBitmap = null;
    Matrix m = new Matrix();

    public LayeredImageView(Context context,Bitmap imageOriginal, Bitmap pointerOrg) {
        super(context);
        init();
        
    }

    public LayeredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLayers = new ArrayList<Bitmap>();
        mMatrix = new ArrayList<Matrix>();
        degrees = new float[2];
        degrees[0] = degrees[1] = 0;
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix;
        int numLayers = mLayers.size();
        
        for (int i = 0; i < numLayers; i++) 
        {  
            Bitmap b = mLayers.get(i);
        	if(i == 0)
        	{
        		processOuterDialer(degrees[i], b, canvas);
        	}
        	else
        	{
        		processPointer(degrees[i], b, canvas);
        	}
        }
        
    }

    void processOuterDialer(float degree, Bitmap b, Canvas canvas)
    {
    	
        float translateX = centerX - b.getWidth()/2;
        float translateY = centerY - b.getHeight()/2;
    	
        if(alteredBitmap == null)
        {
	        alteredBitmap = Bitmap.createBitmap(b.getWidth(), b
		            .getHeight(), b.getConfig());
        }
        
        //Log.i("Vibrate", "isBtnUp:"+isBtnUp+" degree:"+(int)degree);
        
        if(isBtnUp)
        {
        	if(isClockwise)
        	{
        		degree -= 5;
        		if(degree < degrees[1])
            	{
            		degree = degrees[1];
            		isBtnUp = false;
            	}
            	
        	}
        	else
        	{
        		degree += 5;
        		double temp = degree;
        		if(degrees[1] == 0)
        		{
        			temp = degree - 360 + 5;
        		}
        		if(temp > degrees[1])
            	{
            		degree = degrees[1];
            		isBtnUp = false;
            	}
        	}
        	
        	//Log.i("Vibrate", "degree:"+(int)degree+" degree[1]:"+(int)degrees[1] + " isclock:"+isClockwise);
        	
        	degrees[0] = degree;
        }
        
        
        m.setRotate(degree,b.getWidth()/2,b.getHeight()/2);
        m.postTranslate(translateX, translateY);
        Paint paint = new Paint();
        canvas.drawBitmap(b, m, paint);
        setImageBitmap(alteredBitmap);
       
    }
    
    void processPointer(float degree, Bitmap b, Canvas canvas)
    {
        float translateX = centerX - b.getWidth()/2;
        float translateY = centerY - b.getHeight();

        m.setRotate(degree,b.getWidth()/2,b.getHeight());
        m.postTranslate(translateX, translateY);
        
        Paint paint = new Paint();
        canvas.drawBitmap(b, m, paint);
        setImageBitmap(alteredBitmap);
    }
    
    public void SetCenter(float x, float y)
    {
    	centerX = x;
    	centerY = y;
    }
    
    public void addLayer(Bitmap b, Matrix m) {
        mLayers.add(b);
        mMatrix.add(m);
        invalidate();
    }

    public void addLayer(int idx, Bitmap b) {
        mLayers.add(idx, b);
        invalidate();
    }

    public void removeLayer(int idx) {
        mLayers.remove(idx);
    }

    public void removeAllLayers() {
        mLayers.clear();
        invalidate();
    }

    public int getLayersSize() {
        return mLayers.size();
    }
    
    public Bitmap GetLayer(int i)
    {
    	return mLayers.get(i);
    }
    
    public void SetMatrix(int i, Matrix m)
    {
    	if(i >= mMatrix.size())
    	{
    		mMatrix.add(m);
    	}
    	else
    	{
    		mMatrix.set(i, m);
    	}
    }
    
    public Matrix getMatrix(int i)
    {
    	return mMatrix.get(i);
    }
    
    public void setDegress(int i, float degree)
    {
    	if(i < 2)
    	{
    		degrees[i] =  degree;
    	}
    }
    
    public float getDegree(int i)
    {
    	return degrees[i];
    }
}