package com.example.brix;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class Coins extends androidx.appcompat.widget.AppCompatImageView{

    private int time;
    private String[] types = {"stone", "silver", "gold", "diamond"};
    int coinType;
    int coinValue;

    public Coins(Context context, int time, int coinType, int coinValue) {
        super(context);
        this.setImageResource(R.drawable.gold_coins);
        this.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        this.time = time;
        this.coinType = coinType;
        this.coinValue = coinValue;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int collect(){

        this.setVisibility(View.GONE);
        return coinValue;

    }

}
