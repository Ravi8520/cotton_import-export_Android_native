package com.ecotton.impex.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.ecotton.impex.MyApp.filterRequest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.BuildConfig;
import com.ecotton.impex.activities.DashboardCompanyListActivity;
import com.ecotton.impex.materialspinner.MaterialSpinner;
import com.ecotton.impex.models.NegotiationNotifyCount;
import com.ecotton.impex.utils.Constants;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.AddCompanyActivity;
import com.ecotton.impex.activities.DealsActivity;
import com.ecotton.impex.activities.FilterActivity;
import com.ecotton.impex.activities.HomeActivity;
import com.ecotton.impex.adapters.ChangeCpmapnyAdapter;
import com.ecotton.impex.adapters.DashBoardAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentHomeBinding;
import com.ecotton.impex.models.companylist.CompanyListModel;
import com.ecotton.impex.models.dashboard.DashBoardModel;
import com.ecotton.impex.models.login.LoginModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private Context mContext;
    FragmentHomeBinding binding;
    ChangeCpmapnyAdapter companyListAdapter;
    private List<CompanyListModel> companyListModelList = new ArrayList<>();

    private SessionUtil mSessionUtil;


    private CustomDialog customDialog;
    int mPageIndex = 1;
    private HomeFragment activity;

    DashBoardAdapter dashBoardAdapter;
    private List<DashBoardModel> dashBoardModelList = new ArrayList<>();
    private String Usertype;
    private static Socket mSocket;

    private Activity mActivity;
    public String socketSlug = "";

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
        this.mContext = context;
        activity = this;
        mActivity = (HomeActivity) context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        customDialog = new CustomDialog();
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            binding.txtCompanyName.setText(mSessionUtil.getCompanyName());
            if (mSessionUtil.getUsertype().equals("seller")) {
                binding.txtUserType.setText("Exporter");

            } else if (mSessionUtil.getUsertype().equals("buyer")) {
                binding.txtUserType.setText("Importer");
            }

            CompanyList();
        }

        try {
            String test = mSessionUtil.getCompanyName();
            String first = String.valueOf(test.charAt(0));

            String str = test;
            String separator = " ";
            int sepPos = str.indexOf(separator);
            if (sepPos == -1) {
                System.out.println("");
            }
            String data = str.substring(sepPos + separator.length());

            String second = String.valueOf(data.charAt(0));

            binding.firsLetter.setText(first);
            binding.secondLetter.setText(second);
        } catch (Exception e) {
            e.getMessage();
        }



        binding.userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(mContext, R.style.NewDialog1);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.TOP | Gravity.LEFT;
                wmlp.x = 0;   //x position
                wmlp.y = 0;   //y position
                dialog.setContentView(R.layout.change_company_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                RecyclerView superRecyclerView = (RecyclerView) dialog.findViewById(R.id.company_list);
                TextView add_company = (TextView) dialog.findViewById(R.id.add_company);


                add_company.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, AddCompanyActivity.class);
                        intent.putExtra("homeaddcompany", "homeaddcompany");
                        startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                });

                superRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                companyListAdapter = new ChangeCpmapnyAdapter(getActivity(), companyListModelList);
                superRecyclerView.setAdapter(companyListAdapter);
                companyListAdapter.setOnItemClickListener(new ChangeCpmapnyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.txt_buyer) {
                            dialog.dismiss();
                            CompanyDetailFragment.arraylist = null;
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("cotton", MODE_PRIVATE).edit();
                            editor.putInt("issetup", companyListAdapter.companyListModels.get(position).getIs_setupped());
                            editor.apply();
                            SelectUser(companyListAdapter.companyListModels.get(position).getCompany_id(), "buyer", position);
                        }
                        if (view.getId() == R.id.txt_seller) {
                            dialog.dismiss();
                            CompanyDetailFragment.arraylist = null;
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("cotton", MODE_PRIVATE).edit();
                            editor.putInt("issetup", companyListAdapter.companyListModels.get(position).getIs_setupped());
                            editor.apply();
                            SelectUser(companyListAdapter.companyListModels.get(position).getCompany_id(), "seller", position);
                        }
                    }
                });

                CompanyList();

                dialog.show();
            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, FilterActivity.class)
                        .putExtra("screen", HomeFragment.class.getSimpleName()));
            }
        });
        binding.imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DealsActivity.class));
            }
        });
        return binding.getRoot();
    }

    private void SelectUser(int company_id, String Usertype, int position) {
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
                            map.put(SessionUtil.EMAIL, mSessionUtil.getEmail());
                            map.put(SessionUtil.PASS, mSessionUtil.getPass());
                            map.put(SessionUtil.COMPANY_NAME, model.getData().getCompany_name());
                            map.put(SessionUtil.USER_TYPE, model.getData().getUser_type());
                            map.put(SessionUtil.USERID, model.getData().getUserId());
                            map.put(SessionUtil.COMPANY_ID, model.getData().getCompany_id());
                            mSessionUtil.setData(map);
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.putExtra(HomeActivity.COMPANY_Name, model.getData().getCompany_name());
                            intent.putExtra(HomeActivity.USER_Type, model.getData().getUser_type());
                            startActivity(intent);
                            ((Activity) mContext).finish();
                        } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                            final Dialog dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.waiting_approve_dailog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            TextView txt_company_name = (TextView) dialog.findViewById(R.id.txt_company_name);

                            txt_company_name.setText(companyListAdapter.companyListModels.get(position).getCompany_name());

                            Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();

                        } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, model.getMessage());
                            AppUtil.autoLogout(getActivity());
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

    private void CompanyList() {
        try {
            Call<ResponseModel<List<CompanyListModel>>> call = APIClient.getInstance().Company_list(mSessionUtil.getApiToken());
            call.enqueue(new Callback<ResponseModel<List<CompanyListModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<CompanyListModel>>> call, Response<ResponseModel<List<CompanyListModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        companyListModelList.clear();
                        companyListModelList.addAll(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        companyListAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel<List<CompanyListModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void Dashboard_buyerFilter() {
        try {

            String data = new Gson().toJson(filterRequest);
            Log.e("filterRequest", "filterRequest==" + data);
            Call<ResponseModel<List<DashBoardModel>>> call = APIClient.getInstance().filterBuyer(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DashBoardModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DashBoardModel>>> call, Response<ResponseModel<List<DashBoardModel>>> response) {
                    Log.e("dashboard", "dashboard==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS && response.body().data.size() > 0 && response.body() != null) {
                        binding.linStateData.setVisibility(View.VISIBLE);
                        setUpSpiner(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        binding.linStateData.setVisibility(View.GONE);
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<DashBoardModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                    Log.e("dashboard", "dashboard==" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Dashboard_sellerFilter() {
        try {
            String data = new Gson().toJson(filterRequest);
            Log.e("Dashboard_sellerFilter", "Dashboard_sellerFilter==" + data);
            Call<ResponseModel<List<DashBoardModel>>> call = APIClient.getInstance().filteSeller(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<DashBoardModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<DashBoardModel>>> call, Response<ResponseModel<List<DashBoardModel>>> response) {
                    Log.e("dashboard", "dashboard==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS && response.body().data.size() > 0 && response.body() != null) {
                        binding.linStateData.setVisibility(View.VISIBLE);
                        setUpSpiner(response.body().data);

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        binding.linStateData.setVisibility(View.GONE);
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<DashBoardModel>>> call, Throwable t) {
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
        super.onResume();
        if (TextUtils.isEmpty(filterRequest.getProduct_id())) {
            filterRequest.setProduct_id("-1");
        }
        if (TextUtils.isEmpty(filterRequest.getCountry_id())) {
            filterRequest.setCountry_id("-1");
        }
        if (mSessionUtil.getUsertype().equals("seller")) {
            Dashboard_buyerFilter();
        } else if (mSessionUtil.getUsertype().equals("buyer")) {
            Dashboard_sellerFilter();
        }
        setSocket();
        NegotiationNotifyCount();
    }

    public void setUpSpiner(List<DashBoardModel> list) {
        dashBoardModelList.clear();
        DashBoardModel obj = new DashBoardModel();
        obj.setName("All countries");
        dashBoardModelList.add(obj);
        dashBoardModelList.addAll(list);
        try {
           /* CustomAdapter adapter = new CustomAdapter(activity.getActivity(), R.layout.layout_spiner, R.id.txt_company_name, dashBoardModelList);
            binding.spinnerState.setAdapter(adapter);
            binding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (position == 0) {
                        setUpStateRecyclerVeiw(position);
                    } else {
                        filterRequest.setCountry_id(dashBoardAdapter.mArrayList.get(position - 1).getCountry_id() + "");
                        startActivity(new Intent(mContext, DashboardCompanyListActivity.class)
                                .putExtra("countryId", dashBoardAdapter.mArrayList.get(position - 1).getCountry_id() + ""));
                    }
                } // to close the onItemSelected

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }*/
            ArrayList<String> minAttribute = new ArrayList<>();
            for (DashBoardModel objs : dashBoardModelList) {
                minAttribute.add(objs.getName());
            }
            binding.spinnerState.setItems(minAttribute);
            binding.spinnerState.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                    if (position == 0) {
                        setUpStateRecyclerVeiw(position);
                    } else {
                        filterRequest.setCountry_id(dashBoardModelList.get(position).getCountry_id() + "");
                        startActivity(new Intent(mContext, DashboardCompanyListActivity.class)
                                .putExtra("countryId", dashBoardModelList.get(position ).getCountry_id() + ""));
                    }
                }
            });
            binding.spinnerState.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                @Override
                public void onNothingSelected(MaterialSpinner spinner) {
                    //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                }
            });

            setUpStateRecyclerVeiw(0);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public class CustomAdapter extends ArrayAdapter<DashBoardModel> {

        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, int textviewId, List<DashBoardModel> list) {

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

            DashBoardModel rowItem = getItem(position);

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

    public void setUpStateRecyclerVeiw(int position) {
        binding.rvStateList.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvStateList.setLayoutManager(layoutManager);
        dashBoardAdapter = new DashBoardAdapter(getActivity());
        binding.rvStateList.setAdapter(dashBoardAdapter);
        dashBoardAdapter.addAllClass(dashBoardModelList);
        dashBoardAdapter.mArrayList.remove(0);
        dashBoardAdapter.setOnItemClickListener(new DashBoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                filterRequest.setCountry_id(dashBoardAdapter.mArrayList.get(position).getCountry_id() + "");
                startActivity(new Intent(getActivity(), DashboardCompanyListActivity.class)
                        .putExtra("countryId", dashBoardAdapter.mArrayList.get(position).getCountry_id() + ""));
            }
        });
    }

    private void NegotiationNotifyCount() {
        try {
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("company_id", mSessionUtil.getCompanyId());
            String data = object.toString();
            Log.e("data", "data==" + data);
            Call<ResponseBody> call = APIClient.getInstance().unreadNegotiationNotification(mSessionUtil.getApiToken(), data);
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
                        NegotiationNotifyCount model = gson.fromJson(dataa, NegotiationNotifyCount.class);
                        if (model.getStatus() == Utils.StandardStatusCodes.SUCCESS) {
                            int count = model.getData().getCount();
                            if (count > 0) {
                                binding.txtNotifyCount.setVisibility(View.VISIBLE);
                                binding.txtNotifyCount.setText(count + "");
                            } else {
                                binding.txtNotifyCount.setVisibility(View.GONE);
                            }
                        } else if (model.getStatus() == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        } else if (model.getStatus() == Utils.StandardStatusCodes.UNAUTHORISE) {
                            AppUtil.showToast(mContext, model.getMessage());
                            AppUtil.autoLogout(getActivity());
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
    public void setSocket() {
        socketSlug = "UnreadNotifications" + "_" + mSessionUtil.getCompanyId() + "_" + mSessionUtil.getUsertype() + "_" + mSessionUtil.getUserid();
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

                        Log.d("TAG", jsonObject.toString());

                        int count = jsonObject.getJSONObject("data").getInt("UnreadNotifications");
                        if (count >= 0) {
                            binding.txtNotifyCount.setVisibility(View.VISIBLE);
                            binding.txtNotifyCount.setText(count + "");
                        } else {

                            binding.txtNotifyCount.setVisibility(View.GONE);
                        }


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
}