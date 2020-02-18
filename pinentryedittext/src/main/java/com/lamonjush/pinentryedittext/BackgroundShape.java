package com.lamonjush.pinentryedittext;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

class BackgroundShape {
    private int fillColor, strokeWidth;
    private float cornerRadius;

    void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    Drawable getDrawable(int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        return gradientDrawable;
    }
}
