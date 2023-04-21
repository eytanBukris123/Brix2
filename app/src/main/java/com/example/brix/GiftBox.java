package com.example.brix;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.Random;

public class GiftBox extends androidx.appcompat.widget.AppCompatImageView{

    private int time;
    private String[] giftTypes = {"coins", "speed", "power"};
    private int giftType;
    Handler handler;

    public GiftBox(Context context, int time) {
        super(context);
        this.time = time;
        Random r = new Random();
        this.giftType = r.nextInt(2);
        this.setImageResource(R.drawable.gift_box);
        this.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String[] getGiftTypes() {
        return giftTypes;
    }

    public void setGiftTypes(String[] giftTypes) {
        this.giftTypes = giftTypes;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

}
