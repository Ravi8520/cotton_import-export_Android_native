package com.ecotton.impex.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.PageAdapter;

public class CalculatorActivity extends AppCompatActivity {

    ImageView backarrow;
    TabLayout tablaout;
    TabItem tab_ginning,tab_spinning,tab_exports;
    ViewPager tab_view_pager;
    PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        backarrow = findViewById(R.id.backarrow);
        tablaout = findViewById(R.id.tablaout);
        tab_ginning = findViewById(R.id.tab_ginning);
        tab_spinning = findViewById(R.id.tab_spinning);
        tab_exports = findViewById(R.id.tab_exports);
        tab_view_pager = findViewById(R.id.tab_view_pager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(),tablaout.getTabCount());
        tab_view_pager.setAdapter(pageAdapter);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tablaout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab_view_pager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition() == 1 || tab.getPosition()==2){
                    pageAdapter.notifyDataSetChanged();
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