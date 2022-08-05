package com.ecotton.impex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentCompanyDetailBinding;
import com.ecotton.impex.models.CompanyDetailModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyDetailFragment extends Fragment {

    FragmentCompanyDetailBinding binding;
    private Context mContext;
    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;
    public static CompanyDetailModel arraylist;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompanyDetailBinding.inflate(getLayoutInflater());
        customDialog = new CustomDialog();


        GetCompanyDetails();

        return binding.getRoot();
    }

    private void GetCompanyDetails() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            String data = jsonObject.toString();
            Call<ResponseModel<CompanyDetailModel>> call = APIClient.getInstance().Get_company_details(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<CompanyDetailModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<CompanyDetailModel>> call, Response<ResponseModel<CompanyDetailModel>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("CompanyDetailModel", "CompanyDetailModel==" + new Gson().toJson(response.body().data));
                        arraylist = response.body().data;
                        SetData();
                        //  productValueAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        // productValueAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(getActivity(), response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<CompanyDetailModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SetData() {
        binding.txtUserType.setText(arraylist.getSellerBuyer_type());
        binding.txtPanOfCompany.setText(arraylist.getCompany_pan_no());
        binding.txtIecNumber.setText(arraylist.getIec());
        binding.txtCountryName.setText(arraylist.getCountry_name());
        binding.txtGstNumber.setText(arraylist.getGst_no());
        binding.tvBankName.setText(arraylist.getBank_name());
        binding.tvAccountNumber.setText(arraylist.getAccount_number());
        binding.tvBranchAddress.setText(arraylist.getBranch_address());
        binding.tvIfscNumber.setText(arraylist.getIfsc_code());
        Glide.with(mContext).load(arraylist.getStamp_img()).placeholder(R.drawable.placeholder).into(binding.imgStamp);
    }

}