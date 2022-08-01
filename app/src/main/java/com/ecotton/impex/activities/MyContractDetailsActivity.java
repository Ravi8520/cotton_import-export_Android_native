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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.ContractDetailAdapter;
import com.ecotton.impex.adapters.DebitNoteImageAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMyContractDetailsBinding;
import com.ecotton.impex.models.AttributeModel;
import com.ecotton.impex.models.ContractDetailModel;
import com.ecotton.impex.models.DocumentUploadModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Constants;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;
import com.tfb.filepicker.FilePickerController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public ContractDetailModel contractDetailModel = new ContractDetailModel();

    private FilePickerController mFilePickerController;
    private Uri lab_reporturl = null;
    private Uri transmit_dealurl = null;
    private Uri without_gsturl = null;
    private Uri gst_reciepturl = null;
    private Uri debit_noteurl = null;

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
        mFilePickerController = new FilePickerController(mcontext);
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

        binding.btnUploadLabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mFilePickerController.setAllowTakePhoto(true);
                    mFilePickerController.setAllowFileManager(true);
                    mFilePickerController.setAllowCroppingImage(true);
                    mFilePickerController.setAllowMultipleSelection(false);
                    mFilePickerController.setAllowFixAspactRatio(true);
                    mFilePickerController.setAllowFrontFacingCamera(false);
                    mFilePickerController.setAllowMimeTypes(Constants.IMAGE_MIME_TYPES);
                    mFilePickerController.setFilePickerListener(new FilePickerController.FilePickerListener() {
                        @Override
                        public void onSuccess(ArrayList<Uri> mSelectedFileList) {
                            binding.imgUploadLab.setVisibility(View.VISIBLE);
                            binding.btnUploadLabReport.setVisibility(View.GONE);

                            if (mSelectedFileList != null && mSelectedFileList.size() > 0) {
                                lab_reporturl = mSelectedFileList.get(0);
                                binding.layoutLab.setVisibility(View.VISIBLE);
                                Glide.with(mcontext)
                                        .load(lab_reporturl)
                                        .apply(AppUtil.getDefaultRequestOptions1())
                                        .into(binding.imgUploadLab);
                                UploadDocument();
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateLabReportStatus("pass");
            }
        });
        binding.btnFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateLabReportStatus("fail");
            }
        });
        binding.btnFailWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateLabReportStatus("fail_with_renegotiation");
            }
        });

        binding.btnUploadTransmitDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mFilePickerController.setAllowTakePhoto(true);
                    mFilePickerController.setAllowFileManager(true);
                    mFilePickerController.setAllowCroppingImage(true);
                    mFilePickerController.setAllowMultipleSelection(false);
                    mFilePickerController.setAllowFixAspactRatio(true);
                    mFilePickerController.setAllowFrontFacingCamera(false);
                    mFilePickerController.setAllowMimeTypes(Constants.IMAGE_MIME_TYPES);
                    mFilePickerController.setFilePickerListener(new FilePickerController.FilePickerListener() {
                        @Override
                        public void onSuccess(ArrayList<Uri> mSelectedFileList) {

                            if (mSelectedFileList != null && mSelectedFileList.size() > 0) {
                                transmit_dealurl = mSelectedFileList.get(0);
                                binding.imgTransmit.setVisibility(View.VISIBLE);
                                Glide.with(mcontext)
                                        .load(transmit_dealurl)
                                        .apply(AppUtil.getDefaultRequestOptions1())
                                        .into(binding.imgTransmit);

                                UploadDocument();
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.btnUploadWithoutGst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mFilePickerController.setAllowTakePhoto(true);
                    mFilePickerController.setAllowFileManager(true);
                    mFilePickerController.setAllowCroppingImage(true);
                    mFilePickerController.setAllowMultipleSelection(false);
                    mFilePickerController.setAllowFixAspactRatio(true);
                    mFilePickerController.setAllowFrontFacingCamera(false);
                    mFilePickerController.setAllowMimeTypes(Constants.IMAGE_MIME_TYPES);
                    mFilePickerController.setFilePickerListener(new FilePickerController.FilePickerListener() {
                        @Override
                        public void onSuccess(ArrayList<Uri> mSelectedFileList) {

                            if (mSelectedFileList != null && mSelectedFileList.size() > 0) {
                                without_gsturl = mSelectedFileList.get(0);
                                binding.imgGst.setVisibility(View.VISIBLE);
                                Glide.with(mcontext)
                                        .load(without_gsturl)
                                        .apply(AppUtil.getDefaultRequestOptions1())
                                        .into(binding.imgGst);

                                UploadDocument();
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        binding.btnUploadPayGst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    debit_noteurl = Uri.parse("");
                    mFilePickerController.setAllowTakePhoto(true);
                    mFilePickerController.setAllowFileManager(true);
                    mFilePickerController.setAllowCroppingImage(true);
                    mFilePickerController.setAllowMultipleSelection(false);
                    mFilePickerController.setAllowFixAspactRatio(true);
                    mFilePickerController.setAllowFrontFacingCamera(false);
                    mFilePickerController.setAllowMimeTypes(Constants.IMAGE_MIME_TYPES);
                    mFilePickerController.setFilePickerListener(new FilePickerController.FilePickerListener() {
                        @Override
                        public void onSuccess(ArrayList<Uri> mSelectedFileList) {

                            if (mSelectedFileList != null && mSelectedFileList.size() > 0) {
                                gst_reciepturl = mSelectedFileList.get(0);
                                binding.imgPayGst.setVisibility(View.VISIBLE);
                                Glide.with(mcontext)
                                        .load(gst_reciepturl)
                                        .apply(AppUtil.getDefaultRequestOptions1())
                                        .into(binding.imgPayGst);

                                UploadDocument();
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnUploadDebitNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    debit_noteurl = Uri.parse("");
                    mFilePickerController.setAllowTakePhoto(true);
                    mFilePickerController.setAllowFileManager(true);
                    mFilePickerController.setAllowCroppingImage(true);
                    mFilePickerController.setAllowMultipleSelection(false);
                    mFilePickerController.setAllowFixAspactRatio(true);
                    mFilePickerController.setAllowFrontFacingCamera(false);
                    mFilePickerController.setAllowMimeTypes(Constants.IMAGE_MIME_TYPES);
                    mFilePickerController.setFilePickerListener(new FilePickerController.FilePickerListener() {
                        @Override
                        public void onSuccess(ArrayList<Uri> mSelectedFileList) {
                            if (mSelectedFileList != null && mSelectedFileList.size() > 0) {
                                debit_noteurl = mSelectedFileList.get(0);
                                binding.imgDebitNote.setVisibility(View.VISIBLE);
                                debitNoteImageAdapter.notifyDataSetChanged();
                                UploadDocument();
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnUploadDebitNoteMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    debit_noteurl = Uri.parse("");
                    mFilePickerController.setAllowTakePhoto(true);
                    mFilePickerController.setAllowFileManager(true);
                    mFilePickerController.setAllowCroppingImage(true);
                    mFilePickerController.setAllowMultipleSelection(false);
                    mFilePickerController.setAllowFixAspactRatio(true);
                    mFilePickerController.setAllowFrontFacingCamera(false);
                    mFilePickerController.setAllowMimeTypes(Constants.IMAGE_MIME_TYPES);
                    mFilePickerController.setFilePickerListener(new FilePickerController.FilePickerListener() {
                        @Override
                        public void onSuccess(ArrayList<Uri> mSelectedFileList) {

                            if (mSelectedFileList != null && mSelectedFileList.size() > 0) {
                                debit_noteurl = mSelectedFileList.get(0);
                                   /* Glide.with(mcontext)
                                            .load(debit_noteurl)
                                            .apply(AppUtil.getDefaultRequestOptions1())
                                            .into(binding.imgDebitNote);*/
                                UploadDocument();
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void UpdateLabReportStatus(String labReportStatus) {
        try {

            String dealid = Integer.toString(deal_id);
            customDialog.displayProgress(mcontext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("deal_id", dealid);
            jsonObject.put("lab_report_status", labReportStatus);

            String data = jsonObject.toString();

            Log.e("TAG", "data-" + data);

            Call<ResponseModel> call = APIClient.getInstance().updateLabReportStatus(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    customDialog.dismissProgress(mcontext);
                    Log.e("DocumentUploadModel", "DocumentUploadModel==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mcontext, response.body().message);
                        GetDealData();
                        if (labReportStatus.equals("pass")) {

                        } else if (labReportStatus.equals("fail")) {

                        } else if (labReportStatus.equals("fail_with_renegotiation")) {
                            startActivity(new Intent(mcontext, PostDetailActivity.class)
                                    .putExtra("screen", "negotiation")
                                    .putExtra("post_id", contractDetailModel.getPost_notification_id())
                                    .putExtra("seller_id", contractDetailModel.getSeller_id() + "")
                                    .putExtra("buyer_id", contractDetailModel.getBuyer_id() + "")
                                    .putExtra("posted_company_id", contractDetailModel.getPosted_company_id() + "")
                                    .putExtra("negotiation_by_company_id", contractDetailModel.getNegotiation_by_company_id() + "")
                                    .putExtra("post_type", contractDetailModel.getNegotiation_type())
                                    .putExtra("is_from_renegotiation", true)
                                    .putExtra("deal_id", dealid));
                        }

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
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    customDialog.dismissProgress(mcontext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UploadDocument() {
        try {

            String dealid = Integer.toString(deal_id);
            customDialog.displayProgress(mcontext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("upload_by", mSessionUtil.getUsertype());
            jsonObject.put("deal_id", dealid);
            jsonObject.put("sample", false);

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            MultipartBody.Part lab_report = null;
            if (lab_reporturl != null && ValidationUtil.validateString(lab_reporturl.getPath())) {
                File photoFile = AppUtil.getFile(mcontext, lab_reporturl);
                RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(AppUtil.getFileMimeTypeFromUri(mcontext, lab_reporturl)), photoFile);
                lab_report = MultipartBody.Part.createFormData("lab_report", photoFile.getName(), requestPhotoFile);
            }

            MultipartBody.Part transmit_deal = null;

            if (transmit_dealurl != null && ValidationUtil.validateString(transmit_dealurl.getPath())) {
                File photoFile = AppUtil.getFile(mcontext, transmit_dealurl);
                RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(AppUtil.getFileMimeTypeFromUri(mcontext, transmit_dealurl)), photoFile);
                transmit_deal = MultipartBody.Part.createFormData("transmit_deal", photoFile.getName(), requestPhotoFile);
            }

            MultipartBody.Part without_gst = null;

            if (without_gsturl != null && ValidationUtil.validateString(without_gsturl.getPath())) {
                File photoFile = AppUtil.getFile(mcontext, without_gsturl);
                RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(AppUtil.getFileMimeTypeFromUri(mcontext, without_gsturl)), photoFile);
                without_gst = MultipartBody.Part.createFormData("without_gst", photoFile.getName(), requestPhotoFile);
            }


            MultipartBody.Part debit_note = null;

            if (debit_noteurl != null && ValidationUtil.validateString(debit_noteurl.getPath())) {
                File photoFile = AppUtil.getFile(mcontext, debit_noteurl);
                RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(AppUtil.getFileMimeTypeFromUri(mcontext, debit_noteurl)), photoFile);
                debit_note = MultipartBody.Part.createFormData("debit_note", photoFile.getName(), requestPhotoFile);
            }

            MultipartBody.Part gst_reciept = null;

            if (gst_reciepturl != null && ValidationUtil.validateString(gst_reciepturl.getPath())) {
                debit_note = null;
                File photoFile = AppUtil.getFile(mcontext, gst_reciepturl);
                RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(AppUtil.getFileMimeTypeFromUri(mcontext, gst_reciepturl)), photoFile);
                gst_reciept = MultipartBody.Part.createFormData("gst_reciept", photoFile.getName(), requestPhotoFile);
            }

            Call<ResponseModel<DocumentUploadModel>> call = APIClient.getInstance().update_transaction_tracking(mSessionUtil.getApiToken(), AppUtil.createPartFromString(data), lab_report, transmit_deal, without_gst, debit_note, gst_reciept);
            call.enqueue(new Callback<ResponseModel<DocumentUploadModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<DocumentUploadModel>> call, Response<ResponseModel<DocumentUploadModel>> response) {
                    customDialog.dismissProgress(mcontext);
                    Log.e("DocumentUploadModel", "DocumentUploadModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mcontext, response.body().message);
                        GetDealData();
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
                public void onFailure(Call<ResponseModel<DocumentUploadModel>> call, Throwable t) {
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
        if (mSessionUtil.getUsertype().equals("seller")) {
            binding.txtCompanyName.setText(contractDetailModel.getNegotiation_by_company_name());
        } else if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtCompanyName.setText(contractDetailModel.getPosted_company_name());
        }
        binding.txtBrokerName.setText(contractDetailModel.getBroker_name());
        binding.txtSellerName.setText(contractDetailModel.getSeller_name());
        binding.txtPaymentCondition.setText(contractDetailModel.getPayment_condition());
        binding.txtLab.setText(contractDetailModel.getLab());
        binding.txtTransmitCondition.setText(contractDetailModel.getTransmit_condition());

        if (!contractDetailModel.getLab_report().equals("null")) {
            Glide.with(mcontext).load(contractDetailModel.getLab_report()).into(binding.imgUploadLab);
            binding.imgUploadLab.setVisibility(View.VISIBLE);
            binding.layoutLab.setVisibility(View.GONE);
            binding.confirm2.setVisibility(View.GONE);
            binding.mViewOvalLabReport.setBackgroundResource(R.drawable.shape_oval_color_primary);
            binding.mViewLineLabReport.setBackgroundResource(R.drawable.shape_colorprimary_line);
            binding.btnUploadLabReport.setVisibility(View.GONE);
        }
        if (!contractDetailModel.getTransmit_deal().equals("null")) {
            Log.e("Done", "Done===");
            Glide.with(mcontext).load(contractDetailModel.getTransmit_deal()).into(binding.imgTransmit);
            binding.imgTransmit.setVisibility(View.VISIBLE);
            binding.confirm3.setVisibility(View.GONE);
            binding.mViewOvalTransmitdeal.setBackgroundResource(R.drawable.shape_oval_color_primary);
            binding.mViewLineTransmitdeal.setBackgroundResource(R.drawable.shape_colorprimary_line);
            binding.btnUploadTransmitDeal.setVisibility(View.GONE);
        }
        if (!contractDetailModel.getWithout_gst().equals("null")) {
            Log.e("Done111", "Done11===");
            binding.btnUploadWithoutGst.setVisibility(View.GONE);
            binding.imgGst.setVisibility(View.VISIBLE);
            binding.confirm4.setVisibility(View.GONE);
            Glide.with(mcontext).load(contractDetailModel.getWithout_gst()).into(binding.imgGst);
            binding.mViewOvalWithoutGST.setBackgroundResource(R.drawable.shape_oval_color_primary);
            binding.mViewLineWithoutGST.setBackgroundResource(R.drawable.shape_colorprimary_line);
        }
        if (!contractDetailModel.getGst_reciept().equals("null")) {
            binding.imgPayGst.setVisibility(View.VISIBLE);
            Glide.with(mcontext).load(contractDetailModel.getGst_reciept()).into(binding.imgPayGst);
            binding.imgPayGst.setVisibility(View.VISIBLE);
            binding.btnUploadPayGst.setVisibility(View.GONE);
            binding.confirm6.setVisibility(View.GONE);
            binding.mViewOvalGSTRecipt.setBackgroundResource(R.drawable.shape_oval_color_primary);
            binding.mViewLineGSTRecipt.setBackgroundResource(R.drawable.shape_colorprimary_line);
        }
        if (contractDetailModel.getLab_report().equals("null")) {
            binding.imgUploadLab.setVisibility(View.GONE);
            binding.btnUploadTransmitDeal.setEnabled(false);
            if (mSessionUtil.getUsertype().equals("seller")) {
                binding.btnUploadLabReport.setVisibility(View.GONE);
                binding.confirm2.setVisibility(View.VISIBLE);
            } else if (mSessionUtil.getUsertype().equals("buyer")) {
                binding.btnUploadLabReport.setVisibility(View.VISIBLE);
            }
        }
        if (contractDetailModel.getLab_report_status().equals("null") && !contractDetailModel.getLab_report().equals("null")) {
            binding.layoutLab.setVisibility(View.VISIBLE);
            if (contractDetailModel.getLab_report_status().equals("pass")) {
                binding.mViewOvalLabReport.setBackgroundResource(R.drawable.shape_oval_color_primary);
                binding.mViewLineLabReport.setBackgroundResource(R.drawable.shape_colorprimary_line);
                binding.btnUploadLabReport.setVisibility(View.GONE);
            } else if (contractDetailModel.getLab_report_status().equals("fail") && contractDetailModel.getLab_report_status().equals("fail_with_renegotiation")) {
                binding.mViewOvalLabReport.setBackgroundResource(R.drawable.shape_oval_color_primary);
                binding.mViewLineLabReport.setBackgroundResource(R.drawable.shape_colorprimary_line);
                binding.btnUploadLabReport.setVisibility(View.GONE);
            }
        }
        if (contractDetailModel.getTransmit_deal().equals("null")) {
            binding.imgTransmit.setVisibility(View.GONE);
            binding.btnUploadWithoutGst.setEnabled(false);
            if (mSessionUtil.getUsertype().equals("seller")) {
                binding.btnUploadTransmitDeal.setVisibility(View.VISIBLE);
            } else if (mSessionUtil.getUsertype().equals("buyer")) {
                binding.btnUploadTransmitDeal.setVisibility(View.GONE);
                binding.confirm3.setVisibility(View.GONE);
            }
        }
        if (contractDetailModel.getWithout_gst().equals("null")) {
            binding.btnUploadDebitNote.setEnabled(false);
            binding.imgGst.setVisibility(View.GONE);
            if (mSessionUtil.getUsertype().equals("seller")) {
                binding.btnUploadWithoutGst.setVisibility(View.GONE);
                binding.confirm4.setVisibility(View.VISIBLE);
            } else if (mSessionUtil.getUsertype().equals("buyer")) {
                binding.btnUploadWithoutGst.setVisibility(View.VISIBLE);
            }
        }

        if (contractDetailModel.getGst_reciept().equals("null")) {
            binding.imgPayGst.setVisibility(View.GONE);
            if (mSessionUtil.getUsertype().equals("seller")) {
                binding.btnUploadPayGst.setVisibility(View.GONE);
                binding.confirm6.setVisibility(View.VISIBLE);
            } else if (mSessionUtil.getUsertype().equals("buyer")) {
                binding.btnUploadPayGst.setVisibility(View.VISIBLE);
            }
        }
        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.confirm2.setVisibility(View.GONE);
            binding.confirm3.setVisibility(View.VISIBLE);
            binding.confirm4.setVisibility(View.GONE);
            binding.confirm6.setVisibility(View.GONE);

            if (contractDetailModel.getDebit_note_array().size() <= 0) {
                binding.btnUploadDebitNote.setVisibility(View.VISIBLE);
                binding.btnUploadDebitNoteMore.setVisibility(View.GONE);
                binding.confirm5.setVisibility(View.GONE);
                binding.btnUploadPayGst.setEnabled(false);
            } else {
                binding.btnUploadPayGst.setEnabled(true);
                binding.confirm5.setVisibility(View.GONE);
                binding.btnUploadDebitNote.setVisibility(View.GONE);
                binding.mViewOvalDabitNote.setBackgroundResource(R.drawable.shape_oval_color_primary);
                binding.mViewLineDabitNote.setBackgroundResource(R.drawable.shape_colorprimary_line);
            }
        }
        if (mSessionUtil.getUsertype().equals("seller")) {
            if (contractDetailModel.getDebit_note_array().size() <= 0) {
                binding.confirm5.setVisibility(View.VISIBLE);
            } else {
                binding.mViewOvalDabitNote.setBackgroundResource(R.drawable.shape_oval_color_primary);
                binding.mViewLineDabitNote.setBackgroundResource(R.drawable.shape_colorprimary_line);
                binding.confirm5.setVisibility(View.GONE);
            }
        }
        if (mSessionUtil.getUsertype().equals("seller")) {
            binding.layoutLab.setVisibility(View.GONE);
            binding.btnUploadDebitNote.setVisibility(View.GONE);
            binding.btnUploadDebitNoteMore.setVisibility(View.GONE);
        }
        debitNoteImageAdapter = new DebitNoteImageAdapter(mcontext, contractDetailModel.getDebit_note_array());
        binding.recyclerviewDebitNote.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerviewDebitNote.scrollToPosition(contractDetailModel.getDebit_note_array().size() - 1);
        binding.recyclerviewDebitNote.setAdapter(debitNoteImageAdapter);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mFilePickerController.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mFilePickerController.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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