package com.lamonjush.pinentryedittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Objects;

public class PinEntryEditText extends AppCompatEditText {

    float mSpace = 24; //24 dp by default
    float mCharSize = 0;
    float mNumChars = 4;
    float mLineSpacing = 16; //8dp by default

    private OnClickListener mClickListener;

    private PinEntryListener mPinEntryListener;

    private final BackgroundShape backgroundShape = new BackgroundShape();

    private int strokeColor = Color.GRAY;

    private Drawable focusedStateDrawable, unfocusedStateDrawable, selectedStateDrawable;

    int[][] mStates = new int[][]{
            new int[]{android.R.attr.state_selected}, // selected
            new int[]{android.R.attr.state_focused}, // focused
            new int[]{-android.R.attr.state_focused}, // unfocused
    };

    int[] mColors = new int[]{
            Color.GREEN,
            Color.BLACK,
            Color.GRAY
    };

    ColorStateList mColorStates = new ColorStateList(mStates, mColors);


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
            mColors[2] = a.getColor(R.styleable.PinEntryEditText_unFocusedStateLineColor, Color.GRAY);
            mColors[1] = a.getColor(R.styleable.PinEntryEditText_focusedStateLineColor, mColors[2]);
            mColors[0] = a.getColor(R.styleable.PinEntryEditText_selectedStateLineColor, mColors[1]);

            backgroundShape.setFillColor(a.getColor(R.styleable.PinEntryEditText_innerColor, Color.TRANSPARENT));
            backgroundShape.setCornerRadius(Utils.convertDpToPixel(a.getDimension(R.styleable.PinEntryEditText_lineCornerRadius, 4), getContext()));
            backgroundShape.setStrokeWidth((int) Utils.convertDpToPixel(a.getDimension(R.styleable.PinEntryEditText_lineWidth, 2), getContext()));


            setBackgroundDrawable(a);
            getPaint().setColor(getCurrentTextColor());
        } finally {
            a.recycle();
        }

        if (getFilters().length == 0) {
            mNumChars = 4;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(4);
            setFilters(fArray);
        } else {
            boolean valueSet = false;
            for (InputFilter filter : getFilters()) {
                if (filter instanceof InputFilter.LengthFilter) {
                    mNumChars = ((InputFilter.LengthFilter) filter).getMax();
                    if (mNumChars <= 0) {
                        mNumChars = 4;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(4);
                        setFilters(fArray);
                    }
                    valueSet = true;
                    break;
                }
            }
            if (!valueSet) {
                InputFilter[] fArray = new InputFilter[getFilters().length + 1];
                mNumChars = 4;
                fArray[0] = new InputFilter.LengthFilter(4);
                for (int i = 0; i < getFilters().length; i++) {
                    fArray[i + 1] = getFilters()[i];
                }
                setFilters(fArray);
            }
        }


        setCursorVisible(false);
        setTextIsSelectable(false);
        setGravity(Gravity.CENTER);

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

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mPinEntryListener != null) {
                    if (editable.toString().length() == mNumChars) {
                        mPinEntryListener.onPinEntered(editable.toString());
                    }
                }
            }
        });

        setOnFocusChangeListener((view, b) -> {
            if (b) {
                postDelayed(() -> ((EditText) view).setSelection(((EditText) view).getText().length()), 50);
            }
        });

    }

    private void setBackgroundDrawable(TypedArray a) {
        unfocusedStateDrawable = a.getDrawable(R.styleable.PinEntryEditText_unFocusedStateBackgroundDrawable);
        focusedStateDrawable = a.getDrawable(R.styleable.PinEntryEditText_focusedStateBackgroundDrawable);
        selectedStateDrawable = a.getDrawable(R.styleable.PinEntryEditText_selectedStateBackgroundDrawable);
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
        @SuppressLint("DrawAllocation") float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        for (int i = 0; i < mNumChars; i++) {

            Drawable drawable = getDrawable(i == textLength);
            if (drawable == null) {
                updateColorForLines(i == textLength);
                drawable = backgroundShape.getDrawable(strokeColor);
            }
            drawable.setBounds(startX, top, (int) (startX + mCharSize), bottom);
            drawable.draw(canvas);

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

    public void setPinEntryListener(PinEntryListener l) {
        mPinEntryListener = l;
    }


    private int getColorForState(int... states) {
        return mColorStates.getColorForState(states, Color.GRAY);
    }

    /* next = is the current char the next character to be input? */
    private void updateColorForLines(boolean next) {
        if (isFocused()) {
            strokeColor = getColorForState(android.R.attr.state_focused);
            if (next) {
                strokeColor = getColorForState(android.R.attr.state_selected);
            }
        } else {
            strokeColor = getColorForState(-android.R.attr.state_focused);
        }
    }

    private Drawable getDrawable(boolean next) {
        if (isFocused()) {
            Drawable drawable;
            drawable = focusedStateDrawable;
            if (next) {
                drawable = selectedStateDrawable;
            }
            return drawable;
        } else {
            return unfocusedStateDrawable;
        }
    }

}

