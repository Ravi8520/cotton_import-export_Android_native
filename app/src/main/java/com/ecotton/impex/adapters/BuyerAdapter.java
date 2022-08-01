package com.ecotton.impex.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.CustomeBuyerLayoutBinding;
import com.ecotton.impex.models.companylist.CompanyDirectory;
import com.ecotton.impex.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class BuyerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<CompanyDirectory> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<CompanyDirectory> filterArray = new ArrayList<>();
   public  Boolean clicked = true;

    public BuyerAdapter(Context mcontext) {
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

    public void addAllClass(List<CompanyDirectory> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<CompanyDirectory> lst) {
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
            CustomeBuyerLayoutBinding itemCardBinding = CustomeBuyerLayoutBinding.inflate(layoutInflater, parent, false);
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
        holder.binding.companyname.setText(mArrayList.get(position).getCompany_name());
        // holder.binding.ownername.setText(mArrayList.get(position).getCompany_name());
        holder.binding.address.setText("Address : " + mArrayList.get(position).getAddress());
        holder.binding.contactTv.setText(mArrayList.get(position).getMobile_number());
        holder.binding.txtDistrict.setText(mArrayList.get(position).getDistrict());
        holder.binding.txtState.setText(mArrayList.get(position).getState());
        holder.binding.country.setText(mArrayList.get(position).getCountry());

        holder.binding.layoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.openDialPad(mcontext, mArrayList.get(position).getMobile_number());
            }
        });

        if (mArrayList.get(position).getIs_my_favourite() == 1) {
            holder.binding.likebtn.setImageResource(R.drawable.ic_like_fill);
        } else if (mArrayList.get(position).getIs_my_favourite() == 0) {
            holder.binding.likebtn.setImageResource(R.drawable.ic_like_unfill);
        }

        holder.binding.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                   /* if (clicked) {
                        holder.binding.likebtn.setImageResource(R.drawable.ic_like_fill);
                        clicked = false;
                    } else {
                        clicked = true;
                        holder.binding.likebtn.setImageResource(R.drawable.ic_like_unfill);
                    }*/
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder {
        public CustomeBuyerLayoutBinding binding;

        public DataViewHolder(CustomeBuyerLayoutBinding itemView) {
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

                    ArrayList<CompanyDirectory> filteredList = new ArrayList<>();

                    for (CompanyDirectory androidVersion : filterArray) {

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
                mArrayList = (ArrayList<CompanyDirectory>) filterResults.values;
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
