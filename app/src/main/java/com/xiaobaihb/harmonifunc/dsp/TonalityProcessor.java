package com.xiaobaihb.harmonifunc.dsp;


import com.xiaobaihb.harmonifunc.Utils.TonalityUtil;

import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HammingWindow;

/**
 * Created by xiaobai on 16-6-19.
 */
public abstract class TonalityProcessor implements Oscilloscope.OscilloscopeEventHandler {

    private int numPointFFT = 1024;
    private int rateSample = 22050;
    private float devFreqent;

    private FFT fft = null;
    private int thresholdRMSFilter = 3;
    private int thresholdPower = 12;
    private int windFreqHistLow = 10;//c1
    private int windFreqHistHigh = 100;//c4

    private float[] mapWhiteNoisePower;
    private float[] fftPowerMap;
    private float[] fftPhaseMap;
    private int[] histFreqInterval;
    private List<Integer> peakList = new ArrayList<>();

    private float peakHigh;
    private boolean isNeedAddToMap;
    private double remainder;

    private String tone = "", toneChg = "";

    public abstract void baseTone(String tonality, double peakHigh);

    public TonalityProcessor(int rateSample, int numPointFFT) {
        this.numPointFFT = numPointFFT;
        this.rateSample = rateSample;
        devFreqent = (float)this.rateSample/this.numPointFFT;

        fft = new FFT(numPointFFT, new HammingWindow());
        mapWhiteNoisePower = new float[numPointFFT /2];
        fftPowerMap = new float[numPointFFT /2];
        fftPhaseMap = new float[numPointFFT /2];
    }

    @Override
    public void handleEvent(float[] floats, AudioEvent audioEvent) {
        fft.powerPhaseFFT(audioEvent.getFloatBuffer(), fftPowerMap, fftPhaseMap);

        //filter:get white noise power map
        if (audioEvent.getRMS() < thresholdRMSFilter) {
            for (int i=0;i< fftPowerMap.length;i++)
                mapWhiteNoisePower[i] = (mapWhiteNoisePower[i]+ fftPowerMap[i])/2;
            peakList.clear();
            baseTone(TonalityUtil.getToneByHz(0), audioEvent.getRMS());
        }
        else {
            for (int i=0;i< fftPowerMap.length;i++) {
                fftPowerMap[i] = fftPowerMap[i] > mapWhiteNoisePower[i] ? fftPowerMap[i] - mapWhiteNoisePower[i] : 0;
            }

            //hist:calculate a hist map of frequent interval between window
            histFreqInterval = new int[windFreqHistHigh];
            for (int i = 0;i< fftPowerMap.length - windFreqHistHigh;i++) {
                if (fftPowerMap[i] > thresholdPower) {
                    for (int j = windFreqHistLow;j < windFreqHistHigh;j++){
                        if (fftPowerMap[i+j] > thresholdPower) {
                            histFreqInterval[j]++;
                        }
                    }
                }
            }

            peakList.clear();
            for (int i = windFreqHistLow;i < histFreqInterval.length - 5;i++) {
                peakHigh = 0.4f*(histFreqInterval[i]+histFreqInterval[i+1]+histFreqInterval[i+2]+histFreqInterval[i+3]+histFreqInterval[i+4]);
                if (histFreqInterval[i+2] > 0.7*peakHigh && histFreqInterval[i+2] < 1.5*peakHigh) {
                    isNeedAddToMap = true;
                    for (Integer subPeak : peakList) {
                        remainder = (i+2d)%subPeak/subPeak;
                        if (remainder < 0.5 || remainder > 0.95) {
                            isNeedAddToMap = false;
                            break;
                        }
                        if (i+1 == subPeak) {
                            isNeedAddToMap = false;
                            break;
                        }
                    }
                    if (isNeedAddToMap) {
                        peakList.add(i + 2);
                        if (peakList.size() == 1)
                            baseTone(TonalityUtil.getToneByHz((i+2)*devFreqent), audioEvent.getRMS());
                    }
                }
            }
        }

    }
}
