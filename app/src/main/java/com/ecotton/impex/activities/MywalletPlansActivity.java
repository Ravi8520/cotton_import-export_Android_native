package com.ecotton.impex.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.WalletPlansAdapter;

public class MywalletPlansActivity extends AppCompatActivity {

    ImageView backarrow;
    RecyclerView plans_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet_plans);
        plans_recycler = findViewById(R.id.plans_recycler);
        backarrow = findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setWalletplanAdapter();

    }

    private void setWalletplanAdapter() {

        WalletPlansAdapter adapter = new WalletPlansAdapter(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        plans_recycler.setLayoutManager(layoutManager);
        plans_recycler.setAdapter(adapter);
    }
}