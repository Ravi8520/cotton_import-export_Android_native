package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityEditPersonalDetailsBinding;
import com.ecotton.impex.models.EditProfileModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPersonalDetailsActivity extends AppCompatActivity {

    ActivityEditPersonalDetailsBinding binding;

    EditPersonalDetailsActivity mContext;
    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;
    String selected_country_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPersonalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        binding.edtContactPersonNumber.setText(mSessionUtil.getMobileNo());
        binding.edtContactPerson.setText(mSessionUtil.getName());

        binding.edtContactPersonEmail.setText(mSessionUtil.getEmail());
        binding.edtContactPersonEmail.setEnabled(false);

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        binding.layoutMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        binding.ccp1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code = binding.ccp1.getSelectedCountryCodeWithPlus();
            }

        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    EditProfile();
                }
            }
        });
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void EditProfile() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("name", binding.edtContactPerson.getText().toString());
            object.put("mobile_number", binding.edtContactPersonNumber.getText().toString());
            object.put("email", binding.edtContactPersonEmail.getText().toString());
            object.put("country_code", selected_country_code);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<EditProfileModel>> call = APIClient.getInstance().edit_profile(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<EditProfileModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<EditProfileModel>> call, Response<ResponseModel<EditProfileModel>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        onBackPressed();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, response.body().message);
                        // productValueAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<EditProfileModel>> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidForm() {
        if (binding.edtContactPerson.getText().toString().length() < 3) {
            binding.edtContactPerson.setError("Name should contain at least 3 characters");
            binding.edtContactPerson.requestFocus();
            return false;
        }
        if (binding.edtContactPersonNumber.getText().toString().length() < 10) {
            binding.edtContactPersonNumber.setError("Name should contain at least 10 characters");
            binding.edtContactPersonNumber.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtContactPersonEmail.getText().toString()).matches()) {
            binding.edtContactPersonEmail.setError("Please Enter Proper Email Format ");
            binding.edtContactPersonEmail.requestFocus();
            return false;
        }
        if (binding.edtContactPerson.getText().toString().isEmpty()) {
            binding.edtContactPerson.setError("Please enter user name");
            binding.edtContactPerson.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, com.ecotton.impex.activities.MyProfileActivity.class));
        finish();
    }
}