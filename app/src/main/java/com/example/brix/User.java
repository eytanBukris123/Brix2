package com.example.brix;

public class User {

    int coins;
    int power;
    int speed;
    int skin;

    public User(int coins, int power, int speed, int skin){
        this.coins = coins;
        this.power = power;
        this.speed = speed;
        this.skin = skin;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }
}
