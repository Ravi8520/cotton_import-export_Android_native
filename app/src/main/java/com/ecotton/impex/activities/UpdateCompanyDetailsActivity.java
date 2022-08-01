package com.ecotton.impex.activities;

import static com.ecotton.impex.fragments.CompanyDetailFragment.arraylist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityUpdateCompanyDetailsBinding;
import com.ecotton.impex.models.CompanyDetailModel;
import com.ecotton.impex.models.DistrictModel;
import com.ecotton.impex.models.SellertypeBuyerTypeModel;
import com.ecotton.impex.models.StateModel;
import com.ecotton.impex.models.StationModel;
import com.ecotton.impex.models.UploadCompanyDetailModdel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.Constants;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.ecotton.impex.utils.ValidationUtil;
import com.tfb.filepicker.FilePickerController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCompanyDetailsActivity extends AppCompatActivity {

    ActivityUpdateCompanyDetailsBinding binding;
    UpdateCompanyDetailsActivity mContext;
    private FilePickerController mFilePickerController;
    private Uri mUriPharmacyLogo = null;
    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;

    ArrayList<UploadCompanyDetailModdel> arrayListtt;

    private String selectedItem;
    private int selectedState;
    private int selectedDistrict;
    private int selectedStation;

    private List<SellertypeBuyerTypeModel.SellerBuyerType> sellerBuyerTypes = new ArrayList<>();
    private List<StateModel> stateModelList = new ArrayList<>();
    private List<DistrictModel> districtModelList = new ArrayList<>();
    private List<StationModel> stationModelList = new ArrayList<>();
    String edit_profile;
    DistrictAdapter districtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateCompanyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);
        arrayListtt = new ArrayList<>();

        Intent intent = getIntent();

        if (intent != null) {
            edit_profile = intent.getStringExtra("edit");
        }
        if (edit_profile != null) {
            if (edit_profile.equals("edit_profile")) ;
            {
                binding.txtTitle.setText(getResources().getString(R.string.lbl_edit_company_details));
                binding.btnUpdate.setText(getResources().getString(R.string.update));
            }
        }


        GetCompanyDetails();

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


        int maxLength = 10;
        binding.edtPanNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength), new InputFilter.AllCaps()});
        binding.edtCompanyPanNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength), new InputFilter.AllCaps()});

        binding.edtGstNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
  /*      binding.edtCompanyPanNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        binding.edtPanNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});*/

        if (arraylist != null) {
            binding.edtPanNumber.setText(arraylist.getPan_no());
            binding.edtCompanyPanNumber.setText(arraylist.getCompany_pan_no());
            binding.edtAddress.setText(arraylist.getAddress());
            binding.edtGstNumber.setText(arraylist.getGst_no());
            binding.edtBankname.setText(arraylist.getBank_name());
            binding.edtAcHoldername.setText(arraylist.getAccount_holder_name());
            binding.edtBranchAddress.setText(arraylist.getBranch_address());
            binding.edtIfscCode.setText(arraylist.getIfsc_code());
            Glide.with(mContext).load(arraylist.getStamp_img()).placeholder(R.drawable.placeholder).into(binding.imgUploadStamp);
        }

        mFilePickerController = new FilePickerController(mContext);
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.layoutUploadStamp.setOnClickListener(new View.OnClickListener() {
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
                                mUriPharmacyLogo = mSelectedFileList.get(0);
                                Glide.with(mContext)
                                        .load(mUriPharmacyLogo)
                                        .apply(AppUtil.getDefaultRequestOptions())
                                        .into(binding.imgUploadStamp);
                            }
                        }
                    });
                    mFilePickerController.showFilePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    UpdateDetails();
                }
            }
        });
        sellertype_buyertype();
        StateList();
    }

    private void StateList() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("country_id", "1");
            String data = jsonObject.toString();
            Call<ResponseModel<List<StateModel>>> call = APIClient.getInstance().state_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<StateModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<StateModel>>> call, Response<ResponseModel<List<StateModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerState(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<StateModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void StationList(int id) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("district_id", id);
            String data = jsonObject.toString();
            Call<ResponseModel<List<StationModel>>> call = APIClient.getInstance().station_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<StationModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<StationModel>>> call, Response<ResponseModel<List<StationModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerStation(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<StationModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DistrictList(int id) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state_id", id);
            Log.e("state_id", "state_id==" + id);
            String data = jsonObject.toString();
            Call<ResponseModel<List<DistrictModel>>> call = APIClient.getInstance().district_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DistrictModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DistrictModel>>> call, Response<ResponseModel<List<DistrictModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerDistrict(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<DistrictModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sellertype_buyertype() {

        try {
            customDialog.displayProgress(mContext);

            Call<ResponseModel<List<SellertypeBuyerTypeModel>>> call = APIClient.getInstance().sellertype_buyertype(mSessionUtil.getApiToken());
            call.enqueue(new Callback<ResponseModel<List<SellertypeBuyerTypeModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<SellertypeBuyerTypeModel>>> call, Response<ResponseModel<List<SellertypeBuyerTypeModel>>> response) {
                    Log.e("Seller", "SellertypeBuyerTypeModel==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpiner(response.body().data.get(0).getStateModelList());
                        //adapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        //adapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<SellertypeBuyerTypeModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpSpinerState(List<StateModel> list) {
        stateModelList.clear();
        StateModel stateModel = new StateModel();
        stateModel.setName("Select State");
        stateModel.setId(-1);
        stateModelList.add(stateModel);
        stateModelList.addAll(list);
        StateAdapter adapter = new StateAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, stateModelList);
        binding.spinnerState.setAdapter(adapter);
        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtModelList.clear();
                selectedState = stateModelList.get(position).getId();
                DistrictList(stateModelList.get(position).getId());

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpSpinerDistrict(List<DistrictModel> list) {
        districtModelList.clear();
        if (districtModelList.get(0).getId() == -1) {
            DistrictModel districtModel=new DistrictModel();
        }
        districtModelList.addAll(list);
        districtAdapter = new DistrictAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, districtModelList);
        binding.spinnerDistrict.setAdapter(districtAdapter);
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedDistrict = districtModelList.get(position).getId();
                StationList(districtModelList.get(position).getId());
               /* if (position > 0) {

                } else {
                   *//* selectedDistrict = districtModelList.get(position).getId();
                    StationList(districtModelList.get(position).getId());*//*
                }*/

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpSpinerStation(List<StationModel> list) {
        stationModelList.clear();
        stationModelList.addAll(list);
        StationAdapter adapter = new StationAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, stationModelList);
        binding.spinnerStation.setAdapter(adapter);
        binding.spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stationModelList.get(position).getId();

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpSpiner(List<SellertypeBuyerTypeModel.SellerBuyerType> list) {
        sellerBuyerTypes.clear();
        sellerBuyerTypes.addAll(list);
        CustomAdapter adapter = new CustomAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, sellerBuyerTypes);
        binding.spinnerType.setAdapter(adapter);
        binding.spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = sellerBuyerTypes.get(position).getName();
                Log.e("selectedItem", "selectedItem==" + selectedItem);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public class StateAdapter extends ArrayAdapter<StateModel> {

        LayoutInflater flater;

        public StateAdapter(Context context, int resouceId, int textviewId, List<StateModel> list) {

            super(context, resouceId, textviewId, list);
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            StateModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            if (position == 0) {
                holder.txtTitle.setTextColor(Color.GRAY);
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class StationAdapter extends ArrayAdapter<StationModel> {

        LayoutInflater flater;

        public StationAdapter(Context context, int resouceId, int textviewId, List<StationModel> list) {
            super(context, resouceId, textviewId, list);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            StationModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class DistrictAdapter extends ArrayAdapter<DistrictModel> {

        LayoutInflater flater;

        public DistrictAdapter(Context context, int resouceId, int textviewId, List<DistrictModel> list) {
            super(context, resouceId, textviewId, list);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            DistrictModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            if (position == -1) {
                holder.txtTitle.setTextColor(Color.GRAY);
            } else {
                holder.txtTitle.setText(rowItem.getName());
            }


            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class CustomAdapter extends ArrayAdapter<SellertypeBuyerTypeModel.SellerBuyerType> {

        LayoutInflater flater;

        public CustomAdapter(Context context, int resouceId, int textviewId, List<SellertypeBuyerTypeModel.SellerBuyerType> list) {

            super(context, resouceId, textviewId, list);
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            SellertypeBuyerTypeModel.SellerBuyerType rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    private boolean isValidForm() {

        String CamPanInput = binding.edtCompanyPanNumber.getText().toString();
        Pattern CAM_PAN_PATTERN =
                Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        if (!CAM_PAN_PATTERN.matcher(CamPanInput).matches()) {
            binding.edtCompanyPanNumber.setError("Please Enter Proper PAN Number");
            binding.edtCompanyPanNumber.requestFocus();
            return false;
        }

        if (binding.edtCompanyPanNumber.getText().toString().trim().isEmpty()) {
            binding.edtCompanyPanNumber.setError("Please enter Company PAN Number");
            binding.edtCompanyPanNumber.requestFocus();
            return false;
        }

        String passwordInput = binding.edtGstNumber.getText().toString();
        Pattern GST_PATTERN =
                Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}"
                        + "[A-Z]{1}[1-9A-Z]{1}"
                        + "Z[0-9A-Z]{1}$");
        if (!GST_PATTERN.matcher(passwordInput).matches()) {
            binding.edtGstNumber.setError("Please Enter Proper GST Number");
            binding.edtGstNumber.requestFocus();
            return false;
        }
        String PanInput = binding.edtPanNumber.getText().toString();
        Pattern PAN_PATTERN =
                Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        if (!PAN_PATTERN.matcher(PanInput).matches()) {
            binding.edtPanNumber.setError("Please Enter Proper PAN Number");
            binding.edtPanNumber.requestFocus();
            return false;
        }
        if (binding.edtPanNumber.getText().toString().trim().isEmpty()) {
            binding.edtPanNumber.setError("Please Enter PAN Number");
            binding.edtPanNumber.requestFocus();
            return false;
        }
        if (binding.edtBankname.getText().toString().trim().isEmpty()) {
            binding.edtBankname.setError("Please enter Bank name");
            binding.edtBankname.requestFocus();
            return false;
        }
        if (binding.edtAddress.getText().toString().trim().isEmpty()) {
            binding.edtAddress.setError("Please enter Address");
            binding.edtAddress.requestFocus();
            return false;
        }
        if (binding.edtAcHoldername.getText().toString().trim().isEmpty()) {
            binding.edtAcHoldername.setError("Please enter Account Holder Name");
            binding.edtAcHoldername.requestFocus();
            return false;
        }
        if (binding.edtBranchAddress.getText().toString().trim().isEmpty()) {
            binding.edtBranchAddress.setError("Please enter Branch Address");
            binding.edtBranchAddress.requestFocus();
            return false;
        }
        if (binding.edtIfscCode.getText().toString().trim().isEmpty()) {
            binding.edtIfscCode.setError("Please enter IFSC Code");
            binding.edtIfscCode.requestFocus();
            return false;
        }


        if (edit_profile != null) {
            if (edit_profile.equals("edit_profile")) ;
            {
                if (mUriPharmacyLogo == null) {
                    return true;
                }
            }
        } else {

            if (mUriPharmacyLogo == null) {
                AppUtil.showToast(mContext, mContext.getString(R.string.err_upload_company_stamp));
                return false;
            }
        }
        return true;
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
                        binding.edtGstNumber.setText(response.body().data.getGst_no());
                        binding.edtGstNumber.setEnabled(false);
                        //  productValueAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        // productValueAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
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

    private void UpdateDetails() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("company_name", mSessionUtil.getCompanyName());
            object.put("sellerBuyer_type", selectedItem);
            object.put("country_id", "1");
            object.put("state_id", selectedState);
            object.put("district_id", selectedDistrict);
            object.put("city_id", selectedStation);
            object.put("address", binding.edtAddress.getText().toString().trim());
            object.put("gst_no", binding.edtGstNumber.getText().toString().trim());
            object.put("pan_no", binding.edtPanNumber.getText().toString().trim());
            object.put("bank_name", binding.edtBankname.getText().toString().trim());
            object.put("account_holder_name", binding.edtAcHoldername.getText().toString().trim());
            object.put("branch_address", binding.edtBranchAddress.getText().toString().trim());
            object.put("ifsc_code", binding.edtIfscCode.getText().toString().trim());
            object.put("company_pan_no", binding.edtCompanyPanNumber.getText().toString().trim());

            String data = object.toString();

            Log.e("data", "data==" + data);

            MultipartBody.Part companystamp = null;

            if (mUriPharmacyLogo != null && ValidationUtil.validateString(mUriPharmacyLogo.getPath())) {
                File photoFile = AppUtil.getFile(mContext, mUriPharmacyLogo);
                RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(AppUtil.getFileMimeTypeFromUri(mContext, mUriPharmacyLogo)), photoFile);
                companystamp = MultipartBody.Part.createFormData("stamp_image", photoFile.getName(), requestPhotoFile);
            }

            Call<ResponseModel<UploadCompanyDetailModdel>> call = APIClient.getInstance().Update_company(mSessionUtil.getApiToken(), AppUtil.createPartFromString(data), companystamp);
            call.enqueue(new Callback<ResponseModel<UploadCompanyDetailModdel>>() {
                @Override
                public void onResponse(Call<ResponseModel<UploadCompanyDetailModdel>> call, Response<ResponseModel<UploadCompanyDetailModdel>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("response", "response==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        SharedPreferences.Editor editor = getSharedPreferences("cotton", MODE_PRIVATE).edit();
                        editor.putInt("issetup", 1);
                        editor.apply();
                        AppUtil.showToast(mContext, response.body().message);
                        arrayListtt.add(response.body().data);
                        if (edit_profile != null) {
                            if (edit_profile.equals("edit_profile")) ;
                            {
                                onBackPressed();
                            }
                        } else {
                            startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                            finish();
                        }
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<UploadCompanyDetailModdel>> call, Throwable t) {

                }
            });

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
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
        if (edit_profile != null) {
            if (edit_profile.equals("edit_profile")) ;
            {
                startActivity(new Intent(mContext, com.ecotton.impex.activities.MyProfileActivity.class));
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

}