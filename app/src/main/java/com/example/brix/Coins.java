package com.example.brix;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class Coins extends androidx.appcompat.widget.AppCompatImageView{

    private int time;
    private String[] types = {"stone", "silver", "gold", "diamond"};
    int coinType;

    public Coins(Context context, int time, int coinType) {
        super(context);
        this.setImageResource(R.drawable.gold_coins);
        this.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
        this.time = time;
        this.coinType = coinType;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int collect(){
        this.setVisibility(View.GONE);
        if(coinType == 2)
            return 10;
        return 5;

    }

}
