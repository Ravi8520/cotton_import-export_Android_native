package com.ecotton.impex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentPersonalDetailBinding;
import com.ecotton.impex.models.UserProfileModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PersonalDetailFragment extends Fragment {

    private Context mContext;
    FragmentPersonalDetailBinding binding;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;

    UserProfileModel arraylist;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalDetailBinding.inflate(getLayoutInflater());
        customDialog = new CustomDialog();
        getUserProfile();
        return binding.getRoot();
    }

    private void getUserProfile() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            String data = jsonObject.toString();
            Call<ResponseModel<UserProfileModel>> call = APIClient.getInstance().user_profile(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<UserProfileModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<UserProfileModel>> call, Response<ResponseModel<UserProfileModel>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("user_profile", "user_profile==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        arraylist = response.body().data;
                        SetData();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(getActivity(), response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(getActivity(), response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel<UserProfileModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetData() {
        HashMap<String, String> map = new HashMap<>();
        map.put(SessionUtil.NAME, arraylist.getName());
        map.put(SessionUtil.EMAIL, arraylist.getEmail());
        map.put(SessionUtil.MOBILE_NO, arraylist.getMobile_number());
        map.put(SessionUtil.API_TOKEN, mSessionUtil.getApiToken());
        map.put(SessionUtil.COMPANY_NAME, mSessionUtil.getCompanyName());
        map.put(SessionUtil.USER_TYPE, mSessionUtil.getUsertype());
        map.put(SessionUtil.USERID, mSessionUtil.getUserid());
        map.put(SessionUtil.COMPANY_ID, mSessionUtil.getCompanyId());
        map.put(SessionUtil.PASS, mSessionUtil.getPass());
        mSessionUtil.setData(map);

        String test = arraylist.getName();
        String first = String.valueOf(test.charAt(0));

        String str = test;
        String separator = " ";
        int sepPos = str.indexOf(separator);
        if (sepPos == -1) {
            System.out.println("");
        }
        String data = str.substring(sepPos + separator.length());

        String second = String.valueOf(data.charAt(0));

        binding.firsLetter.setText(first);
        binding.secondLetter.setText(second);

        binding.tvUsername.setText(arraylist.getName());
        binding.tvContactName.setText(arraylist.getName());
        binding.tvUserType.setText(arraylist.getUser_type());
        binding.tvMobile.setText(arraylist.getMobile_number());
        binding.tvEmail.setText(arraylist.getEmail());
    }
}