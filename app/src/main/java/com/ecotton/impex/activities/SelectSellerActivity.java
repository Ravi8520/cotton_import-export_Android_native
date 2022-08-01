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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.SellectSellerAdapter;
import com.ecotton.impex.adapters.SellectedSellerAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivitySelectSellerBinding;
import com.ecotton.impex.models.DistrictModel;
import com.ecotton.impex.models.PrivatSellNotificationModel;
import com.ecotton.impex.models.SearchCompanyModel;
import com.ecotton.impex.models.StateModel;
import com.ecotton.impex.models.StationModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

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

    private final List<StateModel> stateModelList = new ArrayList<>();
    private final List<DistrictModel> districtModelList = new ArrayList<>();
    private final List<StationModel> stationModelList = new ArrayList<>();

    private int selectedState;
    private int selectedDistrict;
    private int selectedStation;

    private String data;
    private String impoert_exprot;
    private String sellerType;
    private String price;
    private String no_of_bales;
    private int productid;
    int compnayid;

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
            binding.txtTitle.setText(getResources().getString(R.string.lbl_select_seller));
            binding.LayoutStation.setVisibility(View.VISIBLE);
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
            sellerType = intent.getStringExtra("seller_type");
            impoert_exprot = intent.getStringExtra("d_e");

        }

        binding.searchView.setQueryHint("Search Buyer");
        binding.searchView.setIconified(true);
        binding.searchView.clearFocus();

        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchView.setIconified(false);
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sellectSellerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sellectSellerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        StateList();

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

    private void Notification_to_buy() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("my_company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("product_id", productid);
            jsonObject.put("price", price);
            jsonObject.put("no_of_bales", no_of_bales);
            jsonObject.put("buy_for", "Self");
            jsonObject.put("spinning_meal_name", "");
            jsonObject.put("country_id", "1");
            jsonObject.put("state_id", selectedState);
            jsonObject.put("district_id", selectedDistrict);

            jsonObject.put("seller_type", sellerType);
            jsonObject.put("d_e", impoert_exprot);


            if (mSessionUtil.getUsertype().equals("buyer")) {
                jsonObject.put("city_id", selectedStation);
            }
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

    private void SearchCompany() {
        try {
            sellectSellerAdapter.mArrayList.clear();
            sellectSellerAdapter.filterArray.clear();
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("country_id", "1");
            jsonObject.put("state_id", selectedState);
            jsonObject.put("district_id", selectedDistrict);
            jsonObject.put("city_id", "");
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
                        //stateAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        //stateAdapter.notifyDataSetChanged();
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

    public void setUpSpinerState(List<StateModel> list) {
        stateModelList.clear();
        stateModelList.addAll(list);
        StateAdapter adapter = new StateAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, stateModelList);
        binding.spinnerState.setAdapter(adapter);
        binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateModelList.get(position).getId();
                sellectSellerAdapter.mArrayList.clear();
                DistrictList(stateModelList.get(position).getId());

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void DistrictList(int id) {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state_id", id);
            String data = jsonObject.toString();
            Call<ResponseModel<List<DistrictModel>>> call = APIClient.getInstance().district_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DistrictModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DistrictModel>>> call, Response<ResponseModel<List<DistrictModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpSpinerDistrict(response.body().data);
                        //stateAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        //stateAdapter.notifyDataSetChanged();
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

    public void setUpSpinerDistrict(List<DistrictModel> list) {
        districtModelList.clear();
        districtModelList.addAll(list);
        DistrictAdapter adapter = new DistrictAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, districtModelList);
        binding.spinnerDistrict.setAdapter(adapter);
        binding.spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sellectSellerAdapter.mArrayList.clear();
                selectedDistrict = districtModelList.get(position).getId();
                StationList(districtModelList.get(position).getId());
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    Log.e("TAG", "StationList==" + new Gson().toJson(response.body()));

                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        sellectSellerAdapter.mArrayList.clear();
                        if (mSessionUtil.getUsertype().equals("buyer")) {
                            setUpSpinerStation(response.body().data);
                        } else {
                            setUpSpinerStation(response.body().data);
                            // SearchCompany();
                        }
                        //stateAdapter.addAllClass(response.body().data);
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
                public void onFailure(Call<ResponseModel<List<StationModel>>> call, Throwable t) {
                    Log.e("TAG", "StationList==" + t.getMessage());

                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpSpinerStation(List<StationModel> list) {
        stationModelList.clear();
        stationModelList.addAll(list);
        StationAdapter adapter = new StationAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, stationModelList);
        binding.spinnerStation.setAdapter(adapter);
        binding.spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStation = stationModelList.get(position).getId();
                sellectSellerAdapter.mArrayList.clear();
                SearchCompany();
                /*if (mSessionUtil.getUsertype().equals("buyer")) {
                    SearchCompany();
                }*/

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
            holder.txtTitle.setText(rowItem.getName());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, PrivateSellActivity.class));
        finish();
    }
}