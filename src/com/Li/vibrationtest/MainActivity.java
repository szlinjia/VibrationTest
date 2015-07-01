
package com.Li.vibrationtest;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity 
{
	
	public class MyOnTouchListener implements OnTouchListener 
	{
		double preTouchY = 0;
		double firstTouchAngle = 0;
		double firstTouchY = 0;
		double initDelta = 0;
		int moveEventCnt = 0;
		int N = 10;
		ArrayList<Double> lst = new ArrayList<Double>();
		
	    @Override
	    public boolean onTouch(View v, MotionEvent event) 
	    {
	        switch (event.getAction()) 
	        {
	            case MotionEvent.ACTION_DOWN:
	            	dialer.isBtnUp = false;
	            	vibrator.stop();
	            	lastAngle = firstTouchAngle = getAngle(event.getX(), event.getY());
	            	preTouchY = firstTouchY = event.getY();
	            	initDelta = firstTouchAngle;
	            	lst.clear();
	                break;
	                 
	            case MotionEvent.ACTION_MOVE:
	            	moveEventCnt++;
	            	if(moveEventCnt%N == 0)
	            	{
		            	if(isInBound(event.getX(), event.getY()))
		            	{
		            		dialer.isClockwise = isClockwise(event.getX(), event.getY());
							ProcessPointer(event.getX(), event.getY());
		            	}
		            	moveEventCnt = 0;
		            	dialer.invalidate();
	            	}
	                break;
	                 
	            case MotionEvent.ACTION_UP:
	            	dialer.isBtnUp = true;
	            	vibrator.stop();
	                break;
	        }       
	        
	        return true;
	    }
	    
	 
	    private boolean isInBound(float x, float y)
	    {
	    	boolean isInBound = true;
	    	float centerX = dialerWidth/2;
            float centerY = dialerHeight/2;
            float dy = y - centerY;
            float dx = x - centerX;
            
            double distance = Math.sqrt(dy*dy+dx*dx);
            if(distance > (imageOriginal.getWidth()/2+5))
            {
            	isInBound = false;
            }
	    	return isInBound;
	    }
	    
	    private double lastAngle;
	    private int circleCount = 0;
	    private int errorValue = 10;
	    private boolean isClockwise(float x, float y) 
	    {
	    	boolean isClockWise = true;
	    	double dy = y - dialerHeight / 2d;
	    	
	        if(true)
	        {
		        double angle = getAngle(x,y);
		        
		        if(angle >= 360)
		        {
		        	angle -= 360;
		        }
		        
		        if(lastAngle > 270 && angle > 0 && angle < 90 )
		        {
		        	angle = lastAngle + 0.1;
		        }
		        else if(lastAngle > 0 && lastAngle < 90 && angle >270)
		        {
		        	angle = lastAngle - 0.1;
		        }
		        else if(Math.abs(angle - lastAngle) > 100)
		        {
		        	angle = lastAngle;
		        }
		        else if(angle == 0)
		        {
		        	angle = lastAngle;
		        }
		        
	       
		        if(angle - lastAngle > 0)
		        {
		        	isClockWise = true;
		        }
		        else if(angle - lastAngle < 0)
		        {
		        	isClockWise = false;
		        }
		        else if(angle - lastAngle == 0)
		        {
		        	isClockWise = dialer.isClockwise;
		        }
		        
		        //Log.i("Vibrate", "angle:" + (int)angle);
	        	//Log.i("Vibrate", "lastAngle:" + (int)lastAngle);
	        	//Log.i("Vibrate", "dialer.isClockwise:"+isClockWise);
	        	
	        	lastAngle = angle;
	        	
		        if(isClockWise)
		        { 
		        	if(angle > 360 - errorValue)
		        	{
		        		circleCount = circleCount + 1;
		        	}
	        
		        }
		        else
		        {
			        if(angle < errorValue)
			        {
			        	if(circleCount > 0)
			        	{
			        		circleCount = circleCount - 1;
			        	}
			        }
		        }
	        
	        }
	        
	        return isClockWise;
	    }
	    
	    private void ProcessPointer(float x, float y)
	    {
	    	double curDegree = getAngle(x, y);
	    	double dy = y - dialerHeight / 2d;
	    	double dx = x - dialerWidth/2d;
	    	double delta = curDegree - firstTouchAngle;
	    	
	    	if( dx > 0)
	    	{
	    		double firstdy = (firstTouchY-dialerHeight / 2d);
	    		//moves across 0 degree
	    		if(firstdy/dy < 0)
	    		{
	    			if(firstdy< 0)
	    			{
	    				delta = 360-firstTouchAngle+curDegree;
	    			}
	    			else
	    			{
	    				delta = 360-curDegree + firstTouchAngle;
	    				
	    			}
	    		}
	    	}
	    	
	    	//set outer dialer
			float dialerAngle = dialer.getDegree(0);
			float cur = (float) delta + dialer.getDegree(1);
			if(!dialer.isClockwise)
			{
				cur = (float) (dialer.getDegree(1) - Math.abs(delta));
			}
			if(cur < 0)
			{
				cur += 360;
			}
			else if(cur > 360)
			{
				cur -= 360;
			}
			dialer.setDegress(0, (float)cur);
			
			Log.i("Vibrate", "cur:" + (int)cur);
			Log.i("Vibrate", "delta:" + (int)delta);
			Log.i("Vibrate", "dialerAngle:" + (int)dialerAngle);
			Log.i("Vibrate", "dialer.isClockwise:"+dialer.isClockwise);
        
        	double progress = (Math.abs(delta) * 100)/intervalAngle;
        	if(vibrateIndex == 0)
        	{
        		vibrator.genVibratorPatternContinues((long)progress);
        	}
        	else if(vibrateIndex == 1)
        	{
        		vibrator.genVibratorPatternContinuesInShort((long)progress);
        	}
        	
     
        	if(Math.abs(delta) >= intervalAngle)
        	{
        		float ptrDegree = dialer.getDegree(1);
        		if(dialer.isClockwise)
        		{
        			ptrDegree += intervalAngle;
        			if(ptrDegree > 360)
            		{
            			ptrDegree -= 360;
            		}
        		}
        		else
        		{
        			ptrDegree -= intervalAngle;
        			if(ptrDegree < 0)
            		{
            			ptrDegree += 360;
            		}
        		}
        		
        		dialer.setDegress(1, ptrDegree);
        		sound.playsound("pointer");
        		firstTouchAngle = curDegree;
        		firstTouchY = y;
        	}
	    }
	    
	    
	    private double getAngle(double xTouch, double yTouch) 
	    {
	        double x = xTouch - (dialerWidth / 2d);
	        double y = yTouch - (dialerHeight / 2d);

	        double degree = Math.toDegrees(Math.atan2(y, x));
	       
	        if(degree < 0)
	        {
	        	degree += 360;
	        }
	        
	        return degree;
	    }
	 
	}
	
	private static Bitmap imageOriginal, pointerOrg;
	 
	private LayeredImageView dialer;
	private Button btnRotate;
	private Button btnVibrate;
	private int dialerHeight, dialerWidth;
	private AndroidVibrate vibrator = null;
	private int    intervalAngle = 90; 
    private SoundManager sound = SoundManager.getInstance();

	private int vibrateIndex = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        Log.i("","Vibration app start.");
        
		// Start the view
        vibrator = new AndroidVibrate(this);
        vibrator.enable(true);
        
	    // load the image only once
	    if (imageOriginal == null) 
	    {
	        imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.dailcircle_300x300);
	    }
	    
	    if( pointerOrg == null)
	    {
	    	pointerOrg = BitmapFactory.decodeResource(getResources(), R.drawable.pointer);
	    }
	     
        
	    MyOnTouchListener touchListen = new MyOnTouchListener();
	    
    	setContentView(R.layout.relative);
    	dialer = (LayeredImageView)findViewById(R.id.imageView1);
    	btnRotate = (Button) findViewById(R.id.button1);
    	btnRotate.setText("4");

    	btnVibrate = (Button) findViewById(R.id.button2);
    	
    	btnRotate.setOnClickListener(mClickListener);
    	btnVibrate.setOnClickListener(mClickListener);
    	
	    dialer.setOnTouchListener(touchListen);
	    dialer.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);
	    sound.Initialize(this);
    }

	@Override
	protected void onPause () 
	{
		super.onPause();
	}

	@Override
	protected void onResume () 
	{
		super.onResume();
	}
	
	final View.OnClickListener mClickListener = new View.OnClickListener()
	{

		String[] arrTx = {"4", "8", "16"}; 
		String[] arrVibrate = {"Vibrate1", "Vibrate2"};
		
		int curIndex = 0;
		@Override
		public void onClick(View v) 
		{
			int nClickId = v.getId();
			if(nClickId == R.id.button1)
			{
				curIndex++;
				if(curIndex >= arrTx.length)
				{
					curIndex = 0;
				}
				
				btnRotate.setText(arrTx[curIndex]);
		    	intervalAngle = 360/Integer.parseInt(arrTx[curIndex]);
		    	dialer.setDegress(1, 0);
		    	dialer.setDegress(0, 0);
			}
			else if(nClickId == R.id.button2)
			{
				vibrateIndex++;
				if(vibrateIndex >= arrVibrate.length)
				{
					vibrateIndex = 0;
				}
				
				btnVibrate.setText(arrVibrate[vibrateIndex]);
				
			}
		}
		
		
	};

	final OnGlobalLayoutListener mLayoutListener = new OnGlobalLayoutListener() 
    {
        @Override
        public void onGlobalLayout() 
        {
            // method called more than once, but the values only need to be initialized one time
            if (dialerHeight == 0 || dialerWidth == 0) 
            {
            	dialerHeight = dialer.getHeight();
                dialerWidth = dialer.getWidth();
                float centerX = dialerWidth/2;
                float centerY = dialerHeight/2;
                dialer.SetCenter(centerX, centerY);
                
                Matrix m = new Matrix();
                float translateX = centerX - imageOriginal.getWidth()/2;
                float translateY = centerY - imageOriginal.getHeight()/2;
                
                Bitmap alteredBitmap = Bitmap.createBitmap(imageOriginal.getWidth(), imageOriginal
    		            .getHeight(), imageOriginal.getConfig());
                m.postTranslate(translateX, translateY);
                dialer.setImageMatrix(m);
                dialer.setImageBitmap(alteredBitmap);
                
       
           	    Resources res = getResources();
        	    dialer.addLayer(0,imageOriginal);
        	    dialer.addLayer(1, pointerOrg);
        	   

            }
        }
    };
}
