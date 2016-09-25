package com.xiaobaihb.harmonifunc.staffDraw;


/**
 * Created by xiaobai on 16-8-28.
 */
public class beatBar implements Runnable {

    protected int speedBeat = 120;
    protected int beatNum = 4;

    protected int cntBeat = 0;
    protected int cntBar = 0;

    protected int timeTick = 1;

    public beatBar() {
        timeTick = 30000/speedBeat;
    }

    @Override
    public void run() {

        while (true) {
            try {
                if (cntBeat ++ >beatNum * 2) {
                    cntBeat = 0;
                    cntBar++;
                }

                Thread.sleep(timeTick);
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
