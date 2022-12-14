package com.ecotton.impex.adapters;

import static com.ecotton.impex.activities.AddBrokerActivity.jsonArray;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.SearchBrokerItemBinding;
import com.ecotton.impex.models.SearchBrokerModel;

import java.util.ArrayList;
import java.util.List;

public class SearchBrokerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<SearchBrokerModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<SearchBrokerModel> filterArray = new ArrayList<>();

    public boolean flag = false;


    public SearchBrokerAdapter(Context mcontext) {
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

    public void addAllClass(List<SearchBrokerModel> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<SearchBrokerModel> lst) {
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
            SearchBrokerItemBinding itemCardBinding = SearchBrokerItemBinding.inflate(layoutInflater, parent, false);
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
        holder.binding.txtCompanyName.setText(mArrayList.get(position).getCompany_name());
        holder.binding.txtMobileNumber.setText(mArrayList.get(position).getMobile_number());


        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    jsonArray.put(mArrayList.get(position).getId());
                    Log.e("jsonArray11", "jsonArray111===" + jsonArray);
                } else {
                    for (int i = jsonArray.length(); i >= 0; i--) {
                        jsonArray.remove(i);
                    }
                    Log.e("jsonArray", "jsonArray===" + jsonArray);
                }
            }
        });

      /*  holder.binding.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
                if (!holder.binding.checkbox.isChecked()) {
                    jsonArray.put(mArrayList.get(position).getId());
                    holder.binding.checkbox.setChecked(true);
                } else {
                    for (int i = jsonArray.length(); i >= 0; i--) {
                        jsonArray.remove(i);
                    }
                    Log.e("jsonArray", "jsonArray===" + jsonArray);
                    holder.binding.checkbox.setChecked(false);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        public SearchBrokerItemBinding binding;

        public DataViewHolder(SearchBrokerItemBinding itemView) {
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

                    ArrayList<SearchBrokerModel> filteredList = new ArrayList<>();

                    for (SearchBrokerModel androidVersion : filterArray) {

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
                mArrayList = (ArrayList<SearchBrokerModel>) filterResults.values;
                if (mArrayList.size() > 0) {
                    //  mListener.onListEmpty(true);
                } else {
                    // mListener.onListEmpty(false);
                    Toast.makeText(mcontext, "No Data Found", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        };
    }
}
