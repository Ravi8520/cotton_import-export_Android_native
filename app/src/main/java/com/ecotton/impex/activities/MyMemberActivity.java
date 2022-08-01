package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.adapters.MyMemberAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyMemberBinding;
import com.ecotton.impex.models.MyMembermodel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMemberActivity extends AppCompatActivity {

    List<MyMembermodel> myMembermodelList = new ArrayList<>();

    ActivityMyMemberBinding binding;
    MyMemberAdapter myMemberAdapter;
    MyMemberActivity mContext;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        binding.btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyMemberActivity.this, MyMemberDataActivity.class));
            }
        });
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.tvCompanyname.setText(mSessionUtil.getCompanyName());
        setAddapter();
        GetMemberList();
    }

    private void setAddapter() {
        myMemberAdapter = new MyMemberAdapter(MyMemberActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.membersRecycler.setLayoutManager(layoutManager);
        binding.membersRecycler.setAdapter(myMemberAdapter);
    }
    private void GetMemberList() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            String data = jsonObject.toString();
            customDialog.displayProgress(mContext);
            Call<ResponseModel<List<MyMembermodel>>> call = APIClient.getInstance().GetMemberList(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<MyMembermodel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<MyMembermodel>>> call, Response<ResponseModel<List<MyMembermodel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("response", "response==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        myMemberAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        myMemberAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<MyMembermodel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}