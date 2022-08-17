package com.ecotton.impex.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.MypostAdapter;

public class MYPostActivity extends AppCompatActivity {

    ImageView backarrow;
    TabLayout tablaout;
    ViewPager tab_view_pager;
    MypostAdapter mypostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        backarrow = findViewById(R.id.backarrow);
        tablaout = findViewById(R.id.tablaout);


        tab_view_pager = findViewById(R.id.tab_view_pager);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mypostAdapter = new MypostAdapter(getSupportFragmentManager(), tablaout.getTabCount());
        tab_view_pager.setAdapter(mypostAdapter);

        tab_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Fragment fragment = mypostAdapter.getItem(position);
                if (fragment != null) {
                    fragment.onResume();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tablaout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_view_pager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    mypostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tab_view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablaout));
    }
}