package com.ecotton.impex.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.PostDetailActivity;
import com.ecotton.impex.activities.UpdateCompanyDetailsActivity;
import com.ecotton.impex.adapters.DealNotifictonAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentDealNotificationBinding;
import com.ecotton.impex.models.NotificatioListModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealNotificationFragment extends Fragment {

    DealNotifictonAdapter dealNotifictonAdapter;
    private Context mContext;

    private DealNotificationFragment activity;

    private SessionUtil mSessionUtil;

    private CustomDialog customDialog;

    FragmentDealNotificationBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDealNotificationBinding.inflate(getLayoutInflater());

        customDialog = new CustomDialog();

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSessionUtil.getUsertype().equals("seller")) {
                    SellerGetNotificationList();
                } else {
                    BuyerGetNotificationList();
                }
            }
        });

//        if (mSessionUtil.getUsertype().equals("seller")) {
//            SellerGetNotificationList();
//        } else {
//            BuyerGetNotificationList();
//        }

        dealNotifictonAdapter = new DealNotifictonAdapter(getActivity());
        binding.recyclerviewNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewNotification.setAdapter(dealNotifictonAdapter);

        dealNotifictonAdapter.setOnItemClickListener(new DealNotifictonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.btn_view) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("cotton", MODE_PRIVATE);
                    int idName = prefs.getInt("issetup", 0); //0 is the default value.
                    Log.e("idName", "idName==" + idName);
                    if (idName == 0) {
                        CompanyDetailDailog();
                    } else if (idName == 1) {
                        startActivity(new Intent(mContext, PostDetailActivity.class)
                                .putExtra("screen", "home")
                                .putExtra("post_id", dealNotifictonAdapter.mArrayList.get(position).getNotification_id() + "")
                                .putExtra("post_type", "notification"));
                    }

                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSessionUtil.getUsertype().equals("seller")) {
            SellerGetNotificationList();
        } else {
            BuyerGetNotificationList();
        }
    }

    public void CompanyDetailDailog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.company_details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = (TextView) dialog.findViewById(R.id.txt_ok);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(mContext, UpdateCompanyDetailsActivity.class));
            }
        });

        dialog.show();
    }

    private void SellerGetNotificationList() {
        try {
            binding.refreshLayout.setRefreshing(false);
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            String data = jsonObject.toString();
            Log.e("data", "data11111=" + data);
            Call<ResponseModel<List<NotificatioListModel>>> call = APIClient.getInstance().notification_to_seller_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<NotificatioListModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<NotificatioListModel>>> call, Response<ResponseModel<List<NotificatioListModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("NotificatioListModel", "NotificatioListModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        dealNotifictonAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        dealNotifictonAdapter.clearData();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        dealNotifictonAdapter.clearData();
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<NotificatioListModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BuyerGetNotificationList() {
        try {
            binding.refreshLayout.setRefreshing(false);
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            String data = jsonObject.toString();
            Log.e("data", "data11111=" + data);
            Call<ResponseModel<List<NotificatioListModel>>> call = APIClient.getInstance().notification_to_buy_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<NotificatioListModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<NotificatioListModel>>> call, Response<ResponseModel<List<NotificatioListModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("NotificatioListModel", "NotificatioListModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        dealNotifictonAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        dealNotifictonAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<NotificatioListModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}