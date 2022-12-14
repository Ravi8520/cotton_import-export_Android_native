package com.ecotton.impex.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.WalletPlansAdapter;
import com.ecotton.impex.databinding.ActivityMywalletPlansBinding;
import com.ecotton.impex.utils.SessionUtil;

public class MywalletPlansActivity extends AppCompatActivity {


    private SessionUtil mSessionUtil;
    private Context mContext;
    public ActivityMywalletPlansBinding binding;

    private String company_name;
    private String user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMywalletPlansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        Intent intent = getIntent();
        if (intent != null) {
            company_name = intent.getStringExtra("Company_name");
            user_type = intent.getStringExtra("User_type");
        }
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra(HomeActivity.COMPANY_Name,company_name);
                intent.putExtra(HomeActivity.USER_Type, user_type);
                startActivity(intent);
                finish();
            }
        });

        setWalletplanAdapter();
    }

    private void setWalletplanAdapter() {
        WalletPlansAdapter adapter = new WalletPlansAdapter(getApplicationContext());
        binding.plansRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.plansRecycler.setAdapter(adapter);
    }
}