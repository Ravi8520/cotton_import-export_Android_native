package com.ecotton.impex.activities;

import static com.ecotton.impex.MyApp.filterRequest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.DashboardCompanyAdapter;
import com.ecotton.impex.databinding.ActivityDashboardCompanyListBinding;
import com.ecotton.impex.models.BuyerFilterRequest;
import com.ecotton.impex.models.dashboard.DashBoardModel;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardCompanyListActivity extends AppCompatActivity {
    public ActivityDashboardCompanyListBinding binding;
    private CustomDialog customDialog;
    public DashboardCompanyListActivity mContext;
    private SessionUtil mSessionUtil;
    public DashboardCompanyAdapter companyAdapter;
    public String countryId = "-1";
    private List<DashBoardModel.CompanyModel> companyList = new ArrayList<>();

    private List<DashBoardModel> dashBoardModelList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardCompanyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        countryId = getIntent().getStringExtra("countryId");

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
    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(filterRequest.getProduct_id())) {
            filterRequest.setProduct_id("-1");
        }
        if (TextUtils.isEmpty(filterRequest.getCountry_id())) {
            filterRequest.setCountry_id("-1");
        }
        if (mSessionUtil.getUsertype().equals("seller")) {
            Dashboard_buyerFilter();
        } else if (mSessionUtil.getUsertype().equals("buyer")) {
            Dashboard_sellerFilter();
        }
    }

    public void setUpSpiner(List<DashBoardModel> list) {
        dashBoardModelList.clear();
        dashBoardModelList.addAll(list);
        try {
            CustomAdapter adapter = new CustomAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, dashBoardModelList);
            binding.spinnerState.setAdapter(adapter);
            binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    countryId = dashBoardModelList.get(position).getCountry_id() + "";
                    // filterRequest.setState_id(cityModelList.get(position).getDistrict_id() + "");

                    binding.txtName.setText(dashBoardModelList.get(position).getName());

                    binding.txtPost.setText("Post: " + dashBoardModelList.get(position).getCount());
                    binding.txtBales.setText("Bales:  " + dashBoardModelList.get(position).getBales());
                    setUpStateRecyclerVeiw(dashBoardModelList.get(position).getCompanyModelList());


                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private void Dashboard_buyerFilter() {
        try {

            String data = new Gson().toJson(filterRequest);
            Log.e("filterRequest", "filterRequest==" + data);
            Call<ResponseModel<List<DashBoardModel>>> call = APIClient.getInstance().filterBuyer(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DashBoardModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DashBoardModel>>> call, Response<ResponseModel<List<DashBoardModel>>> response) {
                    Log.e("dashboard", "dashboard==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS && response.body().data.size() > 0 && response.body() != null) {
                        //binding.linStateData.setVisibility(View.VISIBLE);
                        setUpSpiner(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        //binding.linStateData.setVisibility(View.GONE);
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<DashBoardModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Dashboard_sellerFilter() {
        try {
            String data = new Gson().toJson(filterRequest);
            Log.e("Dashboard_sellerFilter", "Dashboard_sellerFilter==" + data);
            Call<ResponseModel<List<DashBoardModel>>> call = APIClient.getInstance().filteSeller(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DashBoardModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DashBoardModel>>> call, Response<ResponseModel<List<DashBoardModel>>> response) {
                    Log.e("dashboard", "dashboard==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS && response.body().data.size() > 0 && response.body() != null) {
                       // binding.linStateData.setVisibility(View.VISIBLE);
                        setUpSpiner(response.body().data);

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        //binding.linStateData.setVisibility(View.GONE);
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<DashBoardModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomAdapter extends ArrayAdapter<DashBoardModel> {

        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, int textviewId, List<DashBoardModel> list) {

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

            DashBoardModel rowItem = getItem(position);

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