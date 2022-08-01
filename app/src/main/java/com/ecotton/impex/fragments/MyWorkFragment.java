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

import com.google.gson.Gson;
import com.ecotton.impex.activities.BrokersActivity;
import com.ecotton.impex.activities.ChangepasswordActivity;
import com.ecotton.impex.activities.LoginActivity;
import com.ecotton.impex.activities.MYPostActivity;
import com.ecotton.impex.activities.MyContractActivity;
import com.ecotton.impex.activities.MyFavouritesActivity;
import com.ecotton.impex.activities.MyMemberActivity;
import com.ecotton.impex.activities.MyProfileActivity;
import com.ecotton.impex.activities.MyWalletActivity;
import com.ecotton.impex.activities.ReportActivity;
import com.ecotton.impex.activities.RequestActivity;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.databinding.FragmentMyWorkBinding;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyWorkFragment extends Fragment {
    private Context mContext;

    FragmentMyWorkBinding binding;

    private SessionUtil mSessionUtil;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyWorkBinding.inflate(getLayoutInflater());


        if (mSessionUtil.getUsertype().equals("seller")) {
            binding.rlMybroker.setVisibility(View.GONE);
        }
        binding.rlMypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MYPostActivity.class));
            }
        });
        binding.rlMycontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MyContractActivity.class));
            }
        });
        binding.rlChangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ChangepasswordActivity.class));
            }
        });
        binding.rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MyProfileActivity.class));
            }
        });
        binding.rlMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MyMemberActivity.class));
            }
        });

        binding.rlMyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, RequestActivity.class));
            }
        });
        binding.rlReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ReportActivity.class));
            }
        });
        binding.rlMybroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BrokersActivity.class));
            }
        });
        binding.rlFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MyFavouritesActivity.class));
            }
        });

        binding.rlWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MyWalletActivity.class));
            }
        });

        binding.rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });

        return binding.getRoot();
    }

    private void LogOut() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().LogOut(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String dataa = null;
                    try {
                        dataa = new String(response.body().bytes());
                        Log.e("response", "response==" + dataa);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    LoginModel model = gson.fromJson(dataa, LoginModel.class);
                    Log.e("response", "response==" + dataa);
                    if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                        mSessionUtil.logOut();
                        AppUtil.showToast1(mContext, model.getMessage());
                        startActivity(new Intent(mContext, LoginActivity.class));
                        ((Activity) mContext).finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, model.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}