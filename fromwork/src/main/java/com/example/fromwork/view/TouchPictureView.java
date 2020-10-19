package com.example.fromwork.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.fromwork.R;
import com.example.fromwork.utils.LogUtils;

/**
 * 图片验证
 */
public class TouchPictureView extends View {

    //背景
    private Bitmap bgBitmap;
    //背景的画笔
    private Paint bgPaint;

    //空白快
    private Bitmap nullBitmap;
    //空白快画笔
    private Paint nullPaint;
    //白快的大小
    private int squareSize = 200;
    //白快宽
    private int squareWitch=0;
    //白快高
    private int squareHeight=0;


    //移动方块
    private Bitmap moveBitmap;
    //移动方块画笔
    private Paint movePaint;
    //移动方块横坐标
    private int moveX = 200;

    //控件的宽
    private int mWidth;
    //控件的高
    private int mHeight;

    //误差值
    private int errorValue = 10;

    //接口
    private OnViewResultListener viewResultListener;

    private boolean flag = false;


    public void setViewResultLisener(OnViewResultListener viewResultListener) {
        this.viewResultListener = viewResultListener;
    }

    public TouchPictureView(Context context) {
        super(context);
        init();
    }

    public TouchPictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //初始化
    private void init(){
        bgPaint = new Paint();
        nullPaint = new Paint();
        movePaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawNull(canvas);
        drawMove(canvas);
    }

    /**
     * 绘制移动方块
     * @param canvas
     */
    private void drawMove(Canvas canvas) {

        moveBitmap = Bitmap.createBitmap(bgBitmap,squareWitch,squareHeight,squareSize-100,squareSize-100);
        canvas.drawBitmap(moveBitmap,moveX,squareHeight,movePaint);
    }

    /**
     * 绘制空白快
     * @param canvas
     */
    private void drawNull(Canvas canvas) {

        nullBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_null_card);
        //白快的横纵坐标
        squareWitch = mWidth / 3 * 2 ;
        squareHeight = mHeight / 2 -(squareSize / 2);

        canvas.drawBitmap(nullBitmap,squareWitch,squareHeight,nullPaint);


    }

    /**
     * 绘制背景
     * @param canvas
     */
    private void drawBg(Canvas canvas){
        //获取图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_bg);

        bgBitmap = bitmap.createBitmap(mWidth,mHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bgBitmap);
        canvas1.drawBitmap(bitmap,null,new Rect(0,0,mWidth,mHeight),bgPaint);
        canvas.drawBitmap(bgBitmap,null,new Rect(0,0,mWidth,mHeight),bgPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if (event.getY() >= squareHeight  && event.getY() < squareHeight+100){
                    flag = true;
                }else {
                    flag = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (flag){
                    if (event.getX() > 0 && event.getX() < (mWidth - (squareSize/2))){
                        moveX = (int) event.getX();
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (moveX > (squareWitch - errorValue) && moveX < (squareWitch + errorValue)){
                        if (viewResultListener!=null){
                            viewResultListener.onResult();
                        }
                }
                break;
        }

        return true;
    }

    public interface OnViewResultListener{
        void  onResult();

    }

}
