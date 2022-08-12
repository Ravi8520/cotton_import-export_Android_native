package com.ecotton.impex.activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.MyContractAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyContractBinding;
import com.ecotton.impex.models.MyContractModel;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.gne.www.lib.PinView;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tfb.filepicker.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContractActivity extends AppCompatActivity {

    ArrayList<MyContractModel> contractdatalist;
    MyContractAdapter myContractAdapter;

    MyContractActivity context;
    ActivityMyContractBinding binding;
    private SessionUtil mSessionUtil;

    private CustomDialog customDialog;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    public String Usertype = "", company_id = "", user_id = "", deal_id = "";

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyContractBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        contractdatalist = new ArrayList<>();
        context = this;
        mSessionUtil = new SessionUtil(context);
        customDialog = new CustomDialog();
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, com.ecotton.impex.activities.MyContractFilterActivity.class));
            }
        });
        setContractAdapter();
        if (getIntent().getExtras() != null) {
            Bundle extra = getIntent().getExtras();
            for (String key : extra.keySet()) {

                if (key.equals("company_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    company_id = extra.getString(key);
                }
                if (key.equals("user_type")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    Usertype = extra.getString(key);
                }
                if (key.equals("user_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    user_id = extra.getString(key);
                }
            }
        }

        if (!TextUtils.isEmpty(company_id)) {
            SelectUser();
        } else {
            GetData();
        }

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
                            Log.e("response", "response==" + dataa);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        LoginModel model = gson.fromJson(dataa, LoginModel.class);
                        if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(SessionUtil.API_TOKEN, mSessionUtil.getApiToken());
                            map.put(SessionUtil.MOBILE_NO, mSessionUtil.getMobileNo());
                            map.put(SessionUtil.PASS, mSessionUtil.getPass());
                            map.put(SessionUtil.COMPANY_NAME, model.getData().getCompany_name());
                            map.put(SessionUtil.USER_TYPE, model.getData().getUser_type());
                            map.put(SessionUtil.USERID, model.getData().getUserId());
                            map.put(SessionUtil.COMPANY_ID, model.getData().getCompany_id());
                            mSessionUtil.setData(map);
                            GetData();
                        } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(MyContractActivity.this, model.getMessage());
                            startActivity(new Intent(MyContractActivity.this, MyContractActivity.class));
                            finish();
                        } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(MyContractActivity.this, model.getMessage());
                            AppUtil.autoLogout(MyContractActivity.this);
                        } else {
                            AppUtil.showToast(MyContractActivity.this, model.getMessage());
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

    private void setContractAdapter() {

        myContractAdapter = new MyContractAdapter(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.contractRecyclerview.setLayoutManager(layoutManager);
        binding.contractRecyclerview.setAdapter(myContractAdapter);

        myContractAdapter.setOnItemClickListener(new MyContractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int childPosition) {
                if (view.getId() == R.id.btn_download) {
                    if (myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getIs_buyer_otp_verify() == 0 || myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getIs_seller_otp_verify() == 0) {
                        Log.e("btn_download111", "btn_download111===");
                        ResendOtp(position, childPosition);
                    } else if (myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getIs_buyer_otp_verify() == 1 && myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getIs_seller_otp_verify() == 1 && myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getIs_broker_otp_verify() == 1) {
                        Log.e("Click", "Click==");
                        if (checkAndRequestPermissions()) {
                            String filepath = myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getUrl();
                            URL url = null;
                            String fileName;
                            try {
                                url = new URL(filepath);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            fileName = url.getPath();
                            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
                            request.setTitle(fileName);
                            request.setMimeType("applcation/pdf");
                            request.allowScanningByMediaScanner();
                            request.setAllowedOverMetered(true);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);
                        }
                    } else if (!checkAndRequestPermissions()) {
                        requestStoragePermission();

                    }
                }
            }
        });

    }


    private void hitMackDealVerify(int position, int childPosition, String mobileotp) {
        try {

            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("deal_id", myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getDeal_id());
            object.put("email_otp", myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getEmail_otp());
            object.put("mobile_otp", mobileotp);
            object.put("company_id", mSessionUtil.getCompanyId());

            strJson = object.toString();
            PrintLog.e("TAG", strJson);

            Call<ResponseModel> call = APIClient.getInstance().makeDealOtpVerify(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Log.e("response", "hitMackDeal==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(context);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        dialog.dismiss();
                        AppUtil.showToast(context, response.body().message);
                        GetData();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(context, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(context, response.body().message);
                        AppUtil.autoLogout(context);
                    } else {
                        AppUtil.showToast(context, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("response", "response==" + t.getMessage());
                    customDialog.dismissProgress(context);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResendOtp(int position, int childPosition) {
        try {
            customDialog.displayProgress(context);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("deal_id", myContractAdapter.mArrayList.get(position).getDeal_details().get(childPosition).getDeal_id());
            strJson = object.toString();
            PrintLog.e("TAG", strJson);

            Call<ResponseModel> call = APIClient.getInstance().resend_deal_otp(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Log.e("ResendOtp", "ResendOtp==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(context);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(context, response.body().message);
                        dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.deal_otp_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        Button btn_verify = (Button) dialog.findViewById(R.id.btn_verify);
                        Button btn_resend = (Button) dialog.findViewById(R.id.btn_resend);
                        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
                        PinView pinView = (PinView) dialog.findViewById(R.id.pinview);
                        TextView txt_mobile_no = (TextView) dialog.findViewById(R.id.txt_mobile_no);
                        String number = "<b>" + mSessionUtil.getMobileNo() + "</b> ";
                        txt_mobile_no.setText(Html.fromHtml("We have sent verification \ncode on your number  " + number));
                        btn_resend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                ResendOtp(position, childPosition);
                            }
                        });
                        btn_verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pin = pinView.getText().toString();
                                if (pin.length() != 6) {
                                    Toast.makeText(context, "Please Enter 6 digit Pin", Toast.LENGTH_LONG).show();
                                } else {
                                    hitMackDealVerify(position, childPosition, pin);
                                }
                            }
                        });
                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(context, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(context, response.body().message);
                        AppUtil.autoLogout(context);
                    } else {
                        AppUtil.showToast(context, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("response", "response==" + t.getMessage());
                    customDialog.dismissProgress(context);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void GetData() {
        try {
            customDialog.displayProgress(context);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<List<MyContractModel>>> call = APIClient.getInstance().my_contract(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<MyContractModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<MyContractModel>>> call, Response<ResponseModel<List<MyContractModel>>> response) {
                    customDialog.dismissProgress(context);
                    Log.e("MyContractModel", "MyContractModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        myContractAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        myContractAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(context, response.body().message);
                        AppUtil.autoLogout(context);
                    } else {
                        AppUtil.showToast(context, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<MyContractModel>>> call, Throwable t) {
                    customDialog.dismissProgress(context);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }

    private boolean isPermissionGranted(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
        } else {
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
        }
    }

}