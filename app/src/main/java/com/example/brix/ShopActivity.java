package com.example.brix;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {

    Button buyWitchPickaxe;
    int numOfCoins;
    TextView coinsTv;
    boolean haveWitchPickaxe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        buyWitchPickaxe = findViewById(R.id.buyWitchPickaxe);
        buyWitchPickaxe.setOnClickListener(this);
        coinsTv = findViewById(R.id.coinsTv);

        SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
        numOfCoins = sharedPref.getInt("Coins", 0);
        coinsTv.setText("" + numOfCoins);

    }

    @Override
    public void onClick(View v) {
        if(v==buyWitchPickaxe){
            if(numOfCoins>=1000 && !haveWitchPickaxe) {
                SharedPreferences sharedPref = getSharedPreferences("application", this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("Pickaxe", R.drawable.pickaxe2);
                editor.apply();
                numOfCoins-=1000;
                editor.putInt("Coins", numOfCoins);
                editor.apply();
                coinsTv.setText("" + numOfCoins);
                haveWitchPickaxe = true;
            }
        }
    }
}
