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
import com.ecotton.impex.databinding.ProductItemLayoutBinding;
import com.ecotton.impex.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<ProductModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<ProductModel> filterArray = new ArrayList<>();


    public ArrayList<String> ids = new ArrayList<>();
    public ArrayList<String> selectedNames = new ArrayList<>();

    public int pos = 0;


    public ProductAdapter(Context mcontext) {
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

    public void addAllClass(List<ProductModel> models) {
        mArrayList.clear();
        filterArray.clear();
        ProductModel productModel = new ProductModel();
        productModel.setName("All");
        productModel.setId(-1);
        mArrayList.add(productModel);
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<ProductModel> lst) {
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
            ProductItemLayoutBinding itemCardBinding = ProductItemLayoutBinding.inflate(layoutInflater, parent, false);
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

        holder.binding.txtProductName.setText(mArrayList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 0) {
                    ids.clear();
                    selectedNames.clear();
                    ids.add("-1");
                    selectedNames.add("All");
                    holder.binding.layoutProduct.setBackground(mcontext.getResources().getDrawable(R.drawable.custome_button));
                    holder.binding.txtProductName.setTextColor(mcontext.getResources().getColor(R.color.white));
                    onItemClickListener.onItemClick(view, position);
                    notifyDataSetChanged();
                } else {
                    if (ids.contains("-1"))
                        ids.remove("-1");
                    if (selectedNames.contains("All"))
                        selectedNames.remove("All");

                    if (ids.size() > 0) {
                        if (ids.contains(mArrayList.get(position).getId() + "")) {
                            holder.binding.layoutProduct.setBackgroundColor(mcontext.getResources().getColor(R.color.light_grey));
                            holder.binding.txtProductName.setTextColor(mcontext.getResources().getColor(R.color.black));

                            ids.remove(mArrayList.get(position).getId() + "");
                            selectedNames.remove(mArrayList.get(position).getName());
                        } else {
                            holder.binding.layoutProduct.setBackground(mcontext.getResources().getDrawable(R.drawable.custome_button));
                            holder.binding.txtProductName.setTextColor(mcontext.getResources().getColor(R.color.white));

                            ids.add(mArrayList.get(position).getId() + "");
                            selectedNames.add(mArrayList.get(position).getName());
                        }
                    } else {
                        holder.binding.layoutProduct.setBackground(mcontext.getResources().getDrawable(R.drawable.custome_button));
                        holder.binding.txtProductName.setTextColor(mcontext.getResources().getColor(R.color.white));

                        ids.add(mArrayList.get(position).getId() + "");
                        selectedNames.add(mArrayList.get(position).getName());
                    }
                    onItemClickListener.onItemClick(view, position);
                    notifyDataSetChanged();
                }

              /*  pos = position;
                onItemClickListener.onItemClick(view, position);
                notifyDataSetChanged();*/
            }
        });
        if (ids.contains(mArrayList.get(position).getId() + "")) {
            holder.binding.layoutProduct.setBackground(mcontext.getResources().getDrawable(R.drawable.custome_button));
            holder.binding.txtProductName.setTextColor(mcontext.getResources().getColor(R.color.white));
        } else {
            holder.binding.layoutProduct.setBackgroundColor(mcontext.getResources().getColor(R.color.light_grey));
            holder.binding.txtProductName.setTextColor(mcontext.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public ProductItemLayoutBinding binding;

        public DataViewHolder(ProductItemLayoutBinding itemView) {
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

                    ArrayList<ProductModel> filteredList = new ArrayList<>();

                    for (ProductModel androidVersion : filterArray) {

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
                mArrayList = (ArrayList<ProductModel>) filterResults.values;
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

