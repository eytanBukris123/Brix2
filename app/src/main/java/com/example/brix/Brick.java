package com.example.brix;

import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;

public class Brick extends androidx.appcompat.widget.AppCompatImageView{

    private int health;
    private int time;
    private String[] types = {"stone", "silver", "gold", "diamond"};
    private String type;
    private int brickType;
    private int size;

    private int[] pictures = {R.drawable.gold_stone3, R.drawable.gold_stone2, R.drawable.gold_stone};

    public Brick(Context context, int health, int time, int brickType, int size) {
        super(context);
        this.setImageResource(R.drawable.gold_stone);
        this.health = health;
        this.time = time;
        this.type = types[brickType];
        this.brickType = brickType;
        this.size = size;
        this.setLayoutParams(new LinearLayout.LayoutParams(size, size));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public int getBrickType() {
        return brickType;
    }

    public void setBrickType(int brickType) {
        this.brickType = brickType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void createBrick(){
        GameActivity ga = new GameActivity();
        ga.createBrick();
    }

    public void Hit(){
        this.health--;
        this.setSize(this.getSize()-80);
        this.setLayoutParams(new LinearLayout.LayoutParams(size-10, size));
        if(health<=0)
            Dead();
        else
            this.setImageResource(pictures[health-1]);
    }

    public void Dead(){
        this.setVisibility(INVISIBLE);
        createBrick();
    }
//
//    public void timer(){
//        Handler h2 = new Handler();
//        Runnable run = new Runnable() {
//
//            @Override
//            public void run() {
//                h2.postDelayed(this, 500);
//            }
//        };
//    }


}
