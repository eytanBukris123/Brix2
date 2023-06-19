package com.example.brix;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

        Brick brick;
        Coins coin;
        GiftBox giftBox;
        ConstraintLayout gameLayout;
        Pickaxe pickaxe;
        float xDown = 0, yDown = 0;
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
        ImageView orangeScreen;

        FirebaseDatabase database;
        DatabaseReference powerRef, speedRef, skinRef, coinsRef;
        SharedPreferences sharedPreferences;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);

                setLayoutData();
                getPickaxeDate();
                createBrick();
                createGift(5000);

        }

        //setting all layoutObjectsData
        private void setLayoutData() {
                gameLayout = findViewById(R.id.gameLayout);
                soundImg = findViewById(R.id.soundImg);
                soundImg.setOnClickListener(this);
                coinsTv = findViewById(R.id.coinsTv);
                coinsTv.setText("" + numOfCoins);
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                handler = new Handler();
                sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                orangeScreen = findViewById(R.id.orangeScreen);
                orangeScreen.animate().scaleX(0).scaleY(0).setDuration(2300).start();
                orangeScreen.setZ(3);
        }

        //create pickaxe object
        public void createPickaxe(){
                pickaxe = new Pickaxe(this, skinLvl, speedLvl, powerLvl);
                gameLayout.addView(pickaxe);
                pickaxe.setX(370);
                pickaxe.setY(800);
                if(skinLvl == 5){
                        pickaxe.setLayoutParams(new LinearLayout.LayoutParams(600, 600));
                }
                pickaxe.setZ(2);
                pickaxe.getParent().requestLayout();
                pickaxe.setOnClickListener(GameActivity.this);
                pickaxe.setOnTouchListener(GameActivity.this);
                timeToHit = (11-speedLvl)*150;

        }

        //create brick object
        public void createBrick(){
                Random r = new Random();
                int size = r.nextInt(10) + 1;
                int brickType = r.nextInt(3);
                int coinValue = size*2;
                int time = 12/(brickType+1);
                brick = new Brick(this, size, time, brickType, size*70);
                gameLayout.addView(brick);
                brick.setX(r.nextInt(800));
                brick.setY(r.nextInt(1600));
                brickTime(time, brick);
        }

        //set brick shown time
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

        //create coins object
        public void createCoins(){
                int coinValue = brick.getSize()/100 + (brick.getBrickType()+1)*10;
                coin = new Coins(GameActivity.this, 3, 2, coinValue);
                gameLayout.addView(coin);
                coin.setX(brick.getX());
                coin.setY(brick.getY());
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

        //set coins shown time
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

        //create gift object
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

        //save number of coins in firebase
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

        // pickaxe hitting action
        public void Hit(){
                if(canHit) {
                        canHit = false;
                        pickaxe.animate().rotation(-80f).setDuration(timeToHit+100).start();
                        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        pickaxe.animate().rotation(0).setDuration(timeToHit-300).start();
                                        if(Collision(brick, pickaxe)){
                                                MediaPlayer hitSound= MediaPlayer.create(GameActivity.this,R.raw.hit);
                                                hitSound.start();
                                                if(brick.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bomb).getConstantState()){
                                                        numOfCoins -= 50*(brick.getSize()/70);
                                                        MediaPlayer bombExplosion= MediaPlayer.create(GameActivity.this,R.raw.bombexplosion);
                                                        bombExplosion.start();
                                                        if(numOfCoins<0){
                                                                numOfCoins = 0;
                                                        }
                                                        coinsTv.setText("" + numOfCoins);
                                                        setNumberOfCoins(numOfCoins);
                                                        createCoins();
                                                        brick.setVisibility(View.GONE);
                                                        createBrick();
                                                }
                                                else {
                                                        Random r = new Random();
                                                        int bomb = r.nextInt(10) + 1;
                                                        if (bomb == 7) {
                                                                brick.setImageResource(R.drawable.bomb);
                                                                handler.postDelayed(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                                brick.setVisibility(View.GONE);
                                                                                createCoins();
                                                                                createBrick();
                                                                        }
                                                                }, 5000);
                                                        } else if (brick.Hit(pickaxe.getDammage())) {
                                                                brick.setVisibility(View.GONE);
                                                                hitSound.stop();
                                                                MediaPlayer rockBreakSound= MediaPlayer.create(GameActivity.this,R.raw.rock_destroy);
                                                                rockBreakSound.setVolume(0.15f, 0.15f);
                                                                rockBreakSound.start();
                                                                createCoins();
                                                                createBrick();

                                                        }
                                                }
                                        }
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

        //get pickaxe data from firebase
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
                                        skinLvl = snapshot.getValue(int.class)-1;
                                }
                                else{
                                        skinLvl = 0;
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

        //gift dialog
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
                                        numOfCoins+=number;
                                        setNumberOfCoins(numOfCoins);
                                        coinsTv.setText("" + numOfCoins);
                                }
                                else{
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

        //check pickaxe hit brick
        public boolean Collision(ImageView brick, ImageView pickaxe)
        {
                Rect PickaxeRect = new Rect();
                pickaxe.getHitRect(PickaxeRect);
                Rect BrickRect = new Rect();
                this.brick.getHitRect(BrickRect);

                return PickaxeRect.intersect(BrickRect);
        }

        //reset user data in firebase
        private void resetFirefoxData(){
                database = FirebaseDatabase.getInstance();
                powerRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("power");
                speedRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("speed");
                skinRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("skin");
                coinsRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid()).child("coins");
                powerRef.setValue(1);
                speedRef.setValue(1);
                skinRef.setValue(1);
                coinsRef.setValue(0);
                numOfCoins = 0;
                coinsTv.setText("0");
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
                music = sharedPreferences.getBoolean("musicOn", false);
                if(music) {
                        soundImg.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                        backgroundMusic.setVolume(1f, 1f);
                        music = true;
                }
                else {
                        soundImg.setImageResource(android.R.drawable.ic_lock_silent_mode);
                        backgroundMusic.setVolume(0f, 0f);
                        music = false;
                }
        }

        @Override
        public void onClick(View v) {
                if(v==soundImg){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
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
                        editor.putBoolean("musicOn", music);
                        editor.apply();
                }
        }
}
