package com.ecotton.impex.activities;

import static com.ecotton.impex.MyApp.filterRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.adapters.WalletPlansAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMywalletPlansBinding;
import com.ecotton.impex.models.AddPlanModel;
import com.ecotton.impex.models.Plan;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MywalletPlansActivity extends AppCompatActivity implements PaymentResultListener {


    private static final String TAG = MywalletPlansActivity.class.getSimpleName();
    ImageView backarrow;
    RecyclerView plans_recycler;

    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    public MywalletPlansActivity mContext;
    WalletPlansAdapter adapter;
    public int planid;

    public ActivityMywalletPlansBinding binding;
    String home;
    String Days;
    String Price;
    String Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMywalletPlansBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        Checkout.preload(getApplicationContext());

        Intent intent = getIntent();

        if (intent != null) {
            home = intent.getStringExtra("home");
        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getPlans();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Days != null && Price != null && Id != null) {
                    startPayment();
                    Toast.makeText(mContext, "Suuuuuu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Please Select Any Plans", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setWalletplanAdapter(List<Plan> planArrayList) {
        adapter = new WalletPlansAdapter(getApplicationContext(), planArrayList);
        binding.plansRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.plansRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new WalletPlansAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Days = String.valueOf(adapter.planList.get(position).getValidity());
                Price = String.valueOf(adapter.planList.get(position).getPrice());
                Id = String.valueOf(adapter.planList.get(position).getId());
                Log.e("Id", "Id==" + Id);
            }
        });
    }

    private void getPlans() {
        try {

            String data = new Gson().toJson(filterRequest);
            Log.e("data", "data==" + data);

            Call<ResponseModel<List<Plan>>> call;

            call = APIClient.getInstance().getPlanList(mSessionUtil.getApiToken(), data);

            call.enqueue(new Callback<ResponseModel<List<Plan>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<Plan>>> call, Response<ResponseModel<List<Plan>>> response) {
                    Log.e("dashboard", "dashboard==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body() != null) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS && response.body().data.size() > 0 && new Gson().toJson(response.body()) != null) {
                            setWalletplanAdapter(response.body().data);
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(mContext, response.body().message);
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(mContext);
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<Plan>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            String userType;
            JSONObject options = new JSONObject();
            if (mSessionUtil.getUsertype().equals("buyer")) {
                userType = "Importer";
            } else {
                userType = "Exporter";
            }
            options.put("name", userType + " " + mSessionUtil.getUserid());
            options.put("description", "Payment of Plan Free with " + Days + " Days..");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://cdn.razorpay.com/logos/IQVt78DBTEPodI_original.png");
            options.put("currency", "USD");
            String samount = Price;
            int amount = Math.round(Float.parseFloat(samount) * 100);
            planid = Integer.parseInt(Id);
            options.put("amount", amount);
            Log.e("getPrice", "getPrice==" + amount);
            JSONObject theme = new JSONObject();
            theme.put("color", "#69BA53");
            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            preFill.put("contact", mSessionUtil.getMobileNo());
            options.put("prefill", preFill);
            // options.put("theme", theme);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            Log.e("getPrice", "getPrice==" + razorpayPaymentID);
            // AddPlans(planid, razorpayPaymentID);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Log.e("failed", "failed==" + code);
            Log.e("response", "response==" + response);

            JSONObject jsonObject = new JSONObject(response);

            JSONObject jsonObject1 = jsonObject.getJSONObject("error");

            JSONObject jsonObject2 = jsonObject1.getJSONObject("metadata");

            String reason = jsonObject1.getString("description");

            String payment_id = jsonObject2.getString("payment_id");

            //AddPlans1(planid, reason, payment_id);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void AddPlans1(int planid, String response, String payment_id) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("plan_id", planid);

            jsonObject.put("transaction_id", payment_id);

            jsonObject.put("payment_status", "faild");

            jsonObject.put("payment_reason", response);

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<AddPlanModel>> call;

            call = APIClient.getInstance().add_user_plan(mSessionUtil.getApiToken(), data);

            call.enqueue(new Callback<ResponseModel<AddPlanModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<AddPlanModel>> call, Response<ResponseModel<AddPlanModel>> response) {
                    Log.e("AddPlanModel", "AddPlanModel==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body() != null) {
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
                }

                @Override
                public void onFailure(Call<ResponseModel<AddPlanModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AddPlans(int planid, String razorpayPaymentID) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("plan_id", planid);

            jsonObject.put("transaction_id", razorpayPaymentID);
            jsonObject.put("payment_status", "success");

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<AddPlanModel>> call;

            call = APIClient.getInstance().add_user_plan(mSessionUtil.getApiToken(), data);

            call.enqueue(new Callback<ResponseModel<AddPlanModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<AddPlanModel>> call, Response<ResponseModel<AddPlanModel>> response) {
                    Log.e("AddPlanModel", "AddPlanModel==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    Log.e("status", "status==" + response.body().status);
                    if (response.body() != null) {
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
                }

                @Override
                public void onFailure(Call<ResponseModel<AddPlanModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (home != null) {
            if (home.equals("home")) {
                startActivity(new Intent(MywalletPlansActivity.this, HomeActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(MywalletPlansActivity.this, MyWalletActivity.class));
            finish();
        }
    }
}