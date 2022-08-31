package com.ecotton.impex.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.databinding.ActivityLoginasBinding;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Constants;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAsActivity extends AppCompatActivity {

    String[] user = {"Select", "Importer", "Exporter"};

    private Context mContext;

    String Usertype;
    public static String COMPANY_ID = "Company_id";
    private int company_id;
    private SessionUtil mSessionUtil;
    public ActivityLoginasBinding binding;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);

        Intent intent = getIntent();
        if (intent != null) {
            company_id = intent.getIntExtra(COMPANY_ID, 0);
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, user);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinnerUser.setAdapter(aa);

        binding.spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                pos = position;
                if (position > 0) {
                    Usertype = binding.spinnerUser.getSelectedItem().toString();
                    Log.e("Usertype", "Usertype==" + Usertype);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                }
                if (position > 0) {
                    if (binding.spinnerUser.getSelectedItem().toString().equals("Importer"))
                        Usertype = Constants.IMPORTER;
                    if (binding.spinnerUser.getSelectedItem().toString().equals("Exporter"))
                        Usertype = Constants.EXPORTER;
                    Log.e("Usertype", "Usertype==" + Usertype);
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                }
                if (position == 0) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                }
                // Showing selected spinner item
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        binding.btnNext.setOnClickListener(v -> {
            btn_nextOnClick();
        });
        binding.backarrow.setOnClickListener(v -> {
            img_backOnClick();
        });

    }

    protected void btn_nextOnClick() {
        if (pos > 0) {
            SelectUser();
        } else if (pos == 0)
            Toast.makeText(mContext, "Please Select User Type", Toast.LENGTH_SHORT);

    }

    private void SelectUser() {
        try {
            JSONObject object = new JSONObject();
            object.put("company_id", company_id);
            object.put("login_as", Usertype);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().Select_company(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String dataa = null;
                        try {
                            dataa = new String(response.body().bytes());
                            Log.e("login_as", "login_as==" + dataa);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        LoginModel model = gson.fromJson(dataa, LoginModel.class);
                        if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                            HashMap<String, String> map = new HashMap<>();

                            if (model.getData().getIs_user_plan() == 1) {
                                map.put(SessionUtil.API_TOKEN, mSessionUtil.getApiToken());
                                map.put(SessionUtil.EMAIL, mSessionUtil.getEmail());
                                map.put(SessionUtil.PASS, mSessionUtil.getPass());
                                map.put(SessionUtil.COMPANY_NAME, model.getData().getCompany_name());
                                map.put(SessionUtil.USER_TYPE, model.getData().getUser_type());
                                map.put(SessionUtil.USERID, model.getData().getUserId());
                                map.put(SessionUtil.COMPANY_ID, model.getData().getCompany_id());
                                mSessionUtil.setData(map);

                                Intent intent = new Intent(mContext, HomeActivity.class);
                                intent.putExtra(HomeActivity.COMPANY_Name, model.getData().getCompany_name());
                                intent.putExtra(HomeActivity.USER_Type, model.getData().getUser_type());
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(mContext, MywalletPlansActivity.class);
                                intent.putExtra("home", "home");
                                startActivity(intent);
                                finish();
                            }
                        } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(mContext, model.getMessage());
                            startActivity(new Intent(mContext, WitingApprovelActivity.class));
                            finish();
                        } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, model.getMessage());
                            AppUtil.autoLogout(LoginAsActivity.this);
                        } else {
                            AppUtil.showToast(mContext, model.getMessage());
                        }
                    } catch (Exception e) {
                        e.getMessage();
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

    protected void img_backOnClick() {
        startActivity(new Intent(mContext, CompanyListActivity.class));
        finish();
    }
}