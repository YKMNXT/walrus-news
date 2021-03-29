package com.walrus.news.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.walrus.news.R;

public class CustomFontTextView extends TextView {
    private int typefaceType;
    private Typeface typeface;

    public CustomFontTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomFontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomFontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initTypeface(context, attrs);
    }

    private void init(Context context) {
        initTypeface(context, null);
    }

    private void initTypeface(Context context, AttributeSet attrs) {
            if (attrs != null) {
                TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
                String fontName = a.getString(R.styleable.CustomFontTextView_customTypeface);
                setCustomFont(context, fontName);
                a.recycle();
            } else {
                setCustomFont(context, null);
            }
    }

    public boolean setCustomFont(Context ctx, String fontName) {
        Typeface typeface;
        try {
            if(fontName == null){
                fontName = AppConstants.DEFAULT_FONT_NAME;
            }
            typeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/" + fontName);
        } catch (Exception e) {
//            Log.e(TAG, "Unable to load typeface: "+e.getMessage());
            return false;
        }
        setTypeface(typeface);
        return true;
    }

}
