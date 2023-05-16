package com.example.brix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView instructionsImg, leftArrow, rightArrow;
    int instructionsNumber = 0;
    int[] instructionsImages = {R.drawable.instructions1, R.drawable.instructions2, R.drawable.instructions3, R.drawable.instructions4, R.drawable.instructions5, R.drawable.instructions6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        instructionsImg = findViewById(R.id.instructionsImg);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);

        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);

        leftArrow.setClickable(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onClick(View v) {
        if(v==leftArrow){
            instructionsNumber--;
            instructionsImg.setImageResource(instructionsImages[instructionsNumber]);

            if(instructionsNumber==0){
                leftArrow.setClickable(false);
            }

        }
        else if(v==rightArrow){
            if(instructionsNumber==5){
                Intent intent = new Intent(InstructionsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
            else {
                leftArrow.setClickable(true);
                instructionsNumber++;
                instructionsImg.setImageResource(instructionsImages[instructionsNumber]);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
