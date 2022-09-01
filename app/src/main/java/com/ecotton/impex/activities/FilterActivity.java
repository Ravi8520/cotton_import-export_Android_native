package com.ecotton.impex.activities;

import static com.ecotton.impex.MyApp.filterRequest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.adapters.ProductAdapter;
import com.ecotton.impex.adapters.ProductValueAdapter;
import com.ecotton.impex.adapters.StateAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityFilterBinding;
import com.ecotton.impex.fragments.HomeFragment;
import com.ecotton.impex.models.BuyerFilterRequest;
import com.ecotton.impex.models.ProductModel;
import com.ecotton.impex.models.ProductValueModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilterActivity extends AppCompatActivity {


    ActivityFilterBinding binding;
    public FilterActivity mContext;
    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;
    private ProductAdapter productAdapter;
    private StateAdapter stateAdapter;
    private ProductValueAdapter productValueAdapter;
    ArrayList<ProductModel> productModelArrayList;

    private String screen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        //  getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        productModelArrayList = new ArrayList<>();
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        screen = getIntent().getStringExtra("screen");
        binding.btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRequest = new BuyerFilterRequest();
                filterRequest.setCompany_id(mSessionUtil.getCompanyId());
                filterRequest.setCountry_id("-1");

                productAdapter.ids.clear();
                productAdapter.selectedNames.clear();
                productAdapter.ids.add("-1");
                productAdapter.selectedNames.add("All");
                productAdapter.notifyDataSetChanged();

                stateAdapter.ids.clear();
                stateAdapter.selectedNames.clear();
                stateAdapter.ids.add("-1");
                stateAdapter.selectedNames.add("All");
                stateAdapter.notifyDataSetChanged();


                productValueAdapter.mArrayList.clear();


                productValueAdapter.notifyDataSetChanged();

                getProductValue();

                /*startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                finishAffinity();*/
            }
        });
        binding.btnApplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productAdapter.ids.size() > 0)
                    filterRequest.setProduct_id(TextUtils.join(",", productAdapter.ids));
                else
                    filterRequest.setProduct_id("-1");
                if (stateAdapter.ids.size() > 0)
                    filterRequest.setCountry_id(TextUtils.join(",", stateAdapter.ids));
                else
                    filterRequest.setCountry_id("-1");
                setUpAtribultesadpter();
               /* if (filterRequest.getState_id().equals("-1")) {
                    if (isHomeFragment()) {
                        onBackPressed();
                    } else {
                        startActivity(new Intent(mContext, HomeActivity.class));
                        finishAffinity();
                    }
                } else {
                    startActivity(new Intent(mContext, BuyerStateListActivity.class)
                            .putExtra("screen", FilterActivity.class.getSimpleName()));
                    finishAffinity();
                }*/

                startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class)
                        .putExtra("screen", FilterActivity.class.getSimpleName()));
                finishAffinity();
            }
        });
        setAdapter();
        getProductList();
        getStateList();


    }

    private boolean isHomeFragment() {
        return screen.equals(HomeFragment.class.getSimpleName());
    }


    private boolean isDashboardCompany() {
        return screen.equals(com.ecotton.impex.activities.DashboardCompanyListActivity.class.getSimpleName());
    }

    private void setAdapter() {
        productAdapter = new ProductAdapter(mContext);
        binding.rvSelectProduct.setLayoutManager(new GridLayoutManager(mContext, 3));
        binding.rvSelectProduct.setAdapter(productAdapter);
        binding.rvSelectProduct.setNestedScrollingEnabled(false);

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getProductValue();

            }
        });

        stateAdapter = new StateAdapter(mContext);
        binding.rvStateList.setLayoutManager(new GridLayoutManager(mContext, 3));
        binding.rvStateList.setAdapter(stateAdapter);
        binding.rvStateList.setNestedScrollingEnabled(false);

        stateAdapter.setOnItemClickListener(new StateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0)
                    filterRequest.setCountry_id("-1");
                else
                    filterRequest.setCountry_id(stateAdapter.mArrayList.get(position).getId() + "");
            }
        });
        setupProductAttributs();

    }

    public void setupProductAttributs() {
        productValueAdapter = new ProductValueAdapter(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvRange.setLayoutManager(layoutManager);
        binding.rvRange.setAdapter(productValueAdapter);
        binding.rvRange.setNestedScrollingEnabled(false);


        productValueAdapter.setOnItemClickListener(new ProductValueAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void getProductList() {
        try {
            customDialog.displayProgress(mContext);
            Call<ResponseModel<List<ProductModel>>> call = APIClient.getInstance().Product_list(mSessionUtil.getApiToken());
            call.enqueue(new Callback<ResponseModel<List<ProductModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProductModel>>> call, Response<ResponseModel<List<ProductModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("getProductList", "getProductList==" + new Gson().toJson(response.body()));

                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        productAdapter.addAllClass(response.body().data);
                        if (!TextUtils.isEmpty(filterRequest.getProduct_id())) {
                            ArrayList<String> ids = new ArrayList<>();
                            ids.addAll(Arrays.asList(filterRequest.getProduct_id().split(",", -1)));
                            //  ids = new ArrayList(Arrays.asList(TextUtils.split(",", filterRequest.getProduct_id())));
                            productAdapter.ids.addAll(ids);
                            productAdapter.notifyDataSetChanged();
                            getProductValue();
                        } else {
                            productAdapter.ids.add("-1");
                            productAdapter.notifyDataSetChanged();
                        }

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        productAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
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

    private void getStateList() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            String data = jsonObject.toString();
            Call<ResponseModel<List<ProductModel>>> call = APIClient.getInstance().state_list_for_dashboard_buyer_filter(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProductModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProductModel>>> call, Response<ResponseModel<List<ProductModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("getStateList", "getStateList==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        stateAdapter.addAllClass(response.body().data);

                        if (!TextUtils.isEmpty(filterRequest.getCountry_id())) {
                            ArrayList<String> ids = new ArrayList<>();
                            ids.addAll(Arrays.asList(filterRequest.getCountry_id().split(",", -1)));
                            stateAdapter.ids.addAll(ids);
                            stateAdapter.notifyDataSetChanged();
                        } else {
                            stateAdapter.ids.add("-1");
                            stateAdapter.notifyDataSetChanged();
                        }


                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        stateAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
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

    private void getProductValue() {
        try {
            String prodcuID = "";
            if (productAdapter.ids.size() > 0)
                prodcuID = TextUtils.join(",", productAdapter.ids);
            else
                prodcuID = "-1";

            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", prodcuID);
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            String data = jsonObject.toString();
            Log.e("getProductValue", "request==" + data);

            Call<ResponseModel<List<ProductValueModel>>> call = APIClient.getInstance().product_attribute_list_for_dashboard_filter(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProductValueModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProductValueModel>>> call, Response<ResponseModel<List<ProductValueModel>>> response) {
                    Log.e("getProductValue", "getProductValue==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setupProductAttributs();
                        filterRequest.attribute_array.clear();
                        productValueAdapter.addAllClass(setUpAtribultes(response.body().data));

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        productValueAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<ProductValueModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ProductValueModel> setUpAtribultes(List<ProductValueModel> attribute_array) {
        List<ProductValueModel> attribute_arrays = new ArrayList<>();
        if (filterRequest.attribute_array.size() > 0) {

            for (ProductValueModel obj : attribute_array) {
                obj.setMaxSelected(obj.getMax());
                obj.setMinSelected(obj.getMin());

                for (BuyerFilterRequest.AttributeModel attributeModel : filterRequest.attribute_array) {
                    if (attributeModel.getAttribute().equalsIgnoreCase(obj.getLable())) {
                        obj.setMaxSelected(attributeModel.getTo());
                        obj.setMinSelected(attributeModel.getFrom());
                    }
                }
                attribute_arrays.add(obj);
            }

        } else {
            for (ProductValueModel obj : attribute_array) {
                obj.setMaxSelected(obj.getMax());
                obj.setMinSelected(obj.getMin());
                attribute_arrays.add(obj);
            }

        }
        return attribute_arrays;
    }


    public void setUpAtribultesadpter() {
        ArrayList<BuyerFilterRequest.AttributeModel> attribute_array = new ArrayList<>();
        for (ProductValueModel obj : productValueAdapter.mArrayList) {
            BuyerFilterRequest.AttributeModel attributeModel = new BuyerFilterRequest.AttributeModel();
            attributeModel.setAttribute(obj.getLable());
            attributeModel.setFrom(String.format("%.2f", Double.valueOf(obj.getMinSelected())));
            attributeModel.setTo(String.format("%.2f", Double.valueOf(obj.getMaxSelected())));
            attribute_array.add(attributeModel);
        }
        filterRequest.setAttribute_array(attribute_array);
    }


}