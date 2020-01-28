package com.example.project5_ksuzu2;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    java.util.Timer timer = new Timer();
    ImageView myImage1, myImage2;
    TextView textImpeach, textReelect, textGameOver;


    int image1X = 0, image1Y=0;
    int speed = 3;
    int direction1X = 1, direction1Y = 1;

    int gameLimit = 5;

    int image2X , image2Y;

    //    int image2X = 200, image2Y=200;
    int speed2 = 15;
    int direction2X = 1, direction2Y = 1;

    final int muellerMove = 20;

    Random rand = new Random();

    int score = 0;
    int impeachCount = 0;
    int reelectCount = 0;

    int screenX, screenY;
    Rect graphic1Rect, graphic2Rect;

    SoundPool soundPool;
    int reelectSound = -1;
    int impeachSound = -1;
    boolean soundPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage1 = (ImageView) findViewById(R.id.imageView);
        myImage2 = (ImageView) findViewById(R.id.imageView2);

        textImpeach = (TextView) findViewById(R.id.textView3);
        textReelect = (TextView) findViewById(R.id.textView4);

        textReelect.setText("Re-elected: 0/" + gameLimit + " times");
        textImpeach.setText("Impeached: 0/" + gameLimit + " times");


        textGameOver = (TextView) findViewById(R.id.textGameOver);

        graphic1Rect = new Rect(image1X, image1Y, image1X + myImage1.getWidth(), image1Y + myImage1.getHeight());
        graphic2Rect = new Rect(image2X, image2Y, image2X + myImage2.getWidth(), image2Y + myImage2.getHeight());



        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenX = size.x;
        screenY = size.y;

        image2Y = screenY/2;
        image1Y = rand.nextInt(screenY-myImage1.getHeight());

//        image2X = 800;
//        image2X = screenX - (3*myImage2.getWidth());
//        image2X = screenX - 3 * myImage2.getWidth();



        final int FPS = 100;

        TimerTask updateGame = new UpdateGameTask();
        timer.scheduleAtFixedRate(updateGame, 0, 1000/FPS);

//        myTextView2.setText(String.valueOf(myImage2.getWidth()));
        //        myTextView2.setText(String.valueOf(screenX - myImage2.getWidth()) + " " + String.valueOf(myImage2.getWidth()));


        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        } else {
            soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        }

        AssetManager assetManager = getAssets();

        try {
            AssetFileDescriptor descriptor = assetManager.openFd("awww_clip.ogg");
            impeachSound = soundPool.load(descriptor, 1);

            AssetFileDescriptor descriptor2 = assetManager.openFd("hail_to_the_chief_clip.ogg");
            reelectSound = soundPool.load(descriptor2, 1);

        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    public void muellerUp(View view) {
        image2Y -= muellerMove;
    }

    public void muellerDown(View view) {
        image2Y += muellerMove;
    }

    public void reset(View view) {
        textReelect.setText("Re-elected: 0/" + gameLimit + " times");
        textImpeach.setText("Impeached: 0/" + gameLimit + " times");

        soundPlaying = false;

        textGameOver.setText("");
        reelectCount = 0;
        impeachCount = 0;
        resetPos();

    }

    public void resetPos() {

        image1X = 0;
        image1Y = rand.nextInt(screenY-myImage1.getHeight());
        //        image1Y = 0;
        direction1X = 1;
        direction1Y = 1;
//        image2Y = rand.nextInt(screenY-myImage2.getHeight());

        //        speed = 10;

//        image2X =
//        image2X = 100;
//        image2Y = 100;
        direction2X = 1;
        direction2Y = 1;
//        speed2 = 10;

    }

    public void reelect() {
        resetPos();
        reelectCount += 1;
        textReelect.setText("Re-elected: " + reelectCount + "/" + gameLimit + " times");

    }

    public void impeach() {
        resetPos();
        impeachCount += 1;
        textImpeach.setText("Impeached: " + impeachCount + "/" + gameLimit +  " times");

    }


    class UpdateGameTask extends TimerTask {

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if ((impeachCount < gameLimit) && (reelectCount < gameLimit)) {

                        image1X += (speed * direction1X);
//                    image1Y += (speed * direction1Y);

//                    image2X += (speed2 * direction2X);
//                    image2Y += (speed2 * direction2Y);

                        //image2X = 800 screenX - myImage2.getWidth();
                        image2X = screenX - myImage2.getWidth();


                        myImage1.setX(image1X);
                        myImage1.setY(image1Y);

                        myImage2.setX(image2X);
                        myImage2.setY(image2Y);

                        graphic1Rect = new Rect(image1X, image1Y, image1X + myImage1.getWidth(), image1Y + myImage1.getHeight());
                        graphic2Rect = new Rect(image2X, image2Y, image2X + myImage2.getWidth(), image2Y + myImage2.getHeight());

                        if (Rect.intersects(graphic1Rect, graphic2Rect)) {
                            impeach();
                        }


                        if ((image1X + myImage1.getWidth()) > screenX || image1X < 0) {
                            reelect();
//                        direction1X = direction1X * -1;
                        }
/*
                        if ((image1Y + myImage1.getHeight()) > screenY || image1Y < 0) {
                            direction1Y = direction1Y * -1;
                        }


                        if ((image2X + myImage2.getWidth()) > screenX || image2X < 0) {
                            direction2X = direction2X * -1;
                        }

                        if ((image2Y + myImage2.getHeight()) > screenY || image2Y < 0) {
                            direction2Y = direction2Y * -1;
                        }
*/


                    } else if ((impeachCount >= gameLimit) && (!soundPlaying)) {

                        soundPlaying = true;

                        textGameOver.setText("TRUMP IMPEACHED!");
                        soundPool.play(impeachSound,1,1,0,0,1);

                    } else if (!soundPlaying) {

                        soundPlaying = true;
                        textGameOver.setText("TRUMP RE-ELECTED!");
                        soundPool.play(reelectSound,1,1,0,0,1);
                    }
                }
            });

        }
    }

    class Dummy extends TimerTask {

        @Override
        public void run() {

        }
    }
}
