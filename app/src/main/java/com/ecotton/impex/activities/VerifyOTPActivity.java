package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gne.www.lib.PinView;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityVerifyOtpactivityBinding;
import com.ecotton.impex.models.InvitedDataModel;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Constants;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity {

    public static String EMAIL_ADDRESS = "email";
    public static String NEWMOBILE_NO = "newmobile_no";
    public static String ISINVITED = "is_invited";
    private String email = "";
    private int is_invited;

    private VerifyOTPActivity mContext;

    PinView pinView;

    String pin;

    ActivityVerifyOtpactivityBinding binding;
    private SessionUtil mSessionUtil;
    CustomDialog customDialog;

    private List<List<InvitedDataModel>> invitedDataModelList = new ArrayList<List<InvitedDataModel>>();
    String isFrom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        customDialog = new CustomDialog();
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pinView = findViewById(R.id.pinview);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        isFrom = getIntent().getStringExtra("isFrom");
        is_invited = bundle.getInt(ISINVITED, 0);
        email = bundle.getString(EMAIL_ADDRESS, "");
        isFrom = bundle.getString("isFrom", "");
        binding.txMobileNumber.setText(Html.fromHtml("Please enter the 6 digit code sent to your mail <b>" + email + "</b> "));
        Log.e("TAG", "EMAIL" + email);
        binding.btnVerify.setOnClickListener(view -> {
            btn_verifyOnClick();
        });
        binding.txtResendOtp.setOnClickListener(view -> {
            txt_resend_otpClick();
        });
        binding.backarrow.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.imgEdit.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void InvitedData() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile_number", email);
            String data = jsonObject.toString();
            Call<ResponseModel<List<InvitedDataModel>>> call = APIClient.getInstance().get_invited_by_company_details(Constants.AUTH, data);
            call.enqueue(new Callback<ResponseModel<List<InvitedDataModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<InvitedDataModel>>> call, Response<ResponseModel<List<InvitedDataModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("InvitedDataModel", "InvitedDataModel==" + new Gson().toJson(response.body().data));
                        invitedDataModelList.add(response.body().data);
                        for (int i = 0; i < invitedDataModelList.size(); i++) {
                            Intent intent = new Intent(mContext, IsInvitedCreateAccountActivity.class);
                            intent.putExtra(IsInvitedCreateAccountActivity.NAME, invitedDataModelList.get(i).get(i).getName());
                            intent.putExtra(IsInvitedCreateAccountActivity.MOBILE_NO, invitedDataModelList.get(i).get(i).getMobile_number());
                            intent.putExtra(IsInvitedCreateAccountActivity.COMPANY_NAME, invitedDataModelList.get(i).get(i).getCompany_name());
                            intent.putExtra(IsInvitedCreateAccountActivity.COMPANY_GST_NO, invitedDataModelList.get(i).get(i).getCompany_gst_no());
                            intent.putExtra(IsInvitedCreateAccountActivity.COMPANY_TYPE, invitedDataModelList.get(i).get(i).getCompany_type());
                            intent.putExtra(IsInvitedCreateAccountActivity.COMPANY_BROKER_CODE, invitedDataModelList.get(i).get(i).getCompany_broker_code());
                            intent.putExtra(IsInvitedCreateAccountActivity.COMPANY_ID, invitedDataModelList.get(i).get(i).getCompany_id());
                            startActivity(intent);
                            finish();
                        }
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        //stateAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<InvitedDataModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void btn_verifyOnClick() {
        pin = pinView.getText().toString();
        if (pin.length() != 6) {
            Toast.makeText(mContext, "Please Enter 6 digit Pin", Toast.LENGTH_LONG).show();
        } else {
            if (isFrom.equals("create")) {
                VerifyOTP();
            } else {
                VerifyOTP1();
                for (int i = 0; i < pinView.getChildCount(); i++) {
                    EditText child = (EditText) pinView.getChildAt(i);
                    child.setText("");
                    child.setCursorVisible(true);
                    child.setFocusableInTouchMode(true);
                    child.requestFocus();
                    child.setSelection(0);
                }
            }
        }
    }

    protected void txt_resend_otpClick() {
        ResendOTP();
    }

    private void VerifyOTP() {
        try {
            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("otp", pin);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().VerifyOTP(Constants.AUTH, data);
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
                        HashMap<String, String> map = new HashMap<>();
                        map.put(SessionUtil.MOBILE_NO, model.getData().getMobileNumber());
                        map.put(SessionUtil.API_TOKEN, model.getData().getApiToken());
                        mSessionUtil.setData(map);
                        /*if (is_invited == 1) {
                            Toast.makeText(mContext, ISINVITED, Toast.LENGTH_LONG).show();
                            InvitedData();
                        } else {
                        }*/
                        Intent intent = new Intent(mContext, CreateAccountActivity.class);
                        intent.putExtra(CreateAccountActivity.EMAIL_ADDRESS, email);
                        startActivity(intent);
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(VerifyOTPActivity.this);
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

    private void VerifyOTP1() {
        try {
            JSONObject object = new JSONObject();
            object.put("email", email);
            object.put("otp", pin);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().VerifyOTP(Constants.AUTH, data);
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
                        HashMap<String, String> map = new HashMap<>();
                        map.put(SessionUtil.API_TOKEN, model.getData().getApiToken());
                        map.put(SessionUtil.EMAIL, model.getData().getEmail());
                        mSessionUtil.setData(map);
                        Intent intent = new Intent(mContext, ResetPasswordActivity.class);
                        intent.putExtra(ResetPasswordActivity.MobileNo, email);
                        startActivity(intent);
                        finish();
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(VerifyOTPActivity.this);
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

    private void ResendOTP() {
        try {
            JSONObject object = new JSONObject();
            object.put("email", email);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().Resend_otp(Constants.AUTH, data);
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
                    if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, model.getMessage());
                    } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, model.getMessage());
                        AppUtil.autoLogout(VerifyOTPActivity.this);
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




    protected void img_editOnClick() {
        Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
        Log.e("number0", "number==" + email);
        intent.putExtra(NEWMOBILE_NO, email);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}