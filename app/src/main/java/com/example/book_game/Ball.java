package com.example.book_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball implements DrawableItem{
    private float mX;
    private float mY;
    private float mSpeedX;
    private float mSpeedY;
    private final float mRadius; // 공의 크기는 생성 시에 결정하므로 final


    public Ball(float radius, float initialX, float initialY) {
        mRadius = radius;
        mSpeedX = radius / 5;
        mSpeedY = -radius / 5; // 비스듬히
        mX = initialX;
        mY = initialY;
    }

    public void move(){
        mX += mSpeedX;
        mY += mSpeedY;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mX, mY, mRadius, paint);
    }

    public float getSpeedX(){ // 클래스의 멤버 변수를 가져올 수 있게 해주는 메서드 = Getter
        return mSpeedX;
    }
    public float getSpeedY(){
        return mSpeedY;
    }
    public float getY(){
        return mY;
    }
    public float getX(){
        return mX;
    }

    public void setSpeedX(float speedX){ // 클래스의 멤버변수를 설정할 수 있게 해주는 메서드 = Setter
        mSpeedX = speedX;
    }
    public void setSpeedY(float speedY){
        mSpeedY = speedY;
    }
}