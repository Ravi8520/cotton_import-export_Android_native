package com.ecotton.impex.activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.adapters.BuyerAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityBuyerBinding;
import com.ecotton.impex.models.BuyerModel;
import com.ecotton.impex.models.companylist.CompanyDirectory;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerActivity extends AppCompatActivity implements BuyerAdapter.OnItemClickListener {
    public ActivityBuyerBinding binding;
    public ArrayList<BuyerModel> buyerdata;
    public BuyerAdapter adapter;
    public BuyerActivity mContext;
    private SessionUtil mSessionUtil;
    int mPageIndex = 1;
    private CustomDialog customDialog;
    BuyerAdapter.DataViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buyerdata = new ArrayList<>();
        mContext = this;

        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();

        setAddapter();

        binding.recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AppUtil.hideSoftKeyboard(mContext);
                return false;
            }
        });

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        if (mSessionUtil.getUsertype().equals("buyer")) {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_exporter_directory));
        } else {
            binding.txtTitle.setText(getResources().getString(R.string.lbl_importer_directory));
        }
    }

    View view1;

    @Override
    public void onItemClick(View view, int position) {
        view1 = view;
        if (position != RecyclerView.NO_POSITION) {
            if (view.getId() == R.id.likebtn) {
                if (adapter.mArrayList.get(position).getIs_my_favourite() == 1) {
                    UnFavourite(position);

                } else if (adapter.mArrayList.get(position).getIs_my_favourite() == 0) {
                    AddTofavourites(position);

                }
            }
        }
     /*   InputMethodManager imm = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);*/
        hideSoftKeyboard(mContext);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    private void setAddapter() {
        DividerItemDecoration divider =
                new DividerItemDecoration(getApplicationContext(),
                        DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        adapter = new BuyerAdapter(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.addItemDecoration(divider);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        adapter.setOnItemClickListener(this);
        binding.recyclerview.setAdapter(adapter);
        getCompanyData();
    }

    private void getCompanyData() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("user_type", mSessionUtil.getUsertype());

            strJson = object.toString();
            PrintLog.e("TAG", strJson);

            Call<ResponseModel<List<CompanyDirectory>>> call = APIClient.getInstance().getBuyerSellerDirectory(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<CompanyDirectory>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<CompanyDirectory>>> call, Response<ResponseModel<List<CompanyDirectory>>> response) {
                    Log.e("response",  "CompanyDirectory==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        adapter.addAllClass(response.body().data);

                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<CompanyDirectory>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void UnFavourite(int position) {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("id", +adapter.mArrayList.get(position).getFavourite_id());

            strJson = object.toString();

            PrintLog.e("TAG", strJson);

            Call<ResponseModel<CompanyDirectory>> call = APIClient.getInstance().remove_from_favourite(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<CompanyDirectory>>() {
                @Override
                public void onResponse(Call<ResponseModel<CompanyDirectory>> call, Response<ResponseModel<CompanyDirectory>> response) {
                    Log.e("jayesh", "jayesh==" + new Gson().toJson(response.body().data));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        getCompanyData();
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
                public void onFailure(Call<ResponseModel<CompanyDirectory>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AddTofavourites(int position) {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("user_id", mSessionUtil.getUserid());
            object.put("company_id", mSessionUtil.getCompanyId());
            object.put("user_type", mSessionUtil.getUsertype());
            object.put("fav_company_id", +adapter.mArrayList.get(position).getCompany_id());

            strJson = object.toString();

            PrintLog.e("TAG", strJson);

            Call<ResponseModel<CompanyDirectory>> call = APIClient.getInstance().add_to_favourites(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<CompanyDirectory>>() {
                @Override
                public void onResponse(Call<ResponseModel<CompanyDirectory>> call, Response<ResponseModel<CompanyDirectory>> response) {
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        AppUtil.showToast(mContext, response.body().message);
                        getCompanyData();
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
                public void onFailure(Call<ResponseModel<CompanyDirectory>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}