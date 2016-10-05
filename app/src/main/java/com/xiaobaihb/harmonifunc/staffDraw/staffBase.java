package com.xiaobaihb.harmonifunc.staffDraw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import com.xiaobaihb.harmonifunc.R;

/**
 * Created by xiaobai on 16-8-14.
 */
public class staffBase extends View {

    private int width,heigh;
    private int perLine = getResources().getInteger(R.integer.perLine);
    private int gapLines = getResources().getInteger(R.integer.gapLines);
    private int widthClef = 4*perLine;
    private Bitmap clep;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public staffBase(Context context,int width,int heigh) {
        super(context);
        this.width = width;
        this.heigh = heigh;
        clep = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.clep_g)
                ,widthClef,2*widthClef,false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p1 = new Paint();
        Paint p2 = new Paint();
        p1.setColor(Color.BLACK);
        p1.setStrokeWidth(2);
        p2.setColor(Color.BLACK);
        p2.setStrokeWidth(5);

        canvas.drawLine(0 , 0 , width , 0 , p2);
        for (int i=0;i< heigh/(perLine*5+gapLines);i++) {
            int yBegin = gapLines+(perLine*5+gapLines)*i;
            int numBar = (width - widthClef)/(widthClef*4);
            int widthBar = (width - widthClef)/numBar;
            for (int j=0;j<5;j++)
                canvas.drawLine(0,yBegin+j*perLine,width,yBegin+j*perLine,p1);
            canvas.drawLine(0, yBegin, 0, yBegin + perLine * 4, p1);
            for (int k=1;k<=numBar;k++) {
                canvas.drawLine(k * widthBar + widthClef, yBegin, k * widthBar + widthClef, yBegin + perLine * 4, p1);
            }
            canvas.drawBitmap(clep,perLine,yBegin-2*perLine,null);
        }
    }


}
