package com.ecotton.impex.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.DashboardCompanyAdapter;
import com.ecotton.impex.databinding.ActivityDashboardCompanyListBinding;
import com.ecotton.impex.models.BuyerFilterRequest;
import com.ecotton.impex.models.dashboard.DashBoardModel;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DashboardCompanyListActivity extends AppCompatActivity {
    public ActivityDashboardCompanyListBinding binding;
    private CustomDialog customDialog;
    public DashboardCompanyListActivity mContext;
    private SessionUtil mSessionUtil;
    public DashboardCompanyAdapter companyAdapter;
    public BuyerFilterRequest filterRequest;
    public String districtID = "-1";
    private List<DashBoardModel.CompanyModel> companyList = new ArrayList<>();
    private List<DashBoardModel.CityModel> cityModelList = new ArrayList<>();
    private List<DashBoardModel.StateModel> stateModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardCompanyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        districtID = getIntent().getStringExtra("districtID");
        String jsonDashboard = getIntent().getStringExtra("stateModelList");
        Type type = new TypeToken<List<DashBoardModel.StateModel>>() {
        }.getType();
        stateModelList = new Gson().fromJson(jsonDashboard, type);

        String jsonCity = getIntent().getStringExtra("cityModelList");
        type = new TypeToken<List<DashBoardModel.CityModel>>() {
        }.getType();
        cityModelList = new Gson().fromJson(jsonCity, type);

        setUpSpiner();

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, FilterActivity.class)
                        .putExtra("screen", DashboardCompanyListActivity.class.getSimpleName()));
            }
        });
        binding.imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, com.ecotton.impex.activities.DealsActivity.class));
            }
        });
    }

    public void setUpSpiner() {

        CustomAdapter adapter = new CustomAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, cityModelList);
        binding.spinnerState.setAdapter(adapter);

        for (int i = 0; i < cityModelList.size(); i++) {
            if (cityModelList.get(i).getDistrict_id() == Integer.parseInt(districtID)) {
                binding.spinnerState.setSelection(i);
                break;
            }
        }

        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                districtID = cityModelList.get(position).getDistrict_id() + "";
                // filterRequest.setState_id(cityModelList.get(position).getDistrict_id() + "");

                binding.txtName.setText(cityModelList.get(position).getName());

                binding.txtPost.setText("Post: " + cityModelList.get(position).getCount());
                binding.txtBales.setText("Bales:  " + cityModelList.get(position).getBales());
                setUpStateRecyclerVeiw(cityModelList.get(position).getCompanyModelList());
                //Dashboard_buyer();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CustomAdapter extends ArrayAdapter<DashBoardModel.CityModel> {

        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, int textviewId, List<DashBoardModel.CityModel> list) {

            super(context, resouceId, textviewId, list);
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            DashBoardModel.CityModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.layout_spiner, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public void setUpStateRecyclerVeiw(List<DashBoardModel.CompanyModel> models) {
        binding.recyclerview.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.recyclerview.setLayoutManager(layoutManager);
        companyAdapter = new DashboardCompanyAdapter(this);
        binding.recyclerview.setAdapter(companyAdapter);
        companyAdapter.addAllClass(models);
        companyAdapter.setOnItemClickListener(new DashboardCompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPreferences prefs = getSharedPreferences("cotton", MODE_PRIVATE);
                int idName = prefs.getInt("issetup", 0); //0 is the default value.
                Log.e("idName", "idName==" + idName);
                if (idName == 0) {
                    CompanyDetailDailog();
                } else if (idName == 1) {
                    startActivity(new Intent(mContext, com.ecotton.impex.activities.PostDetailActivity.class)
                            .putExtra("screen", "home")
                            .putExtra("post_id", companyAdapter.mArrayList.get(position).getPost_id() + "")
                            .putExtra("post_type", companyAdapter.mArrayList.get(position).getType()));
                }

            }
        });
    }
    public void CompanyDetailDailog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.company_details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = (TextView) dialog.findViewById(R.id.txt_ok);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(mContext, UpdateCompanyDetailsActivity.class));
            }
        });

        dialog.show();
    }
}