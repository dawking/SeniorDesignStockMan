package com.example.seniordesignstockman;
import android.content.Context;
import android.os.Vibrator;
public class vibration {

    public void vibrator(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        v.vibrate(400);
    }

    public Object getSystemService(String vibratorService) {
        return 0;
    }
}
