package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.ProfilePageAdapter;
import com.ecotton.impex.databinding.ActivityMyProfileBinding;

public class MyProfileActivity extends AppCompatActivity {


    ProfilePageAdapter profilePageAdapter;

    ActivityMyProfileBinding binding;

    public MyProfileActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tablaout.addTab(binding.tablaout.newTab().setText(getResources().getString(R.string.tab_personal_detail)));
        binding.tablaout.addTab(binding.tablaout.newTab().setText(getResources().getString(R.string.tab_company_detail)));

        profilePageAdapter = new ProfilePageAdapter(getSupportFragmentManager(), binding.tablaout.getTabCount());
        binding.tabViewPager.setAdapter(profilePageAdapter);

        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tablaout.getSelectedTabPosition() == 0) {
                    startActivity(new Intent(mContext, EditPersonalDetailsActivity.class));
                    finish();
                } else {
                    Intent intent = new Intent(mContext, UpdateCompanyDetailsActivity.class);
                    intent.putExtra("edit", "edit_profile");
                    startActivity(intent);
                    finish();
                }
            }
        });


        binding.tablaout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    profilePageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.tabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tablaout));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}