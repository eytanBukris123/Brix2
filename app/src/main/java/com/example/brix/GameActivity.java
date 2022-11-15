package com.example.brix;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.security.Provider;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

        ConstraintLayout gameLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_game);

                gameLayout = findViewById(R.id.gameLayout);

        }
}

class myBackgroundService extends Service {

        Brick brick1;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
                new Thread(new Runnable() {
                        @Override
                        public void run() {
                                while(true){

                                        brick1 = new Brick(GameActivity.class, 2, 3, 2, 30);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
                                        brick1.setLayoutParams(lp);
                                        GameActivity ga = new GameActivity();
                                        ga.gameLayout.addView(brick1);
                                        Random r = new Random();
                                        brick1.setX(r.nextInt(900));
                                        brick1.setY(r.nextInt(1700));
                                        try {
                                                Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }
                                }
                        }
                });
                return super.onStartCommand(intent, flags, startId);

        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }
}
