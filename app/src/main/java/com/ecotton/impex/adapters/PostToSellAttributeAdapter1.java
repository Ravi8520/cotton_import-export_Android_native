package com.ecotton.impex.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.databinding.PosttosellattributeItemBinding;
import com.ecotton.impex.materialspinner.MaterialSpinner;
import com.ecotton.impex.models.ProductAttributeModel;

import java.util.ArrayList;
import java.util.List;

public class PostToSellAttributeAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    Context mcontext;
    public ArrayList<ProductAttributeModel> mArrayList;
    private OnItemClickListener onItemClickListener;
    private OnItemClickListener1 onItemClickListener1;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<ProductAttributeModel> filterArray = new ArrayList<>();


    public PostToSellAttributeAdapter1(Context mcontext) {
        this.mcontext = mcontext;
        mArrayList = new ArrayList<>();

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, String attributs, String value);
    }

    public interface OnItemClickListener1 {
        public void onItemClick1(View view, String attributs, String value);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public void setOnItemClickListener1(OnItemClickListener1 myClickListener) {
        this.onItemClickListener1 = myClickListener;
    }

    public void setOnLoadMore(OnLoadMoreListener myClickListener) {
        this.onLoadMoreListener = myClickListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void addAllClass(List<ProductAttributeModel> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<ProductAttributeModel> lst) {
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
            PosttosellattributeItemBinding itemCardBinding = PosttosellattributeItemBinding.inflate(layoutInflater, parent, false);
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
        holder.binding.txtLabel.setText(mArrayList.get(position).getLabel());

        ArrayList<String> minAttribute = new ArrayList<>();
        for (ProductAttributeModel.Value obj : mArrayList.get(position).getStateModelList()) {
            minAttribute.add(obj.getLabel());
        }
        holder.binding.spinnerAttribute.setItems(minAttribute);
        holder.binding.spinnerAttribute.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int i, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                onItemClickListener1.onItemClick1(view, mArrayList.get(position).getLabel(), mArrayList.get(position).getStateModelList().get(i).getLabel());

            }
        });
        holder.binding.spinnerAttribute.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

    /*    SppinerAdapter adapter = new SppinerAdapter(mcontext, R.layout.spinner_layout, R.id.txt_company_name, mArrayList.get(position).getStateModelList());
        holder.binding.spinnerAttribute.setAdapter(adapter);
        holder.binding.spinnerAttribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                onItemClickListener.onItemClick(view, mArrayList.get(position).getLabel(), mArrayList.get(position).getStateModelList().get(pos).getLabel());

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        if (mArrayList.get(position).getIs_double() == 0) {
            holder.binding.layoutProductMax.setVisibility(View.GONE);
            holder.binding.layoutMain.setWeightSum(2);
            holder.binding.layoutProduct.setGravity(Gravity.CENTER);
        }

        ArrayList<String> maxAttribute = new ArrayList<>();
        for (ProductAttributeModel.Value obj : mArrayList.get(position).getStateModelList()) {
            maxAttribute.add(obj.getLabel());
        }
        holder.binding.maxSpinnerAttribute.setItems(maxAttribute);
        holder.binding.maxSpinnerAttribute.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int i, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                onItemClickListener1.onItemClick1(view, mArrayList.get(position).getLabel(), mArrayList.get(position).getStateModelList().get(i).getLabel());

            }
        });
        holder.binding.maxSpinnerAttribute.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                //Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        /*SppinerAdapter1 adapter1 = new SppinerAdapter1(mcontext, R.layout.spinner_layout, R.id.txt_company_name, mArrayList.get(position).getStateModelList());
        holder.binding.maxSpinnerAttribute.setAdapter(adapter1);
        holder.binding.maxSpinnerAttribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                onItemClickListener1.onItemClick1(view, mArrayList.get(position).getLabel(), mArrayList.get(position).getStateModelList().get(i).getLabel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        public PosttosellattributeItemBinding binding;

        public DataViewHolder(PosttosellattributeItemBinding itemView) {
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

                    ArrayList<ProductAttributeModel> filteredList = new ArrayList<>();

                    for (ProductAttributeModel androidVersion : filterArray) {

                        if (androidVersion.getLabel().toLowerCase().contains(charString)) {
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
                mArrayList = (ArrayList<ProductAttributeModel>) filterResults.values;
                if (mArrayList.size() > 0) {
                    //  mListener.onListEmpty(true);
                } else {
                    // mListener.onListEmpty(false);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class SppinerAdapter extends ArrayAdapter<ProductAttributeModel.Value> {

        LayoutInflater flater;

        public SppinerAdapter(Context context, int resouceId, int textviewId, List<ProductAttributeModel.Value> list) {

            super(context, resouceId, textviewId, list);
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            ProductAttributeModel.Value rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getLabel());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }

    public class SppinerAdapter1 extends ArrayAdapter<ProductAttributeModel.Value> {

        LayoutInflater flater;

        public SppinerAdapter1(Context context, int resouceId, int textviewId, List<ProductAttributeModel.Value> list) {

            super(context, resouceId, textviewId, list);
//        flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            ProductAttributeModel.Value rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_layout, null, false);

                holder.txtTitle = rowview.findViewById(R.id.txt_company_name);

                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getLabel());

            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }
}



