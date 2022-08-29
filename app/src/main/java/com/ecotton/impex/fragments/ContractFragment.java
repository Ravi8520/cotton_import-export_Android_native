package com.ecotton.impex.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.ecotton.impex.R;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentContractBinding;
import com.ecotton.impex.materialspinner.MaterialSpinner;
import com.ecotton.impex.models.BrokerReportModel;
import com.ecotton.impex.models.ContractProductModel;
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


public class ContractFragment extends Fragment {


    private Context mContext;
    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;

    String productwise = "";
    String brokerwise = "";
    String sellerwise = "sellerwise";

    String date;
    String status_string;
    int product_id;
    int broker_id;
    String product_name;

    FragmentContractBinding binding;

    private List<ContractProductModel> CompanyList = new ArrayList<>();
    private List<BrokerReportModel.MemberList> CompanyList1 = new ArrayList<>();
    List<ContractProductModel> list;
    MemberAdapter memberAdapter;
    String PDFPath;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    BrokerReportModel brokerReportModellist;
    BrokerReportModel brokerReportModellist1;

    int company_id, seller_buyer_id;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContractBinding.inflate(getLayoutInflater());


        if (mSessionUtil.getUsertype().equals("seller")) {
            binding.btnSellerwise.setText("Importer Wise");
            binding.txtTitle.setText("Importer");
        }


        binding.btnSellerwise.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_sellerwise) {
                    binding.layoutDownload.setVisibility(View.GONE);
                    sellerwise = "sellerwise";
                    productwise = "";
                    brokerwise = "";
                    PDFPath = null;
                    date = null;
                    binding.layoutCompany.setVisibility(View.VISIBLE);
                    binding.btnSellerwise.setBackground(getResources().getDrawable(R.drawable.custome_button));
                    binding.btnSellerwise.setTextColor(getResources().getColor(R.color.white));

                    binding.btnProductwise.setBackground(getResources().getDrawable(R.drawable.grey_button));
                    binding.btnProductwise.setTextColor(getResources().getColor(R.color.black));
                    binding.pickDateButton.setText("Select Date");
                    ContractPartyList();
                }

            }
        });
        binding.btnProductwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_productwise) {
                    productwise = "productwise";
                    brokerwise = "";
                    sellerwise = "";
                    PDFPath = null;
                    date = null;
                    binding.layoutCompany.setVisibility(View.GONE);
                    binding.layoutDownload.setVisibility(View.GONE);
                    binding.btnProductwise.setBackground(getResources().getDrawable(R.drawable.custome_button));
                    binding.btnProductwise.setTextColor(getResources().getColor(R.color.white));


                    binding.btnSellerwise.setBackground(getResources().getDrawable(R.drawable.grey_button));
                    binding.btnSellerwise.setTextColor(getResources().getColor(R.color.black));
                    binding.pickDateButton.setText("Select Date");
                    ProductLis();
                }

            }
        });

        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setTheme(R.style.Widget_AppTheme_MaterialDatePicker).
                setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();


        binding.pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PDFPath = null;
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        Pair selectedDates = (Pair) materialDatePicker.getSelection();
                        final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                        Date startDate = rangeDate.first;
                        Date endDate = rangeDate.second;

                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                        binding.pickDateButton.setText(simpleFormat.format(startDate) + "/" + simpleFormat.format(endDate));
                        date = simpleFormat.format(startDate) + "#" + simpleFormat.format(endDate);

                        if (sellerwise.equals("sellerwise")) {
                            Log.e("sellerwise", "sellerwise===" + sellerwise);
                            if (date != null) {
                                GetContractData(seller_buyer_id, company_id, date);
                                binding.pickDateButton.setText(simpleFormat.format(startDate) + "/" + simpleFormat.format(endDate));
                            } else {

                                Toast.makeText(mContext, "Plase Select Date", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        if (productwise.equals("productwise")) {
                            Log.e("productwise", "productwise===" + productwise);
                            if (date != null) {
                                GetLabReport(date);
                                binding.pickDateButton.setText(simpleFormat.format(startDate) + "/" + simpleFormat.format(endDate));
                            } else {

                                Toast.makeText(mContext, "Plase Select Date", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
            }
        });

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
                    } else {
                        Toast.makeText(mContext, "Please Select Date", Toast.LENGTH_SHORT).show();
                    }
                } else if (!checkAndRequestPermissions()) {
                    requestStoragePermission();

                }
            }
        });


        ContractPartyList();
        return binding.getRoot();
    }

    private void ContractPartyList() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("company_id", mSessionUtil.getCompanyId());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<BrokerReportModel>> call = APIClient.getInstance().contract_party_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<BrokerReportModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<BrokerReportModel>> call, Response<ResponseModel<BrokerReportModel>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        brokerReportModellist = response.body().data;
                        brokerReportModellist1 = response.body().data;
                        brokerReportModellist1.getMember_list();
                        brokerReportModellist.getCompany_list();
                        SetData1();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        AppUtil.showToast(mContext, response.body().message);
                        Log.e("response", "request_list==" + response.body().message);
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

    public void SetData1() {

        ArrayList<String> minAttribute = new ArrayList<>();
        for (BrokerReportModel.CompanyList obj : brokerReportModellist.getCompany_list()) {
            minAttribute.add(obj.getCompany_name());
        }
        binding.companySpinner.setItems(minAttribute);
        binding.companySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                PDFPath = null;
                DistrictList(position);
                // GetAttribute(productModelList.get(i).getId());

            }
        });
        binding.companySpinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });
        PDFPath = null;
        DistrictList(0);

      /*  StateAdapter adapter = new StateAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, brokerReportModellist.getCompany_list());
        binding.companySpinner.setAdapter(adapter);
        binding.companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PDFPath = null;
                DistrictList(position);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    public class StateAdapter extends ArrayAdapter<BrokerReportModel.CompanyList> {

        LayoutInflater flater;

        public StateAdapter(Context context, int resouceId, int textviewId, List<BrokerReportModel.CompanyList> list) {

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

            BrokerReportModel.CompanyList rowItem = getItem(position);

            StateAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new StateAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);
                rowview.setTag(holder);
            } else {
                holder = (StateAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getCompany_name());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    private void DistrictList(int pos) {
        try {
            CompanyList1.clear();
            BrokerReportModel.MemberList brokerReportModel = new BrokerReportModel.MemberList();
            brokerReportModel.setSeller_buyer_name(brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_name());
            CompanyList1.add(brokerReportModel);


            ArrayList<String> minAttribute = new ArrayList<>();
            for (BrokerReportModel.MemberList obj : CompanyList1) {
                minAttribute.add(obj.getSeller_buyer_name());
            }
            binding.spinnerUser.setItems(minAttribute);
            binding.spinnerUser.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                    PDFPath = "";
                    seller_buyer_id = brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_id();
                    company_id = brokerReportModellist1.getMember_list().get(pos).getCompany_id();
                    binding.txtName.setText(brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_name());
                    // GetAttribute(productModelList.get(i).getId());

                }
            });

            binding.spinnerUser.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

                @Override
                public void onNothingSelected(MaterialSpinner spinner) {
                    //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                }
            });

            PDFPath = "";
            seller_buyer_id = brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_id();
            company_id = brokerReportModellist1.getMember_list().get(pos).getCompany_id();
            binding.txtName.setText(brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_name());
          /*
            memberAdapter = new MemberAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, CompanyList1);
            binding.spinnerUser.setAdapter(memberAdapter);
            binding.spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    PDFPath = "";
                    seller_buyer_id = brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_id();
                    company_id = brokerReportModellist1.getMember_list().get(pos).getCompany_id();
                    binding.txtName.setText(brokerReportModellist1.getMember_list().get(pos).getSeller_buyer_name());
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MemberAdapter extends ArrayAdapter<BrokerReportModel.MemberList> {

        LayoutInflater flater;

        public MemberAdapter(Context context, int resouceId, int textviewId, List<BrokerReportModel.MemberList> list) {

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

            BrokerReportModel.MemberList rowItem = getItem(position);

            MemberAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new MemberAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (MemberAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getSeller_buyer_name());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    private void GetContractData(int seller_buyer_id, int com_id, String datee) {
        try {
            PDFPath = "";
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("my_company_id", mSessionUtil.getCompanyId());
            object.put("company_id", com_id);
            object.put("seller_buyer_id", seller_buyer_id);
            object.put("date_range", datee);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<BrokerReportModel>> call = APIClient.getInstance().party_wise_contract_report(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<BrokerReportModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<BrokerReportModel>> call, Response<ResponseModel<BrokerReportModel>> response) {
                    customDialog.dismissProgress(mContext);
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


    private void ProductLis() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<List<ContractProductModel>>> call = APIClient.getInstance().contract_product_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ContractProductModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ContractProductModel>>> call, Response<ResponseModel<List<ContractProductModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("ContractProductModel", "ContractProductModel==" + new Gson().toJson(response.body().data));
                        AppUtil.showToast(mContext, response.body().message);

                        list = response.body().data;

                        SetData();

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
                public void onFailure(Call<ResponseModel<List<ContractProductModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void BrokerWiseReport(String date) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("broker_id", broker_id);
            object.put("date_range", date);
            String data = object.toString();
            Log.e("BrokerWiseReport", "BrokerWiseReport==" + data);
            Call<ResponseModel<ContractProductModel>> call = APIClient.getInstance().broker_wise_contract_report(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<ContractProductModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ContractProductModel>> call, Response<ResponseModel<ContractProductModel>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("broker_wise_", "broker_wise_contract_report==" + new Gson().toJson(response.body().data));
                        AppUtil.showToast(mContext, response.body().message);
                        PDFPath = response.body().data.getReport_url();
                        if (PDFPath != null) {
                            binding.layoutDownload.setVisibility(View.VISIBLE);
                        } else {
                            AppUtil.showToast(mContext, "No Record Found");
                        }
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
                public void onFailure(Call<ResponseModel<ContractProductModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetLabReport(String date) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            if (product_id == -1) {
                object.put("product_id", 0);
            } else {
                object.put("product_id", product_id);
            }
            object.put("date_range", date);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseModel<ContractProductModel>> call = APIClient.getInstance().product_wise_contract_report(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<ContractProductModel>>() {
                @Override
                public void onResponse(Call<ResponseModel<ContractProductModel>> call, Response<ResponseModel<ContractProductModel>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("ContractProductModel", "ContractProductModel==" + new Gson().toJson(response.body().data));
                        AppUtil.showToast(mContext, response.body().message);
                        PDFPath = response.body().data.getReport_url();
                        if (PDFPath != null) {
                            binding.layoutDownload.setVisibility(View.VISIBLE);
                        } else {
                            AppUtil.showToast(mContext, "No Record Found");
                        }
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
                public void onFailure(Call<ResponseModel<ContractProductModel>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SetData() {
        CompanyList.clear();
        ContractProductModel contractProductModel = new ContractProductModel();
        contractProductModel.setProduct_name("All");
        contractProductModel.setProduct_id(-1);
        CompanyList.add(contractProductModel);
        CompanyList.addAll(list);

        ArrayList<String> minAttribute = new ArrayList<>();
        for (ContractProductModel obj : CompanyList) {
            minAttribute.add(obj.getProduct_name());
        }
        binding.spinnerUser.setItems(minAttribute);
        binding.spinnerUser.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                product_name = CompanyList.get(position).getProduct_name();
                product_id = CompanyList.get(position).getProduct_id();
                binding.txtName.setText(product_name);
                // GetAttribute(productModelList.get(i).getId());

            }
        });
        binding.spinnerUser.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });
        product_name = CompanyList.get(0).getProduct_name();
        product_id = CompanyList.get(0).getProduct_id();
        binding.txtName.setText(product_name);

       /* ProductAdapter adapter = new ProductAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, CompanyList);
        binding.spinnerUser.setAdapter(adapter);
        binding.spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product_name = CompanyList.get(position).getProduct_name();
                product_id = CompanyList.get(position).getProduct_id();
                binding.txtName.setText(product_name);
                Log.e("status_string", "status_string==" + product_id);
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }


    public class BrokerAdapter extends ArrayAdapter<ContractProductModel> {

        LayoutInflater flater;

        public BrokerAdapter(Context context, int resouceId, int textviewId, List<ContractProductModel> list) {

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

            ContractProductModel rowItem = getItem(position);

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
            holder.txtTitle.setText(rowItem.getBroker_name());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class ProductAdapter extends ArrayAdapter<ContractProductModel> {

        LayoutInflater flater;

        public ProductAdapter(Context context, int resouceId, int textviewId, List<ContractProductModel> list) {

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

            ContractProductModel rowItem = getItem(position);

            ProductAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new ProductAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);
                rowview.setTag(holder);
            } else {
                holder = (ProductAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getProduct_name());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
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