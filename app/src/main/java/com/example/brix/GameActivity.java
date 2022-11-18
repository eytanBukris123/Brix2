package com.example.brix;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.Random;
import android.graphics.Rect;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

        Brick brick1;
        ConstraintLayout gameLayout;
        ImageView pickaxe;
        float xDown = 0, yDown = 0;
        Animation pickaxeHitAnim;
        Handler handler;
        boolean canHit = true;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);
                handler = new Handler();
                gameLayout = findViewById(R.id.gameLayout);
                pickaxe = findViewById(R.id.pickaxe);
                pickaxe.setOnClickListener(this);
                pickaxe.setOnTouchListener(this);
                createBrick();


        }

        public void createBrick(){
                brick1 = new Brick(this, 3, 3, 2, 300);
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
                        case MotionEvent.ACTION_UP:
                                Hit();

                                break;
                }

                return true;
        }

        public void Hit(){
                if(canHit) {
                        canHit = false;
                        pickaxe.animate().rotation(-80f).setDuration(300).start();
                        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        pickaxe.animate().rotation(0).setDuration(200).start();
                                        if(Collision(brick1, pickaxe)){
                                                brick1.Hit();
                                        }
                                        handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                        canHit=true;
                                                }
                                        }, 200);
                                }
                        }, 300);
                }
        }

        public boolean Collision(ImageView brick, ImageView pickaxe)
        {
                Rect PickaxeRect = new Rect();
                pickaxe.getHitRect(PickaxeRect);
                Rect BrickRect = new Rect();
                brick1.getHitRect(BrickRect);
                return PickaxeRect.intersect(BrickRect);
        }

        @Override
        public void onClick(View v) {

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
