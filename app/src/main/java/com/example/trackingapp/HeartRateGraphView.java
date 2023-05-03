package com.example.trackingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.Path;

public class HeartRateGraphView extends View {

    private Paint linePaint;
    private Path linePath;
    private float[] dataPoints;
    private float maxValue = 0;
    private float minValue = 0;

    public HeartRateGraphView(Context context) {
        super(context);
        init();
    }

    public HeartRateGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
        linePaint.setColor(Color.RED);

        linePath = new Path();
    }

    public void setDataPoints(float[] dataPoints) {
        this.dataPoints = dataPoints;

        //Suche maximalen und minimalen Datenpunkt
        for (float dataPoint : dataPoints) {
            if (dataPoint > maxValue) {
                maxValue = dataPoint;
            }
            if (dataPoint < minValue) {
                minValue = dataPoint;
            }
        }

        //Ansicht aktualisieren
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.length == 0) {
            return;
        }

        //Abstand zwischen Punkten bestimmen
        float xInterval = (float) getWidth() / (dataPoints.length - 1);
        float yInterval = (float) getHeight() / (maxValue - minValue);

        //Startpunkt des Pfads
        linePath.moveTo(0, getHeight() - ((dataPoints[0] - minValue) * yInterval));

        //Zeichne Linie durch Datenpunkte
        for (int i = 1; i < dataPoints.length; i++) {
            float x = i * xInterval;
            float y = getHeight() - ((dataPoints[i] - minValue) * yInterval);
            linePath.lineTo(x, y);
        }

        //Zeichne Line auf Ansicht
        canvas.drawPath(linePath, linePaint);
    }

}
