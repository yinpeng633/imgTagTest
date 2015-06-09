package com.kit.yinp.imgtagtest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kit.yinp.imgtagtest.R;

/**
 * Created by yinp on 2015/6/3.
 */
public class ImgTagView extends View {

    private int centerX;
    private int centerY;

   // private boolean drawonce = false;
    private boolean centerClick = false;
    private boolean otherAreaClick = false;


    //字符串数组 一个时放中间  两个时 放两边
    private String[] drawText = {"圣地亚哥","五星好评，正品","260"};
    private int[] oneLefttwoRight = {45,135,-45};
    private int[] twoLeftoneRight = {45,-135,-45};
    private int[] threeLeft = {180,-90,0};  //这个特殊点
    private int[] threeRight = {180,90,0};
    private int drawStyle = 0;
    int screenWidth;
    int screenHeight;
    //拖动相关
    int lastX;
    int lastY;
    //假边界
    float realLeft = 0;
    float realRight = 0;

    private boolean canTouch = true;
    //是否屏蔽 触摸事件
    public void setCanTouch(boolean canTouch){
        this.canTouch = canTouch;
    }


    //自定义动画相关

    //中间logo宽度变化
    private float centerPicWidth = 1;
    //中间波纹透明度
    private float waveAlpha = 1;
    //中间波纹半径
    private float waveRadius;

    Paint paint;
    Bitmap bit;
    boolean drawline = true;

    private boolean outofRight = false;



    public void setDrawline(boolean drawline){
        this.drawline = drawline;
    }




    public void drawOnce(){
        if(drawStyle == 3){
            drawStyle = 0;
        }else{
            drawStyle ++;
        }
        invalidate();
    }
    public ImgTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImgTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ImgTagView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        screenWidth = ScreenUtils.getWindowWidth(context);
        screenHeight = (int)ScreenUtils.dpToPx(context,400.0f);
        paint = new Paint();
        bit = BitmapFactory.decodeResource(getResources(), R.drawable.ic_tag_circle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTag(canvas);
    }

    public final float getCenterPicWidth(){
        return this.centerPicWidth;
    }

    public final void setCenterPicWidth(float centerPicWidth){
        this.centerPicWidth = centerPicWidth;
    }

    public final float getWaveAlpha(){
        return this.waveAlpha;
    }

    public final void setWaveAlpha(float waveAlpha){
        this.waveAlpha = waveAlpha;

    }

    public final float getWaveRadius(){
        return this.waveRadius;
    }

    public final void setWaveRadius(float waveRadius){
        this.waveRadius = waveRadius;
        invalidate();
    }


    public void setTags(String[] tags){
        this.drawText = tags;
    }


    private void drawTag(Canvas canvas){

        //左一 右二
        //左二 右一
        //左三 右无
        //左无 右三
        //四种

        //画中心

        centerX = getWidth()/2;
        centerY = getHeight()/2;

        //画中心点

        int bitw = bit.getWidth();
        int bith = bit.getHeight();
//        float extra = 100;

        paint.setAntiAlias(true);
        paint.setStrokeWidth((int)ScreenUtils.dpToPx(getContext(),1.5f));

        paint.setColor(Color.WHITE);
        paint.setAlpha((int)(255.0f * waveAlpha));
        canvas.drawCircle(centerX, centerY, waveRadius, paint);
        paint.setAlpha(255);
        float centerLeft = centerX - bitw/2 * centerPicWidth;
        float centerTop = centerY - bith / 2 * centerPicWidth;
        float centerRight = centerX + bitw/2 * centerPicWidth;
        float centerBottom = centerY + bith / 2 * centerPicWidth;

        canvas.drawBitmap(bit,new Rect(0,0,bitw,bith),new RectF(centerLeft,centerTop,centerRight,centerBottom),paint);
        //canvas.drawBitmap(bit, centerX - bitw / 2, centerY - bith / 2, paint);
        //45度角斜

        // 左一右二
        if(drawline){
            if(drawStyle == 0){
                drawThreeLines(canvas,oneLefttwoRight,paint,false);
            }else if(drawStyle == 1){
                drawThreeLines(canvas,twoLeftoneRight,paint,false);
            }else if(drawStyle == 2){
                drawThreeLines(canvas,threeLeft,paint,true);
            }else if(drawStyle == 3){
                drawThreeLines(canvas,threeRight,paint,false);
            }
        }


    }


    private void drawThreeLines(Canvas canvas, int[] angles, Paint paint,boolean isDrawingThreeLeft){

        double angle = Math.toRadians(angles[0]);
        float x1 = drawLine(canvas,angle,paint,drawText[0],isDrawingThreeLeft);

        double angle1 = Math.toRadians(angles[1]);
        float x2 = drawLine(canvas,angle1,paint,drawText[1],isDrawingThreeLeft);

        double angle2 = Math.toRadians(angles[2]);
        float x3 = drawLine(canvas,angle2,paint,drawText[2],isDrawingThreeLeft);

        //先得到最大最小 两个x
        //当最小值大于centerX时，把centerX作为左边界  最大值当做右边界
        //当最大值小于centerX时，把centerX作为右边界  最小值当做左边界
        //其他情况 都是最小值左边界 最大值右边界

        float minX = 0;
        float maxX = 0;
        if(x2 == 0 && x1 != 0 && x3 != 0){
            minX = Math.min(x1,x3);
            maxX = Math.max(x1,x3);
        }else if(x2 != 0 && x1 == 0 && x3 == 0){
            minX = Math.min(x2,centerX);
            maxX = Math.max(x2,centerX);
        }else{
            minX = Math.min(Math.min(x1,x2),x3);
            maxX = Math.max(Math.max(x1,x2),x3);
        }

        if(minX > centerX){
            realLeft = centerX;
            realRight = maxX;
        }else if(maxX < centerX){
            realLeft = minX;
            realRight = centerX;
        }else{
            realLeft = minX;
            realRight = maxX;
        }


    }



    private float drawLine(Canvas canvas, double angle,Paint paint,String text,boolean isDrawingThreeLeft){

        if(TextUtils.isEmpty(text)){
            return 0;
        }

        int extra = 1;
        float shortRadius = (int)ScreenUtils.dpToPx(getContext(),5.0f);
        float longRadius = (int)ScreenUtils.dpToPx(getContext(),24.0f);

        double sinValue = Math.sin(angle);
        double cosValue = Math.cos(angle);

        int x1 = Math.round(centerX + (float) (shortRadius * sinValue));
        int y1 = Math.round(centerY + (float) (shortRadius * cosValue));
        int x2 = Math.round(centerX + (float) (longRadius * sinValue));
        int y2 = Math.round(centerY + (float) (longRadius * cosValue));

        paint.setTextAlign(Paint.Align.LEFT);
        if(x1 > x2 || isDrawingThreeLeft){
            extra = -extra;
            paint.setTextAlign(Paint.Align.RIGHT);
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize((int)ScreenUtils.dpToPx(getContext(),12.0f));
        paint.setShadowLayer(1.6F * getResources().getDisplayMetrics().density, 0.0F, 0.0F, Color.argb(204, 0, 0, 0));

        canvas.drawLine(x1, y1, x2, y2, paint);
        float labelW = paint.measureText(text);
        canvas.drawLine(x2, y2, x2 + extra*(labelW+(int)ScreenUtils.dpToPx(getContext(),3.0f)), y2, paint);
        canvas.drawText(text,x2+extra*(int)ScreenUtils.dpToPx(getContext(),1.0f),y2-(int)ScreenUtils.dpToPx(getContext(),6.0f),paint);

        return x2 + extra*(labelW+(int)ScreenUtils.dpToPx(getContext(),3.0f));

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!canTouch)
            return super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e("MotionEvent","ACTION_DOWN");
                float x = event.getX();
                float y = event.getY();
                centerX = getWidth()/2;
                centerY = getHeight()/2;
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();

                if(x > centerX - 30 && x < centerX + 30
                        && y > centerY - 30 && y < centerY + 30){
                    centerClick = true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("MotionEvent","ACTION_MOVE");
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;

                if(dx > 5 || dy > 5)
                    centerClick = false;

                int left = getLeft()  + dx;
                int top = getTop() + dy;
                int right = getRight()  + dx;
                int bottom = getBottom() + dy;

                // 设置不能出界
                if (left + (int)realLeft < 0) {
                    left = 0 - (int)realLeft;
                    right = left + getWidth();
                }

                if (right + (int)realRight - getWidth() > screenWidth) {
                    right = screenWidth + getWidth() - (int)realRight;
                    left = right - getWidth();

                }else{

                }


                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }

                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - getHeight();
                }

                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("MotionEvent","ACTION_UP");
                float upX = event.getX();
                float upY = event.getY();
                if(upX > centerX - 30 && upX < centerX + 30
                        && upY > centerY - 30 && upY < centerY + 30 && centerClick){
                    centerClick = false;
                    //触发中间点击事件
                    drawOnce();
                }

                //这一步需要，如果不设置，parent的改变也会影响child的位置
                RelativeLayout.LayoutParams childLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                childLayoutParams.leftMargin = getLeft();
                childLayoutParams.topMargin = getTop();
                setLayoutParams(childLayoutParams);

                break;

            case MotionEvent.ACTION_CANCEL:
                Log.e("MotionEvent","ACTION_CANCEL");
                centerClick = false;
                break;
        }
        return true;
    }


    /**
     * Set the layout parameters associated with this view. These supply
     * parameters to the <i>parent</i> of this view specifying how it should be
     * arranged. There are many subclasses of ViewGroup.LayoutParams, and these
     * correspond to the different subclasses of ViewGroup that are responsible
     * for arranging their children.
     *
     * @param params The layout parameters for this view, cannot be null
     */
    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);

    }
}
