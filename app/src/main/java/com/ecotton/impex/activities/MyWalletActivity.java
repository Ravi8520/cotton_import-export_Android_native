package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.MyWalletAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyWalletBinding;
import com.ecotton.impex.models.MyWalletModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletActivity extends AppCompatActivity {

    ActivityMyWalletBinding binding;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    MyWalletActivity mcontext;
    MyWalletAdapter myWalletAdapter;
    MyWalletModel myWalletModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mcontext = this;
        mSessionUtil = new SessionUtil(mcontext);
        customDialog = new CustomDialog();


        binding.btnAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int company_id = Integer.parseInt(mSessionUtil.getCompanyId());
                Intent intent = new Intent(mcontext, MywalletPlansActivity.class);
                intent.putExtra("home", "wallet");
                intent.putExtra("login_as", mSessionUtil.getUsertype());
                intent.putExtra("user_id", mSessionUtil.getUserid());
                intent.putExtra("company_id", company_id);
                startActivity(intent);
                finish();
            }
        });


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GetDealData();

    }


    private void setWalletAdapter() {
        myWalletAdapter = new MyWalletAdapter(mcontext, myWalletModelList.getTransaction_history());
        binding.transactionRv.setLayoutManager(new LinearLayoutManager(mcontext));
        binding.transactionRv.setAdapter(myWalletAdapter);

    }

    private void GetDealData() {
        try {
            customDialog.displayProgress(mcontext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<MyWalletModel>> call = APIClient.getInstance().transaction_history(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<MyWalletModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<MyWalletModel>> call, Response<ResponseModel<MyWalletModel>> response) {
                    customDialog.dismissProgress(mcontext);
                    if (response.body().data != null) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            myWalletModelList = response.body().data;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setWalletAdapter();
                                    SetData();
                                }
                            }, 100);
                        }
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
                public void onFailure(Call<ResponseModel<MyWalletModel>> call, Throwable t) {
                    customDialog.dismissProgress(mcontext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetData() {
        binding.txtWalletAmount.setText(getResources().getString(R.string.lbl_rupees_symbol_only) + " " + myWalletModelList.getWallet_amount());
        binding.txtLeftDays.setText(myWalletModelList.getLeft_days());
        Log.e("getLeft_days", "getLeft_days==" + myWalletModelList.getLeft_days());
        binding.walletProgressBar.setMax(Integer.parseInt(myWalletModelList.getValidity()));
        binding.walletProgressBar.setProgress(Integer.parseInt(myWalletModelList.getLeft_days()));
    }

}