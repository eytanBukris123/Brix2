package com.example.brix;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.SharedPreferences;
import java.util.Random;
import android.graphics.Rect;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

        Brick brick1, brick2;
        Coins coin;
        ConstraintLayout gameLayout;
        Pickaxe pickaxe;
        float xDown = 0, yDown = 0;
        Animation pickaxeHitAnim;
        Handler handler;
        boolean canHit = true;
        int numOfCoins = 0;
        TextView coinsTv;
        int timeToHit = 1000;
        int powerLvl;
        int speedLvl;
        int skinLvl;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);

                handler = new Handler();
                gameLayout = findViewById(R.id.gameLayout);
                createPickaxe();
                pickaxe.setZ(2);
                pickaxe.getParent().requestLayout();
                pickaxe.setOnClickListener(this);
                pickaxe.setOnTouchListener(this);
                createBrick();
                brick1.setOnClickListener(this);
                coinsTv = findViewById(R.id.coinsTv);
                numOfCoins = getNumberOfCoins();
                coinsTv.setText("" + numOfCoins);
                timeToHit = (11-pickaxe.getSpeed())*150;
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);

//                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putInt("SkinLvl", 1);
//                editor.putInt("PowerLvl", 1);
//                editor.putInt("SpeedLvl", 9);
//                editor.putInt("Coins", 0);
//                editor.apply();

        }

        public void createPickaxe(){
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                powerLvl = sharedPref.getInt("PowerLvl", 1);
                speedLvl = sharedPref.getInt("SpeedLvl", 1);
                skinLvl = sharedPref.getInt("SkinLvl", 0) - 1;
                pickaxe = new Pickaxe(this, skinLvl, speedLvl, powerLvl);
                gameLayout.addView(pickaxe);
                pickaxe.setX(450);
                pickaxe.setY(850);
                if(skinLvl == 5){
                        pickaxe.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                }

        }

        public void createBrick(){
                Random r = new Random();
                int size = r.nextInt(10) + 1;
                int brickType = r.nextInt(3);
                int coinValue = size*2;
                int time = 12/(brickType+1);
                brick1 = new Brick(this, size, time, brickType, size*70);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
//                brick1.setLayoutParams(lp);
                gameLayout.addView(brick1);
                brick1.setX(r.nextInt(800));
                brick1.setY(r.nextInt(1600));
                brickTime(time, brick1);
//                handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                                if(!brick2create) {
//                                        createBrick2();
//                                        brick2create = true;
//                                }
//                        }
//                }, 5000);
        }

//        public void createBrick2(){
//                Random r = new Random();
//                int size = r.nextInt(8) + 1;
//                int brickType = r.nextInt(3);
//                int coinValue = size*2;
//                int time = 12/(brickType+1);
//                brick2 = new Brick(this, size, time, brickType, size*70);
////              LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
////              brick1.setLayoutParams(lp);
//                gameLayout.addView(brick2);
//                brick2.setX(r.nextInt(800));
//                brick2.setY(r.nextInt(1600));
//                brickTime(time, brick2, 2);
//        }

        public void brickTime(int time, ImageView brick){
                handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                if(brick.getVisibility() != View.GONE) {
                                        brick.setVisibility(View.GONE);
                                        createBrick();
                                }
                        }
                }, time*1000);
        }

        public void createCoins(){
                int coinValue = brick1.getSize()/100 + (brick1.getBrickType()+1)*10;
                coin = new Coins(GameActivity.this, 3, 2, coinValue);
                gameLayout.addView(coin);
                coin.setX(brick1.getX());
                coin.setY(brick1.getY());
                coin.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                                MediaPlayer coinsCollected= MediaPlayer.create(GameActivity.this,R.raw.cash_register);
                                coinsCollected.start();
                                numOfCoins += ((Coins) v).collect();
                                coinsTv.setText("" + numOfCoins);
                                setNumberOfCoins(numOfCoins);
                        }
                });
                coinTime(5, coin);
        }

        public void coinTime(int time, ImageView coin){
                handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                if(coin.getVisibility() != View.GONE) {
                                        coin.setVisibility(View.GONE);
                                }
                        }
                }, time*1000);
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
                if (canHit) {
                        switch (motionEvent.getActionMasked()) {
                                case MotionEvent.ACTION_DOWN:
                                        xDown = motionEvent.getX();
                                        yDown = motionEvent.getY();

                                        break;

                                case MotionEvent.ACTION_MOVE:
                                        float movedX, movedY;
                                        movedX = motionEvent.getX();
                                        movedY = motionEvent.getY();

                                        float distanceX = movedX - xDown;
                                        float distanceY = movedY - yDown;

                                        pickaxe.setX(pickaxe.getX() + distanceX);
                                        pickaxe.setY(pickaxe.getY() + distanceY);

                                        break;
                                case MotionEvent.ACTION_UP:
                                        Hit();

                                        break;
                        }

                }
                        return true;
        }

        public void Hit(){
                if(canHit) {
                        canHit = false;
                        pickaxe.animate().rotation(-80f).setDuration(timeToHit+100).start();
                        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        pickaxe.animate().rotation(0).setDuration(timeToHit-300).start();
                                        if(Collision(brick1, pickaxe, 1)){
                                                MediaPlayer hitSound= MediaPlayer.create(GameActivity.this,R.raw.hit);
                                                hitSound.start();
                                                if(brick1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bomb).getConstantState()){
                                                        numOfCoins -= 50*(brick1.getSize()/70);
                                                        MediaPlayer bombExplosion= MediaPlayer.create(GameActivity.this,R.raw.bombexplosion);
                                                        bombExplosion.start();
                                                        if(numOfCoins<0){
                                                                numOfCoins = 0;
                                                        }
                                                        coinsTv.setText("" + numOfCoins);
                                                        setNumberOfCoins(numOfCoins);
                                                        SharedPreferences sharedPref = getSharedPreferences("application", GameActivity.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putInt("Coins", numOfCoins);
                                                        editor.apply();
                                                        createCoins();
                                                        brick1.setVisibility(View.GONE);
                                                        createBrick();
                                                }
                                                else {
                                                        Random r = new Random();
                                                        int bomb = r.nextInt(10) + 1;
                                                        if (bomb == 7) {
                                                                brick1.setImageResource(R.drawable.bomb);
                                                                handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                                brick1.setVisibility(View.GONE);
                                                                                createCoins();
                                                                                createBrick();
                                                                        }
                                                                }, 5000);
                                                        } else if (brick1.Hit(pickaxe.getDammage())) {
                                                                brick1.setVisibility(View.GONE);
                                                                hitSound.stop();
                                                                MediaPlayer rockBreakSound= MediaPlayer.create(GameActivity.this,R.raw.rock_destroy);
                                                                rockBreakSound.setVolume(0.15f, 0.15f);
                                                                rockBreakSound.start();
                                                                createCoins();
                                                                createBrick();

                                                        }
                                                }
                                        }
//                                        if(brick2!=null) {
//                                                if (Collision(brick2, pickaxe, 2)) {
//                                                        if (brick2.Hit(pickaxe.getDammage())) {
//                                                                brick2.setVisibility(View.GONE);
//                                                                createCoins();
//                                                        }
//                                                }
//                                        }
                                        handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                        canHit=true;
                                                }
                                        }, timeToHit-300);
                                }
                        }, timeToHit+100);
                }
        }

        public boolean Collision(ImageView brick, ImageView pickaxe, int num)
        {
                Rect PickaxeRect = new Rect();
                pickaxe.getHitRect(PickaxeRect);
                Rect BrickRect = new Rect();
                if(num==2){
                        brick2.getHitRect(BrickRect);
                }
                else{
                        brick1.getHitRect(BrickRect);
                }
                return PickaxeRect.intersect(BrickRect);
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                        case android.R.id.home:
                                this.finish();
                                return true;
                }
                return super.onOptionsItemSelected(item);
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
