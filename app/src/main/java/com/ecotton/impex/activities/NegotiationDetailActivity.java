package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.MyApp;
import com.ecotton.impex.BuildConfig;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.NegotiationDetailAdapter;
import com.ecotton.impex.adapters.PostDetailAttributesAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityNegotiationDetailBinding;
import com.ecotton.impex.models.NegotiationList;
import com.ecotton.impex.models.PostDetail;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NegotiationDetailActivity extends AppCompatActivity {
    public ActivityNegotiationDetailBinding binding;
    private CustomDialog customDialog;
    public NegotiationDetailActivity mContext;
    private SessionUtil mSessionUtil;
    public NegotiationList negotiationObj;
    public String postID = "", type = "";
    public PostDetail postDetail;
    public NegotiationDetailAdapter negotiationAdapter;
    public String socketSlug = "";
    private static Socket mSocket;

    static {
        try {
            IO.Options options = new IO.Options();
            options.port = 3001;
            mSocket = IO.socket(BuildConfig.SOCKET_URI_NGOTIATION);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNegotiationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        negotiationObj = (NegotiationList) getIntent().getSerializableExtra("obj");
        postID = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        getPostDetail();
        binding.recyclerviewNegotiation.setLayoutManager(new LinearLayoutManager(mContext));
        negotiationAdapter = new NegotiationDetailAdapter(mContext);
        binding.recyclerviewNegotiation.setAdapter(negotiationAdapter);
        negotiationAdapter.addAllClass(negotiationObj.getPost_detail());
        negotiationAdapter.setOnItemClickListener(new NegotiationDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NegotiationList.PostDetail obj) {
                if (view.getId() == R.id.btn_view)
                    startActivity(new Intent(mContext, com.ecotton.impex.activities.PostDetailActivity.class)
                            .putExtra("screen", "negotiation")
                            .putExtra("post_id", postID)
                            .putExtra("seller_id", obj.getSeller_id() + "")
                            .putExtra("buyer_id", obj.getBuyer_id() + "")
                            .putExtra("posted_company_id", obj.getPosted_company_id() + "")
                            .putExtra("negotiation_by_company_id", obj.getNegotiation_by_company_id() + "")
                            .putExtra("post_type", obj.getNegotiation_type()));
                else if (obj.getNegotiation_by().equals(MyApp.mSessionUtil.getUsertype())) {
                    startActivity(new Intent(mContext, com.ecotton.impex.activities.NegotiationDetailLastTwoActivity.class)
                            .putExtra("screen", "negotiation")
                            .putExtra("post_id", postID)
                            .putExtra("seller_id", obj.getSeller_id() + "")
                            .putExtra("buyer_id", obj.getBuyer_id() + "")
                            .putExtra("posted_company_id", obj.getPosted_company_id() + "")
                            .putExtra("negotiation_by_company_id", obj.getNegotiation_by_company_id() + "")
                            .putExtra("post_type", obj.getNegotiation_type()));
                }

            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getPostDetail() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("post_notification_id", postID);
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("type", type);
            strJson = object.toString();

            PrintLog.d("TAG", strJson);

            Call<ResponseModel<List<PostDetail>>> call = APIClient.getInstance().getPostDetail(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<PostDetail>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<PostDetail>>> call, Response<ResponseModel<List<PostDetail>>> response) {
                    Log.e("response", "response==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        postDetail = response.body().data.get(0);
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
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNegotiationList() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            Log.e("TAG negotiationList", jsonObject.toString());
            Call<ResponseModel<List<NegotiationList>>> call;
            if (mSessionUtil.getUsertype().equals("seller"))
                call = APIClient.getInstance().negotiationList(mSessionUtil.getApiToken(), jsonObject.toString());
            else
                call = APIClient.getInstance().negotiationListBuyer(mSessionUtil.getApiToken(), jsonObject.toString());

            call.enqueue(new Callback<ResponseModel<List<NegotiationList>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<NegotiationList>>> call, Response<ResponseModel<List<NegotiationList>>> response) {
                    Log.e("response", "Negotiation==" + new Gson().toJson(response.body()));

                    customDialog.dismissProgress(mContext);
                    try {
                        if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {

                            if (response.body().data.size() > 0) {
                               for(NegotiationList obj:response.body().data)
                               {
                                   if(obj.getPost_id()==negotiationObj.getPost_id()){
                                       negotiationObj = obj;
                                       negotiationAdapter.addAllClass(obj.getPost_detail());

                                       break;
                                   }
                               }

                            }

                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(NegotiationDetailActivity.this);
                        } else {
                            AppUtil.showToast(mContext, response.body().message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<NegotiationList>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setData() {
        binding.txtCompanyName.setText(postDetail.getCompany_name());
        binding.txtUserName.setText(postDetail.getSeller_buyer_name());
        binding.txtPrice.setText("₹ " + postDetail.getPrice() + " " + "(" + postDetail.getNo_of_bales() + ")");
        binding.txtTitle.setText(postDetail.getProduct_name());
        binding.rvAttributs.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        binding.rvAttributs.setLayoutManager(layoutManager);
        PostDetailAttributesAdapter adapter = new PostDetailAttributesAdapter(this);
        binding.rvAttributs.setAdapter(adapter);
        adapter.addAllClass(postDetail.getAttribute_array());
    }


    @Override
    public void onResume() {
        getPostDetail();
        setSocket();
        getNegotiationList();
        super.onResume();
    }

    public void setSocket() {
        socketSlug = "negotiation_" + mSessionUtil.getUsertype() + "_" + mSessionUtil.getCompanyId() + "_" + mSessionUtil.getUserid();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(socketSlug, mcxEvent);
        mSocket.connect();
    }

    private Emitter.Listener mcxEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(args[0].toString());
                       // JSONObject value = jsonObject.getJSONObject("data").getJSONObject("negotiationSeller");
                        Log.d("TAG", jsonObject.toString());
                        updateData(new JSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //if(!mSocket.connected()) {
                    Log.d("TAG", "Connected");
                    // isConnected = true;
                    // }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("TAG", "disconnected");
                    //isConnected = false;
                    Toast.makeText(mContext,
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "Error connecting " + args[0].toString());
                    Toast.makeText(mContext,
                            "Error connecting", Toast.LENGTH_LONG).show();
                    mContext.finish();
                }
            });
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(socketSlug, mcxEvent);
        mSocket.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(socketSlug, mcxEvent);
        mSocket.disconnect();
    }

    public void updateData(JSONObject jsonObject) {
        try {
           /* if (negotiationObj.getPost_id() == jsonObject.getInt("post_notification_id")) {
                negotiationObj.setSeller_buyer_id(jsonObject.getString("seller_buyer_id"));
                negotiationObj.setBroker_name(jsonObject.getString("broker_name"));
                negotiationObj.setCount(jsonObject.getInt("negotiation_count"));
//                if (negotiationObj.getCount() > 1) {
//                    negotiationObj.setBest_price(jsonObject.getString("best_price"));
//                    negotiationObj.setBest_bales(jsonObject.getString("best_bales"));
//                    negotiationObj.setBest_name(jsonObject.getString("best_name"));
//                }
                for (int j = 0; j < negotiationAdapter.mArrayList.size(); j++) {
                    NegotiationList.PostDetail postDetail = negotiationAdapter.mArrayList.get(j);
                    if (postDetail.getNegotiation_id() == jsonObject.getInt("negotiation_id")) {
                        negotiationObj.getPost_detail().get(j).setBroker_name(jsonObject.getString("broker_name"));
                        negotiationObj.getPost_detail().get(j).setPrev_price(jsonObject.getInt("prev_price"));
                        negotiationObj.getPost_detail().get(j).setCurrent_price(jsonObject.getInt("new_price"));
                        negotiationObj.getPost_detail().get(j).setPrev_no_of_bales(jsonObject.getInt("prev_bales"));
                        negotiationObj.getPost_detail().get(j).setCurrent_no_of_bales(jsonObject.getInt("new_bales"));
                        negotiationObj.getPost_detail().get(j).setNegotiation_by(jsonObject.getString("negotiation_by"));

                        postDetail.setBroker_name(jsonObject.getString("broker_name"));
                        postDetail.setPrev_price(jsonObject.getInt("prev_price"));
                        postDetail.setCurrent_price(jsonObject.getInt("new_price"));
                        postDetail.setPrev_no_of_bales(jsonObject.getInt("prev_bales"));
                        postDetail.setCurrent_no_of_bales(jsonObject.getInt("new_bales"));
                        postDetail.setNegotiation_by(jsonObject.getString("negotiation_by"));

                        negotiationAdapter.notifyItemChanged(j);
                        break;
                    }

                }
            }*/
            getNegotiationList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}