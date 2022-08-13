package com.ecotton.impex.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.PostToSellAttributeAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityPersonalSellBinding;
import com.ecotton.impex.models.AttributeRequestModel;
import com.ecotton.impex.models.CountryModel;
import com.ecotton.impex.models.PostDetailSpinerData;
import com.ecotton.impex.models.ProductAttributeModel;
import com.ecotton.impex.models.ProductModel;
import com.ecotton.impex.models.ProtModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateSellActivity extends AppCompatActivity {

    private PrivateSellActivity mContext;
    ActivityPersonalSellBinding binding;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    List<ProductModel> productModelList = new ArrayList<>();
    PostToSellAttributeAdapter postToSellAttributeAdapter;
    List<AttributeRequestModel> attributeRequestModels = new ArrayList<>();

    private List<CountryModel> stateModelList = new ArrayList<>();
    private List<CountryModel> dispatchcontryList = new ArrayList<>();
    private List<CountryModel> destinationcontryList = new ArrayList<>();
    private List<ProtModel> portList = new ArrayList<>();
    private List<ProtModel> destinationportList = new ArrayList<>();

    private int productid;
    private int selectedStation;
    private int dispatchcontryid;
    private int destinationcontryid;
    private int selectedport;
    private int selecteddestinationport;

    public PostDetailSpinerData detailSpinerData;
    public String selectedTransmitCondition = "";
    public int is_destination;
    public int delivery_condition_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalSellBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_private_buy));
            binding.btnSelectBuyer.setText(getResources().getString(R.string.lbl_select_exporter));
        } else {
            binding.btnSelectBuyer.setText(getResources().getString(R.string.lbl_select_importer));
        }

        binding.btnSelectBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    if (selectedStation == -1) {
                        Toast.makeText(mContext, "Please select country of origin", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (productid == -1) {
                        Toast.makeText(mContext, "Please select Product", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(new Gson().toJson(attributeRequestModels));
                            Log.e("jsonArray", "jsonArray==" + jsonArray.toString());
                            Intent intent = new Intent(mContext, com.ecotton.impex.activities.SelectSellerActivity.class);
                            intent.putExtra("data", jsonArray.toString());
                            intent.putExtra("productid", productid);
                            intent.putExtra("price", binding.edtPrice.getText().toString().trim());
                            intent.putExtra("no_of_bales", binding.edtBales.getText().toString().trim());
                            intent.putExtra("country_origin_id", selectedStation);
                            intent.putExtra("delivery_condition_id", delivery_condition_id);
                            intent.putExtra("country_dispatch_id", dispatchcontryid);
                            intent.putExtra("port_dispatch_id", selectedport);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    }
                }
            }
        });
        binding.recyclerviewPostToSell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getProductList();

        postToSellAttributeAdapter = new PostToSellAttributeAdapter(mContext);
        binding.recyclerviewPostToSell.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewPostToSell.setAdapter(postToSellAttributeAdapter);
        postToSellAttributeAdapter.setOnItemClickListener(new PostToSellAttributeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String attributs, String value) {
                setAttributeArray(attributs, value);
            }
        });

        CountryList();
        getSpinerData();

    }

    private void CountryList() {
        Log.e("StateModel", "StateModel==");
        customDialog.displayProgress(mContext);
        Call<ResponseModel<List<CountryModel>>> call = APIClient.getInstance().country_list(mSessionUtil.getApiToken());
        Log.e("StateModel", "StateModel==" + call);
        Log.e("getApiToken", "getApiToken==" + mSessionUtil.getApiToken());
        call.enqueue(new Callback<ResponseModel<List<CountryModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CountryModel>>> call, Response<ResponseModel<List<CountryModel>>> response) {
                Log.e("StateModel", "StateModel==" + new Gson().toJson(response.body().data));
                customDialog.dismissProgress(mContext);
                if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                    setUpSpinerStation(response.body().data);
                    setUpSpinercountrydispatch(response.body().data);
                    setUpSpinercountrydestination(response.body().data);
                } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                    AppUtil.showToast(mContext, response.body().message);
                    AppUtil.autoLogout(mContext);
                } else {
                    AppUtil.showToast(mContext, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<CountryModel>>> call, Throwable t) {
                customDialog.dismissProgress(mContext);
            }
        });
    }

    public void setUpSpinerStation(List<CountryModel> list) {
        stateModelList.clear();
        CountryModel countryModel = new CountryModel();
        countryModel.setName("Country of origin");
        countryModel.setId(-1);
        stateModelList.add(countryModel);
        stateModelList.addAll(list);
        CountryAdapter adapter = new CountryAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, stateModelList);
        binding.spinnerCountry.setAdapter(adapter);
        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stateModelList.get(position).getId();
                Log.e("selectedStation", "selectedStation==" + selectedStation);


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpSpinercountrydispatch(List<CountryModel> list) {
        dispatchcontryList.clear();
        dispatchcontryList.addAll(list);
        CountryDispatchAdapter adapter = new CountryDispatchAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, dispatchcontryList);
        binding.spinnerCountryOfDispatch.setAdapter(adapter);
        binding.spinnerCountryOfDispatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dispatchcontryid = dispatchcontryList.get(position).getId();
                Log.e("selectedStation", "selectedStation==" + dispatchcontryid);
                PortList(dispatchcontryid);
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpSpinercountrydestination(List<CountryModel> list) {
        destinationcontryList.clear();
        destinationcontryList.addAll(list);
        CountryDispatchAdapter adapter = new CountryDispatchAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, destinationcontryList);
        binding.spinnerDestinationCountry.setAdapter(adapter);
        binding.spinnerDestinationCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destinationcontryid = destinationcontryList.get(position).getId();
                Log.e("selectedStation", "selectedStation==" + dispatchcontryid);
                DestinationPortList(destinationcontryid);
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void DestinationPortList(int countryid) {
        try {
            destinationportList.clear();
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("country_id", countryid);
            String data = jsonObject.toString();
            Call<ResponseModel<List<ProtModel>>> call = APIClient.getInstance().port_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProtModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProtModel>>> call, Response<ResponseModel<List<ProtModel>>> response) {
                    Log.e("ProtModel", "ProtModel==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerdestinationport(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<ProtModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setUpSpinerdestinationport(List<ProtModel> list) {
        destinationportList.clear();
        destinationportList.addAll(list);
        PortAdapter adapter = new PortAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, destinationportList);
        binding.spinnerDestinationPort.setAdapter(adapter);
        binding.spinnerDestinationPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecteddestinationport = destinationportList.get(position).getId();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void PortList(int countryid) {
        try {
            portList.clear();
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("country_id", countryid);
            String data = jsonObject.toString();
            Call<ResponseModel<List<ProtModel>>> call = APIClient.getInstance().port_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProtModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProtModel>>> call, Response<ResponseModel<List<ProtModel>>> response) {
                    Log.e("ProtModel", "ProtModel==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerport(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<ProtModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setUpSpinerport(List<ProtModel> list) {
        portList.clear();
        portList.addAll(list);
        PortAdapter adapter = new PortAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, portList);
        binding.spinnerPortOfDispatch.setAdapter(adapter);
        binding.spinnerPortOfDispatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedport = portList.get(position).getId();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class PortAdapter extends ArrayAdapter<ProtModel> {

        LayoutInflater flater;

        public PortAdapter(Context context, int resouceId, int textviewId, List<ProtModel> list) {

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

            ProtModel rowItem = getItem(position);

            PortAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new PortAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (PortAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class CountryDispatchAdapter extends ArrayAdapter<CountryModel> {

        LayoutInflater flater;

        public CountryDispatchAdapter(Context context, int resouceId, int textviewId, List<CountryModel> list) {

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

            CountryModel rowItem = getItem(position);

            CountryDispatchAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new CountryDispatchAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (CountryDispatchAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class CountryAdapter extends ArrayAdapter<CountryModel> {

        LayoutInflater flater;

        public CountryAdapter(Context context, int resouceId, int textviewId, List<CountryModel> list) {

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

            CountryModel rowItem = getItem(position);

            CountryAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new CountryAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.layout_spiner, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (CountryAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    private void getSpinerData() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            strJson = object.toString();
            PrintLog.d("TAG SpinerData", strJson);
            Call<ResponseModel<List<PostDetailSpinerData>>> call = APIClient.getInstance().getTransmitPaymentLabList(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<PostDetailSpinerData>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<PostDetailSpinerData>>> call, Response<ResponseModel<List<PostDetailSpinerData>>> response) {
                    Log.e("response", "spinerData==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        detailSpinerData = response.body().data.get(0);
                        setSpinerData();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<PostDetailSpinerData>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSpinerData() {

        CustomAdapter adapterTransmit = new CustomAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, detailSpinerData.getTransmit_condition());
        binding.spinnerDeliveryCondition.setAdapter(adapterTransmit);

        binding.spinnerDeliveryCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTransmitCondition = detailSpinerData.getTransmit_condition().get(position).getName();
                is_destination = detailSpinerData.getTransmit_condition().get(position).getIs_destination();
                delivery_condition_id = detailSpinerData.getTransmit_condition().get(position).getId();

                if (is_destination == 0) {
                    binding.layoutDestinationCountry.setVisibility(View.GONE);
                    binding.layoutDestinationPort.setVisibility(View.GONE);
                } else {
                    binding.layoutDestinationCountry.setVisibility(View.VISIBLE);
                    binding.layoutDestinationPort.setVisibility(View.VISIBLE);
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public class CustomAdapter extends ArrayAdapter<PostDetailSpinerData.SpinerModel> {
        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, int textviewId, List<PostDetailSpinerData.SpinerModel> list) {

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

            PostDetailSpinerData.SpinerModel rowItem = getItem(position);

            CustomAdapter.viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new CustomAdapter.viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (CustomAdapter.viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }


    private boolean isValidForm() {
        try {
            int price = Integer.parseInt(binding.edtPrice.getText().toString());
            if (TextUtils.isEmpty(binding.edtPrice.getText().toString()) || price == 0) {
                binding.edtPrice.setError("Please enter valid price");
                binding.edtPrice.requestFocus();
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            int price = Integer.parseInt(binding.edtBales.getText().toString());
            if (TextUtils.isEmpty(binding.edtBales.getText().toString()) || price == 0) {
                binding.edtBales.requestFocus();
                AppUtil.showToast(mContext, "Please enter valid bales");
                return false;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        if (binding.edtPrice.getText().toString().isEmpty()) {
            binding.edtPrice.setError("Please Enter Price");
            binding.edtPrice.requestFocus();
            return false;
        }
        if (binding.edtBales.getText().toString().isEmpty()) {
            binding.edtBales.setError("Please Enter Bales");
            binding.edtBales.requestFocus();
            return false;
        }

        return true;
    }

    public void setAttributeArray(String attribute, String value) {

        for (AttributeRequestModel obj : attributeRequestModels) {
            if (obj.getAttribute().equals(attribute)) {
                obj.setAttribute_value(value);
            }
        }
    }

    private void getProductList() {
        try {
            customDialog.displayProgress(mContext);
            Call<ResponseModel<List<ProductModel>>> call = APIClient.getInstance().Product_list(mSessionUtil.getApiToken());
            call.enqueue(new Callback<ResponseModel<List<ProductModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProductModel>>> call, Response<ResponseModel<List<ProductModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status != 0) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            setUpSpinnerProduct(response.body().data);
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
                public void onFailure(Call<ResponseModel<List<ProductModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpSpinnerProduct(List<ProductModel> list) {
        productModelList.clear();
        ProductModel productModel = new ProductModel();
        productModel.setName("Product type");
        productModel.setId(-1);
        productModelList.add(productModel);
        productModelList.addAll(list);
        ProductAdapter adapter = new ProductAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, productModelList);
        binding.spinnerProduct.setAdapter(adapter);
        binding.spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productid = productModelList.get(position).getId();
                GetAttribute(productid);

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class ProductAdapter extends ArrayAdapter<ProductModel> {

        LayoutInflater flater;

        public ProductAdapter(Context context, int resouceId, int textviewId, List<ProductModel> list) {

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

            ProductModel rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.layout_spiner, null, false);

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

    public void GetAttribute(int id) {
        try {
            postToSellAttributeAdapter.mArrayList.clear();
            postToSellAttributeAdapter.notifyDataSetChanged();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", id);
            String data = jsonObject.toString();
            customDialog.displayProgress(mContext);
            Call<ResponseModel<List<ProductAttributeModel>>> call = APIClient.getInstance().Product_attribute_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProductAttributeModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProductAttributeModel>>> call, Response<ResponseModel<List<ProductAttributeModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        fillAttributs(response.body().data);
                        postToSellAttributeAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        postToSellAttributeAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<ProductAttributeModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillAttributs(List<ProductAttributeModel> array) {
        for (ProductAttributeModel obj : array) {
            AttributeRequestModel objs = new AttributeRequestModel();
            objs.setAttribute(obj.getLabel());
            if (obj.getStateModelList().size() > 0)
                objs.setAttribute_value(obj.getStateModelList().get(0).getValue());
            else
                objs.setAttribute_value("");

            attributeRequestModels.add(objs);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
        finish();
    }
}