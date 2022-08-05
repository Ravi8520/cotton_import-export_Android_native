package com.ecotton.impex.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityAddCompanyBinding;
import com.ecotton.impex.models.AddCompanyModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompanyActivity extends AppCompatActivity {

    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;

    private AddCompanyActivity mContext;

    ActivityAddCompanyBinding binding;

    String homeaddcompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            homeaddcompany = intent.getStringExtra("homeaddcompany");
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    AddCompany();
                }
            }
        });

        binding.edtGstNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void AddCompany() {
        try {
            JSONObject object = new JSONObject();
            object.put("company_name", binding.edtCompanyName.getText().toString().trim());
            object.put("iec", binding.edtGstNumber.getText().toString().trim());

            String data = object.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<AddCompanyModel>> call = APIClient.getInstance().Addcompany(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<AddCompanyModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<AddCompanyModel>> call, Response<ResponseModel<AddCompanyModel>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("response", "response==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("AddCompanyModel", "AddCompanyModel==" + response.body().status);
                        onBackPressed();
                        AppUtil.showToast(mContext, response.body().message);
                        //stateAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        //stateAdapter.notifyDataSetChanged();
                        AppUtil.showToast(mContext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<AddCompanyModel>> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidForm() {
        String passwordInput = binding.edtGstNumber.getText().toString();
        Pattern PASSWORD_PATTERN =
                Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}"
                        + "[A-Z]{1}[1-9A-Z]{1}"
                        + "Z[0-9A-Z]{1}$");
      /*  if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.edtGstNumber.setError("Please Enter Proper GST Number");
            binding.edtGstNumber.requestFocus();
            return false;
        }*/
        return ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtCompanyName, binding.mTilcpmpanyName, mContext.getString(R.string.err_company_name), 4, mContext.getString(R.string.err_company, 4), 60, "")
          && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtGstNumber, binding.mTilgstNumber, mContext.getString(R.string.err_iec_number), 10, mContext.getString(R.string.err_enter_valid_iec_number));
        // && ValidationUtil.isBlankETAndTextInputError(mContext, binding.edtGstNumber, binding.mTilgstNumber, mContext.getString(R.string.err_gst), 15, mContext.getString(R.string.err_gst_number, 15), 20, "");
        // && ValidationUtil.isBlankETAndTextInputError(mContext, edt_primary_broker, mTilprimary_broker, mContext.getString(R.string.err_primary_broker), 6, mContext.getString(R.string.err_name_character, 6), 60, "");
    }

    public static boolean isValidGSTNo(String str) {
        // Regex to check valid
        // GST (Goods and Services Tax) number
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
                + "[A-Z]{1}[1-9A-Z]{1}"
                + "Z[0-9A-Z]{1}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find the matching
        // between the given string
        // and the regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }


}