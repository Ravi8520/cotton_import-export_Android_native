package com.ecotton.impex.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.adapters.MyFavouriteAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyFavouritesBinding;
import com.ecotton.impex.models.MyFavouriteModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFavouritesActivity extends AppCompatActivity {

    ArrayList<MyFavouriteModel> datalist;
    public MyFavouriteAdapter adapter;
    public MyFavouritesActivity mContext;
    private SessionUtil mSessionUtil;
    int mPageIndex = 1;
    private CustomDialog customDialog;
    ActivityMyFavouritesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyFavouritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        datalist = new ArrayList<>();
        mContext = this;

        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        setAdapter();

        GetFevorite();

    }

    private void GetFevorite() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("user_type", mSessionUtil.getUsertype());
            Log.e("response", "response==" + mSessionUtil.getUserid() + mSessionUtil.getCompanyId());
            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel<List<MyFavouriteModel>>> call = APIClient.getInstance().my_favourites(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<MyFavouriteModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<MyFavouriteModel>>> call, Response<ResponseModel<List<MyFavouriteModel>>> response) {
                    Log.e("response", "response==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        if (response.body().data.isEmpty()) {
                            binding.emptyView.setVisibility(View.VISIBLE);
                        }
                        adapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        adapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<MyFavouriteModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        adapter = new MyFavouriteAdapter(MyFavouritesActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.myfavouriteRv.setLayoutManager(layoutManager);
        binding.myfavouriteRv.setAdapter(adapter);

    }
}