package com.ecotton.impex.activities;

import static com.ecotton.impex.MyApp.filterRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.BuyerStateCompanyAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityBuyerStateListBinding;
import com.ecotton.impex.models.dashboard.DashBoardModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerStateListActivity extends AppCompatActivity {
    public ActivityBuyerStateListBinding binding;
    private CustomDialog customDialog;
    public BuyerStateListActivity mContext;
    private SessionUtil mSessionUtil;
    public BuyerStateCompanyAdapter adpter;
    public String stateID = "-1";
    private List<DashBoardModel.CityModel> cityModelList = new ArrayList<>();
    private List<DashBoardModel.StateModel> stateModelList = new ArrayList<>();

    private String screen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyerStateListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        setUpStateRecyclerVeiw();


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, com.ecotton.impex.activities.FilterActivity.class)
                        .putExtra("screen", BuyerStateListActivity.class.getSimpleName()));
            }
        });
        binding.imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, com.ecotton.impex.activities.DealsActivity.class));
            }
        });
    }

    private boolean isFromFilter() {
        return screen.equals(com.ecotton.impex.activities.FilterActivity.class.getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        stateID = filterRequest.getState_id();
        Dashboard_buyer();
    }

    public void setUpStateRecyclerVeiw() {
        binding.recyclerview.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.recyclerview.setLayoutManager(layoutManager);
        adpter = new BuyerStateCompanyAdapter(this);
        binding.recyclerview.setAdapter(adpter);

        adpter.setOnItemClickListener(new BuyerStateCompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, com.ecotton.impex.activities.DashboardCompanyListActivity.class)
                        .putExtra("dashBoardModelList", new Gson().toJson(stateModelList))
                        .putExtra("cityModelList", new Gson().toJson(cityModelList))
                        .putExtra("districtID", adpter.mArrayList.get(position).getDistrict_id() + ""));
            }
        });
    }

    private void Dashboard_buyer() {
        try {

            String data = new Gson().toJson(filterRequest);
            Log.e("data", "data==" + data);

            Call<ResponseModel<List<DashBoardModel>>> call;
            if (mSessionUtil.getUsertype().equals("seller"))
                call = APIClient.getInstance().filterBuyer(mSessionUtil.getApiToken(), data);
            else
                call = APIClient.getInstance().filteSeller(mSessionUtil.getApiToken(), data);


            call.enqueue(new Callback<ResponseModel<List<DashBoardModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DashBoardModel>>> call, Response<ResponseModel<List<DashBoardModel>>> response) {

                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        if (stateModelList.size() == 0) {
                            stateModelList.addAll(response.body().data.get(0).getStateModelList());
                            setUpSpiner();
                        }

                        cityModelList.clear();
                        adpter.addAllClass(cityModelList);
                        if (response.body().data.size() > 0)
                            if (response.body().data.get(0).getStateModelList().size() > 0) {
                                cityModelList.addAll(response.body().data.get(0).getStateModelList().get(0).getCityModelList());
                                adpter.addAllClass(response.body().data.get(0).getStateModelList().get(0).getCityModelList());
                            }


                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
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

    public void setUpSpiner() {

        CustomAdapter adapter = new CustomAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, stateModelList);
        binding.spinnerState.setAdapter(adapter);

        for (int i = 0; i < stateModelList.size(); i++) {
            if (stateModelList.get(i).getState_id() == Integer.parseInt(stateID)) {
                binding.spinnerState.setSelection(i);
                break;
            }
        }

        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                stateID = stateModelList.get(position).getState_id() + "";
                filterRequest.setState_id(stateModelList.get(position).getState_id() + "");
                Dashboard_buyer();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CustomAdapter extends ArrayAdapter<DashBoardModel.StateModel> {

        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, int textviewId, List<DashBoardModel.StateModel> list) {

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

            DashBoardModel.StateModel rowItem = getItem(position);

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

    public void setUpStateRecyclerVeiw(int position) {
        binding.recyclerview.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.recyclerview.setLayoutManager(layoutManager);
        adpter = new BuyerStateCompanyAdapter(mContext);
        binding.recyclerview.setAdapter(adpter);
        adpter.addAllClass(cityModelList);

    }

    @Override
    public void onBackPressed() {
        filterRequest.setState_id("-1");
        filterRequest.setProduct_id("-1");
       super.onBackPressed();
    }
}