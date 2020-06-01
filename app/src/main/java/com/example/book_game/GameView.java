package com.example.book_game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import java.util.ArrayList;

public class GameView extends TextureView implements TextureView.
SurfaceTextureListener, View.OnTouchListener{

    private Thread mThread;
    volatile private boolean mIsRunnable;
    volatile private float mTouchedX;
    volatile private float mTouchedY;
    private Pad mPad;
    private float mPadHalfWidth;
    private Ball mBall;
    private float mBallRadius;

    public GameView(Context context) {
        super(context);
        setSurfaceTextureListener(this);
        setOnTouchListener(this);
    }

    public void start(){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);
                while (true) {
                    long startTime = System.currentTimeMillis();
                    synchronized (GameView.this) {
                        if (!mIsRunnable) {
                            break;
                        }
                        Canvas canvas = lockCanvas();
                        if (canvas == null) {
                            continue;
                        }
                        canvas.drawColor(Color.BLACK);
                        float padLeft = mTouchedX - mPadHalfWidth;
                        float padRight = mTouchedX + mPadHalfWidth;
                        mPad.setLeftRight(padLeft, padRight);
                        mBall.move();

                        float ballTop = mBall.getY() - mBallRadius; // 안드로이드는 y가 밑의 방향
                        float ballLeft = mBall.getX() - mBallRadius;
                        float ballBottom = mBall.getY() + mBallRadius;
                        float ballRight = mBall.getX() + mBallRadius;
                        if(ballLeft <0 && mBall.getSpeedX() <0 || ballRight >= getWidth() &&mBall.getSpeedX() >0){
                            // 공왼쪽좌표이 0보다 작고 공스피드가 음의 x방향이면 왼쪽벽 이거나 공오른쪽좌표가 화면너비보다 크고 스피드가 양의 x방향이면 오른쪽벽
                            // 좌우에 대해서는 호면 밖으로 날라갔을때 충돌을 반복하지 않게 하기 위해 바깥쪽(공속도)으로 향할때만 조건 추가
                            mBall.setSpeedX(-mBall.getSpeedX()); // 공의 x방향(좌우)의 속도를 반전
                        }
                        if (ballTop <0 || ballBottom >= getHeight() ){
                            mBall.setSpeedY((-mBall.getSpeedY()));
                        }
                        for (DrawableItem item : mItemList) {
                            item.draw(canvas, paint);
                        }
                        unlockCanvasAndPost(canvas);
                    }
                    long sleepTime = 16 - System.currentTimeMillis() + startTime;
                    if (sleepTime>0){
                        try{
                            Thread.sleep(sleepTime);
                        }catch (InterruptedException e){
                        }
                    }
                }
            }
        });
        mIsRunnable = true;
        mThread.start();
    }

    public void stop(){
        mIsRunnable = false;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        readyObjects(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        readyObjects(width, height);
    }

        private ArrayList<DrawableItem> mItemList;

        public void readyObjects(int width, int height){
            float blockWidth = width/10;
            float blockHeight = height / 20;
            mItemList = new ArrayList<DrawableItem>();

            for (int i = 0 ; i < 100 ; i++){
                float blockTop = i/10*blockHeight;
                float blockLeft = i%10*blockWidth;
                float blockBottom = blockTop + blockHeight;
                float blockRight = blockLeft + blockWidth;
                mItemList.add(new Block(blockTop, blockLeft, blockBottom, blockRight));
            }
            mPad = new Pad(height * 0.8f, height * 0.85f);
            mItemList.add(mPad);
            mPadHalfWidth = width / 10;
            mBallRadius = width < height ? width/40 : height/40;
            mBall = new Ball(mBallRadius, width/2, height/2);
            mItemList.add(mBall);
        }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        synchronized(this){
            return true;
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mTouchedX = event.getX();
        mTouchedY = event.getY();
        return true;

        }


    }

