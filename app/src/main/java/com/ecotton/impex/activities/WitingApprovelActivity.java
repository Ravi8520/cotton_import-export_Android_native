package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ecotton.impex.R;

public class WitingApprovelActivity extends AppCompatActivity {

    String create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witing_approvel);

        Intent intent = getIntent();
        if (intent != null) {
            create = intent.getStringExtra("create");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        if (create.equals("create")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), CompanyListActivity.class));
        }
        finish();
    }
}