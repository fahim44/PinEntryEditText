package com.lamonjush.pinentryedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

public class PinEntryEditText extends AppCompatEditText {

    float mSpace = 24; //24 dp by default
    float mCharSize = 0;
    float mNumChars = 4;
    float mLineSpacing = 16; //8dp by default
    float mBoxWidth = 10;
    float mBoxCornerRadius = 8;
    Paint mOuterBoxPaint = new Paint();
    Paint mInnerBoxPaint = new Paint();

    private OnClickListener mClickListener;

    public PinEntryEditText(Context context) {
        super(context);
    }

    public PinEntryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEntryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundResource(0);

        float multi = context.getResources().getDisplayMetrics().density;
        mSpace = multi * mSpace; //convert to pixels for our density
        mLineSpacing = multi * mLineSpacing; //convert to pixels

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PinEntryEditText, 0, 0);
        try {
            mOuterBoxPaint.setColor(a.getColor(R.styleable.PinEntryEditText_lineColor, Color.RED));
            mInnerBoxPaint.setColor(a.getColor(R.styleable.PinEntryEditText_innerColor, Color.WHITE));
            mBoxCornerRadius = a.getDimension(R.styleable.PinEntryEditText_lineCornerRadius, 8);
            mBoxWidth = a.getDimension(R.styleable.PinEntryEditText_lineWidth, 10);
            getPaint().setColor(getCurrentTextColor());
        } finally {
            a.recycle();
        }
        for (InputFilter filter : getFilters()) {
            if (filter instanceof InputFilter.LengthFilter) {
                mNumChars = ((InputFilter.LengthFilter) filter).getMax();
                if (mNumChars <= 0)
                    mNumChars = 4;
                break;
            }
        }

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(
                new ActionMode.Callback() {
                    public boolean onPrepareActionMode(ActionMode mode,
                                                       Menu menu) {
                        return false;
                    }

                    public void onDestroyActionMode(ActionMode mode) {
                    }

                    public boolean onCreateActionMode(ActionMode mode,
                                                      Menu menu) {
                        return false;
                    }

                    public boolean onActionItemClicked(ActionMode mode,
                                                       MenuItem item) {
                        return false;
                    }
                });
        //When tapped, move cursor to end of the text
        super.setOnClickListener(v -> {
            setSelection(Objects.requireNonNull(getText()).length());
            if (mClickListener != null) {
                mClickListener.onClick(v);
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        int availableWidth =
                getWidth() - getPaddingRight() - getPaddingLeft();
        if (mSpace < 0) {
            mCharSize = (availableWidth / (mNumChars * 2 - 1));
        } else {
            mCharSize =
                    (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        /*int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();
        int top = getPaddingTop();*/
        int startX = 0;
        int bottom = getHeight();
        int top = 0;

        //Text Width
        Editable text = getText();
        int textLength = Objects.requireNonNull(text).length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        for (int i = 0; i < mNumChars; i++) {
            canvas.drawRoundRect(startX, top, startX + mCharSize, bottom, mBoxCornerRadius, mBoxCornerRadius, mOuterBoxPaint);
            canvas.drawRoundRect(startX + mBoxWidth, top + mBoxWidth, startX + mCharSize - mBoxWidth, bottom - mBoxWidth, mBoxCornerRadius, mBoxCornerRadius, mInnerBoxPaint);

            if (getText().length() > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText(text,
                        i,
                        i + 1,
                        middle - textWidths[0] / 2,
                        bottom - mLineSpacing,
                        getPaint());
            }

            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + mSpace;
            }
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }
}

