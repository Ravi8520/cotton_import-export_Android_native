package com.ecotton.impex.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.ReportsAdapter;

public class ReportActivity extends AppCompatActivity {

    ImageView backarrow;
    TabLayout tablaout;
    ViewPager tab_view_pager;
    ReportsAdapter reportsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        backarrow = findViewById(R.id.backarrow);
        tablaout = findViewById(R.id.tablaout);
        tab_view_pager = findViewById(R.id.tab_view_pager);

        reportsAdapter = new ReportsAdapter(getSupportFragmentManager(), tablaout.getTabCount());

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tab_view_pager.setAdapter(reportsAdapter);
        tablaout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_view_pager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    reportsAdapter.notifyDataSetChanged();
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