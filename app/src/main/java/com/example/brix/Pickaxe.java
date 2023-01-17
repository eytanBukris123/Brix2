package com.example.brix;

import android.content.Context;
import android.widget.LinearLayout;

public class Pickaxe extends androidx.appcompat.widget.AppCompatImageView{

    private int skin;
    private int speed;
    private int dammage;
    int[] skins = {R.drawable.pickaxe, R.drawable.witch_pickaxe, R.drawable.silver_pickaxe, R.drawable.cool_pickaxe, R.drawable.poision_pickaxe, R.drawable.king_axe};


    public Pickaxe(Context context, int skin, int speed, int power) {
        super(context);
        this.skin = skin;
        this.speed = speed;
        this.dammage = power;
        this.setImageResource(skins[skin]);
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
