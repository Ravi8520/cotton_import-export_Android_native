package com.ecotton.impex.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.om.superrecyclerview.OnMoreListener;
import com.ecotton.impex.adapters.NewsAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityNewsBinding;
import com.ecotton.impex.models.NewsList;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {


    private List<NewsList> mNewsList = new ArrayList<>();
    public ActivityNewsBinding binding;
    private CustomDialog customDialog;
    public NewsActivity mContext;
    public NewsAdapter adapter;
    int mPageIndex = 1;

    private SessionUtil mSessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;

        mSessionUtil = new SessionUtil(mContext);
        customDialog = new CustomDialog();
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setSellerinfo();

        initSuperRecyclerView();

    }

    private void setSellerinfo() {
        binding.mRvNewsList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new NewsAdapter(NewsActivity.this, mNewsList);
        binding.mRvNewsList.setAdapter(adapter);
    }

    private void initSuperRecyclerView() {
        binding.mRvNewsList.removeMoreListener();
        binding.mRvNewsList.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                getPharmacyList();
            }
        });
        mPageIndex = 1;
        binding.mRvNewsList.showProgress();
        mNewsList.clear();
        getPharmacyList();
    }

    private void getPharmacyList() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("offset", mPageIndex);
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());

            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel<List<NewsList>>> call = APIClient.getInstance().getNews(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<List<NewsList>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<NewsList>>> call, Response<ResponseModel<List<NewsList>>> response) {
                    Log.e("response", "response==" + response);
                    customDialog.dismissProgress(mContext);
                    binding.mRvNewsList.showRecycler();
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        if (mPageIndex == 1) {
                            mNewsList.clear();
                            adapter.newsdatalist.clear();
                            mNewsList.addAll(response.body().data);
                            adapter.notifyDataSetChanged();
                        } else if (mPageIndex > 1) {
                            int previousSize = mNewsList.size();
                            mNewsList.addAll(response.body().data);
                            adapter.notifyItemRangeInserted(previousSize, mNewsList.size());
                        }

                        if (mPageIndex >= response.body().getPage()) {
                            binding.mRvNewsList.removeMoreListener();
                        } else {
                            mPageIndex++;
                        }
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        if (mPageIndex >= response.body().getPage()) {
                            binding.mRvNewsList.removeMoreListener();
                        }
                        adapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(NewsActivity.this);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<NewsList>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}