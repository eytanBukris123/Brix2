package com.example.brix;

import android.annotation.SuppressLint;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
    DatabaseReference usersRef;

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
        SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
        numOfCoins = sharedPref.getInt("Coins", 0);
        coinsTv.setText("" + numOfCoins);
        powerLvl = sharedPref.getInt("PowerLvl", 1);
        speedLvl = sharedPref.getInt("SpeedLvl", 1);
        skinLvl = sharedPref.getInt("SkinLvl", 1);
        powerTitle.setText("Power Level " + powerLvl);
        speedTitle.setText("Power Level " + speedLvl);
        powerPrice = powerLvl*15;
        speedPrice = speedLvl*10;
        skinPrice = 200 * (sharedPref.getInt("SkinLvl", 0) + 1);
        upgradePower.setText("Upgrade " + powerPrice + "$");
        upgradeSpeed.setText("Upgrade " + speedPrice + "$");
        upgradeSkin.setText("Upgrade " + skinPrice + "$");
        if(powerLvl==7){
            maxLvl(upgradePower);
        }
        if(speedLvl==9){
            maxLvl(upgradeSpeed);
        }
        if(skinLvl==6){
            maxLvl(upgradeSkin);
        }

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid());

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
                usersRef.child("power").setValue(powerLvl);
                buySound.start();
                powerTitle.setText("Power Level " + powerLvl);
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("PowerLvl", powerLvl);
                numOfCoins-= powerPrice;
                editor.putInt("Coins", numOfCoins);
                coinsTv.setText("" + numOfCoins);
                editor.apply();
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
                usersRef.child("speed").setValue(speedLvl);
                buySound.start();
                speedTitle.setText("Power Level " + speedLvl);
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("SpeedLvl", speedLvl);
                numOfCoins-= speedPrice;
                editor.putInt("Coins", numOfCoins);
                coinsTv.setText("" + numOfCoins);
                editor.apply();
                speedPrice = speedLvl*10;
                upgradeSpeed.setText("Upgrade " + speedPrice + "$");
                if(speedLvl==9){
                    maxLvl(upgradeSpeed);
                }
            }
        }
        else if(v==upgradeSkin){
            if(numOfCoins>=skinPrice){
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                skinLvl++;
                usersRef.child("skin").setValue(skinLvl);
                buySound.start();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("SkinLvl", skinLvl);
                numOfCoins-= skinPrice;
                editor.putInt("Coins", numOfCoins);
                coinsTv.setText("" + numOfCoins);
                editor.apply();
                skinPrice = 200 * (sharedPref.getInt("SkinLvl", 0) + 1);
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
