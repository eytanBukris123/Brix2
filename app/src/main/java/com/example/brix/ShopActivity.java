package com.example.brix;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    Button upgradePower, upgradeSpeed;
    int numOfCoins;
    int powerLvl;
    int speedLvl;
    TextView coinsTv;
    TextView powerTitle, speedTitle;
    boolean haveWitchPickaxe = false;
    int powerPrice, speedPrice;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        powerTitle = findViewById(R.id.powerTitle);
        speedTitle = findViewById(R.id.speedTitle);
        upgradePower = findViewById(R.id.upgradePower);
        upgradeSpeed = findViewById(R.id.upgradeSpeed);
        upgradePower.setOnClickListener(this);
        upgradeSpeed.setOnClickListener(this);
        coinsTv = findViewById(R.id.coinsTv);
        SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
        numOfCoins = sharedPref.getInt("Coins", 0);
        coinsTv.setText("" + numOfCoins);
        powerLvl = sharedPref.getInt("PowerLvl", 1);
        speedLvl = sharedPref.getInt("SpeedLvl", 1);
        powerTitle.setText("Power Level " + powerLvl);
        speedTitle.setText("Power Level " + speedLvl);
        powerPrice = powerLvl*15;
        speedPrice = speedLvl*10;
        upgradePower.setText("Upgrade " + powerPrice + "$");
        upgradeSpeed.setText("Upgrade " + speedPrice + "$");

    }

    @Override
    public void onClick(View v) {
        if(v==upgradePower){
            if((numOfCoins >= powerPrice)) {
                powerLvl++;
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
            }
        }
        else if(v==upgradeSpeed){
            if(numOfCoins >= speedPrice) {
                speedLvl++;
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
            }
        }
    }
}
