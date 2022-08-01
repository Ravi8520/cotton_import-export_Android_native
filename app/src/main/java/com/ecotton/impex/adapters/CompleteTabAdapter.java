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

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.CustomeMypostLayoutBinding;
import com.ecotton.impex.models.CompleteTabModel;
import com.ecotton.impex.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CompleteTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<CompleteTabModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<CompleteTabModel> filterArray = new ArrayList<>();


    public CompleteTabAdapter(Context mcontext) {
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

    public void addAllClass(List<CompleteTabModel> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<CompleteTabModel> lst) {
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
            CustomeMypostLayoutBinding itemCardBinding = CustomeMypostLayoutBinding.inflate(layoutInflater, parent, false);
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
        holder.binding.txtBeale.setText(mArrayList.get(position).getNo_of_bales() + "  Bales");

        Calendar post_date = DateTimeUtil.getCalendarWithUtcTimeZone(mArrayList.get(position).getCreated_at(), DateTimeUtil.DISPLAY_DATE_TIME_FORMAT_WITH_COMMA);
        holder.binding.txtPostTime.setText(DateTimeUtil.getStringFromCalendar(post_date, DateTimeUtil.DISPLAY_DATE_TIME_FORMAT));

        Calendar reject_date = DateTimeUtil.getCalendarWithUtcTimeZone(mArrayList.get(position).getUpdated_at(), DateTimeUtil.DISPLAY_DATE_TIME_FORMAT_WITH_COMMA);
        holder.binding.txtCancelTime.setText(DateTimeUtil.getStringFromCalendar(reject_date, DateTimeUtil.DISPLAY_DATE_TIME_FORMAT));


        holder.binding.txtAmount.setText(mcontext.getResources().getString(R.string.lbl_rupees_symbol_only) + " " + mArrayList.get(position).getPrice());

        holder.binding.txtCompanyName.setText(mArrayList.get(position).getName());
        holder.binding.txtSellerName.setText(mArrayList.get(position).getDone_by());

        if (mArrayList.get(position).getStatus().equals("cancel")) {
            holder.binding.cancelTimeRl.setVisibility(View.VISIBLE);
            holder.binding.layoutCompany.setVisibility(View.GONE);
            holder.binding.txtDealStatus.setTextColor(mcontext.getResources().getColor(R.color.dark_red));
            holder.binding.txtDealStatus.setBackground(mcontext.getDrawable(R.drawable.custome_box_dotted));

        } else {
            holder.binding.txtDealStatus.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
            holder.binding.txtDealStatus.setBackground(mcontext.getDrawable(R.drawable.custome_box_dotted_green));
            holder.binding.txtDealStatus.setText(mcontext.getResources().getString(R.string.lbl_deal_done));
            holder.binding.cancelTimeRl.setVisibility(View.GONE);
            holder.binding.layoutCompany.setVisibility(View.VISIBLE);
        }
        if (mArrayList.get(position).getType().equals("post")) {
            holder.binding.imgType.setImageResource(R.drawable.ic_post);
        } else if (mArrayList.get(position).getType().equals("notification")) {
            holder.binding.imgType.setImageResource(R.drawable.ic_baseline_notifications_none_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public CustomeMypostLayoutBinding binding;

        public DataViewHolder(CustomeMypostLayoutBinding itemView) {
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

                    ArrayList<CompleteTabModel> filteredList = new ArrayList<>();

                    for (CompleteTabModel androidVersion : filterArray) {

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
                mArrayList = (ArrayList<CompleteTabModel>) filterResults.values;
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
