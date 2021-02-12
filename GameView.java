package com.example.flappybird;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DiscretePathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


public class GameView extends View {

    Handler handler;
    Runnable runnable;
    final int UPDATE_MILLIS=30;
    Bitmap background;
    Bitmap topTube, bottomTube;
    Display display;
    Point point;
    int dWidth,dHeight;
    Rect rect;
    Bitmap[] birds;
    int birdFrame = 0;
    int velocity=0,gravity=3;
    int birdX,birdY;
    boolean gameState = false;
    int gap=400;
    int minTubeOffest,maxTubeOffest;
    int numberOftubes=4;
    int distanceBetweenTubes;
    int[] tubX =new int[numberOftubes];
    int[] toptubeY= new int[numberOftubes];
    Random random;
    int tubeVelocity=8;

    public GameView(Context context) {
        super(context);
        handler= new Handler();
        runnable= new Runnable() {
            @Override
            public void run() {
                invalidate();

            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        topTube = BitmapFactory.decodeResource(getResources(),R.drawable.pipes1);
        bottomTube = BitmapFactory.decodeResource(getResources(),R.drawable.pipes2);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.bird1);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.bird2);
        birdX = dWidth/2-birds[0].getWidth()/2;
        birdY = dHeight/2-birds[0].getHeight()/2;
        distanceBetweenTubes = dWidth*3/4;
        minTubeOffest = gap/2;
        maxTubeOffest = dHeight-minTubeOffest-gap;
        random = new Random();
        for (int i =0;i<numberOftubes;i++){
            tubX[i] = dWidth + i*distanceBetweenTubes;
            toptubeY[i] =minTubeOffest+random.nextInt(maxTubeOffest-minTubeOffest+1);
        }





    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawBitmap(background,0,0,null);
        canvas.drawBitmap(background,null,rect,null);
        if (birdFrame==0){
            birdFrame=1;
        }else{
            birdFrame=0;
        }
        if (gameState) {
            if (birdY < dHeight - birds[0].getHeight() || velocity < 0) {
                velocity = velocity + gravity;
                birdY = birdY + velocity;
            }
            for (int i = 0; i < numberOftubes; i++) {
                tubX[i] -=tubeVelocity;
                if (tubX[i]< -topTube.getWidth()){
                    tubX[i]+=numberOftubes*distanceBetweenTubes;
                    toptubeY[i] =minTubeOffest+random.nextInt(maxTubeOffest-minTubeOffest+1);

                }
                canvas.drawBitmap(topTube, tubX[i], toptubeY[i] - topTube.getHeight(), null);
                canvas.drawBitmap(bottomTube, tubX[i], toptubeY[i] + gap, null);
            }
        }
        canvas.drawBitmap(birds[birdFrame],birdX,birdY,null);
        handler.postDelayed(runnable,UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action==MotionEvent.ACTION_DOWN){
            velocity = -30;
            gameState =true;

        }
        return true;

    }
}
