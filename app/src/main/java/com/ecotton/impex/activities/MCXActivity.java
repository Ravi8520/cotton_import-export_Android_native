package com.ecotton.impex.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.BuildConfig;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.MCXAdapter;
import com.ecotton.impex.models.MCXModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MCXActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView backarrow;
    ArrayList<MCXModel> mcxdata;
    private static final String SOCKET_PATH = "/socket.io";
    private static final String[] TRANSPORTS = {
            "websocket"};
    private static Socket mSocket;
    MCXAdapter adapter;

    static {
        try {
            //mSocket = IO.socket("https://socket-io-chat.now.sh/");
            IO.Options options = new IO.Options();
            options.port = 3000;
            mSocket = IO.socket(BuildConfig.SOCKET_URI);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcxactivity);
        recyclerView = findViewById(R.id.mcxrecyclerview);
        backarrow = findViewById(R.id.backarrow);
        mcxdata = new ArrayList<>();

        setMcxInfo();
        setMcxAdapter();
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on("McxEvent", mcxEvent);
        mSocket.connect();
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private Emitter.Listener mcxEvent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(args[0].toString());
                        JSONArray value = jsonObject.getJSONObject("data").getJSONObject("Mcx").getJSONArray("parameters");
                       // if (mcxdata.size() == 0) {
                        mcxdata.clear();
                            for (int i = 0; i < value.length(); i++) {
                                JSONObject jobj = value.getJSONObject(i);
                                MCXModel objs = new MCXModel();
                                objs.setClose_price(jobj.getInt("close_price"));
                                objs.setCurrent_price(jobj.getInt("current_price"));
                                objs.setHigh_price(jobj.getInt("high_price"));
                                objs.setLow_price(jobj.getInt("low_price"));
                                objs.setOpen_price(jobj.getInt("open_price"));
                                objs.setName(jobj.getString("name"));
                                mcxdata.add(objs);
                            }
                       /* } else {
                            for (int i = 0; i < value.length(); i++) {
                                JSONObject jobj = value.getJSONObject(i);
                                for (MCXModel obj : mcxdata) {
                                    if (jobj.getString("name").equalsIgnoreCase(obj.getName())) {
                                        obj.setClose_price(jobj.getInt("close_price"));
                                        obj.setCurrent_price(jobj.getInt("current_price"));
                                        obj.setHigh_price(jobj.getInt("high_price"));
                                        obj.setLow_price(jobj.getInt("low_price"));
                                        obj.setOpen_price(jobj.getInt("open_price"));
                                    } else {
                                        MCXModel objs = new MCXModel();
                                        objs.setClose_price(jobj.getInt("close_price"));
                                        objs.setCurrent_price(jobj.getInt("current_price"));
                                        objs.setHigh_price(jobj.getInt("high_price"));
                                        objs.setLow_price(jobj.getInt("low_price"));
                                        objs.setOpen_price(jobj.getInt("open_price"));
                                        objs.setName(jobj.getString("name"));
                                        mcxdata.add(objs);
                                    }
                                }
                            }
                        }*/

                            adapter.notifyDataSetChanged();
                        Log.d("TAG", value.toString());
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
                    Toast.makeText(getApplicationContext(),
                            "Connected", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),
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
                    Toast.makeText(getApplicationContext(),
                            "Error connecting", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    };

    private void setMcxInfo() {


    }

    private void setMcxAdapter() {
        DividerItemDecoration divider =
                new DividerItemDecoration(getApplicationContext(),
                        DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        adapter = new MCXAdapter(MCXActivity.this, mcxdata);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);

        super.onDestroy();
        //mSocket.off("new message", onNewMessage);
    }
}