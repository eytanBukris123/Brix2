package com.example.brix;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Brick extends androidx.appcompat.widget.AppCompatImageView{

    private int health;
    private int time;
    private String[] types = {"silver", "gold", "diamond"};
    private int brickType;
    private int size;

    private int[] silverPictures = {R.drawable.rock3, R.drawable.rock2, R.drawable.rock};
    private int[] goldPictures = {R.drawable.gold_stone2, R.drawable.gold_stone, R.drawable.gold_stone3};
    private int[] dimondPictures = {R.drawable.diamond2, R.drawable.diamond1, R.drawable.diamond};
    private int[] pictures;

    public Brick(Context context, int health, int time, int brickType, int size) {
        super(context);
        this.health = health;
        this.time = time;
        this.brickType = brickType;
        this.size = size;
        if(brickType==0){
            pictures = silverPictures;
        }
        else if(brickType==1){
            pictures = goldPictures;
        }
        else if(brickType==2){
            pictures = dimondPictures;
        }
        this.setImageResource(pictures[2]);
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

    //lose health when brick is hit and check if brick destroyed
    public boolean Hit(int damage){
        this.health-= damage;
        if(health<=0) {
            return true;
        }
        else {
            if(health<=pictures.length) {
                if (pictures[health - 1] != 0) {
                    this.setImageResource(pictures[health - 1]);
                }
            }
        }
            return false;
    }


}
