package com.ecotton.impex.adapters;


import static com.ecotton.impex.activities.SelectSellerActivity.jsonArray1;
import static com.ecotton.impex.activities.SelectSellerActivity.selectedList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.SelectsellerItemBinding;
import com.ecotton.impex.models.SearchCompanyModel;

import java.util.ArrayList;
import java.util.List;

public class SellectSellerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<SearchCompanyModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<SearchCompanyModel> filterArray = new ArrayList<>();

    boolean[] checkBoxState;

    public SellectSellerAdapter(Context mcontext) {
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

    public void addAllClass(List<SearchCompanyModel> models) {
        mArrayList.clear();
        filterArray.clear();
        if (selectedList.size() > 0)
            for (SearchCompanyModel objs : selectedList) {
                for (SearchCompanyModel obj : models) {
                    if (objs.getCompany_id() == obj.getCompany_id())
                        obj.setSelected(true);
                }
            }


        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void refreshData() {
        for (SearchCompanyModel obj : mArrayList) {
            obj.setSelected(false);
        }
        for (SearchCompanyModel objs : selectedList) {
            for (SearchCompanyModel obj : mArrayList) {
                if (objs.getCompany_id() == obj.getCompany_id())
                    obj.setSelected(true);
            }
        }
        filterArray.clear();
        filterArray.addAll(mArrayList);
        notifyDataSetChanged();
    }

    public void addItemMore(List<SearchCompanyModel> lst) {
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
            SelectsellerItemBinding itemCardBinding = SelectsellerItemBinding.inflate(layoutInflater, parent, false);
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

    public void bindMyViewHolder(final DataViewHolder holder, int position) {
        holder.binding.txtCompanyName.setText(mArrayList.get(position).getCompany_name());
        holder.binding.txtCityName.setText(mArrayList.get(position).getCity());
        if (mArrayList.get(position).isSelected())
            holder.binding.checkboxSelectSeller.setChecked(true);
        else
            holder.binding.checkboxSelectSeller.setChecked(false);

        holder.binding.checkboxSelectSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedList.contains(mArrayList.get(position)))
                    selectedList.remove(mArrayList.get(position));
                else
                    selectedList.add(mArrayList.get(position));

                if (holder.binding.checkboxSelectSeller.isChecked()) {
                    mArrayList.get(position).setSelected(true);
                    jsonArray1.put(mArrayList.get(position).getCompany_id());
                    Log.e("jsonArray1", "jsonArray1==" + jsonArray1);
                } else {
                    mArrayList.get(position).setSelected(false);
                    jsonArray1.remove(mArrayList.get(position).getCompany_id());
                    Log.e("jsonArr", "jsonA==" + jsonArray1);
                }

                onItemClickListener.onItemClick(holder.binding.checkboxSelectSeller, position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        public SelectsellerItemBinding binding;

        public DataViewHolder(SelectsellerItemBinding itemView) {
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

                    ArrayList<SearchCompanyModel> filteredList = new ArrayList<>();

                    for (SearchCompanyModel androidVersion : filterArray) {

                        if (androidVersion.getCompany_name().toLowerCase().contains(charString)) {
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
                mArrayList = (ArrayList<SearchCompanyModel>) filterResults.values;
                if (mArrayList.size() > 0) {
                    //  mListener.onListEmpty(true);
                } else {
                    //Toast.makeText(mcontext, "No Data Found", Toast.LENGTH_SHORT).show();
                    // mListener.onListEmpty(false);
                }
                notifyDataSetChanged();
            }
        };
    }
}