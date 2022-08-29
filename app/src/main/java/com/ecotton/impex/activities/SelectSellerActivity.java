package com.ecotton.impex.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.SellectSellerAdapter;
import com.ecotton.impex.adapters.SellectedSellerAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivitySelectSellerBinding;
import com.ecotton.impex.materialspinner.MaterialSpinner;
import com.ecotton.impex.models.CountryModel;
import com.ecotton.impex.models.PrivatSellNotificationModel;
import com.ecotton.impex.models.SearchCompanyModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSellerActivity extends AppCompatActivity {

    private SelectSellerActivity mContext;
    SellectSellerAdapter sellectSellerAdapter;
    SellectedSellerAdapter sellectedSellerAdapter;
    ActivitySelectSellerBinding binding;

    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;

    private List<CountryModel> stateModelList = new ArrayList<>();


    private int selectedState;
    private int selectedDistrict;
    private int selectedStation;

    private String data;
    private String price;
    private String no_of_bales;
    private int productid;
    private int country_origin_id;
    private int delivery_condition_id;
    private int country_dispatch_id;
    private int port_dispatch_id;


    public static JSONArray jsonArray1;

    public static ArrayList<SearchCompanyModel> selectedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);
        jsonArray1 = new JSONArray();
        selectedList.clear();
        binding.selectBuyerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        sellectSellerAdapter = new SellectSellerAdapter(mContext);
        binding.selectBuyerRecyclerView.setNestedScrollingEnabled(false);
        binding.selectBuyerRecyclerView.setAdapter(sellectSellerAdapter);
        sellectSellerAdapter.setOnItemClickListener(new SellectSellerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sellectedSellerAdapter.addAllClass(selectedList);
            }
        });


        binding.layoutMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });
        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        // binding.selectedBuyerRecyclerView.setLayoutManager(layoutManager);
        binding.selectedBuyerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        sellectedSellerAdapter = new SellectedSellerAdapter(mContext);
        binding.selectedBuyerRecyclerView.setNestedScrollingEnabled(false);
        binding.selectedBuyerRecyclerView.setAdapter(sellectedSellerAdapter);
        sellectedSellerAdapter.setOnItemClickListener(new SellectedSellerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                sellectSellerAdapter.refreshData();
            }
        });
        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_select_exporter));
        } else {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_select_importer));
        }

        binding.btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                finish();
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getStringExtra("data");
            price = intent.getStringExtra("price");
            no_of_bales = intent.getStringExtra("no_of_bales");
            productid = intent.getIntExtra("productid", 0);
            country_origin_id = intent.getIntExtra("country_origin_id", 0);
            delivery_condition_id = intent.getIntExtra("delivery_condition_id", 0);
            country_dispatch_id = intent.getIntExtra("country_dispatch_id", 0);
            port_dispatch_id = intent.getIntExtra("port_dispatch_id", 0);

        }

        CountryList();


        binding.btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedList.size() > 0)
                    Notification_to_buy();
                else
                    AppUtil.showToast(mContext, "please select company");
            }
        });
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

        ArrayList<String> minAttribute = new ArrayList<>();
        for (CountryModel obj : stateModelList) {
            minAttribute.add(obj.getName());
        }
        binding.spinnerCountry.setItems(minAttribute);
        binding.spinnerCountry.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                selectedStation = stateModelList.get(position).getId();
                // GetAttribute(productModelList.get(i).getId());
                SearchCompany(selectedStation);
            }
        });
        binding.spinnerCountry.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });
        selectedStation = stateModelList.get(0).getId();
        //selectedStation = stateModelList.get(0).getId();

      /*
        CountryAdapter adapter = new CountryAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, stateModelList);
        binding.spinnerCountry.setAdapter(adapter);
        binding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stateModelList.get(position).getId();
                SearchCompany(selectedStation);
                Log.e("selectedStation", "selectedStation==" + selectedStation);


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
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

    private void Notification_to_buy() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("my_company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("product_id", productid);
            jsonObject.put("price", price);
            jsonObject.put("no_of_bales", no_of_bales);
            jsonObject.put("country_origin_id", country_origin_id);
            jsonObject.put("delivery_condition_id", delivery_condition_id);
            jsonObject.put("country_dispatch_id", country_dispatch_id);
            jsonObject.put("port_dispatch_id", port_dispatch_id);


            jsonArray1 = new JSONArray();
            for (SearchCompanyModel obj : selectedList) {
                jsonArray1.put(obj.getCompany_id());
            }

            Log.e("jsonArray", "jsonArray==" + jsonArray1.toString());

            jsonObject.put("company_ids", jsonArray1);

            JSONArray jsonArray = new JSONArray(data);
            jsonObject.put("attribute_array", jsonArray);


            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            if (mSessionUtil.getUsertype().equals("buyer")) {
                Call<ResponseModel<PrivatSellNotificationModel>> call = APIClient.getInstance().Notification_to_seller(mSessionUtil.getApiToken(), data);
                call.enqueue(new Callback<ResponseModel<PrivatSellNotificationModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<PrivatSellNotificationModel>> call, Response<ResponseModel<PrivatSellNotificationModel>> response) {
                        customDialog.dismissProgress(mContext);
                        Log.e("ionModel", "PrivatSellNotificationModel1111==" + new Gson().toJson(response.body()));
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast1(mContext, response.body().message);
                            startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                            finish();
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            sellectSellerAdapter.notifyDataSetChanged();
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(mContext);
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<PrivatSellNotificationModel>> call, Throwable t) {
                        customDialog.dismissProgress(mContext);
                    }
                });
            } else if (mSessionUtil.getUsertype().equals("seller")) {
                Call<ResponseModel<PrivatSellNotificationModel>> call = APIClient.getInstance().Notification_to_buy(mSessionUtil.getApiToken(), data);
                call.enqueue(new Callback<ResponseModel<PrivatSellNotificationModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<PrivatSellNotificationModel>> call, Response<ResponseModel<PrivatSellNotificationModel>> response) {
                        customDialog.dismissProgress(mContext);
                        Log.e("ionModel", "PrivatSellNotificationModel==" + new Gson().toJson(response.body()));
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast1(mContext, response.body().message);
                            startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                            finish();
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            sellectSellerAdapter.notifyDataSetChanged();
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(mContext);
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<PrivatSellNotificationModel>> call, Throwable t) {
                        customDialog.dismissProgress(mContext);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchCompany(int selectedStation) {
        try {
            sellectSellerAdapter.mArrayList.clear();
            sellectSellerAdapter.filterArray.clear();
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("country_id", selectedStation);
            String data = jsonObject.toString();
            Log.e("data", "data==" + data);

            Call<ResponseModel<List<SearchCompanyModel>>> call = APIClient.getInstance().Search_company(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<SearchCompanyModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<SearchCompanyModel>>> call, Response<ResponseModel<List<SearchCompanyModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("SearchCompanyModel", "SearchCompanyModel==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        sellectSellerAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        sellectSellerAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<SearchCompanyModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, PrivateSellActivity.class));
        finish();
    }
}