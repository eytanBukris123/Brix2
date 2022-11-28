package com.example.brix;

import android.content.Context;
import android.widget.LinearLayout;

public class Pickaxe extends androidx.appcompat.widget.AppCompatImageView{

    int skin;
    int speed;
    int dammage;


    public Pickaxe(Context context, int skin, int speed, int power) {
        super(context);
        this.setImageResource(R.drawable.pickaxe);
        this.skin = skin;
        this.speed = speed;
        this.dammage = power;
        this.setLayoutParams(new LinearLayout.LayoutParams(350, 350));
    }

    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDammage() {
        return dammage;
    }

    public void setDammage(int dammage) {
        this.dammage = dammage;
    }


}
