package com.ecotton.impex.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.models.CountryModel;
import com.ecotton.impex.models.ProtModel;
import com.ecotton.impex.utils.DateTimeUtil;
import com.gne.www.lib.PinView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.PostDetailAttributesAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityPostDetailBinding;
import com.ecotton.impex.models.BrokerModel;
import com.ecotton.impex.models.MakeDeal;
import com.ecotton.impex.models.NegotiationDetail;
import com.ecotton.impex.models.PostDetail;
import com.ecotton.impex.models.PostDetailSpinerData;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    private PostDetailActivity mContext;
    private SessionUtil mSessionUtil;
    ActivityPostDetailBinding binding;
    private CustomDialog customDialog;
    public PostDetail postDetail;
    public PostDetailSpinerData detailSpinerData;
    public String selectedTransmitCondition = "", selectedTransmitConditionName = "", selectedPaymentCondition = "", selectedLab = "", selectedHeader = "", selectedBroker = "";
    public String postID = "", type = "";
    public int noOfBales = 0, totalBales = 0;
    public String buyerId = "", sellerId = "", postedCompanyId = "", negotiationByCompanyId = "";
    public String isFromScreen = "";
    public NegotiationDetail negotiationDetail;
    public List<BrokerModel> brokerModelList = new ArrayList<>();

    public String Usertype = "", company_id = "", user_id = "", deal_id = "",deliveryPeriod="";

    private List<CountryModel> dispatchcontryList = new ArrayList<>();
    private List<CountryModel> destinationcontryList = new ArrayList<>();
    private List<ProtModel> portList = new ArrayList<>();
    private List<ProtModel> destinationportList = new ArrayList<>();
    ArrayList<PostDetailSpinerData.SpinerModel> deliveryConditionList;
    private int dispatchContryID;
    private int dispatchdportID;
    private int destinationContryID;
    private int destinationPortID;

    public boolean
            is_highlight_broker_name = false,
            is_highlight_current_bales = false,
            is_highlight_current_price = false,
            is_highlight_header_name = false,
            is_highlight_lab = false,
            is_highlight_notes = false,
            is_highlight_payment_condition = false,
            is_highlight_transmit_condition = false,
            is_from_renegotiation = false,
            is_highlight_delivery_condition = false,
            is_highlight_country_dispatch = false,
            is_highlight_port_dispatch = false,
            is_highlight_country_destination = false,
            is_highlight_port_destination = false,
            is_highlight_delivery_period = false;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(this);
        isFromScreen = getIntent().getStringExtra("screen");
        if (TextUtils.isEmpty(isFromScreen))
            isFromScreen = "";
        postID = getIntent().getStringExtra("post_id");
        type = getIntent().getStringExtra("post_type");
        is_from_renegotiation = getIntent().getBooleanExtra("is_from_renegotiation", false);
        if (is_from_renegotiation)
            deal_id = getIntent().getStringExtra("deal_id");
        if (isFromNagotiation()) {
            sellerId = getIntent().getStringExtra("seller_id");
            buyerId = getIntent().getStringExtra("buyer_id");
            postedCompanyId = getIntent().getStringExtra("posted_company_id");
            negotiationByCompanyId = getIntent().getStringExtra("negotiation_by_company_id");
        }

        long daysMili = 1296000000;
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().
                setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), (MaterialDatePicker.todayInUtcMilliseconds() + daysMili))).build();

        binding.linDeliveryPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override public void onPositiveButtonClick(Pair<Long,Long> selection) {
                        Long startDate = selection.first;
                        Long endDate = selection.second;
                        is_highlight_delivery_period = true;
                        deliveryPeriod= DateTimeUtil.getDate(startDate,"yyyy-MM-dd")+"#"+DateTimeUtil.getDate(endDate,"yyyy-MM-dd");
                        Log.e("TAG","Start-Date-"+ DateTimeUtil.getDate(startDate,"yyyy-MM-dd"));
                        Log.e("TAG","End-Date-"+ DateTimeUtil.getDate(endDate,"yyyy-MM-dd"));
                        //Do something...
                        binding.edtStartDate.setText("TO - "+DateTimeUtil.getDate(startDate,"yyyy-MM-dd")+"   |   From - "+ DateTimeUtil.getDate(endDate,"yyyy-MM-dd"));
                    }
                });

            }
        });

        binding.scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        if (getIntent().getExtras() != null) {
            Bundle extra = getIntent().getExtras();
            for (String key : extra.keySet()) {
                if (key.equals("post_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    postID = extra.getString(key);
                }
                if (key.equals("post_type")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    type = extra.getString(key);
                }
                if (key.equals("screen")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    isFromScreen = extra.getString(key);
                }
                if (key.equals("seller_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    sellerId = extra.getString(key);
                }
                if (key.equals("buyer_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    buyerId = extra.getString(key);
                }
                if (key.equals("posted_company_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    postedCompanyId = extra.getString(key);
                }
                if (key.equals("negotiation_by_company_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    negotiationByCompanyId = extra.getString(key);
                }
                if (key.equals("company_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    company_id = extra.getString(key);
                }
                if (key.equals("user_type")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    Usertype = extra.getString(key);
                }
                if (key.equals("user_id")) {
                    Log.e("TAG", key + "-" + extra.getString(key));
                    user_id = extra.getString(key);
                }
            }
        }
        customDialog.displayProgress(mContext);
        if (!TextUtils.isEmpty(company_id)) {
            SelectUser();
        } else {
            getPostDetail();
        }

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnNegotiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitNagotiation();
            }
        });
        binding.btnMakeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.make_deal_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView txt_done = (TextView) dialog.findViewById(R.id.txt_done);
                TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
                TextView txt_company_name = (TextView) dialog.findViewById(R.id.txt_company_name);
                TextView txt_seller_name = (TextView) dialog.findViewById(R.id.txt_seller_name);
                TextView txt_broker_name = (TextView) dialog.findViewById(R.id.txt_broker_name);
                TextView txt_bales = (TextView) dialog.findViewById(R.id.txt_bales);
                TextView txt_price = (TextView) dialog.findViewById(R.id.txt_price);
                TextView txt_payment_condition = (TextView) dialog.findViewById(R.id.txt_payment_condition);
                TextView txt_header = (TextView) dialog.findViewById(R.id.txt_header);
                TextView txt_transmit_condition = (TextView) dialog.findViewById(R.id.txt_transmit_condition);
                TextView txt_lab_condition = (TextView) dialog.findViewById(R.id.txt_lab_condition);

                if (!negotiationDetail.getNegotiation_by_company_name().equals("null")) {
                    txt_company_name.setText(negotiationDetail.getNegotiation_by_company_name());
                }
                txt_seller_name.setText(negotiationDetail.getSeller_name());
                txt_company_name.setText(negotiationDetail.getNegotiation_by_company_name());
                txt_broker_name.setText(negotiationDetail.getBroker_name());
                txt_bales.setText(negotiationDetail.getCurrent_no_of_bales());
                txt_price.setText(negotiationDetail.getCurrent_price());
                txt_payment_condition.setText(negotiationDetail.getPayment_condition());
                txt_header.setText(negotiationDetail.getHeader_name());
                txt_transmit_condition.setText(negotiationDetail.getTransmit_condition());
                txt_lab_condition.setText(negotiationDetail.getLab());

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                txt_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hitMackDeal();
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

    }

    private void outputDateFormat() {
        SimpleDateFormat smDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        smDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    }


    private void SelectUser() {
        try {
            JSONObject object = new JSONObject();
            object.put("company_id", company_id);
            object.put("login_as", Usertype);
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().Select_company(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        String dataa = null;
                        try {
                            dataa = new String(response.body().bytes());
                            Log.e("response", "response==" + dataa);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        LoginModel model = gson.fromJson(dataa, LoginModel.class);
                        if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(SessionUtil.API_TOKEN, mSessionUtil.getApiToken());
                            map.put(SessionUtil.MOBILE_NO, mSessionUtil.getMobileNo());
                            map.put(SessionUtil.PASS, mSessionUtil.getPass());
                            map.put(SessionUtil.COMPANY_NAME, model.getData().getCompany_name());
                            map.put(SessionUtil.USER_TYPE, model.getData().getUser_type());
                            map.put(SessionUtil.USERID, model.getData().getUserId());
                            map.put(SessionUtil.COMPANY_ID, model.getData().getCompany_id());
                            mSessionUtil.setData(map);
                            Log.e("TAG postID", "postID-" + postID);
                            Log.e("TAG type", "type-" + type);
                            getPostDetail();

                        } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                            AppUtil.showToast(mContext, model.getMessage());
                            startActivity(new Intent(mContext, WitingApprovelActivity.class));
                            finish();
                        } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, model.getMessage());
                            AppUtil.autoLogout(PostDetailActivity.this);
                        } else {
                            AppUtil.showToast(mContext, model.getMessage());
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isFromHome() {
        return isFromScreen.equals("home");
    }

    private boolean isFromNagotiation() {
        return isFromScreen.equals("negotiation");
    }


    private void getPostDetail() {
        try {

            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("post_notification_id", postID);
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("type", type);
            strJson = object.toString();

            Log.e("TAG PostDetail", strJson);


            Call<ResponseModel<List<PostDetail>>> call = APIClient.getInstance().getPostDetail(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<PostDetail>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<PostDetail>>> call, Response<ResponseModel<List<PostDetail>>> response) {
                    Log.e("response", "postDetail==" + new Gson().toJson(response.body()));

                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        postDetail = response.body().data.get(0);
                        if (isFromHome()) {
                            postedCompanyId = postDetail.getCompany_id() + "";
                            negotiationByCompanyId = mSessionUtil.getCompanyId() + "";
                            if (mSessionUtil.getUsertype().equals("seller")) {
                                sellerId = mSessionUtil.getUserid();
                                buyerId = postDetail.getSeller_buyer_id();
                            } else {
                                sellerId = postDetail.getSeller_buyer_id();
                                buyerId = mSessionUtil.getUserid();
                            }
                        }
                        setData();

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<PostDetail>>> call, Throwable t) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setData() {

        getSpinerData();
        binding.txtCountryOfOrgin.setText(postDetail.getCountry_origin_name());
        binding.txtCompanyName.setText(postDetail.getCompany_name());
        binding.txtUserName.setText(postDetail.getSeller_buyer_name());
        binding.txtPrice.setText("₹ " + postDetail.getPrice() + "(" + postDetail.getNo_of_bales() + ")");
        binding.txtTitle.setText(postDetail.getProduct_name());
        binding.txtQty.setText(postDetail.getNo_of_bales());
        binding.edtPrice.setText("" + postDetail.getPrice());
        binding.rvAttributs.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        binding.rvAttributs.setLayoutManager(layoutManager);
        PostDetailAttributesAdapter adapter = new PostDetailAttributesAdapter(this);
        binding.rvAttributs.setAdapter(adapter);
        adapter.addAllClass(postDetail.getAttribute_array());

        noOfBales = Integer.parseInt(postDetail.getNo_of_bales());
        binding.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noOfBales >= totalBales) {
                    noOfBales = totalBales;
                } else {
                    noOfBales++;

                }
                is_highlight_current_bales = true;
                binding.txtQty.setText(noOfBales + "");

            }
        });

        binding.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOfBales <= 1) {
                    noOfBales = 1;
                } else {
                    noOfBales--;
                }
                is_highlight_current_bales = true;
                binding.txtQty.setText(noOfBales + "");
            }
        });
    }

    private void getSpinerData() {
        try {

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

                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        detailSpinerData = response.body().data.get(0);
                        CountryList();
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

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setSpinerData() {

        deliveryConditionList = new ArrayList<>();
        PostDetailSpinerData.SpinerModel obj = new PostDetailSpinerData.SpinerModel();
        obj.setName("Select");
        deliveryConditionList.add(obj);
        deliveryConditionList.addAll(detailSpinerData.getTransmit_condition());

        CustomAdapter adapterTransmit = new CustomAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, deliveryConditionList);
        binding.spTransmitCondition.setAdapter(adapterTransmit);


        CustomAdapter adapterPaymentCondition = new CustomAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, detailSpinerData.getPayment_condition());
        binding.spPaymentCondition.setAdapter(adapterPaymentCondition);

        CustomAdapter adapterLab = new CustomAdapter(mContext, R.layout.layout_spiner, R.id.txt_company_name, detailSpinerData.getLab_list());
        binding.spLab.setAdapter(adapterLab);

    }

    private void CountryList() {
        Log.e("StateModel", "StateModel==");

        Call<ResponseModel<List<CountryModel>>> call = APIClient.getInstance().country_list(mSessionUtil.getApiToken());
        Log.e("StateModel", "StateModel==" + call);
        Log.e("getApiToken", "getApiToken==" + mSessionUtil.getApiToken());
        call.enqueue(new Callback<ResponseModel<List<CountryModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CountryModel>>> call, Response<ResponseModel<List<CountryModel>>> response) {
                Log.e("StateModel", "StateModel==" + new Gson().toJson(response.body().data));

                if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                    setUpSpinercountrydispatch(response.body().data);
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

            }
        });
    }

    public void setUpSpinercountrydispatch(List<CountryModel> list) {
        dispatchcontryList.clear();
        CountryModel obj = new CountryModel();
        obj.setName("Select");
        dispatchcontryList.add(obj);
        dispatchcontryList.addAll(list);
        CountryDispatchAdapter adapter = new CountryDispatchAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, dispatchcontryList);
        binding.spinnerCountryOfDispatch.setAdapter(adapter);
        binding.spinnerCountryOfDispatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    dispatchContryID = 0;
                } else {
                    dispatchContryID = dispatchcontryList.get(position).getId();
                    Log.e("selectedStation", "selectedStation==" + dispatchContryID);
                    PortList(dispatchContryID);
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setUpSpinercountrydestination(list);
    }

    public void setUpSpinercountrydestination(List<CountryModel> list) {
        destinationcontryList.clear();
        CountryModel obj = new CountryModel();
        obj.setName("Select");
        destinationcontryList.add(obj);
        destinationcontryList.addAll(list);
        CountryDispatchAdapter adapter = new CountryDispatchAdapter(mContext, R.layout.spinner_layout, R.id.txt_company_name, destinationcontryList);
        binding.spinnerDestinationCountry.setAdapter(adapter);
        binding.spinnerDestinationCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    destinationContryID = 0;
                } else {
                    destinationContryID = destinationcontryList.get(position).getId();
                    Log.e("selectedStation", "selectedStation==" + dispatchContryID);
                    DestinationPortList(destinationContryID);
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.layoutDestinationCountry.setVisibility(View.GONE);
        binding.layoutDestinationPort.setVisibility(View.GONE);
        setSpinerData();
        getNegotiationDetail();
    }

    private void PortList(int countryid) {
        try {
            portList.clear();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("country_id", countryid);
            String data = jsonObject.toString();
            Call<ResponseModel<List<ProtModel>>> call = APIClient.getInstance().port_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProtModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProtModel>>> call, Response<ResponseModel<List<ProtModel>>> response) {
                    Log.e("ProtModel", "ProtModel==" + new Gson().toJson(response.body().data));

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
                dispatchdportID = portList.get(position).getId();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void DestinationPortList(int countryid) {
        try {
            destinationportList.clear();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("country_id", countryid);
            String data = jsonObject.toString();
            Call<ResponseModel<List<ProtModel>>> call = APIClient.getInstance().port_list(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<ProtModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<ProtModel>>> call, Response<ResponseModel<List<ProtModel>>> response) {
                    Log.e("ProtModel", "ProtModel==" + new Gson().toJson(response.body().data));

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
                destinationPortID = destinationportList.get(position).getId();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getNegotiationDetail() {
        try {

            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("seller_id", sellerId);
            object.put("buyer_id", buyerId);
            object.put("post_notification_id", postID);
            object.put("type", type);
            object.put("negotiation_by_company_id", negotiationByCompanyId);
            object.put("posted_company_id", postedCompanyId);
            object.put("is_from_renegotiation", is_from_renegotiation);
            if (is_from_renegotiation)
                object.put("deal_id", deal_id);

            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel<NegotiationDetail>> call = APIClient.getInstance().negotiationDetail(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<NegotiationDetail>>() {
                @Override
                public void onResponse(Call<ResponseModel<NegotiationDetail>> call, Response<ResponseModel<NegotiationDetail>> response) {
                    Log.e("response", "NegotiationDetail==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        negotiationDetail = response.body().data;
                        if (mSessionUtil.getUsertype().equals("seller")) {
                            if (!response.body().data.getSeller_id().equals("") && response.body().data.getNegotiation_by().equals("seller")) {
                                alertDiloags();
                            } else {
                                setNegotiationData();
                            }
                        } else {
                            if (!response.body().data.getBuyer_id().equals("") && response.body().data.getNegotiation_by().equals("buyer")) {
                                alertDiloags();
                            } else {
                                setNegotiationData();
                            }
                        }
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<NegotiationDetail>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("response onFailure", "NegotiationDetail==" + t.getMessage());

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setNegotiationData() {
        binding.edtPrice.setText(negotiationDetail.getCurrent_price());
        binding.edtNotes.setText(negotiationDetail.getNotes());

        if (mSessionUtil.getUsertype().equals("seller")) {
            if (negotiationDetail.getSeller_id().equals("")) {
                noOfBales = Integer.parseInt(negotiationDetail.getRemain_bales());
            } else {
                noOfBales = Integer.parseInt(negotiationDetail.getCurrent_no_of_bales());
            }
        } else {
            if (negotiationDetail.getBuyer_id().equals("")) {
                noOfBales = Integer.parseInt(negotiationDetail.getRemain_bales());
            } else {
                noOfBales = Integer.parseInt(negotiationDetail.getCurrent_no_of_bales());
            }
        }
        totalBales = noOfBales;
        binding.txtQty.setText(noOfBales + "");
        binding.txtPrice.setText("₹ " + postDetail.getPrice() + "(" + noOfBales + ")");

        //noOfBales = Integer.parseInt(negotiationDetail.getCurrent_no_of_bales());

        for (int i = 0; i < detailSpinerData.getTransmit_condition().size(); i++) {
            if (negotiationDetail.getTransmit_condition_id().equals(detailSpinerData.getTransmit_condition().get(i).getId() + "")) {
                binding.spTransmitCondition.setSelection(i);
                selectedTransmitCondition = detailSpinerData.getTransmit_condition().get(i).getId() + "";
                selectedTransmitConditionName = detailSpinerData.getTransmit_condition().get(i).getName();
            }
        }

        for (int i = 0; i < detailSpinerData.getPayment_condition().size(); i++) {
            if (negotiationDetail.getPayment_condition_id().equals(detailSpinerData.getPayment_condition().get(i).getId() + "")) {
                binding.spPaymentCondition.setSelection(i);
                selectedPaymentCondition = detailSpinerData.getPayment_condition().get(i).getId() + "";
            }
        }

        for (int i = 0; i < detailSpinerData.getLab_list().size(); i++) {
            if (negotiationDetail.getLab_id().equals(detailSpinerData.getLab_list().get(i).getId() + "")) {
                binding.spLab.setSelection(i);
                selectedLab = detailSpinerData.getLab_list().get(i).getId() + "";
            }
        }


        for (int i = 0; i < brokerModelList.size(); i++) {
            if (negotiationDetail.getBroker_id().equals(brokerModelList.get(i).getId() + "")) {
                binding.spBroker.setSelection(i);
                selectedBroker = brokerModelList.get(i).getId() + "";
            }
        }

       /* AppCompatTextView txtTransmitCondition, txtPaymentCondition, txtLab, txtHeader, txtBroker;
        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
        Typeface typefaceTransmitCondition = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
        Typeface typefacePaymentCondition = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
        Typeface typefaceLab = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
        Typeface typefaceHeader = ResourcesCompat.getFont(mContext, R.font.poppins_bold);
        txtBroker =  binding.spBroker.findViewById(R.id.txt_company_name);
        txtBroker.setTypeface(typeface);
        txtLab =binding.spLab.findViewById(R.id.txt_company_name);
        txtLab.setTypeface(typefaceLab);
        txtHeader =  binding.spHeader.findViewById(R.id.txt_company_name);
        txtHeader.setTypeface(typefaceHeader);
        txtTransmitCondition =  binding.spTransmitCondition.findViewById(R.id.txt_company_name);
        txtTransmitCondition.setTypeface(typefaceTransmitCondition);
        txtPaymentCondition =  binding.spPaymentCondition.findViewById(R.id.txt_company_name);
        txtPaymentCondition.setTypeface(typefacePaymentCondition);*/

        if (TextUtils.isEmpty(negotiationDetail.getTransmit_condition_id()) && detailSpinerData.getTransmit_condition().size() > 0) {
            selectedTransmitCondition = detailSpinerData.getTransmit_condition().get(0).getId() + "";
            selectedTransmitConditionName = detailSpinerData.getTransmit_condition().get(0).getName();
        }
        if (TextUtils.isEmpty(negotiationDetail.getPayment_condition_id()) && detailSpinerData.getPayment_condition().size() > 0)
            selectedPaymentCondition = detailSpinerData.getPayment_condition().get(0).getId() + "";

        if (TextUtils.isEmpty(negotiationDetail.getLab_id()) && detailSpinerData.getLab_list().size() > 0)
            selectedLab = detailSpinerData.getLab_list().get(0).getId() + "";

        if (TextUtils.isEmpty(negotiationDetail.getHeader()) && detailSpinerData.getHeader().size() > 0)
            selectedHeader = detailSpinerData.getHeader().get(0).getId() + "";

        if (TextUtils.isEmpty(negotiationDetail.getBroker_id()) && brokerModelList.size() > 0)
            selectedBroker = brokerModelList.get(0).getId() + "";


        setSpinerClick();
    }

    public void setSpinerClick() {
        binding.spTransmitCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTransmitCondition = detailSpinerData.getTransmit_condition().get(position).getId() + "";
                selectedTransmitConditionName = detailSpinerData.getTransmit_condition().get(position).getName();
                if (deliveryConditionList.get(position).getName().equals("FOB")) {
                    binding.layoutDestinationCountry.setVisibility(View.GONE);
                    binding.layoutDestinationPort.setVisibility(View.GONE);
                    //destinationContryID = 0;
                    //destinationPortID = 0;
                } else {
                    binding.layoutDestinationCountry.setVisibility(View.VISIBLE);
                    binding.layoutDestinationPort.setVisibility(View.VISIBLE);
                }
                is_highlight_delivery_condition = true;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.spPaymentCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentCondition = detailSpinerData.getPayment_condition().get(position).getId() + "";
                is_highlight_payment_condition = true;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.spLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLab = detailSpinerData.getLab_list().get(position).getId() + "";
                is_highlight_lab = true;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.edtNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                is_highlight_notes = true;
            }
        });
        binding.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                is_highlight_current_price = true;
            }
        });

        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.poppins_regular);
        Typeface typefaceBold = ResourcesCompat.getFont(mContext, R.font.poppins_bold);

        AppCompatTextView textView;

        if (negotiationDetail.getIs_highlight_payment_condition().equalsIgnoreCase("false") || TextUtils.isEmpty(negotiationDetail.getIs_highlight_payment_condition())) {
            textView = (AppCompatTextView) binding.spPaymentCondition.findViewById(R.id.txt_company_name);
            textView.setTypeface(typeface);
        }
        if (negotiationDetail.getIs_highlight_delivery_condition().equalsIgnoreCase("false") || TextUtils.isEmpty(negotiationDetail.getIs_highlight_transmit_condition())) {
            textView = (AppCompatTextView) binding.spTransmitCondition.findViewById(R.id.txt_company_name);
            textView.setTypeface(typeface);
        }
        if (negotiationDetail.getIs_highlight_lab().equalsIgnoreCase("false") || TextUtils.isEmpty(negotiationDetail.getIs_highlight_lab())) {
            textView = (AppCompatTextView) binding.spLab.findViewById(R.id.txt_company_name);
            textView.setTypeface(typeface);
        }

        if (negotiationDetail.getIs_highlight_notes().equals("true")) {
            binding.edtNotes.setTypeface(typefaceBold);
        }

        if (negotiationDetail.getIs_highlight_current_bales().equals("true")) {
            binding.txtQty.setTypeface(typefaceBold);
        }
        if (negotiationDetail.getIs_highlight_current_price().equals("true")) {
            binding.edtPrice.setTypeface(typefaceBold);
        }
    }

    public void alertDiloags() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Waiting for response")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onBackPressed();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("AlertDialogExample");
        alert.show();
    }

    private void hitNagotiation() {
        try {
            int price = Integer.parseInt(binding.edtPrice.getText().toString());
            if (TextUtils.isEmpty(binding.edtPrice.getText().toString()) || price == 0) {
                AppUtil.showToast(mContext, "Please enter valid price");
                return;
            }
            if (TextUtils.isEmpty(deliveryPeriod) ) {
                AppUtil.showToast(mContext, "Please select delivery period");
                return;
            }
        } catch (Exception e) {
            e.getMessage();
        }


        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("seller_id", sellerId);
            object.put("buyer_id", buyerId);
            object.put("posted_company_id", postedCompanyId);
            object.put("negotiation_by_company_id", negotiationByCompanyId);
            object.put("post_notification_id", postDetail.getId());
            object.put("negotiation_type", type);
            object.put("negotiation_by", mSessionUtil.getUsertype());

            object.put("price", binding.edtPrice.getText().toString());
            object.put("no_of_bales", noOfBales);
            object.put("payment_condition", selectedPaymentCondition);
            object.put("transmit_condition", selectedTransmitCondition);
            object.put("lab", selectedLab);
            object.put("broker_id", selectedBroker);
            object.put("header", selectedHeader);
            object.put("notes", binding.edtNotes.getText().toString());


            object.put("is_highlight_current_bales", is_highlight_current_bales);
            object.put("is_highlight_current_price", is_highlight_current_price);
            object.put("is_highlight_lab", is_highlight_lab);
            object.put("is_highlight_notes", is_highlight_notes);
            object.put("is_highlight_payment_condition", is_highlight_payment_condition);

            object.put("is_highlight_delivery_condition", is_highlight_delivery_condition);
            object.put("is_highlight_country_dispatch", is_highlight_country_dispatch);
            object.put("is_highlight_port_dispatch", is_highlight_port_dispatch);
            object.put("is_highlight_country_destination", is_highlight_country_destination);
            object.put("is_highlight_port_destination", is_highlight_port_destination);
            object.put("is_highlight_delivery_period", is_highlight_delivery_period);

            object.put("delivery_condition_id", selectedTransmitCondition);
            object.put("country_dispatch_id", dispatchContryID + "");
            object.put("port_dispatch_id", dispatchdportID + "");

            if (selectedTransmitConditionName.equals("FOB")) {
                object.put("country_destination_id", "");
                object.put("port_destination_id", "");
            } else {
                object.put("country_destination_id", destinationContryID);
                object.put("port_destination_id", destinationPortID);
            }
            object.put("delivery_period", deliveryPeriod);


            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel> call = APIClient.getInstance().addNegotiation(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        Log.e("addNegotiation", "addNegotiation==" + response.body().message);
                        AppUtil.showToast(mContext, response.body().message);
                        onBackPressed();
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
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("response", "response==" + t.getMessage());
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hitMackDeal() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("seller_id", sellerId);
            object.put("buyer_id", buyerId);
            object.put("no_of_bales", negotiationDetail.getCurrent_no_of_bales());
            object.put("type", type);
            object.put("done_by", mSessionUtil.getUsertype());
            object.put("posted_company_id", postedCompanyId);
            object.put("negotiation_by_company_id", negotiationByCompanyId);
            object.put("post_notification_id", postDetail.getId());

            strJson = object.toString();

            Log.e("TAG", "TAG===" + strJson);

            Call<ResponseModel<MakeDeal>> call = APIClient.getInstance().makeDeal(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<MakeDeal>>() {
                @Override
                public void onResponse(Call<ResponseModel<MakeDeal>> call, Response<ResponseModel<MakeDeal>> response) {
                    Log.e("response", "hitMackDeal==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.deal_otp_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        Button btn_verify = (Button) dialog.findViewById(R.id.btn_verify);
                        Button btn_resend = (Button) dialog.findViewById(R.id.btn_resend);
                        PinView pinView = (PinView) dialog.findViewById(R.id.pinview);
                        TextView txt_mobile_no = (TextView) dialog.findViewById(R.id.txt_mobile_no);
                        String number = "<b>" + mSessionUtil.getMobileNo() + "</b> ";
                        txt_mobile_no.setText(Html.fromHtml("We have sent verification \ncode on your number  " + number));
                        btn_resend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                ResendOtp(response.body().data);
                            }
                        });

                        btn_verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pin = pinView.getText().toString();
                                if (pin.length() != 6) {
                                    Toast.makeText(mContext, "Please Enter 6 digit Pin", Toast.LENGTH_LONG).show();
                                } else {
                                    hitMackDealVerify(response.body().data, pin);
                                }
                            }
                        });

                        dialog.show();
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        customDialog.dismissProgress(mContext);
                        AppUtil.showToast(mContext, response.body().message);
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        customDialog.dismissProgress(mContext);
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        customDialog.dismissProgress(mContext);
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<MakeDeal>> call, Throwable t) {
                    Log.e("response", "response==" + t.getMessage());
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ResendOtp(MakeDeal makeDeal) {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("deal_id", makeDeal.getDeal_id());
            strJson = object.toString();
            PrintLog.e("TAG", strJson);

            Call<ResponseModel> call = APIClient.getInstance().resend_deal_otp(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Log.e("ResendOtp", "ResendOtp==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.deal_otp_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        Button btn_verify = (Button) dialog.findViewById(R.id.btn_verify);
                        Button btn_resend = (Button) dialog.findViewById(R.id.btn_resend);
                        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);


                        PinView pinView = (PinView) dialog.findViewById(R.id.pinview);
                        TextView txt_mobile_no = (TextView) dialog.findViewById(R.id.txt_mobile_no);
                        String number = "<b>" + mSessionUtil.getMobileNo() + "</b> ";
                        txt_mobile_no.setText(Html.fromHtml("We have sent verification \ncode on your number  " + number));
                        btn_resend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                ResendOtp(makeDeal);
                            }
                        });
                        btn_verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pin = pinView.getText().toString();
                                if (pin.length() != 6) {
                                    Toast.makeText(mContext, "Please Enter 6 digit Pin", Toast.LENGTH_LONG).show();
                                } else {
                                    hitMackDealVerify(makeDeal, pin);
                                }
                            }
                        });

                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });
                        dialog.show();
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
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("response", "response==" + t.getMessage());
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hitMackDealVerify(MakeDeal makeDeal, String otp) {
        try {

            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("deal_id", makeDeal.getDeal_id());
            object.put("email_otp", makeDeal.getEmail_otp());
            object.put("mobile_otp", otp);

            strJson = object.toString();

            Log.e("strJson", "strJson==" + strJson);

            Call<ResponseModel> call = APIClient.getInstance().makeDealOtpVerify(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Log.e("response", "hitMackDeal==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        Log.e("response", "hitMackDeal==" + response.body().message);
                        dialog.dismiss();
                        onBackPressed();
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
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("response", "response==" + t.getMessage());
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public class BrokerCustomAdapter extends ArrayAdapter<BrokerModel> {

        LayoutInflater flater;

        public BrokerCustomAdapter(Activity context, int resouceId, int textviewId, List<BrokerModel> list) {

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

            BrokerModel rowItem = getItem(position);

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

}