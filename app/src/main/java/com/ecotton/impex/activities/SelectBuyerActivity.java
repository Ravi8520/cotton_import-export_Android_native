package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.models.AttributeModel;
import com.ecotton.impex.models.AttributeModelFromTo;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.AttributeAdapter;
import com.ecotton.impex.adapters.CountryAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivitySelectBuyerBinding;
import com.ecotton.impex.models.SearchSellerModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectBuyerActivity extends AppCompatActivity {

    ActivitySelectBuyerBinding binding;
    CountryAdapter countryAdapter;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    SelectBuyerActivity mContext;
    List<SearchSellerModel> countryModelList = new ArrayList<>();
    String data;
    List<AttributeModelFromTo> attributeRequestModels = new ArrayList<>();
    AttributeAdapter attributeAdapter;

    private int productid;
    private String productname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectBuyerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getStringExtra("data");
            productid = intent.getIntExtra("product_id", 0);
            productname = intent.getStringExtra("productname");

        }
        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_select_seller));
        }

        binding.txtProductName.setText(productname);

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String from = jsonObject.getString("from");
                String to = jsonObject.getString("to");
                String attribute = jsonObject.getString("attribute");

                AttributeModelFromTo attributeModel = new AttributeModelFromTo(to, from, attribute);
                attributeRequestModels.add(attributeModel);

            }

        } catch (Exception e) {
            e.getMessage();
        }

        DividerItemDecoration divider =
                new DividerItemDecoration(getApplicationContext(),
                        DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.line_divider));

        attributeAdapter = new AttributeAdapter(mContext, attributeRequestModels);
        binding.recyclerviewAttribute.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerviewAttribute.addItemDecoration(divider);
        binding.recyclerviewAttribute.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.HORIZONTAL));
        binding.recyclerviewAttribute.setAdapter(attributeAdapter);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        countryAdapter = new CountryAdapter(mContext);
        binding.recyclerviewCountry.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewCountry.setAdapter(countryAdapter);

        countryAdapter.addAllClass(countryModelList);

        countryAdapter.setOnItemClickListener(new CountryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        if (mSessionUtil.getUsertype().equals("seller")) {
            SearchBuyer();
        } else if (mSessionUtil.getUsertype().equals("buyer")) {
            SearchSeller();
        }

    }

    private void SearchSeller() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("seller_buyer_id", mSessionUtil.getUserid());
            jsonObject.put("type","post");
            jsonObject.put("product_id", productid);

          /*  jsonObject.put("price", price);
            jsonObject.put("no_of_bales", no_of_bales);*/
            JSONArray jsonArray = new JSONArray(data);

            jsonObject.put("attribute_array", jsonArray);


            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            customDialog.displayProgress(mContext);

            Call<ResponseModel<List<SearchSellerModel>>> call = APIClient.getInstance().Search_to_buy(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<SearchSellerModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<SearchSellerModel>>> call, Response<ResponseModel<List<SearchSellerModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("Response", "Response==" + new Gson().toJson(response.body()));
                    if (response.body().status != 0) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast(mContext, response.body().message);
                            countryAdapter.addAllClass(response.body().data);
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(mContext, response.body().message);
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(mContext);
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<SearchSellerModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchBuyer() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("seller_buyer_id", mSessionUtil.getUserid());
            jsonObject.put("type", "post");
            jsonObject.put("product_id", productid);
           /* jsonObject.put("price", price);
            jsonObject.put("no_of_bales", no_of_bales);*/
            JSONArray jsonArray = new JSONArray(data);

            jsonObject.put("attribute_array", jsonArray);


            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            customDialog.displayProgress(mContext);
            Call<ResponseModel<List<SearchSellerModel>>> call;
            call = APIClient.getInstance().Search_to_sell(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<SearchSellerModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<SearchSellerModel>>> call, Response<ResponseModel<List<SearchSellerModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("Response", "Response==" + new Gson().toJson(response.body()));
                    if (response.body().status != 0) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast(mContext, response.body().message);
                            countryAdapter.addAllClass(response.body().data);
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(mContext, response.body().message);
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(mContext);
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<SearchSellerModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}