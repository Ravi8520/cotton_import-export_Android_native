package com.ecotton.impex.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.MyApp;
import com.ecotton.impex.R;
import com.ecotton.impex.databinding.NegotiationDetailItemBinding;
import com.ecotton.impex.models.NegotiationList;
import com.ecotton.impex.utils.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class NegotiationDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<NegotiationList.PostDetail> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<NegotiationList.PostDetail> filterArray = new ArrayList<>();

    SessionUtil sessionUtil;

    public NegotiationDetailAdapter(Context mcontext) {
        this.mcontext = mcontext;
        mArrayList = new ArrayList<>();
        sessionUtil = new SessionUtil(mcontext);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, NegotiationList.PostDetail obj);
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

    public void addAllClass(List<NegotiationList.PostDetail> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<NegotiationList.PostDetail> lst) {
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
            NegotiationDetailItemBinding itemCardBinding = NegotiationDetailItemBinding.inflate(layoutInflater, parent, false);
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

        NegotiationList.PostDetail postDetail = mArrayList.get(position);
        holder.binding.txtPreviousPrice.setText("₹ " + postDetail.getPrev_price() + "(" + postDetail.getPrev_no_of_bales() + ")");
        holder.binding.txtNewPrice.setText("₹ " + postDetail.getCurrent_price() + "(" + postDetail.getCurrent_no_of_bales() + ")");
        holder.binding.txtCompany.setText(postDetail.getPosted_company_name());
        holder.binding.txtBrokerName.setText(postDetail.getBroker_name());

        if (sessionUtil.getUsertype().equals("buyer")) {
            holder.binding.txtSellerBuyerName.setText(postDetail.getSeller_name());
        } else {
            holder.binding.txtSellerBuyerName.setText(postDetail.getBuyer_name());
        }

        if (postDetail.getNegotiation_by().equals(MyApp.mSessionUtil.getUsertype())) {
            holder.binding.btnView.setVisibility(View.GONE);
            holder.binding.txtWaiting.setVisibility(View.VISIBLE);
        } else {
            holder.binding.txtWaiting.setVisibility(View.GONE);
            holder.binding.btnView.setVisibility(View.VISIBLE);
        }

        holder.binding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, postDetail);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, postDetail);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public NegotiationDetailItemBinding binding;

        public DataViewHolder(NegotiationDetailItemBinding itemView) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mArrayList = filterArray;
                } else {

                    ArrayList<NegotiationList.PostDetail> filteredList = new ArrayList<>();

                    for (NegotiationList.PostDetail androidVersion : filterArray) {

                        if (androidVersion.getBuyer_name().toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }

                    mArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mArrayList = (ArrayList<NegotiationList.PostDetail>) filterResults.values;
                if (mArrayList.size() > 0) {
                    //  mListener.onListEmpty(true);
                } else {
                    // mListener.onListEmpty(false);
                }
                notifyDataSetChanged();
            }
        };
    }
}

