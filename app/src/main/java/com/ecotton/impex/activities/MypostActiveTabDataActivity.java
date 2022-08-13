package com.ecotton.impex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ecotton.impex.adapters.SendToAdapter;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.adapters.PostDetailAttributeAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.ActivityMypostActiveTabDataBinding;
import com.ecotton.impex.models.PostDetailsModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.DateTimeUtil;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypostActiveTabDataActivity extends AppCompatActivity {

    ActivityMypostActiveTabDataBinding binding;
    MypostActiveTabDataActivity mContext;

    List<PostDetailsModel> postDetailsModelList = new ArrayList<>();

    String type;
    int postid;

    private SessionUtil mSessionUtil;
    private CustomDialog customDialog;

    PostDetailAttributeAdapter postDetailAttributesAdapter;
    SendToAdapter sendToAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMypostActiveTabDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = this;
        customDialog = new CustomDialog();
        mSessionUtil = new SessionUtil(mContext);
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            postid = intent.getIntExtra("post_notification_id", 0);
            type = intent.getStringExtra("type");
        }
        DividerItemDecoration divider =
                new DividerItemDecoration(mContext,
                        DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.line_divider));


        postDetailAttributesAdapter = new PostDetailAttributeAdapter(mContext);
        binding.recyclerviewAttribute.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerviewAttribute.addItemDecoration(divider);
        binding.recyclerviewAttribute.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.HORIZONTAL));
        binding.recyclerviewAttribute.setAdapter(postDetailAttributesAdapter);

        sendToAdapter = new SendToAdapter(mContext);
        binding.recyclerSendto.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerSendto.setAdapter(sendToAdapter);

        GetData();

    }


    private void GetData() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("post_notification_id", postid);
            jsonObject.put("type", type);

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<List<PostDetailsModel>>> call = APIClient.getInstance().post_details(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<PostDetailsModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<PostDetailsModel>>> call, Response<ResponseModel<List<PostDetailsModel>>> response) {
                    customDialog.dismissProgress(mContext);

                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        postDetailsModelList = response.body().data;
                        Log.e("notification_post", "PostDetailsModel==" + new Gson().toJson(response.body().data));
                        postDetailAttributesAdapter.addAllClass(response.body().data.get(0).getAttribute_array());
                        sendToAdapter.addAllClass(response.body().data.get(0).getSent_to());
                        Log.e("getSent_to", "getSent_to==" + new Gson().toJson(response.body().data.get(0).getSent_to()));
                        setData();
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
                public void onFailure(Call<ResponseModel<List<PostDetailsModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData() {
        binding.txtProductName.setText(postDetailsModelList.get(0).getProduct_name());
        binding.txtBeale.setText(postDetailsModelList.get(0).getNo_of_bales());
        binding.txtAmount.setText(getResources().getString(R.string.lbl_rupees_symbol_only) + " " + postDetailsModelList.get(0).getPrice());
        Calendar post_date = DateTimeUtil.getCalendarWithUtcTimeZone(postDetailsModelList.get(0).getDate(), DateTimeUtil.DISPLAY_DATE_TIME_FORMAT_WITH_COMMA);
        binding.txtPostDate.setText(DateTimeUtil.getStringFromCalendar(post_date, DateTimeUtil.DISPLAY_DATE_TIME_FORMAT));

        if (!postDetailsModelList.get(0).getStatus().equals("null")) {
            if (postDetailsModelList.get(0).getStatus().equals("complete")) {
                binding.txtPostAt.setText("Deal Done at");
            }
            if (postDetailsModelList.get(0).getStatus().equals("cancel")) {
                binding.txtPostAt.setText("cancel at");
            }
            if (postDetailsModelList.get(0).getStatus().equals("active")) {
                binding.layout.setVisibility(View.GONE);
            }
        }

        if (mSessionUtil.getUsertype().equals("seller")) {
            binding.txtTitle.setText("Notification to Buyer");
        }
        if (type.equals("post")) {
            binding.txtTitle.setVisibility(View.GONE);
        }
        if (type.equals("notification")) {
            binding.tvPostat.setText("Notification Send at");
            binding.postLbl.setText("Notification");
        }

        Calendar txtUpdateDate = DateTimeUtil.getCalendarWithUtcTimeZone(postDetailsModelList.get(0).getUpdated_at(), DateTimeUtil.DISPLAY_DATE_TIME_FORMAT_WITH_COMMA);
        binding.txtUpdateDate.setText(DateTimeUtil.getStringFromCalendar(txtUpdateDate, DateTimeUtil.DISPLAY_DATE_TIME_FORMAT));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}