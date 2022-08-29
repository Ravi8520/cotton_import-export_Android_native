package com.ecotton.impex.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.R;
import com.ecotton.impex.activities.SelectBuyerActivity;
import com.ecotton.impex.adapters.PostToSellAttributeAdapter1;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentSearchBuyerBinding;
import com.ecotton.impex.materialspinner.MaterialSpinner;
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

public class SearchBuyerFragment extends Fragment {

    private Context mContext;
    FragmentSearchBuyerBinding binding;

    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    List<ProductModel> productModelList = new ArrayList<>();
    PostToSellAttributeAdapter1 postToSellAttributeAdapter;
    List<AttributeRequestModel> attributeRequestModels = new ArrayList<>();


    private int productid;
    private String productname;

    public PostDetailSpinerData detailSpinerData;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBuyerBinding.inflate(getLayoutInflater());
        getProductList();
        SetAdapter();

        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(mContext.getString(R.string.lbl_search_seller));
        }
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (productid == -1) {
                        Toast.makeText(mContext, "Please select Product", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(attributeRequestModels));
                        Intent intent = new Intent(mContext, SelectBuyerActivity.class);
                        intent.putExtra("data", jsonArray.toString());
                        intent.putExtra("product_id", productid);
                        intent.putExtra("productname", productname);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                }
            }
        });
        getSpinerData();

        return binding.getRoot();
    }


    private void getSpinerData() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            strJson = object.toString();
            PrintLog.e("TAG SpinerData", strJson);
            Call<ResponseModel<List<PostDetailSpinerData>>> call = APIClient.getInstance().getTransmitPaymentLabList(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<PostDetailSpinerData>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<PostDetailSpinerData>>> call, Response<ResponseModel<List<PostDetailSpinerData>>> response) {
                    Log.e("response", "spinerData==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        detailSpinerData = response.body().data.get(0);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
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




    private void SetAdapter() {
        postToSellAttributeAdapter = new PostToSellAttributeAdapter1(mContext);
        binding.recyclerviewPostToSell.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerviewPostToSell.setAdapter(postToSellAttributeAdapter);


        postToSellAttributeAdapter.setOnItemClickListener(new PostToSellAttributeAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String attributs, String value) {
                setAttributeArray(attributs, value);
            }
        });

        postToSellAttributeAdapter.setOnItemClickListener1(new PostToSellAttributeAdapter1.OnItemClickListener1() {
            @Override
            public void onItemClick1(View view, String attributs, String value) {
                setAttributeArray1(attributs, value);
            }
        });
    }

    public void setAttributeArray(String attribute, String value) {

        for (AttributeRequestModel obj : attributeRequestModels) {
            if (obj.getAttribute().equals(attribute)) {
                obj.setFrom(value);

            }
        }
    }

    public void setAttributeArray1(String attribute, String value) {
        for (AttributeRequestModel obj : attributeRequestModels) {
            if (obj.getAttribute().equals(attribute)) {
                obj.setTo(value);
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
                    Log.e("ProductModel", "ProductModel==" + new Gson().toJson(response.body()));
                    if (response.body().status != 0) {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                            setUpSpinnerProduct(response.body().data);
                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(mContext, response.body().message);
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(getActivity());
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
        //binding.spinnerProduct.setAdapter(adapter);

        ArrayList<String> minAttribute = new ArrayList<>();
        for (ProductModel obj : productModelList) {
            minAttribute.add(obj.getName());
        }
        binding.spinnerProduct.setItems(minAttribute);
        binding.spinnerProduct.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int i, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                productid = productModelList.get(i).getId();
                productname = productModelList.get(i).getName();
                GetAttribute(productModelList.get(i).getId());

            }
        });
        binding.spinnerProduct.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });
        productid = productModelList.get(0).getId();
        GetAttribute(productModelList.get(0).getId());
        /*binding.spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productid = productModelList.get(position).getId();
                productname = productModelList.get(position).getName();
                GetAttribute(productModelList.get(position).getId());
                //GetAttribute1(productModelList.get(position).getId());

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

   /* public void setUpSpinnerProduct(List<ProductModel> list) {
        productModelList.clear();
        ProductModel productModel = new ProductModel();
        productModel.setName("Product type");
        productModel.setId(-1);
        productModelList.add(productModel);
        productModelList.addAll(list);
        ProductAdapter adapter = new ProductAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, productModelList);
        binding.spinnerProduct.setAdapter(adapter);
        binding.spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                productid = productModelList.get(position).getId();
                productname = productModelList.get(position).getName();
                GetAttribute(productModelList.get(position).getId());
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }*/

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
                    Log.e("TAG", "GetAttribute==" + new Gson().toJson(response.body()));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        postToSellAttributeAdapter.addAllClass(response.body().data);
                        fillAttributs1(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        postToSellAttributeAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
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


    public void fillAttributs1(List<ProductAttributeModel> array) {
        attributeRequestModels.clear();
        for (ProductAttributeModel obj : array) {
            AttributeRequestModel objs = new AttributeRequestModel();
            objs.setAttribute(obj.getLabel());
            if (obj.getStateModelList().size() > 0) {
                objs.setFrom(obj.getStateModelList().get(0).getLabel());
                objs.setTo(obj.getStateModelList().get(0).getLabel());
            } else {
                objs.setFrom("");
                objs.setTo("");
            }
            attributeRequestModels.add(objs);
        }
    }
}