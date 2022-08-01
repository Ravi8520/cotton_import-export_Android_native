package com.ecotton.impex.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.CustomeMycontractLayoutBinding;
import com.ecotton.impex.models.MyContractModel;
import com.ecotton.impex.utils.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class MyContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    public ArrayList<MyContractModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<MyContractModel> filterArray = new ArrayList<>();
    SessionUtil sessionUtil;


    public MyContractAdapter(Context mcontext) {
        this.mcontext = mcontext;
        mArrayList = new ArrayList<>();
        sessionUtil = new SessionUtil(mcontext);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, int childPosition);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public void setOnLoadMore(OnLoadMoreListener myClickListener) {
        this.onLoadMoreListener = myClickListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void addAllClass(List<MyContractModel> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<MyContractModel> lst) {
        int sizeInit = mArrayList.size();
        mArrayList.addAll(lst);
        filterArray.addAll(lst);
        notifyItemRangeChanged(sizeInit, mArrayList.size());
    }

    public void showLoading() {
        if (isMoreLoading && mArrayList != null && onLoadMoreListener != null) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mArrayList.add(null);
                    notifyItemInserted(mArrayList.size() - 1);
                    onLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissLoading() {
        if (mArrayList != null && mArrayList.size() > 0) {
            mArrayList.remove(mArrayList.size() - 1);
            notifyItemRemoved(mArrayList.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mArrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_ITEM) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            CustomeMycontractLayoutBinding itemCardBinding = CustomeMycontractLayoutBinding.inflate(layoutInflater, parent, false);
            return new DataViewHolder(itemCardBinding);
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                DataViewHolder offerHolder = (DataViewHolder) holder;
                bindMyViewHolder(offerHolder, position);
                break;
            default:
                break;
        }
    }

    public void bindMyViewHolder(final DataViewHolder holder, final int position) {
        holder.binding.txtDate.setText(mArrayList.get(position).getDeal_date());

        ContractDataAdapter contractDataAdapter = new ContractDataAdapter(mcontext, mArrayList.get(position).getDeal_details());
        holder.binding.recyclerview.setLayoutManager(new LinearLayoutManager(mcontext));
        holder.binding.recyclerview.setAdapter(contractDataAdapter);

        contractDataAdapter.setOnItemClickListener(new ContractDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int positions) {
                if (view.getId() == R.id.btn_download) {
                    Log.e("positions", "positions==");
                    onItemClickListener.onItemClick(view, position, positions);
                }
                if (view.getId() == R.id.layout_main) {
                    onItemClickListener.onItemClick(view, position, positions);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public CustomeMycontractLayoutBinding binding;

        public DataViewHolder(CustomeMycontractLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
