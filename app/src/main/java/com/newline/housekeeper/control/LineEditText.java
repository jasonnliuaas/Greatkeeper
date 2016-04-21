package com.newline.housekeeper.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.newline.core.utils.AndroidUtils;
import com.greatkeeper.keeper.R;

public class LineEditText extends EditText {

    private Paint mPaint;

    /**
     * @param context
     * @param attrs
     */
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.lineColor));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int oneDp = AndroidUtils.dip2px(getContext(), 1);
        
        // 画底线
        canvas.drawLine(0, this.getHeight() - oneDp, this.getWidth() * 3, this.getHeight() - oneDp, mPaint);
    }

}
