package com.ecotton.impex.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.AttributeAdapter;
import com.ecotton.impex.adapters.CountryAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivitySelectBuyerBinding;
import com.ecotton.impex.models.AttributeModelFromTo;
import com.ecotton.impex.models.SearchSellerModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

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

            Log.e("newdata", "newdata==" + data);
        }
        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_select_exporter));
        } else {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_select_importer));
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
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("type", "post");
            jsonObject.put("product_id", productid);

            JSONArray jsonArray = new JSONArray(data);

            jsonObject.put("attribute_array", jsonArray);


            String data = jsonObject.toString();

            Log.e("jayesh", "jayesh==" + data);

            customDialog.displayProgress(mContext);

            Call<ResponseModel<List<SearchSellerModel>>> call = APIClient.getInstance().Search_to_buy(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<SearchSellerModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<SearchSellerModel>>> call, Response<ResponseModel<List<SearchSellerModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("SearchSellerModel", "SearchSellerModel==" + new Gson().toJson(response.body()));
                    if (response.body().status != 0) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast(mContext, response.body().message);
                            countryAdapter.addAllClass(response.body().data);
                            if (response.body().message.equals("Records not found")) {
                                final Dialog dialog = new Dialog(mContext);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setContentView(R.layout.importer_exporter_notfound_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                TextView txt_title = dialog.findViewById(R.id.txt_title);
                                txt_title.setText("No Exporter Found !");
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onBackPressed();
                                    }
                                });

                                dialog.show();
                            }
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
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("type", "post");
            jsonObject.put("product_id", productid);

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
                    Log.e("SearchSellerModel", "SearchSellerModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status != 0) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast(mContext, response.body().message);
                            countryAdapter.addAllClass(response.body().data);

                            if (response.body().message.equals("Records not found")) {
                                final Dialog dialog = new Dialog(mContext);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setContentView(R.layout.importer_exporter_notfound_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                TextView txt_title = dialog.findViewById(R.id.txt_title);
                                txt_title.setText("No Importer Found !");
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onBackPressed();
                                    }
                                });

                                dialog.show();
                            }
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