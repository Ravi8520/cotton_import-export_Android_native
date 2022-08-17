package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyMemberDataBinding;
import com.ecotton.impex.models.AddMemberModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMemberDataActivity extends AppCompatActivity {


    ActivityMyMemberDataBinding binding;
    MyMemberDataActivity mContext;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyMemberDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    AddMember();
                }
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean isValidForm() {
        if (binding.edtName.getText().toString().length() < 3 || binding.edtName.getText().toString().trim().isEmpty()) {
            binding.edtName.setError("Please Enter Name");
            binding.edtName.requestFocus();
            return false;
        }
        if (binding.edtNumber.getText().toString().isEmpty()) {
            binding.edtNumber.setError("Please Enter Email ");
            binding.edtNumber.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtNumber.getText().toString()).matches()) {
            binding.edtNumber.setError("Please Enter Proper Email Format ");
            binding.edtNumber.requestFocus();
            return false;
        }

        if (binding.edtDesignation.getText().toString().trim().equals("")) {
            binding.edtDesignation.setError("Please Enter Designation");
            binding.edtDesignation.requestFocus();
            return false;
        }
        return true;
    }

    private void AddMember() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("name", binding.edtName.getText().toString().trim());
            jsonObject.put("email", binding.edtNumber.getText().toString().trim());
            jsonObject.put("designation", binding.edtDesignation.getText().toString().trim());

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<AddMemberModel>> call = APIClient.getInstance().Addmember(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<AddMemberModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<AddMemberModel>> call, Response<ResponseModel<AddMemberModel>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("AddMemberModel", "AddMemberModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        onBackPressed();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel<AddMemberModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, MyMemberActivity.class));
        finish();
    }
}