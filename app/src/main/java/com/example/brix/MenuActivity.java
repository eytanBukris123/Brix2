package com.example.brix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView playbtn, shopbtn, instructionsBtn, signoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setLayoutData();

        setDailyNotifications();

        }

        //setting all layoutObjectsData
        private void setLayoutData(){
            playbtn = findViewById(R.id.playbtn);
            shopbtn = findViewById(R.id.shopbtn);
            signoutBtn = findViewById(R.id.signoutBtn);
            instructionsBtn = findViewById(R.id.instructionsBtn);
            playbtn.setOnClickListener(this);
            shopbtn.setOnClickListener(this);
            instructionsBtn.setOnClickListener(this);
            signoutBtn.setOnClickListener(this);
        }


        //setting the daily notifications
        private void setDailyNotifications(){
            notificationChannel();


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);

            if(Calendar.getInstance().after(calendar)){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(MenuActivity.this, MemoBrodcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

        //creating notification channel
        private void notificationChannel(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = "PASTICCINO";
                String description = "PASTICCINO'S CHANNEL";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("DaileyNotification", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
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