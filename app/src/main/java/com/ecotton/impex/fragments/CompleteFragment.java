package com.ecotton.impex.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.MypostActiveTabDataActivity;
import com.ecotton.impex.adapters.CompleteTabAdapter;
import com.ecotton.impex.api.APIClient;
import com.ecotton.impex.api.ResponseModel;
import com.ecotton.impex.databinding.FragmentCompleteBinding;
import com.ecotton.impex.models.CompleteTabModel;
import com.ecotton.impex.utils.AppUtil;
import com.ecotton.impex.utils.CustomDialog;
import com.ecotton.impex.utils.SessionUtil;
import com.ecotton.impex.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompleteFragment extends Fragment {


    ArrayList<CompleteTabModel> modelArrayList;

    private Context mContext;
    private SessionUtil mSessionUtil;

    private CustomDialog customDialog;
    FragmentCompleteBinding binding;
    CompleteTabAdapter completeTabAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        mSessionUtil = new SessionUtil(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCompleteBinding.inflate(getLayoutInflater());


        customDialog = new CustomDialog();
        modelArrayList = new ArrayList<>();


        setAdapter();

        CompletePost();


        return binding.getRoot();

    }

    private void setAdapter() {
        DividerItemDecoration divider =
                new DividerItemDecoration(getActivity(),
                        DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        completeTabAdapter = new CompleteTabAdapter(mContext);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerviewCompletePost.setLayoutManager(layoutManager);
        binding.recyclerviewCompletePost.addItemDecoration(divider);
        binding.recyclerviewCompletePost.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        binding.recyclerviewCompletePost.setAdapter(completeTabAdapter);

        completeTabAdapter.setOnItemClickListener(new CompleteTabAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, MypostActiveTabDataActivity.class);
                intent.putExtra("post_notification_id", completeTabAdapter.mArrayList.get(position).getPost_id());
                intent.putExtra("type", completeTabAdapter.mArrayList.get(position).getType());
                startActivity(intent);
                ((Activity) mContext).finish();
            }
        });
    }

    private void CompletePost() {
        try {
            customDialog.displayProgress(mContext);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", mSessionUtil.getUserid());
            jsonObject.put("company_id", mSessionUtil.getCompanyId());
            jsonObject.put("user_type", mSessionUtil.getUsertype());
            jsonObject.put("type", "post");

            String data = jsonObject.toString();

            Log.e("data", "data==" + data);

            Call<ResponseModel<List<CompleteTabModel>>> call = APIClient.getInstance().completed_deal(mSessionUtil.getApiToken(), data);
            call.enqueue(new Callback<ResponseModel<List<CompleteTabModel>>>() {
                @Override
                public void onResponse(Call<ResponseModel<List<CompleteTabModel>>> call, Response<ResponseModel<List<CompleteTabModel>>> response) {
                    customDialog.dismissProgress(mContext);
                    Log.e("CompleteTabModel", "CompleteTabModel==" + new Gson().toJson(response.body().data));
                    if (response.body().status == Utils.StandardStatusCodes.SUCCESS) {
                        completeTabAdapter.addAllClass(response.body().data);
                    } else if (response.body().status == Utils.StandardStatusCodes.NO_DATA_FOUND) {
                        completeTabAdapter.notifyDataSetChanged();
                    } else if (response.body().status == Utils.StandardStatusCodes.UNAUTHORISE) {
                        AppUtil.showToast(mContext, response.body().message);
                        AppUtil.autoLogout(getActivity());
                    } else {
                        AppUtil.showToast(mContext, response.body().message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel<List<CompleteTabModel>>> call, Throwable t) {
                    customDialog.dismissProgress(mContext);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

