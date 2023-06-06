package com.example.brix;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    Button upgradePower, upgradeSpeed, upgradeSkin;
    int numOfCoins;
    int powerLvl;
    int speedLvl;
    int skinLvl;
    TextView coinsTv;
    TextView powerTitle, speedTitle;
    boolean haveWitchPickaxe = false;
    int powerPrice, speedPrice, skinPrice;
    MediaPlayer buySound;
    FirebaseDatabase database;
    DatabaseReference powerRef, speedRef, skinRef, coinsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        buySound = MediaPlayer.create(ShopActivity.this,R.raw.buy_sound);

        powerTitle = findViewById(R.id.powerTitle);
        speedTitle = findViewById(R.id.speedTitle);
        upgradeSkin = findViewById(R.id.upgradeSkin);
        upgradePower = findViewById(R.id.upgradePower);
        upgradeSpeed = findViewById(R.id.upgradeSpeed);
        upgradeSkin.setOnClickListener(this);
        upgradePower.setOnClickListener(this);
        upgradeSpeed.setOnClickListener(this);
        coinsTv = findViewById(R.id.coinsTv);

        final ArrayList<User> users = new ArrayList<>();

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

                powerTitle.setText("Power Level " + powerLvl);
                powerPrice = powerLvl*15;
                upgradePower.setText("Upgrade " + powerPrice + "$");
                if(powerLvl==7){
                    maxLvl(upgradePower);
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

                speedTitle.setText("Power Level " + speedLvl);
                speedPrice = speedLvl*10;
                upgradeSpeed.setText("Upgrade " + speedPrice + "$");
                if(speedLvl==9){
                    maxLvl(upgradeSpeed);
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

                skinPrice = 200 * (skinLvl + 1);
                upgradeSkin.setText("Upgrade " + skinPrice + "$");
                if(skinLvl==6){
                    maxLvl(upgradeSkin);
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

    }

    public static void maxLvl(Button maxLvlBtn){
        maxLvlBtn.setText("MAX LVL");
        maxLvlBtn.setBackgroundColor(Color.GRAY);
        maxLvlBtn.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        if(v==upgradePower){
            if((numOfCoins >= powerPrice)) {
                powerLvl++;
                powerRef.setValue(powerLvl);
                buySound.start();
                powerTitle.setText("Power Level " + powerLvl);
                numOfCoins-= powerPrice;
                coinsRef.setValue(numOfCoins);
                coinsTv.setText("" + numOfCoins);
                powerPrice = powerLvl*15;
                upgradePower.setText("Upgrade " + powerPrice + "$");
                if(powerLvl==7){
                    maxLvl(upgradePower);
                }
            }
        }
        else if(v==upgradeSpeed){
            if(numOfCoins >= speedPrice) {
                speedLvl++;
                speedRef.setValue(speedLvl);
                buySound.start();
                speedTitle.setText("Power Level " + speedLvl);
                numOfCoins-= speedPrice;
                coinsRef.setValue(numOfCoins);
                coinsTv.setText("" + numOfCoins);
                speedPrice = speedLvl*10;
                upgradeSpeed.setText("Upgrade " + speedPrice + "$");
                if(speedLvl==9){
                    maxLvl(upgradeSpeed);
                }
            }
        }
        else if(v==upgradeSkin){
            if(numOfCoins>=skinPrice){
                skinLvl++;
                skinRef.setValue(skinLvl);
                buySound.start();
                numOfCoins-= skinPrice;
                coinsRef.setValue(numOfCoins);
                coinsTv.setText("" + numOfCoins);
                skinPrice = 200 * (skinLvl + 1);
                upgradeSkin.setText("Upgrade " + skinPrice + "$");
                if(skinLvl==6){
                    maxLvl(upgradeSkin);
                }
            }
        }
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

}
