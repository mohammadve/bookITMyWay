package com.virtual.customervendor.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.virtual.customervendor.R;


public class CustomButton extends android.support.v7.widget.AppCompatButton {


    /**
     * @param context refer to the context
     */
    public CustomButton(Context context) {
        super(context);
    }

    /**
     * @param context refer to the context
     * @param attrs   the attributes to be applied
     */
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    /**
     * @param context  refer to the context
     * @param attrs    the attributes to be applied
     * @param defStyle the style to be applied
     */
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    /**
     * @param ctx   refer to the context
     * @param attrs the attributes to be applied
     */
    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        String customFont = a.getString(R.styleable.CustomButton_customFontbtn);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    /**
     * @param ctx   refer to the context
     * @param asset the asset to be applied
     * @return returns a boolean value
     */
    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            return false;
        }
        setTypeface(tf);
        return true;
    }


}
