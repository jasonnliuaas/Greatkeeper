package com.newline.core.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Color;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageUtils {

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = null;
        try {
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            // 得到画布
            Canvas canvas = new Canvas(output);
            // 将画布的四角圆化
            final int color = Color.RED;
            final Paint paint = new Paint();
            // 得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
            final Rect rect = new Rect(0, 0, width, height);
            final RectF rectF = new RectF(rect);
            // 值越大角度越明显
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            // 压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg);
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap = null;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
                
                // 从图中截取正中间的正方形部分。
                int xTopLeft = (scaledWidth - edgeLength) / 2;
                int yTopLeft = (scaledHeight - edgeLength) / 2;
                
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
            } catch (Exception e) {
                return null;
            } finally {
                if(scaledBitmap != null){
                    scaledBitmap.recycle();
                }
            }
        }

        return result;
    }

}
