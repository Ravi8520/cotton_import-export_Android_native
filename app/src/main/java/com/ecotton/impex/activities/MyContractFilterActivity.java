package com.ecotton.impex.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.MyContractFilterAdapter;

public class MyContractFilterActivity extends AppCompatActivity {

    RecyclerView by_Seller_recycler;
    Context context;
    Button btn_custome,btn_monthly,btn_weekly;
    ImageView iv_search,backarrow;
    SearchView searchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contract_filter);
        by_Seller_recycler = findViewById(R.id.by_Seller_recycler);
        btn_custome = findViewById(R.id.btn_custome);
        btn_monthly = findViewById(R.id.btn_monthly);
        btn_weekly = findViewById(R.id.btn_weekly);
        iv_search = findViewById(R.id.iv_search);
        searchview = findViewById(R.id.searchview);
        backarrow = findViewById(R.id.backarrow);

        searchview.setVisibility(View.GONE);  

        context = this;
        setAdapter();

        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().
                setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view.getId()==R.id.btn_weekly){
                    btn_weekly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_weekly.setTextColor(getResources().getColor(R.color.white));

                    btn_monthly.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_monthly.setTextColor(getResources().getColor(R.color.black));

                    btn_custome.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_custome.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        btn_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.btn_monthly){
                    btn_monthly.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_monthly.setTextColor(getResources().getColor(R.color.white));

                    btn_weekly.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_weekly.setTextColor(getResources().getColor(R.color.black));

                    btn_custome.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_custome.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        btn_custome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.btn_custome){
                    btn_custome.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_custome.setTextColor(getResources().getColor(R.color.white));

                    btn_monthly.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_monthly.setTextColor(getResources().getColor(R.color.black));

                    btn_weekly.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_weekly.setTextColor(getResources().getColor(R.color.black));

                    materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick(Object selection) {

                        }
                    });
                }
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_search.setVisibility(View.INVISIBLE);
                searchview.setVisibility(View.VISIBLE);
                searchview.setIconified(false);
            }
        });
    }
    private void setAdapter() {

        MyContractFilterAdapter adapter = new MyContractFilterAdapter(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        by_Seller_recycler.setLayoutManager(layoutManager);
        by_Seller_recycler.setAdapter(adapter);
    }
}