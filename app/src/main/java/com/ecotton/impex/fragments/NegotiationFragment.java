package com.ecotton.impex.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.BuildConfig;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.DealsActivity;
import com.ecotton.impex.activities.NegotiationDetailActivity;
import com.ecotton.impex.activities.NegotiationDetailLastTwoActivity;
import com.ecotton.impex.activities.PostDetailActivity;
import com.ecotton.impex.adapters.NegotiationAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.models.NegotiationList;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
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


public class NegotiationFragment extends Fragment {
    private Context mContext;
    private NegotiationAdapter negotiationAdapter;
    private CustomDialog customDialog;
    private SessionUtil mSessionUtil;
    RecyclerView recyclerview_negotiation;
    public String socketSlug = "";
    private static Socket mSocket;
    private Activity mActivity;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (DealsActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_negotiation, container, false);

        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();


        recyclerview_negotiation = view.findViewById(R.id.recyclerview_negotiation);
        recyclerview_negotiation.setLayoutManager(new LinearLayoutManager(getActivity()));
        negotiationAdapter = new NegotiationAdapter(getActivity());

        recyclerview_negotiation.setAdapter(negotiationAdapter);
        negotiationAdapter.setOnItemClickListener(new NegotiationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NegotiationList obj) {
                if (R.id.btn_view == view.getId()) {
                    if (obj.getNegotiation_type().equals("post")) {
                        startActivity(new Intent(mContext, PostDetailActivity.class)
                                .putExtra("screen", "negotiation")
                                .putExtra("post_id", obj.getPost_id() + "")
                                .putExtra("seller_id", obj.getPost_detail().get(0).getSeller_id() + "")
                                .putExtra("buyer_id", obj.getPost_detail().get(0).getBuyer_id() + "")
                                .putExtra("posted_company_id", obj.getPost_detail().get(0).getPosted_company_id() + "")
                                .putExtra("negotiation_by_company_id", obj.getPost_detail().get(0).getNegotiation_by_company_id() + "")
                                .putExtra("post_type", obj.getNegotiation_type()));
                    } else {
                        startActivity(new Intent(mContext, PostDetailActivity.class)
                                .putExtra("screen", "negotiation")
                                .putExtra("post_id", obj.getNotification_id() + "")
                                .putExtra("seller_id", obj.getNotification_detail().get(0).getSeller_id() + "")
                                .putExtra("buyer_id", obj.getNotification_detail().get(0).getBuyer_id() + "")
                                .putExtra("posted_company_id", obj.getNotification_detail().get(0).getPosted_company_id() + "")
                                .putExtra("negotiation_by_company_id", obj.getNotification_detail().get(0).getNegotiation_by_company_id() + "")
                                .putExtra("post_type", obj.getNegotiation_type()));
                    }
                } else {


                        if (obj.getNegotiation_type().equals("post")) {
                            if (obj.getCount() <= 1) {
                          //  if (obj.getPost_detail().get(0).getNegotiation_by().equals(App.mSessionUtil.getUsertype())) {
                                startActivity(new Intent(mContext, NegotiationDetailLastTwoActivity.class)
                                        .putExtra("screen", "negotiation")
                                        .putExtra("post_id", obj.getPost_id() + "")
                                        .putExtra("seller_id", obj.getPost_detail().get(0).getSeller_id() + "")
                                        .putExtra("buyer_id", obj.getPost_detail().get(0).getBuyer_id() + "")
                                        .putExtra("posted_company_id", obj.getPost_detail().get(0).getPosted_company_id() + "")
                                        .putExtra("negotiation_by_company_id", obj.getPost_detail().get(0).getNegotiation_by_company_id() + "")
                                        .putExtra("post_type", obj.getNegotiation_type()));
                         //   }
                        } else {
                              //  if (obj.getPost_detail().get(0).getNegotiation_by().equals(App.mSessionUtil.getUsertype())) {
                                    Intent intent = new Intent(mContext, NegotiationDetailActivity.class)
                                            .putExtra("id", obj.getPost_id() + "")
                                            .putExtra("obj", obj)
                                            .putExtra("type", obj.getNegotiation_type());
                                    startActivity(intent);
                                //}
                        }
                    }
                   /* if (obj.getCount() > 1) {
                        if (obj.getNegotiation_type().equals("post")) {
                            if (obj.getPost_detail().get(0).getNegotiation_by().equals(App.mSessionUtil.getUsertype())) {
                                Intent intent = new Intent(mContext, NegotiationDetailActivity.class)
                                        .putExtra("id", obj.getPost_id() + "")
                                        .putExtra("obj", obj)
                                        .putExtra("type", obj.getNegotiation_type());
                                startActivity(intent);
                            }
                        } else {
                            if (obj.getPost_detail().get(0).getNegotiation_by().equals(App.mSessionUtil.getUsertype())) {
                                Intent intent = new Intent(mContext, NegotiationDetailActivity.class)
                                        .putExtra("id", obj.getNotification_id() + "")
                                        .putExtra("obj", obj)
                                        .putExtra("type", obj.getNegotiation_type());
                                startActivity(intent);
                            }
                        }
                    }*/
                }
            }
        });

        return view;
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
                                negotiationAdapter.addAllClass(response.body().data);
                            }

                        } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, response.body().message);
                            AppUtil.autoLogout(getActivity());
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

    @Override
    public void onResume() {
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
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(args[0].toString());
                        JSONObject value;
                        if (mSessionUtil.getUsertype().equals("seller"))
                            value = jsonObject.getJSONObject("data").getJSONObject("negotiationSeller");
                        else
                            value = jsonObject.getJSONObject("data").getJSONObject("negotiationBuyer");

                        Log.d("TAG", jsonObject.toString());
                        updateData(value);
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
            mActivity.runOnUiThread(new Runnable() {
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
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("TAG", "disconnected");
                    //isConnected = false;
                    Toast.makeText(getActivity(),
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "Error connecting " + args[0].toString());
                    Toast.makeText(getActivity(),
                            "Error connecting", Toast.LENGTH_LONG).show();
                    getActivity().finish();
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
        boolean isData = false;

        for (int i = 0; i < negotiationAdapter.mArrayList.size(); i++) {
            try {
                NegotiationList obj = negotiationAdapter.mArrayList.get(i);
                if (obj.getPost_id() == jsonObject.getInt("post_notification_id")) {
                    isData = true;
                    obj.setSeller_buyer_id(jsonObject.getString("seller_buyer_id"));
                    obj.setBroker_name(jsonObject.getString("broker_name"));
                    obj.setCount(jsonObject.getInt("negotiation_count"));
                    if (obj.getCount() > 1) {
                        obj.setBest_price(jsonObject.getString("best_price"));
                        obj.setBest_bales(jsonObject.getString("best_bales"));
                        obj.setBest_name(jsonObject.getString("best_name"));
                    }
                    for (int j = 0; j < obj.getPost_detail().size(); j++) {
                        NegotiationList.PostDetail postDetail = obj.getPost_detail().get(j);
                        if (postDetail.getNegotiation_id() == jsonObject.getInt("negotiation_id")) {
                            obj.getPost_detail().get(j).setBroker_name(jsonObject.getString("broker_name"));
                            obj.getPost_detail().get(j).setPrev_price(jsonObject.getInt("prev_price"));
                            obj.getPost_detail().get(j).setCurrent_price(jsonObject.getInt("new_price"));
                            obj.getPost_detail().get(j).setPrev_no_of_bales(jsonObject.getInt("prev_bales"));
                            obj.getPost_detail().get(j).setCurrent_no_of_bales(jsonObject.getInt("new_bales"));
                            obj.getPost_detail().get(j).setNegotiation_by(jsonObject.getString("negotiation_by"));
                        }
                    }
                    negotiationAdapter.notifyItemChanged(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
       // if (!isData)
            getNegotiationList();

    }
}