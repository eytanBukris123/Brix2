package com.example.brix;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.security.Provider;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

        Brick brick1;
        ConstraintLayout gameLayout;
        ImageView pickaxe;
        float xDown = 0, yDown = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);
                gameLayout = findViewById(R.id.gameLayout);
                pickaxe = findViewById(R.id.pickaxe);
                pickaxe.setOnTouchListener(this);
                createBrick();


        }

        public void createBrick(){
                brick1 = new Brick(this, 2, 3, 2, 30);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
                brick1.setLayoutParams(lp);
                gameLayout.addView(brick1);
                Random r = new Random();
                brick1.setX(r.nextInt(900));
                brick1.setY(r.nextInt(1700));
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getActionMasked()){
                        case MotionEvent.ACTION_DOWN:
                                xDown=motionEvent.getX();
                                yDown=motionEvent.getY();
                                break;

                        case MotionEvent.ACTION_MOVE:
                                float movedX, movedY;
                                movedX = motionEvent.getX();
                                movedY = motionEvent.getY();

                                float distanceX = movedX-xDown;
                                float distanceY = movedY-yDown;

                                pickaxe.setX(pickaxe.getX()+distanceX);
                                pickaxe.setY(pickaxe.getY()+distanceY);

                                break;
                }

                return true;
        }
}

//class myBackgroundService extends Service {
//
//
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//                new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                                while(true){
//
//
//                                        try {
//                                                Thread.sleep(3000);
//                                        } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                        }
//                                }
//                        }
//                });
//                return super.onStartCommand(intent, flags, startId);
//
//        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//                return null;
//        }
//}
