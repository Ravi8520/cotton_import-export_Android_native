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
import com.ecotton.impex.databinding.CustomeMyfavouriteLayoutBinding;
import com.ecotton.impex.models.MyFavouriteModel;

import java.util.ArrayList;
import java.util.List;

public class MyFavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    ArrayList<MyFavouriteModel> dataarraylist;
    private BuyerAdapter.OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private BuyerAdapter.OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<MyFavouriteModel> filterArray = new ArrayList<>();

    public MyFavouriteAdapter(Context mcontext) {
        this.mcontext = mcontext;
        dataarraylist = new ArrayList<>();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(BuyerAdapter.OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public void setOnLoadMore(BuyerAdapter.OnLoadMoreListener myClickListener) {
        this.onLoadMoreListener = myClickListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void addAllClass(List<MyFavouriteModel> models) {
        dataarraylist.clear();
        filterArray.clear();
        dataarraylist.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<MyFavouriteModel> lst) {
        int sizeInit = dataarraylist.size();
        dataarraylist.addAll(lst);
        filterArray.addAll(lst);
        notifyItemRangeChanged(sizeInit, dataarraylist.size());
    }

    public void showLoading() {
        if (isMoreLoading && dataarraylist != null && onLoadMoreListener != null) {
            isMoreLoading = false;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    dataarraylist.add(null);
                    notifyItemInserted(dataarraylist.size() - 1);
                    onLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    public void setMore(boolean isMore) {
        this.isMoreLoading = isMore;
    }

    public void dismissLoading() {
        if (dataarraylist != null && dataarraylist.size() > 0) {
            dataarraylist.remove(dataarraylist.size() - 1);
            notifyItemRemoved(dataarraylist.size());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataarraylist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_ITEM) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            CustomeMyfavouriteLayoutBinding itemCardBinding = CustomeMyfavouriteLayoutBinding.inflate(layoutInflater, parent, false);
            return new DataViewHolder(itemCardBinding);
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false));
        }

    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        public CustomeMyfavouriteLayoutBinding binding;

        public DataViewHolder(CustomeMyfavouriteLayoutBinding itemView) {
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

        holder.binding.txtCompanyName.setText(dataarraylist.get(position).getCompany_name());
        holder.binding.txtStateName.setText(dataarraylist.get(position).getCountry_name());
        holder.binding.txtAddress.setText(dataarraylist.get(position).getAddress());
        holder.binding.txtDistrictName.setText(dataarraylist.get(position).getDistrict_name());

        String test = dataarraylist.get(position).getCompany_name();
        String first = String.valueOf(test.charAt(0));

        String str = test;
        String separator = " ";
        int sepPos = str.indexOf(separator);
        if (sepPos == -1) {
            System.out.println("");
        }
        String data = str.substring(sepPos + separator.length());

        String second = String.valueOf(data.charAt(0));

        holder.binding.firsLetter.setText(first);
        holder.binding.secondLetter.setText(second);

    }

    @Override
    public int getItemCount() {
        return dataarraylist.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    dataarraylist = filterArray;
                } else {

                    ArrayList<MyFavouriteModel> filteredList = new ArrayList<>();

                    for (MyFavouriteModel androidVersion : filterArray) {

                        if (androidVersion.getCompany_name().toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }

                    dataarraylist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataarraylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataarraylist = (ArrayList<MyFavouriteModel>) filterResults.values;
                if (dataarraylist.size() > 0) {
                    //  mListener.onListEmpty(true);
                } else {
                    // mListener.onListEmpty(false);
                }
                notifyDataSetChanged();
            }
        };
    }
}
