package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.ecotton.impex.R;

public class UtilitiesActivity extends AppCompatActivity {

    RelativeLayout rl_buyer_dir,rl_mcx,rl_calculator,rl_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);
        rl_buyer_dir = findViewById(R.id.rl_buyer_dir);
        rl_mcx = findViewById(R.id.rl_mcx);
        rl_calculator = findViewById(R.id.rl_calculator);
        rl_news = findViewById(R.id.rl_news);


        rl_buyer_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UtilitiesActivity.this, BuyerActivity.class));
            }
        });

        rl_mcx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UtilitiesActivity.this, com.ecotton.impex.activities.MCXActivity.class));
            }
        });

        rl_calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UtilitiesActivity.this, com.ecotton.impex.activities.CalculatorActivity.class));
            }
        });

        rl_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UtilitiesActivity.this, com.ecotton.impex.activities.NewsActivity.class));
            }
        });
    }
}