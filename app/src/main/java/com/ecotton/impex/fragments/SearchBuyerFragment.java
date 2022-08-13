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

    private List<CountryModel> stateModelList = new ArrayList<>();
    private List<CountryModel> dispatchcontryList = new ArrayList<>();
    private List<CountryModel> destinationcontryList = new ArrayList<>();
    private List<ProtModel> portList = new ArrayList<>();
    private List<ProtModel> destinationportList = new ArrayList<>();

    private int productid;
    private String productname;

    public PostDetailSpinerData detailSpinerData;
    public String selectedTransmitCondition = "";


    public int is_destination;
    public int delivery_condition_id;
    private int dispatchcontryid;
    private int selectedport;

    private int destinationcontryid;
    private int selecteddestinationport;

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
                        intent.putExtra("delivery_condition_id", delivery_condition_id);
                        intent.putExtra("dispatchcontryid", dispatchcontryid);
                        intent.putExtra("selectedport", selectedport);
                        intent.putExtra("destinationcontryid", destinationcontryid);
                        intent.putExtra("selecteddestinationport", selecteddestinationport);

                        startActivity(intent);
                    }
                } catch (Exception e) {
                }
            }
        });
        getSpinerData();
        CountryList();

        return binding.getRoot();
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
                    setUpSpinercountrydispatch(response.body().data);
                    setUpSpinercountrydestination(response.body().data);
                } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                    AppUtil.showToast(mContext, response.body().message);
                    AppUtil.autoLogout(getActivity());
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
                        AppUtil.autoLogout(getActivity());
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
                        AppUtil.autoLogout(getActivity());
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
                        setSpinerData();
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

    public void setSpinerData() {

        CustomAdapter adapterTransmit = new CustomAdapter(getActivity(), R.layout.spinner_layout, R.id.txt_company_name, detailSpinerData.getTransmit_condition());
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
                productname = productModelList.get(position).getName();
                GetAttribute(productModelList.get(position).getId());
                //GetAttribute1(productModelList.get(position).getId());

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
                        // fillAttributs1(response.body().data);

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