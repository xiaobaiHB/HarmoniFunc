package com.xiaobaihb.harmonifunc.staffDraw;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.daasuu.library.DisplayObject;
import com.daasuu.library.FPSSurfaceView;
import com.daasuu.library.drawer.BitmapDrawer;
import com.daasuu.library.easing.Ease;
import com.xiaobaihb.harmonifunc.R;

/**
 * Created by xiaobai on 16-8-28.
 */
public class meterBase implements Runnable {

    private static final String TAG = "meterBase";
    private final FPSSurfaceView staffView;
    private final Context context;
    private final int w_halfBitmap = 50;
    private Bitmap m_bitMap;

    protected int speedMeter = 60;
    protected int beatNum = 4;

    protected int cntBeat = 0;//dev in 4
    protected int cntBar = 0;

    protected int timeTick = 1;

    protected DisplayObject beatDisplay = null;

    public meterBase(Context context, FPSSurfaceView staffView) {
        timeTick = 60000/ speedMeter;
        this.context = context;
        this.staffView = staffView;
        m_bitMap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.beat_point)
                ,w_halfBitmap*2,w_halfBitmap*2/5,false);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (++cntBeat >= beatNum * 4) {
                    cntBeat = 0;
                    cntBar++;
                }
                if (cntBeat % 4 == 0)
                    flashDisplay(cntBeat/4);
                Thread.sleep(timeTick/4);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void flashDisplay(int cntBeat) {
        int width = staffView.getWidth();
        if (width == 0)
            return;
        if (beatDisplay != null)
            staffView.removeChild(beatDisplay);
        beatDisplay = new DisplayObject();
        if (cntBeat % 2 == 0) {
            beatDisplay.with(new BitmapDrawer(m_bitMap))
                    .tween()
                    .transform(width / 2 - w_halfBitmap, 0)
                    .toX(timeTick / 2, width - w_halfBitmap, Ease.CIRC_IN)
                    .toX(timeTick / 2, width / 2 - w_halfBitmap, Ease.CIRC_OUT)
                    .end();
        } else {
            beatDisplay.with(new BitmapDrawer(m_bitMap))
                    .tween()
                    .transform(width / 2 - w_halfBitmap, 0)
                    .toX(timeTick / 2, -w_halfBitmap, Ease.CIRC_IN)
                    .toX(timeTick / 2, width / 2 - w_halfBitmap, Ease.CIRC_OUT)
                    .end();
        }
        staffView.addChild(beatDisplay);
    }
}

