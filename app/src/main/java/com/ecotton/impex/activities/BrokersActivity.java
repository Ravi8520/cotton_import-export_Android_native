package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.BrokerAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMybrokersBinding;
import com.ecotton.impex.models.BrokerModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrokersActivity extends AppCompatActivity {

    ActivityMybrokersBinding binding;
    BrokersActivity mcontext;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    BrokerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMybrokersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mcontext = this;

        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mcontext);

        if (mSessionUtil.getUsertype().equals("buyer")) {
            setBrokerAdapter();
            GetBrokerListData();
        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.layoutAddBroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mcontext, AddBrokerActivity.class));
            }
        });
    }

    private void setBrokerAdapter() {
        adapter = new BrokerAdapter(mcontext);
        binding.brokerRecycler.setLayoutManager(new LinearLayoutManager(mcontext));
        binding.brokerRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BrokerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.layout_delete) {
                    DeleteBroker(position);
                }
            }
        });
    }

    private void GetBrokerListData() {
        try {
            customDialog.displayProgress(mcontext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            String data = jsonObject.toString();

            Call<ResponseModel<List<BrokerModel>>> call = APIClient.getInstance().broker_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<BrokerModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<BrokerModel>>> call, Response<ResponseModel<List<BrokerModel>>> response) {
                    customDialog.dismissProgress(mcontext);
                    Log.e("BrokerModel", "BrokerModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        adapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mcontext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mcontext, response.body().message);
                        AppUtil.autoLogout(mcontext);
                    } else {
                        AppUtil.showToast(mcontext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<BrokerModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mcontext);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteBroker(int position) {
        try {
            customDialog.displayProgress(mcontext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("broker_id", adapter.mArrayList.get(position).getId());
            String data = jsonObject.toString();

            Call<ResponseModel<List<BrokerModel>>> call = APIClient.getInstance().broker_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<BrokerModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<BrokerModel>>> call, Response<ResponseModel<List<BrokerModel>>> response) {
                    customDialog.dismissProgress(mcontext);
                    Log.e("BrokerModel", "BrokerModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        adapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mcontext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mcontext, response.body().message);
                        AppUtil.autoLogout(mcontext);
                    } else {
                        AppUtil.showToast(mcontext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<BrokerModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mcontext);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}