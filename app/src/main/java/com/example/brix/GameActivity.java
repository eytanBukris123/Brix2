package com.example.brix;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.SharedPreferences;
import java.util.Random;
import android.graphics.Rect;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

        Brick brick1;
        Coins coin;
        ConstraintLayout gameLayout;
        ImageView pickaxe;
        float xDown = 0, yDown = 0;
        Animation pickaxeHitAnim;
        Handler handler;
        boolean canHit = true;
        int numOfCoins = 0;
        TextView coinsTv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);
                handler = new Handler();
                gameLayout = findViewById(R.id.gameLayout);
                pickaxe = findViewById(R.id.pickaxe);
                pickaxe.bringToFront();
                pickaxe.getParent().requestLayout();
                pickaxe.setOnClickListener(this);
                pickaxe.setOnTouchListener(this);
                createBrick();
                brick1.setOnClickListener(this);
                coinsTv = findViewById(R.id.coinsTv);
                numOfCoins = getNumberOfCoins();
                coinsTv.setText("" + numOfCoins);

        }

        public void createBrick(){
                brick1 = new Brick(this, 3, 3, 2, 200);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
//                brick1.setLayoutParams(lp);
                gameLayout.addView(brick1);
                Random r = new Random();
                brick1.setX(r.nextInt(900));
                brick1.setY(r.nextInt(1700));
        }

        public void createCoins(){
                coin = new Coins(GameActivity.this, 5, 2);
                gameLayout.addView(coin);
                coin.setX(brick1.getX());
                coin.setY(brick1.getY());
                coin.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                                numOfCoins += ((Coins) v).collect();
                                coinsTv.setText("" + numOfCoins);
                                setNumberOfCoins(numOfCoins);
                        }
                });
        }

        public int getNumberOfCoins() {
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                return sharedPref.getInt("Coins", 0);
        }

        public void setNumberOfCoins(int numOfCoins) {
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Coins", numOfCoins);
                editor.apply();
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
                //Hit();
                pickaxe.bringToFront();
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
                                                if(brick1.Hit()){
                                                        brick1.setVisibility(View.GONE);
                                                        createCoins();
                                                        createBrick();
                                                }
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
