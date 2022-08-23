package com.ecotton.impex.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.CompanyListAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityCompanyListBinding;
import com.ecotton.impex.models.companylist.CompanyListModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyListActivity extends AppCompatActivity {


    CompanyListAdapter companyListAdapter;

    private CustomDialog customDialog;
    int mPageIndex = 1;

    ActivityCompanyListBinding binding;
    private Context mContext;
    private SessionUtil mSessionUtil;

    public static int is_setupped;

    private List<CompanyListModel> companyListModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);
        CardView add_company = (CardView) findViewById(R.id.add_company);

      /*  binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSessionUtil.isLogin()) {
                    finish();
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                    finish();
                }
            }
        });*/

        add_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AddCompanyActivity.class));


            }
        });
        setSellerinfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CompanyList();
    }

    private void setSellerinfo() {
        binding.companyList.setLayoutManager(new LinearLayoutManager(mContext));
        companyListAdapter = new CompanyListAdapter(CompanyListActivity.this, companyListModelList);
        binding.companyList.setAdapter(companyListAdapter);
    }

    private void CompanyList() {
        try {
            Call<ResponseModel<List<CompanyListModel>>> call = APIClient.getInstance().Company_list(mSessionUtil.getApiToken());
            call.enqueue(new Callback<ResponseModel<List<CompanyListModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<CompanyListModel>>> call, Response<ResponseModel<List<CompanyListModel>>> response) {
                    Log.e("response", "response==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        companyListModelList.clear();
                        companyListAdapter.companyListModels.clear();
                        companyListModelList.addAll(response.body().data);
                        companyListAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        companyListAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(CompanyListActivity.this);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<CompanyListModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(mContext)
                .setTitle("Alert")
                .setMessage("Are you sure you want to Logout this App?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

}