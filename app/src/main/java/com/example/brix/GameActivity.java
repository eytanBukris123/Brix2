package com.example.brix;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

        Brick brick1, brick2;
        Coins coin;
        GiftBox giftBox;
        ConstraintLayout gameLayout;
        Pickaxe pickaxe;
        float xDown = 0, yDown = 0;
        Animation pickaxeHitAnim;
        Handler handler;
        boolean canHit = true;
        int numOfCoins = 0;
        TextView coinsTv;
        int timeToHit;
        int powerLvl;
        int speedLvl;
        int skinLvl;
        boolean giftOpen = false;
        MediaPlayer backgroundMusic;
        ImageView soundImg;
        boolean music = true;

        FirebaseDatabase database;
        DatabaseReference powerRef, speedRef, skinRef, coinsRef;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);
                handler = new Handler();
                getPickaxeDate();
                gameLayout = findViewById(R.id.gameLayout);
                soundImg = findViewById(R.id.soundImg);
                soundImg.setOnClickListener(this);
                createBrick();
                brick1.setOnClickListener(this);
                coinsTv = findViewById(R.id.coinsTv);
                numOfCoins = getNumberOfCoins();
                coinsTv.setText("" + numOfCoins);
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);

                createGift(5000);

//                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putInt("SkinLvl", 1);
//                editor.putInt("PowerLvl", 1);
//                editor.putInt("SpeedLvl", 4);
//                editor.putInt("Coins", 500);
//                editor.apply();

        }

        public void createPickaxe(){
                pickaxe = new Pickaxe(this, skinLvl, speedLvl, powerLvl);
                gameLayout.addView(pickaxe);
                pickaxe.setX(450);
                pickaxe.setY(850);
                if(skinLvl == 5){
                        pickaxe.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                }
                pickaxe.setZ(2);
                pickaxe.getParent().requestLayout();
                pickaxe.setOnClickListener(GameActivity.this);
                pickaxe.setOnTouchListener(GameActivity.this);
                timeToHit = (11-speedLvl)*150;

        }

        public void createBrick(){
                Random r = new Random();
                int size = r.nextInt(10) + 1;
                int brickType = r.nextInt(3);
                int coinValue = size*2;
                int time = 12/(brickType+1);
                brick1 = new Brick(this, size, time, brickType, size*70);
                gameLayout.addView(brick1);
                brick1.setX(r.nextInt(800));
                brick1.setY(r.nextInt(1600));
                brickTime(time, brick1);
        }


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

        public void createGift(int time){
                Random r = new Random();
                int giftTime = r.nextInt(7)+3;
                handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                giftBox = new GiftBox(GameActivity.this, 5000);
                                gameLayout.addView(giftBox);
                                giftBox.setX(r.nextInt(800));
                                giftBox.setY(r.nextInt(1600));
                                giftBox.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                                giftBox.setVisibility(View.GONE);
                                                int number = r.nextInt(49)+1;
                                                OpenGiftWindow(number*10);
                                        }
                                });
                                handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                                giftBox.setVisibility(View.GONE);
                                                createGift(5000);
                                        }
                                }, 5000);
                        }
                }, 10000*giftTime);
        }

        public int getNumberOfCoins() {
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                return sharedPref.getInt("Coins", 0);
        }

        public void setNumberOfCoins(int numOfCoins) {
                database = FirebaseDatabase.getInstance();
                coinsRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("coins");
                coinsRef.setValue(numOfCoins);
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

        public void getPickaxeDate (){
                database = FirebaseDatabase.getInstance("https://bricks-86a18-default-rtdb.firebaseio.com/");

                powerRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("power");
                powerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                        powerLvl = snapshot.getValue(int.class);
                                }
                                else{
                                        powerLvl = 1;
                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });

                speedRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("speed");
                speedRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                        speedLvl = snapshot.getValue(int.class);
                                }
                                else{
                                        speedLvl = 1;
                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });

                skinRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("skin");
                skinRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                        skinLvl = snapshot.getValue(int.class);
                                }
                                else{
                                        skinLvl = 1;
                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });

                coinsRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("coins");
                coinsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                        numOfCoins = snapshot.getValue(int.class);
                                        coinsTv.setText("" + numOfCoins);
                                }
                                else{
                                        coinsTv.setText("0");
                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                });

                handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                createPickaxe();
                        }
                }, 2000);

        }

        public void OpenGiftWindow(int number){
                final Dialog dialog = new Dialog(GameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.gift_window);
                final ImageView giftBox;
                final ImageView typeImg;
                final TextView numText;
                giftOpen = false;
                giftBox = dialog.findViewById(R.id.gift_box);
                typeImg = dialog.findViewById(R.id.typeImg);
                numText = dialog.findViewById(R.id.numText);
                typeImg.setVisibility(View.INVISIBLE);
                numText.setVisibility(View.INVISIBLE);

                giftBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                typeImg.setVisibility(View.VISIBLE);
                                numText.setVisibility(View.VISIBLE);
                                if(!giftOpen) {
                                        MediaPlayer openGift = MediaPlayer.create(GameActivity.this, R.raw.open_gift);
                                        openGift.start();
                                        giftOpen = true;
                                }
                                else{
                                        numOfCoins+=number;
                                        setNumberOfCoins(numOfCoins);
                                        coinsTv.setText("" + numOfCoins);

                                        dialog.cancel();
                                }
                        }
                });

                numText.setText("" + number);
                numText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                numOfCoins+=number;
                                SharedPreferences sharedPref = getSharedPreferences("application", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("Coins", numOfCoins);
                                editor.apply();
                                coinsTv.setText("" + numOfCoins);

                                dialog.cancel();
                        }
                });

                dialog.show();

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
        protected void onStop() {
                super.onStop();
                backgroundMusic.stop();
        }

        @Override
        protected void onResume() {
                super.onResume();
                backgroundMusic= MediaPlayer.create(GameActivity.this,R.raw.bricks_background_music);
                backgroundMusic.start();
        }

        @Override
        public void onClick(View v) {
                if(v==soundImg){
                        if(music) {
                                soundImg.setImageResource(android.R.drawable.ic_lock_silent_mode);
                                backgroundMusic.setVolume(0f, 0f);
                                music = false;
                        }
                        else {
                                soundImg.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                                backgroundMusic.setVolume(1f, 1f);
                                music = true;
                        }
                }
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
