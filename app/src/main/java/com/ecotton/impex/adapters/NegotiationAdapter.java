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
import com.ecotton.impex.databinding.NegotiationItemBinding;
import com.ecotton.impex.databinding.NegotiationMyPostItemBinding;
import com.ecotton.impex.models.NegotiationList;

import java.util.ArrayList;
import java.util.List;

public class NegotiationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<NegotiationList> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM_MY_POST = 1;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<NegotiationList> filterArray = new ArrayList<>();


    public NegotiationAdapter(Context mcontext) {
        this.mcontext = mcontext;
        mArrayList = new ArrayList<>();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, NegotiationList obj);
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

    public void addAllClass(List<NegotiationList> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<NegotiationList> lst) {
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
        if (mArrayList.get(position) != null && mArrayList.get(position).getCount() == 1) {
            return VIEW_ITEM;
        } else if (mArrayList.get(position).getCount() > 1) {
            return VIEW_ITEM_MY_POST;
        } else {
            return VIEW_PROG;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_ITEM) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            NegotiationItemBinding itemCardBinding1 = NegotiationItemBinding.inflate(layoutInflater, parent, false);
            return new DataViewHolder(itemCardBinding1);
        } else if (viewType == VIEW_ITEM_MY_POST) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            NegotiationMyPostItemBinding itemCardBinding = NegotiationMyPostItemBinding.inflate(layoutInflater, parent, false);
            return new DataMyPostViewHolder(itemCardBinding);
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
            case 2:
                DataMyPostViewHolder offerHolders = (DataMyPostViewHolder) holder;
                bindMyPostViewHolder(offerHolders, position);
                break;
            default:
                break;
        }
    }

    public void bindMyViewHolder(final DataViewHolder holder, final int position) {

        NegotiationList obj = mArrayList.get(position);
        holder.binding.txtProductName.setText(obj.getProduct_name());

        NegotiationList.PostDetail postDetail;
        NegotiationList.NotificatioDetail notificatioDetail;
        try {
            if (mArrayList.get(position).getNegotiation_type().equals("post")) {
                postDetail = obj.getPost_detail().get(obj.getPost_detail().size() - 1);
                holder.binding.txtPreviousPrice.setText("$ " + postDetail.getPrev_price() + "(" + postDetail.getPrev_no_of_bales() + ")");
                holder.binding.txtNewPrice.setText("$ " + postDetail.getCurrent_price() + "(" + postDetail.getCurrent_no_of_bales() + ")");
                holder.binding.txtPosterName.setText(postDetail.getPosted_company_name());

                if (obj.getSeller_buyer_id().equals(MyApp.mSessionUtil.getUserid())) {
                    if (obj.getCount() > 1) {
                        holder.binding.txtWaiting.setText(obj.getCount() + " Negotiation");
                        holder.binding.txtWaiting.setVisibility(View.VISIBLE);
                        holder.binding.btnView.setVisibility(View.GONE);
                    } else {
                        if (postDetail.getNegotiation_by().equals(MyApp.mSessionUtil.getUsertype())) {
                            holder.binding.btnView.setVisibility(View.GONE);
                            holder.binding.txtWaiting.setVisibility(View.VISIBLE);
                        } else {
                            holder.binding.txtWaiting.setVisibility(View.GONE);
                            holder.binding.btnView.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    if (postDetail.getNegotiation_by().equals(MyApp.mSessionUtil.getUsertype())) {
                        holder.binding.btnView.setVisibility(View.GONE);
                        holder.binding.txtWaiting.setVisibility(View.VISIBLE);
                    } else {
                        holder.binding.txtWaiting.setVisibility(View.GONE);
                        holder.binding.btnView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                notificatioDetail = obj.getNotification_detail().get(obj.getNotification_detail().size() - 1);
                holder.binding.txtPreviousPrice.setText("$ " + notificatioDetail.getPrev_price() + "(" + notificatioDetail.getPrev_no_of_bales() + ")");
                holder.binding.txtNewPrice.setText("$ " + notificatioDetail.getCurrent_price() + "(" + notificatioDetail.getCurrent_no_of_bales() + ")");
                holder.binding.txtPosterName.setText(notificatioDetail.getPosted_company_name());

                if (obj.getSeller_buyer_id().equals(MyApp.mSessionUtil.getUserid())) {
                    if (obj.getCount() > 1) {
                        holder.binding.txtWaiting.setText(obj.getCount() + " Negotiation");
                        holder.binding.txtWaiting.setVisibility(View.VISIBLE);
                        holder.binding.btnView.setVisibility(View.GONE);
                    } else {
                        if (notificatioDetail.getNegotiation_by().equals(MyApp.mSessionUtil.getUsertype())) {
                            holder.binding.btnView.setVisibility(View.GONE);
                            holder.binding.txtWaiting.setVisibility(View.VISIBLE);
                        } else {
                            holder.binding.txtWaiting.setVisibility(View.GONE);
                            holder.binding.btnView.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    if (notificatioDetail.getNegotiation_by().equals(MyApp.mSessionUtil.getUsertype())) {
                        holder.binding.btnView.setVisibility(View.GONE);
                        holder.binding.txtWaiting.setVisibility(View.VISIBLE);
                    } else {
                        holder.binding.txtWaiting.setVisibility(View.GONE);
                        holder.binding.btnView.setVisibility(View.VISIBLE);
                    }
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }


        holder.binding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, obj);
            }
        });
        holder.binding.txtWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, obj);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, obj);
            }
        });
    }

    public void bindMyPostViewHolder(final DataMyPostViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public NegotiationItemBinding binding;

        public DataViewHolder(NegotiationItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    class DataMyPostViewHolder extends RecyclerView.ViewHolder {
        public NegotiationMyPostItemBinding binding;

        public DataMyPostViewHolder(NegotiationMyPostItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pBar;

        public ProgressViewHolder(View v) {
            super(v);
            pBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            pBar.setVisibility(View.GONE);
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

                    ArrayList<NegotiationList> filteredList = new ArrayList<>();

                    for (NegotiationList androidVersion : filterArray) {

                        if (androidVersion.getName().toLowerCase().contains(charString)) {
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
                mArrayList = (ArrayList<NegotiationList>) filterResults.values;
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

