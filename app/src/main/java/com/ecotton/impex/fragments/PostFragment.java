package com.ecotton.impex.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentPostBinding;
import com.ecotton.impex.materialspinner.MaterialSpinner;
import com.ecotton.impex.models.BrokerReportModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostFragment extends Fragment {

    FragmentPostBinding binding;
    private Context mContext;
    private SessionUtil mSessionUtil;

    private CustomDialog customDialog;

    String[] status = {"active", "complete", "cancel"};
    String status_string;
    String date;
    String PDFPath;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPostBinding.inflate(getLayoutInflater());


        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.Widget_AppTheme_MaterialDatePicker).
                setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();


        binding.pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        binding.pickDateButton.setText(materialDatePicker.getHeaderText());
                        binding.activeDate.setText(materialDatePicker.getHeaderText());

                        Pair selectedDates = (Pair) materialDatePicker.getSelection();
                        final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                        Date startDate = rangeDate.first;
                        Date endDate = rangeDate.second;
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                        binding.pickDateButton.setText(simpleFormat.format(startDate) + "/" + simpleFormat.format(endDate));
                        binding.activeDate.setText(simpleFormat.format(startDate) + "/" + simpleFormat.format(endDate));

                        date = simpleFormat.format(startDate) + "#" + simpleFormat.format(endDate);

                        if (date != null) {
                            GetContractData(date);
                        } else {
                            Toast.makeText(mContext, "Plase Select Date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });

        binding.spinnerStatus.setItems(status);
        binding.spinnerStatus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                status_string = item;
                // GetAttribute(productModelList.get(i).getId());

            }
        });
        binding.spinnerStatus.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });
        status_string = "active";
       /* ArrayAdapter adapter1 = new ArrayAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, status);
        binding.spinnerStatus.setAdapter(adapter1);
        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status_string = parent.getItemAtPosition(position).toString();
                Log.e("status_string", "status_string==" + status_string);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        binding.layoutDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    if (PDFPath != null) {
                        String filepath = PDFPath;
                        Log.e("filepath", "filepath===" + filepath);
                        URL url = null;
                        String fileName;
                        try {
                            url = new URL(filepath);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        if (url.getPath() != null) {
                            Toast.makeText(mContext, "Download Started...", Toast.LENGTH_SHORT).show();
                            fileName = url.getPath();
                            Log.e("filepath", "filepath===" + url);
                            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url + ""));
                            request.setTitle(fileName);
                            request.setMimeType("applcation/pdf");
                            request.allowScanningByMediaScanner();
                            request.setAllowedOverMetered(true);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                            DownloadManager dm = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);
                        }
                    }
                } else if (!checkAndRequestPermissions()) {
                    requestStoragePermission();

                }
            }
        });

        return binding.getRoot();
    }

    private void GetContractData(String datee) {
        try {
            PDFPath = "";
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("status", status_string);
            object.put("date_range", datee);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<BrokerReportModel>> call = APIClient.getInstance().post_report(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<BrokerReportModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<BrokerReportModel>> call, Response<ResponseModel<BrokerReportModel>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("PDFPath", "PDFPath==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        PDFPath = response.body().data.getReport_url();
                        if (PDFPath != null) {
                            binding.layoutDownload.setVisibility(View.VISIBLE);
                        }
                        Log.e("PDFPath", "PDFPath==" + PDFPath);
                        AppUtil.showToast(mContext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<BrokerReportModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void requestStoragePermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(mContext, "All permissions are granted!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}