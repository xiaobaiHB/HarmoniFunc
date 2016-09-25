package com.xiaobaihb.harmonifunc;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daasuu.library.FPSSurfaceView;
import com.xiaobaihb.harmonifunc.dsp.TonalityProcessor;
import com.xiaobaihb.harmonifunc.staffDraw.beatBar;
import com.xiaobaihb.harmonifunc.staffDraw.staffBase;

import be.tarsos.dsp.*;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;

public class MainAct extends AppCompatActivity {
    private static final String TAG = "harmoniFunc";

    public static final int SAMPLERATE = 22050;
    public static final int LEN_DATABUF = 1024;

    private int staffVH = 0,staffVW = 0;

    FrameLayout backGround;
    View staffBase = null;
    TextView debugText;
    FPSSurfaceView staffView;
    AudioDispatcher dispatcher;
    beatBar m_beatBar = new beatBar();

    protected String currentTone = "";
    protected double currentToneVal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        backGround = (FrameLayout) findViewById(R.id.backGround);
        debugText = (TextView) findViewById(R.id.debugText);
        staffView = (FPSSurfaceView) findViewById(R.id.staffView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(SAMPLERATE, LEN_DATABUF, 0);
        dispatcher.addAudioProcessor(new Oscilloscope(new TonalityProcessor(SAMPLERATE, LEN_DATABUF) {
            @Override
            public void baseTone(final String tonality, final double peakHigh) {
                currentTone = tonality;
                currentToneVal = peakHigh;
                debugText.post(new Runnable() {
                    @Override
                    public void run() {
                        debugText.setText(String.format("Tone:%3s,val:%.2f", currentTone, currentToneVal));
                    }
                });
            }
        }));
        new Thread(dispatcher,"Audio Dispatcher").start();
        new Thread(m_beatBar).start();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatcher.stop();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            staffVH = staffView.getHeight();
            staffVW = staffView.getWidth();
            if (staffBase == null) {
                staffBase = new staffBase(this, staffVW, staffVH);
                backGround.addView(staffBase);
            }
        }
    }

}
