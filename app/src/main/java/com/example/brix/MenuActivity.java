package com.example.brix;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView playbtn, shopbtn, instructionsBtn, signoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        playbtn = findViewById(R.id.playbtn);
        shopbtn = findViewById(R.id.shopbtn);
        signoutBtn = findViewById(R.id.signoutBtn);
        instructionsBtn = findViewById(R.id.instructionsBtn);
        playbtn.setOnClickListener(this);
        shopbtn.setOnClickListener(this);
        instructionsBtn.setOnClickListener(this);
        signoutBtn.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
        if(v==playbtn){
            Intent intent = new Intent(MenuActivity.this, GameActivity.class);
            startActivity(intent);
        }
        else if(v==shopbtn){
            Intent intent = new Intent(MenuActivity.this, ShopActivity.class);
            startActivity(intent);
        }
        else if(v==instructionsBtn){
            Intent intent = new Intent(MenuActivity.this, InstructionsActivity.class);
            startActivity(intent);
        }
        else if(v == signoutBtn){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}