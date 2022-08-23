package com.ecotton.impex.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.ContractDetailAdapter;
import com.ecotton.impex.adapters.DebitNoteImageAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyContractDetailsBinding;
import com.ecotton.impex.models.AttributeModel;
import com.ecotton.impex.models.ContractDetailModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContractDetailsActivity extends AppCompatActivity {

    ActivityMyContractDetailsBinding binding;

    MyContractDetailsActivity mcontext;
    private CustomDialog customDialog;
    private int deal_id;
    private String attribute;
    private SessionUtil mSessionUtil;
    ContractDetailAdapter myContractAdapter;

    public ContractDetailModel contractDetailModel = new ContractDetailModel();


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ArrayList<AttributeModel> stringArray = new ArrayList<>();

    DebitNoteImageAdapter debitNoteImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyContractDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mcontext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mcontext);

        Intent intent = getIntent();
        if (intent != null) {
            deal_id = intent.getIntExtra("deal_id", 0);
            attribute = intent.getStringExtra("attribute");
        }
        try {

            JSONArray jsonArray = new JSONArray(attribute);
            for (int i = 0, count = jsonArray.length(); i < count; i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String attribute = jsonObject.getString("attribute");
                    String attribute_value = jsonObject.getString("attribute_value");
                    AttributeModel attributeModel = new AttributeModel(attribute, attribute_value);
                    stringArray.add(attributeModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }

        DividerItemDecoration divider =
                new DividerItemDecoration(mcontext,
                        DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(mcontext.getResources().getDrawable(R.drawable.line_divider));

        ContractDetailAdapter contractDetailAdapter = new ContractDetailAdapter(mcontext, stringArray);
        binding.recyclerviewAttribute.setLayoutManager(new GridLayoutManager(mcontext, 3));
        binding.recyclerviewAttribute.addItemDecoration(divider);
        binding.recyclerviewAttribute.addItemDecoration(new DividerItemDecoration(mcontext, DividerItemDecoration.HORIZONTAL));
        binding.recyclerviewAttribute.setAdapter(contractDetailAdapter);


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.layoutDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    PdfDownload();
                } else {
                    requestStoragePermission();
                }
            }
        });
        GetDealData();
    }


    private void GetDealData() {
        try {
            customDialog.displayProgress(mcontext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("deal_id", deal_id);

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<ContractDetailModel>> call = APIClient.getInstance().contract_details(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<ContractDetailModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ContractDetailModel>> call, Response<ResponseModel<ContractDetailModel>> response) {
                    customDialog.dismissProgress(mcontext);
                    Log.e("ContractDetailModel", "ContractDetailModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        contractDetailModel = response.body().data;
                        setData();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mcontext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mcontext, response.body().message);
                        AppUtil.autoLogout(mcontext);
                    } else {
                        AppUtil.showToast(mcontext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<ContractDetailModel>> call, Throwable t) {
                    customDialog.dismissProgress(mcontext);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setData() {
        binding.txtProductName.setText(contractDetailModel.getProduct_name());
        binding.txtPostBales.setText(contractDetailModel.getPost_bales());
        binding.txtSellBales.setText(contractDetailModel.getSell_bales());
        binding.txtPostPrice.setText(getResources().getString(R.string.lbl_rupees_symbol_only) + " " + contractDetailModel.getPost_price());
        binding.txtSellPrice.setText(getResources().getString(R.string.lbl_rupees_symbol_only) + " " + contractDetailModel.getSell_price());

        binding.txtCompanyName.setText(contractDetailModel.getPosted_company_name());
        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtSellerName.setText(contractDetailModel.getSeller_name());
            binding.txtLabel.setText("Exporter Name");
        } else {
            binding.txtSellerName.setText(contractDetailModel.getBuyer_name());
            binding.txtLabel.setText("Importer Name");
        }
        binding.txtCountryOfOrigin.setText(contractDetailModel.getCountry_origin_name());
        binding.txtInspection.setText(contractDetailModel.getLab());
        binding.txtDeliveryPeriod.setText(contractDetailModel.getDelivery_period());
        binding.txtCountryOfDispatch.setText(contractDetailModel.getCountry_dispatch_name());
        binding.txtPaymentCondition.setText(contractDetailModel.getPayment_condition());
        if (contractDetailModel.getPort_dispatch_name() != null) {
            binding.txtPortOfDispatch.setText(contractDetailModel.getPort_dispatch_name());
        }
        binding.txtDeliveryCondition.setText(contractDetailModel.getDelivery_condition_name());

    }

    public void PdfDownload() {
        String filepath = contractDetailModel.getUrl();
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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            ActivityCompat.requestPermissions(mcontext, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
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
}