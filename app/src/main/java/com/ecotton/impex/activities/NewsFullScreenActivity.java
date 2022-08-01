package com.ecotton.impex.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityNewsFullScreenBinding;
import com.ecotton.impex.htmltextview.HtmlResImageGetter;
import com.ecotton.impex.htmltextview.OnClickATagListener;
import com.ecotton.impex.models.NewsList;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.PrintLog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFullScreenActivity extends AppCompatActivity {

    int id;
    private CustomDialog customDialog;
    public NewsFullScreenActivity mContext;
    private SessionUtil mSessionUtil;
    public ActivityNewsFullScreenBinding binding;
    NewsList newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsFullScreenBinding.inflate(getLayoutInflater());
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
        id = getIntent().getIntExtra("id", 0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //  id = bundle.getString("id", "");
        }
        getNewsDetail();
        binding.ivNewsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsFullScreenActivity.this, FullscreenImageActivity.class);
                intent.putExtra("image", newsList.getImage());
                startActivity(intent);
            }
        });
    }

    private void getNewsDetail() {
        try {
            customDialog.displayProgress(mContext);
            String strJson = "";
            JSONObject object = new JSONObject();
            object.put("news_id", id);
            object.put("user_id", mSessionUtil.getUserid());
            object.put("user_type", mSessionUtil.getUsertype());

            strJson = object.toString();
            PrintLog.d("TAG", strJson);

            Call<ResponseModel<NewsList>> call = APIClient.getInstance().getNewsDetail(mSessionUtil.getApiToken(), strJson);
            call.enqueue(new Callback<ResponseModel<NewsList>>() {
                @Override
                public void onResponse(Call<ResponseModel<NewsList>> call, Response<ResponseModel<NewsList>> response) {
                    Log.e("response", "response==" + new Gson().toJson(response.body()));
                    customDialog.dismissProgress(mContext);
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        setUpdata(response.body().data);
                        newsList = response.body().data;
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {

                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(mContext);
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<NewsList>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setUpdata(NewsList obj) {
        if (TextUtils.isEmpty(obj.getImage())) {
            binding.ivNewsimg.setVisibility(View.GONE);
        } else {
            binding.ivNewsimg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(obj.getImage())
                    .apply(AppUtil.getPlaceholderRequestOption())
                    .into(binding.ivNewsimg);

        }
        binding.tvNewsTitle.setText(obj.getName());
        binding.tvNewstime.setText(obj.getTime_ago());
        binding.tvNewsDesc.setHtml(obj.getDescription(),
                new HtmlResImageGetter(mContext));

        binding.tvNewsDesc.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(href));
                startActivity(i);
                return false;
            }
        });
    }


}