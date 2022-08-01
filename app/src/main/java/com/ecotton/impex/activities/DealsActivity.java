package com.ecotton.impex.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.ecotton.impex.adapters.TabLayoutAdapter;
import com.ecotton.impex.databinding.ActivityDealsBinding;

public class DealsActivity extends AppCompatActivity {


    private DealsActivity mContext;

    ActivityDealsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDealsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.tablayout.addTab(binding.tablayout.newTab().setText("In Negotiation"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("Notification"));
        binding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);


        TabLayoutAdapter adapter = new TabLayoutAdapter(mContext, getSupportFragmentManager(), binding.tablayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tablayout));
        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}