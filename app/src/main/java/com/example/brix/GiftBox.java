package com.example.brix;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.Random;

public class GiftBox extends androidx.appcompat.widget.AppCompatImageView{

    private int time;

    public GiftBox(Context context, int time) {
        super(context);
        this.time = time;
        Random r = new Random();
        this.setImageResource(R.drawable.gift_box);
        this.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
