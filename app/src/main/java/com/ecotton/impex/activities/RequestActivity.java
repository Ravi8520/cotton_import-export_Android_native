package com.ecotton.impex.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.RequestAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityRequestBinding;
import com.ecotton.impex.models.RequestModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends AppCompatActivity {

    ActivityRequestBinding binding;
    RequestAdapter requestAdapter;

    private RequestActivity mContext;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GetRequestList();


        DividerItemDecoration divider =
                new DividerItemDecoration(mContext,
                        DividerItemDecoration.VERTICAL);
        divider.setDrawable(mContext.getResources().getDrawable(R.drawable.line_divider));

        requestAdapter = new RequestAdapter(mContext);
        binding.requestRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        binding.requestRecyclerview.addItemDecoration(divider);
        binding.requestRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        binding.requestRecyclerview.setAdapter(requestAdapter);

        requestAdapter.setOnItemClickListener(new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.btn_accept) {

                    AcceptRequest(requestAdapter.mArrayList.get(position).getRequested_company_broker(), position, "accept");

                    // dialog.show();
                }
                if (view.getId() == R.id.btn_reject) {
                    RejectRequest(position, "reject");
                }
            }
        });
    }

    private void RejectRequest(int position, String accept) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("request_id", requestAdapter.mArrayList.get(position).getId());
            object.put("referral_code", requestAdapter.mArrayList.get(position).getRequested_company_broker());
            object.put("status", accept);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<RequestModel>> call = APIClient.getInstance().accept_reject_member_request(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<RequestModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<RequestModel>> call, Response<ResponseModel<RequestModel>> response) {
                    customDialog.dismissProgress(mContext);
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
                public void onFailure(Call<ResponseModel<RequestModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AcceptRequest(String referralcode, int position, String accept) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("request_id", requestAdapter.mArrayList.get(position).getId());
            object.put("referral_code", referralcode);
            object.put("status", accept);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<RequestModel>> call = APIClient.getInstance().accept_reject_member_request(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<RequestModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<RequestModel>> call, Response<ResponseModel<RequestModel>> response) {
                    customDialog.dismissProgress(mContext);
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
                public void onFailure(Call<ResponseModel<RequestModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void GetRequestList() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<List<RequestModel>>> call = APIClient.getInstance().request_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<RequestModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<RequestModel>>> call, Response<ResponseModel<List<RequestModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        requestAdapter.addAllClass(response.body().data);
                        Log.e("response", "request_list==" + new Gson().toJson(response.body().data));
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<RequestModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}