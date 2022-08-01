package com.ecotton.impex.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.adapters.PostDetailAttributesAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityNegotiationDetailLastTwoBinding;
import com.ecotton.impex.models.Nagoatiaationdetaillasttwo;
import com.ecotton.impex.models.NegotiationList;
import com.ecotton.impex.models.PostDetail;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NegotiationDetailLastTwoActivity extends AppCompatActivity {
    public ActivityNegotiationDetailLastTwoBinding binding;
    private CustomDialog customDialog;
    public NegotiationDetailLastTwoActivity mContext;
    private SessionUtil mSessionUtil;
    public NegotiationList negotiationObj;
    public String postID = "", type = "";
    public PostDetail postDetail;
    public String buyerId = "", sellerId = "", postedCompanyId = "", negotiationByCompanyId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNegotiationDetailLastTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        //negotiationObj = (NegotiationList) getIntent().getSerializableExtra("obj");
        postID = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        postID = getIntent().getStringExtra("post_id");
        type = getIntent().getStringExtra("post_type");
        sellerId = getIntent().getStringExtra("seller_id");
        buyerId = getIntent().getStringExtra("buyer_id");
        postedCompanyId = getIntent().getStringExtra("posted_company_id");
        negotiationByCompanyId = getIntent().getStringExtra("negotiation_by_company_id");
        getNegotiationDetail();
        getPostDetail();

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getPostDetail() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("post_notification_id", postID);
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("type", type);
            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel<List<PostDetail>>> call = APIClient.getInstance().getPostDetail(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<PostDetail>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<PostDetail>>> call, Response<ResponseModel<List<PostDetail>>> response) {
                    Log.e("response", "response==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        postDetail = response.body().data.get(0);
                        setData();

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<PostDetail>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setData() {
        binding.txtCompanyName.setText(postDetail.getCompany_name());
        binding.txtUserName.setText(postDetail.getSeller_buyer_name());
        binding.txtPrice.setText("₹ " + postDetail.getPrice() + " " + "(" + postDetail.getNo_of_bales() + ")");
        binding.txtTitle.setText(postDetail.getProduct_name());
        binding.rvAttributs.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        binding.rvAttributs.setLayoutManager(layoutManager);
        PostDetailAttributesAdapter adapter = new PostDetailAttributesAdapter(this);
        binding.rvAttributs.setAdapter(adapter);
        adapter.addAllClass(postDetail.getAttribute_array());
    }


    private void getNegotiationDetail() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("posted_company_id", postedCompanyId);
            object.put("negotiation_by_company_id", negotiationByCompanyId);
            object.put("seller_id", sellerId);
            object.put("buyer_id", buyerId);
            object.put("post_notification_id", postID);
            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel<List<Nagoatiaationdetaillasttwo>>> call = APIClient.getInstance().getNegotiationDetail(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<Nagoatiaationdetaillasttwo>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<Nagoatiaationdetaillasttwo>>> call, Response<ResponseModel<List<Nagoatiaationdetaillasttwo>>> response) {
                     Log.e("response", "response==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setDataNegotiation(response.body().data);

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<Nagoatiaationdetaillasttwo>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDataNegotiation(List<Nagoatiaationdetaillasttwo> list) {

        binding.txtUserNameOne.setText(list.get(0).getSeller_name());
        binding.txtNgOne.setText(list.get(0).getCurrent_price() + "(" + list.get(0).getCurrent_no_of_bales() + ")");
        binding.txtPaymentConditionOne.setText(list.get(0).getPayment_condition());
        binding.txtTransmitConditionOne.setText(list.get(0).getTransmit_condition());
        binding.txtComapnyOne.setText(list.get(0).getSeller_company_name());
        binding.txtHeaderOne.setText(list.get(0).getHeader());
        binding.txtLabOne.setText(list.get(0).getLab());
        binding.txtBrokerNameOne.setText(list.get(0).getBroker_name());

        if (list.size() > 1){
            binding.buyerLayout.setVisibility(View.VISIBLE);
            binding.txtUserNameTwo.setText(list.get(1).getBuyer_name());
            binding.txtNgTwo.setText(list.get(1).getCurrent_price() + "(" + list.get(1).getCurrent_no_of_bales() + ")");
            binding.txtPaymentConditionTwo.setText(list.get(1).getPayment_condition());
            binding.txtTransmitConditionTwo.setText(list.get(1).getTransmit_condition());
            binding.txtComapnyTwo.setText(list.get(1).getBuyer_company_name());
            binding.txtHeaderTwo.setText(list.get(1).getHeader());
            binding.txtLabTwo.setText(list.get(1).getLab());
            binding.txtBrokerNameTwo.setText(list.get(1).getBroker_name());
        }else{
            binding.buyerLayout.setVisibility(View.GONE);
        }


    }
}