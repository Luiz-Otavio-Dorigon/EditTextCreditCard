package br.com.dorigon.edittextcreditcard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.SparseArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Luiz Otávio Dorigon <luiz@datajuri.com.br> on 31/07/18.
 */
public class EditTextCreditCard extends AppCompatEditText {

    private SparseArray<Pattern> mCCPatterns = null;
    private int mCurrentDrawableResId = 0;
    private Drawable mCurrentDrawable;

    public EditTextCreditCard(Context context) {
        super(context);
        init();
    }

    public EditTextCreditCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextCreditCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mCCPatterns == null) {
            mCCPatterns = new SparseArray<>();
            // Without spaces for credit card masking
            mCCPatterns.put(R.drawable.ic_credit_card_visa, Pattern.compile("^4[0-9]{2,12}(?:[0-9]{3})?$"));
            mCCPatterns.put(R.drawable.ic_credit_card_mastercard, Pattern.compile("^5[1-5][0-9]{1,14}$"));
            mCCPatterns.put(R.drawable.ic_credit_card_amex, Pattern.compile("^3[47][0-9]{1,13}$"));
            mCCPatterns.put(R.drawable.ic_credit_card_elo, Pattern.compile("^(?:401178|401179|431274|438935|451416|457393|457631|457632|504175|627780|636297|636368|655000|655001|651652|651653|651654|650485|650486|650487|650488|506699|5067[0-6][0-9]|50677[0-8]|509\\d{3})\\d{10}$"));
            mCCPatterns.put(R.drawable.ic_credit_card_discover, Pattern.compile("^6(?:011\\d{12}|5\\d{14}|4[4-9]\\d{13}|22(?:1(?:2[6-9]|[3-9]\\d)|[2-8]\\d{2}|9(?:[01]\\d|2[0-5]))\\d{10})$"));
            mCCPatterns.put(R.drawable.ic_credit_card_diners_club, Pattern.compile("(^30[0-5][0-9]{11}$)|(^(36|38)[0-9]{12}$)"));
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        if (mCCPatterns == null) {
            init();
        }

        int mDrawableResId = 0;
        for (int i = 0; i < mCCPatterns.size(); i++) {
            int key = mCCPatterns.keyAt(i);
            // get the object by the key.
            Pattern p = mCCPatterns.get(key);

            Matcher m = p.matcher(text);
            if (m.find()) {
                mDrawableResId = key;
                break;
            }
        }
        if (mDrawableResId > 0 && mDrawableResId != mCurrentDrawableResId) {
            mCurrentDrawableResId = mDrawableResId;
        } else if (mDrawableResId == 0) {
            mCurrentDrawableResId = R.drawable.ic_credit_card;
        }

        mCurrentDrawable = getResources().getDrawable(mCurrentDrawableResId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentDrawable == null) {
            return;
        }

        int rightOffset = 0;
        if (getError() != null && getError().length() > 0) {
            rightOffset = (int) getResources().getDisplayMetrics().density * 32;
        }

        int right = getWidth() - getPaddingRight() - rightOffset;

        int top = getPaddingTop();
        int bottom = getHeight() - getPaddingBottom();
        float ratio = (float) mCurrentDrawable.getIntrinsicWidth() / (float) mCurrentDrawable.getIntrinsicHeight();
        //int left = right - mCurrentDrawable.getIntrinsicWidth(); //If images are correct size.
        int left = (int) (right - ((bottom - top) * ratio)); //scale image depeding on height available.
        mCurrentDrawable.setBounds(left, top, right, bottom);
        mCurrentDrawable.draw(canvas);
    }
}