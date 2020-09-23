package com.thriive.app.models;

import android.graphics.drawable.Drawable;

public class SelectBusinessPOJO {
    Drawable drawable;

    public SelectBusinessPOJO() {
    }

    public SelectBusinessPOJO(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
