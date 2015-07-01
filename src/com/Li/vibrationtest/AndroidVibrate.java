//package com.abonvita.game.framework.impl;
package com.Li.vibrationtest;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

/*___________________________________________________________________
|
| Class: AndroidVibrate
|
| Description: Interface to device vibrator.
|__________________________________________________________________*/
public class AndroidVibrate
{   
	Vibrator vibrator = null;
	boolean enabled = false;	// device defaults to disabled.  Call enable() before using.
    
	/*___________________________________________________________________
	|
	| Function: AndroidInput (constructor)
	|__________________________________________________________________*/
    public AndroidVibrate (Context context) 
    {
    	vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

	/*___________________________________________________________________
	|
	| Function: enable
	|
	| Description: Enables/disables vibrations.
	|__________________________________________________________________*/
    public void enable (boolean flag)
    {
    	// Enable it
    	if (flag) {
    		if (vibrator != null)
    			enabled = true;
    	}
    	// Disable it
       	else {
    		if (enabled) {
    			stop ();
    			enabled = false;
    		}
    	}
    }

    /*___________________________________________________________________
	|
	| Function: start
	|
	| Description: Vibrates.
	|__________________________________________________________________*/
    public void start (long milliseconds) 
    {
    	if (enabled)
    		vibrator.vibrate (milliseconds);
    }

	/*___________________________________________________________________
	|
	| Function: start
	|
	| Description: Vibrates using a pattern such as:
	|	{ 2000, 1000, 5000 }
	|		wait 2 sec, vib 1 sec, wait 5 sec (run indefinitely)
	|	Set offset to -1 to cause no repeat of the pattern
	|
	| Source: Android Developer's Cookbook, pg. 191.
	|__________________________________________________________________*/
    public void start (long [] pattern, int offset) 
    {
    	if (enabled)
    		vibrator.vibrate (pattern, offset);
    }

	/*___________________________________________________________________
	|
	| Function: stop
	|
	| Description: Stops vibrating.
	|__________________________________________________________________*/
    public void stop () 
    {
    	Log.i("Vibration", "stop viabrate:" + String.valueOf(enabled));
    	if (enabled)
    		vibrator.cancel ();	
    }
    
	/*___________________________________________________________________
	|
	| Function: genVibratorPattern
	|
	| Description: set intensity for vibration. It will return an array 
	|              pattern that is used in start(pattern[],...) interface
	|__________________________________________________________________*/
    public void genVibratorPatternContinues( long intensity )
    {
    	long duration = 100;
    	int value[] = {20, 25, 25, 30, 40, 50,60, 70, 80, 90};
    	if(intensity >= 100)
    	{
    		intensity = 99;
    	}
    	//Log.i("VibrateTest","intensity:"+intensity);
    	int index = (int)intensity/10;
        long hWidth = value[index] + intensity%10;
        long lWidth = duration - hWidth;
        long[] pattern = {lWidth, hWidth, lWidth, hWidth, lWidth, hWidth, lWidth, hWidth};
        Log.i("Vibrate","genVibratorPatternContinues:"+pattern);
        
        start(pattern, 0);

    }
    
	/*___________________________________________________________________
	|
	| Function: genVibratorPatternContinuesInShort
	|
	| Description: set intensity for vibration. It will return an array 
	|              pattern that is used in start(pattern[],...) interface
	|__________________________________________________________________*/
    public void genVibratorPatternContinuesInShort( long intensity )
    {
    	long duration = 100;
    	int value[] = {5, 10, 20, 30, 40, 50,60, 70, 80, 90};
    	if(intensity >= 100)
    	{
    		intensity = 99;
    	}
    	//Log.i("VibrateTest","intensity:"+intensity);
    	int index = (int)intensity/10;
        long hWidth = (value[index] + intensity%10)/10;
        long[] pattern = {hWidth, hWidth, hWidth, hWidth, hWidth, hWidth, hWidth, hWidth};
        Log.i("Vibrate","genVibratorPatternContinuesInShort:"+pattern);
        
        start(pattern, 0);
    }
}