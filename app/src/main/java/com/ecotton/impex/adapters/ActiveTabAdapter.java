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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.CustomeMypostActivetabLayoutBinding;
import com.ecotton.impex.models.MyPostActiveModel;
import com.ecotton.impex.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActiveTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<MyPostActiveModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<MyPostActiveModel> filterArray = new ArrayList<>();


    public ActiveTabAdapter(Context mcontext) {
        this.mcontext = mcontext;
        mArrayList = new ArrayList<>();

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

    public void addAllClass(List<MyPostActiveModel> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<MyPostActiveModel> lst) {
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
            CustomeMypostActivetabLayoutBinding itemCardBinding = CustomeMypostActivetabLayoutBinding.inflate(layoutInflater, parent, false);
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
        holder.binding.txtProductName.setText(mArrayList.get(position).getProduct_name());
        holder.binding.txtBeale.setText(mArrayList.get(position).getRemaining_bales() + " Bales");
        holder.binding.txtAmount.setText(mcontext.getResources().getString(R.string.lbl_rupees_symbol_only) + mArrayList.get(position).getPrice());
        Calendar reject_date = DateTimeUtil.getCalendarWithUtcTimeZone(mArrayList.get(position).getDate(), DateTimeUtil.DISPLAY_DATE_TIME_FORMAT_WITH_COMMA);
        holder.binding.txtPostTime.setText(DateTimeUtil.getStringFromCalendar(reject_date, DateTimeUtil.DISPLAY_DATE_TIME_FORMAT));

        DividerItemDecoration divider =
                new DividerItemDecoration(mcontext,
                        DividerItemDecoration.HORIZONTAL);
        divider.setDrawable(mcontext.getResources().getDrawable(R.drawable.line_divider));

        ActivePostAttributeAdapter activePostAttributeAdapter = new ActivePostAttributeAdapter(mcontext, mArrayList.get(position).getAttribute_array());
        holder.binding.recyclerviewAttribute.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false));
        holder.binding.recyclerviewAttribute.addItemDecoration(divider);
        holder.binding.recyclerviewAttribute.addItemDecoration(new DividerItemDecoration(mcontext, DividerItemDecoration.HORIZONTAL));
        holder.binding.recyclerviewAttribute.setAdapter(activePostAttributeAdapter);

        holder.binding.activetabcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.binding.recyclerviewAttribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.binding.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        if (mArrayList.get(position).getType().equals("post")) {
            holder.binding.ivType.setImageResource(R.drawable.ic_post);
        } else if (mArrayList.get(position).getType().equals("notification")) {
            holder.binding.ivType.setImageResource(R.drawable.ic_baseline_notifications_none_24);
        }

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public CustomeMypostActivetabLayoutBinding binding;

        public DataViewHolder(CustomeMypostActivetabLayoutBinding itemView) {
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

                    ArrayList<MyPostActiveModel> filteredList = new ArrayList<>();

                    for (MyPostActiveModel androidVersion : filterArray) {

                        if (androidVersion.getProduct_name().toLowerCase().contains(charString)) {
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
                mArrayList = (ArrayList<MyPostActiveModel>) filterResults.values;
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
