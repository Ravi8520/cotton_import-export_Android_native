package com.ecotton.impex.activities;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.PostToSellAttributeAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityPostToSellBinding;
import com.ecotton.impex.models.AttributeRequestModel;
import com.ecotton.impex.models.PostToSellModel;
import com.ecotton.impex.models.ProductAttributeModel;
import com.ecotton.impex.models.ProductModel;
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

public class PostToSellActivity extends AppCompatActivity {


    private PostToSellActivity mContext;
    ActivityPostToSellBinding binding;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    List<ProductModel> productModelList = new ArrayList<>();
    PostToSellAttributeAdapter postToSellAttributeAdapter;
    List<AttributeRequestModel> attributeRequestModels = new ArrayList<>();

    private int productid;
    String[] import_export = {"Export", "Domestic"};
    private String impoert_exprott;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostToSellBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        binding.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.hideSoftKeyboard(mContext);
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_post_to_buy));

            binding.lblbales.setText("Buy Bales");
        } else {

            binding.lblbales.setText("Sell Bales");
        }

        getProductList();
        if (mSessionUtil.getUsertype().equals("seller")) {
            binding.lblbales.setText(getResources().getString(R.string.sell_bales));
            binding.layoutDe.setVisibility(View.GONE);
        } else if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.layoutDe.setVisibility(View.VISIBLE);
        }

        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, import_export);
        binding.spinnerDe.setAdapter(adapter);
        binding.spinnerDe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                impoert_exprott = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.recyclerviewPostToSell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        postToSellAttributeAdapter = new PostToSellAttributeAdapter(mContext);
        binding.recyclerviewPostToSell.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewPostToSell.setAdapter(postToSellAttributeAdapter);
        postToSellAttributeAdapter.setOnItemClickListener(new PostToSellAttributeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String attributs, String value) {
                setAttributeArray(attributs, value);
            }
        });


        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidForm()) {
                    PostToSell();
                }
            }
        });

    }

    public void setAttributeArray(String attribute, String value) {

        for (AttributeRequestModel obj : attributeRequestModels) {
            if (obj.getAttribute().equals(attribute)) {
                obj.setAttribute_value(value);
            }
        }
    }

    private boolean isValidForm() {
        try {
            int price = Integer.parseInt(binding.edtPrice.getText().toString());
            if (TextUtils.isEmpty(binding.edtPrice.getText().toString()) || price == 0) {
                binding.edtPrice.requestFocus();
                AppUtil.showToast(mContext, "Please enter valid price");
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
        if (binding.edtBales.getText().toString().isEmpty()) {
            binding.edtBales.setError("Please Enter Bales");
            binding.edtBales.requestFocus();
            return false;
        }

        return true;
    }

    public void PostToSell() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("seller_buyer_id", mSessionUtil.getUserid());
            jsonObject.put("product_id", productid);
            jsonObject.put("price", binding.edtPrice.getText().toString().trim());
            jsonObject.put("no_of_bales", binding.edtBales.getText().toString().trim());
            jsonObject.put("address", "");

            if (mSessionUtil.getUsertype().equals("buyer")) {
                jsonObject.put("d_e", impoert_exprott);
                jsonObject.put("buy_for", "Self");
                jsonObject.put("spinning_meal_name", "");
            }

            JSONArray jsonArray = new JSONArray(new Gson().toJson(attributeRequestModels));

            jsonObject.put("attribute_array", jsonArray);

            Log.e("attribute_array", "attribute_array==" + jsonArray);

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            if (mSessionUtil.getUsertype().equals("buyer")) {
                customDialog.displayProgress(mContext);
                Call<ResponseModel<PostToSellModel>> call = APIClient.getInstance().Post_to_buy(mSessionUtil.getApiToken(), data);
                call.enqueue(new Callback<ResponseModel<PostToSellModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<PostToSellModel>> call, Response<ResponseModel<PostToSellModel>> response) {
                        customDialog.dismissProgress(mContext);
                        Log.e("Response", "Response==" + new Gson().toJson(response.body()));
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast(mContext, response.body().message);
                            startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                            finish();
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
                    public void onFailure(Call<ResponseModel<PostToSellModel>> call, Throwable t) {
                        customDialog.dismissProgress(mContext);
                    }
                });
            } else if (mSessionUtil.getUsertype().equals("seller")) {
                customDialog.displayProgress(mContext);
                Call<ResponseModel<PostToSellModel>> call = APIClient.getInstance().Post_to_sell(mSessionUtil.getApiToken(), data);
                call.enqueue(new Callback<ResponseModel<PostToSellModel>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<PostToSellModel>> call, Response<ResponseModel<PostToSellModel>> response) {
                        customDialog.dismissProgress(mContext);
                        Log.e("Response", "Response==" + new Gson().toJson(response.body()));
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            AppUtil.showToast(mContext, response.body().message);
                            startActivity(new Intent(mContext, com.ecotton.impex.activities.HomeActivity.class));
                            finish();
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
                    public void onFailure(Call<ResponseModel<PostToSellModel>> call, Throwable t) {
                        customDialog.dismissProgress(mContext);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
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
        productModelList.addAll(list);
        ProductAdapter adapter = new ProductAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, productModelList);
        binding.spinnerProduct.setAdapter(adapter);
        binding.spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productid = productModelList.get(position).getId();
                GetAttribute(productModelList.get(position).getId());

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

    public void fillAttributs(List<ProductAttributeModel> array) {
        attributeRequestModels.clear();
        for (ProductAttributeModel obj : array) {
            AttributeRequestModel objs = new AttributeRequestModel();
            objs.setAttribute(obj.getLabel());
            if (obj.getStateModelList().size() > 0)
                objs.setAttribute_value(obj.getStateModelList().get(0).getLabel());
            else
                objs.setAttribute_value("");
            Log.e("getLabel", "getLabel==" + obj.getLabel());
            Log.e("getValue", "getValue==" + obj.getStateModelList().get(0).getLabel());
            attributeRequestModels.add(objs);
        }
    }


    public void GetAttribute(int id) {
        try {
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
}