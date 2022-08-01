package com.ecotton.impex.adapters;

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

import com.google.android.material.slider.RangeSlider;
import com.ecotton.impex.R;
import com.ecotton.impex.databinding.ProductVlaueItemBinding;
import com.ecotton.impex.models.ProductValueModel;

import java.util.ArrayList;
import java.util.List;

public class ProductValueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<ProductValueModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<ProductValueModel> filterArray = new ArrayList<>();

    public int pos = 0;


    public ProductValueAdapter(Context mcontext) {
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

    public void addAllClass(List<ProductValueModel> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<ProductValueModel> lst) {
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
            ProductVlaueItemBinding itemCardBinding = ProductVlaueItemBinding.inflate(layoutInflater, parent, false);
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

        float minvalueSelected = Float.parseFloat(mArrayList.get(position).getMinSelected());
        float maxvalueSelected = Float.parseFloat(mArrayList.get(position).getMaxSelected());
        float minvalue = Float.parseFloat(mArrayList.get(position).getMin());
        float maxvalue = Float.parseFloat(mArrayList.get(position).getMax());
        float duration = mArrayList.get(position).getDuration();


        Log.e("maxvalueSelected", "maxvalueSelected==" + maxvalueSelected);
        Log.e("minvalueSelected", "minvalueSelected==" + minvalueSelected);

        holder.binding.slider.setValueFrom(minvalue);
        holder.binding.slider.setValueTo(maxvalue);
        holder.binding.slider.setStepSize(duration);
        holder.binding.slider.setValues(minvalueSelected, maxvalueSelected);


        holder.binding.slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                holder.binding.txtMaxValue.setText(slider.getValues().get(1).toString());
                holder.binding.txtMinValue.setText(slider.getValues().get(0).toString());
            }
        });

        holder.binding.slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                // holder.binding.txtMaxValue
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                mArrayList.get(position).setMinSelected(slider.getValues().get(0).toString());
                mArrayList.get(position).setMaxSelected(slider.getValues().get(1).toString());
            }
        });

      /*  holder.binding.slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance("USD"));
                Log.e("value", "value==" + value);
                return String.format(Locale.US, "%.0f", value);
            }
        });*/

        holder.binding.txtLabel.setText(mArrayList.get(position).getLable());
        holder.binding.txtMinValue.setText(mArrayList.get(position).getMin());
        holder.binding.txtMaxValue.setText(mArrayList.get(position).getMax());

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {
        public ProductVlaueItemBinding binding;

        public DataViewHolder(ProductVlaueItemBinding itemView) {
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

                    ArrayList<ProductValueModel> filteredList = new ArrayList<>();

                    for (ProductValueModel androidVersion : filterArray) {

                        if (androidVersion.getMin().toLowerCase().contains(charString)) {
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
                mArrayList = (ArrayList<ProductValueModel>) filterResults.values;
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


