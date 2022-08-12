package com.ecotton.impex.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.MypostActiveTabDataActivity;
import com.ecotton.impex.adapters.ActiveTabAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentActiveBinding;
import com.ecotton.impex.models.CancelPostModel;
import com.ecotton.impex.models.MyPostActiveModel;
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


public class ActiveFragment extends Fragment {

    ArrayList<MyPostActiveModel> activeuserdata;
    FragmentActiveBinding binding;

    private Context mContext;
    private SessionUtil mSessionUtil;

    private CustomDialog customDialog;
    private HomeFragment activity;

    ActiveTabAdapter activeTabAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentActiveBinding.inflate(getLayoutInflater());

        activeuserdata = new ArrayList<>();
        customDialog = new CustomDialog();

        activeTabAdapter = new ActiveTabAdapter(getActivity());
        binding.recyclerviewActivePost.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewActivePost.setAdapter(activeTabAdapter);
        activeTabAdapter.setOnItemClickListener(new ActiveTabAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.activetabcard) {
                    Intent intent = new Intent(mContext, MypostActiveTabDataActivity.class);
                    intent.putExtra("post_notification_id", activeTabAdapter.mArrayList.get(position).getId());
                    intent.putExtra("type", activeTabAdapter.mArrayList.get(position).getType());
                    startActivity(intent);
                    ((Activity) mContext).finish();
                }
                if (view.getId() == R.id.recyclerview_attribute) {
                    Intent intent = new Intent(mContext, MypostActiveTabDataActivity.class);
                    intent.putExtra("post_notification_id", activeTabAdapter.mArrayList.get(position).getId());
                    intent.putExtra("type", activeTabAdapter.mArrayList.get(position).getType());
                    startActivity(intent);
                    ((Activity) mContext).finish();
                }
                if (view.getId() == R.id.btn_cancle) {
                    CancelPost(position);
                }
            }
        });

        MyActivePost();

        return binding.getRoot();
    }

    private void CancelPost(int pos) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("post_notification_id", activeTabAdapter.mArrayList.get(pos).getId());
            jsonObject.put("type", activeTabAdapter.mArrayList.get(pos).getType());

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<CancelPostModel>> call = APIClient.getInstance().cancel_post(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<CancelPostModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<CancelPostModel>> call, Response<ResponseModel<CancelPostModel>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("CancelPostModel", "CancelPostModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        MyActivePost();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        activeTabAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<CancelPostModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void MyActivePost() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            if (mSessionUtil.getUsertype().equals("seller")) {
                Call<ResponseModel<List<MyPostActiveModel>>> call = APIClient.getInstance().notification_post_seller_list(mSessionUtil.getApiToken(), data);
                call.enqueue(new Callback<ResponseModel<List<MyPostActiveModel>>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<List<MyPostActiveModel>>> call, Response<ResponseModel<List<MyPostActiveModel>>> response) {
                        customDialog.dismissProgress(mContext);
                        Log.e("notification_post", "notification_post_seller_list==" + new Gson().toJson(response.body().data));
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            activeTabAdapter.addAllClass(response.body().data);
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            activeTabAdapter.notifyDataSetChanged();
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(getActivity());
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<List<MyPostActiveModel>>> call, Throwable t) {
                        customDialog.dismissProgress(mContext);
                    }
                });

            } else if (mSessionUtil.getUsertype().equals("buyer")) {
                Call<ResponseModel<List<MyPostActiveModel>>> call = APIClient.getInstance().notification_post_buy_list(mSessionUtil.getApiToken(), data);
                call.enqueue(new Callback<ResponseModel<List<MyPostActiveModel>>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<List<MyPostActiveModel>>> call, Response<ResponseModel<List<MyPostActiveModel>>> response) {
                        customDialog.dismissProgress(mContext);
                        Log.e("notification_post", "notification_post_seller_list111==" + new Gson().toJson(response.body().data));
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            activeTabAdapter.addAllClass(response.body().data);
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            activeTabAdapter.notifyDataSetChanged();
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(getActivity());
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<List<MyPostActiveModel>>> call, Throwable t) {
                        customDialog.dismissProgress(mContext);
                    }
                });

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}