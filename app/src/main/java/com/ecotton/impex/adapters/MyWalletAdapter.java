package com.ecotton.impex.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.CustomeMywalletLayoutBinding;
import com.ecotton.impex.models.MyWalletModel;
import com.ecotton.impex.utils.DateTimeUtil;
import com.ecotton.impex.utils.SessionUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyWalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mcontext;
    public List<MyWalletModel.Transaction> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public List<MyWalletModel.Transaction> filterArray = new ArrayList<>();
    SessionUtil sessionUtil;


    public MyWalletAdapter(Context mcontext, List<MyWalletModel.Transaction> transaction_history) {
        this.mcontext = mcontext;
        this.mArrayList = transaction_history;
        this.filterArray = transaction_history;
        sessionUtil = new SessionUtil(mcontext);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
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

   /* public void addAllClass(List<MyWalletModel.Transaction> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<MyWalletModel.Transaction> lst) {
        int sizeInit = mArrayList.size();
        mArrayList.addAll(lst);
        filterArray.addAll(lst);
        notifyItemRangeChanged(sizeInit, mArrayList.size());
    }*/

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
            CustomeMywalletLayoutBinding itemCardBinding = CustomeMywalletLayoutBinding.inflate(layoutInflater, parent, false);
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
        holder.binding.txtMessage.setText(mArrayList.get(position).getMessage());

        Calendar post_date = DateTimeUtil.getCalendarWithUtcTimeZone(mArrayList.get(position).getDate(), DateTimeUtil.DISPLAY_DATE_FORMAT);
        holder.binding.txtDate.setText(DateTimeUtil.getStringFromCalendar(post_date, DateTimeUtil.DISPLAY_DATE_TIME_FORMAT));

        if (mArrayList.get(position).getType().equals("plan_deposite")) {
            holder.binding.txtRupee.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
            holder.binding.txtRupee.setText("+ " + mcontext.getResources().getString(R.string.lbl_rupees_symbol_only) + " " + mArrayList.get(position).getAmount());
        } else if (mArrayList.get(position).getType().equals("withdraw")) {
            holder.binding.txtRupee.setTextColor(mcontext.getResources().getColor(R.color.dark_red));
            holder.binding.txtRupee.setText("- " + mcontext.getResources().getString(R.string.lbl_rupees_symbol_only) + " " + mArrayList.get(position).getAmount());
        }else if (mArrayList.get(position).getType().equals("deposite")) {
            holder.binding.txtRupee.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
            holder.binding.txtRupee.setText("- " + mcontext.getResources().getString(R.string.lbl_rupees_symbol_only) + " " + mArrayList.get(position).getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public CustomeMywalletLayoutBinding binding;

        public DataViewHolder(CustomeMywalletLayoutBinding itemView) {
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
