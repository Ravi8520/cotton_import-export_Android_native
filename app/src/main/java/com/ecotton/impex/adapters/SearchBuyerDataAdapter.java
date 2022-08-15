package com.ecotton.impex.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.activities.PostDetailActivity;
import com.ecotton.impex.activities.UpdateCompanyDetailsActivity;
import com.ecotton.impex.databinding.SearchbuyerDataItemBinding;
import com.ecotton.impex.models.SearchSellerModel;
import com.ecotton.impex.utils.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchBuyerDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SearchSellerModel.Data> mArrayList;
    private OnItemClickListener onItemClickListener;
    private SessionUtil sessionUtil;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isMoreLoading = true;
    public ArrayList<SearchSellerModel.Data> filterArray = new ArrayList<>();

    public SearchBuyerDataAdapter(Context mcontext) {
        this.context = mcontext;
        this.mArrayList = new ArrayList<>();
        sessionUtil = new SessionUtil(context);
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

    public void addAllClass(List<SearchSellerModel.Data> models) {
        mArrayList.clear();
        filterArray.clear();
        mArrayList.addAll(models);
        filterArray.addAll(models);
        notifyDataSetChanged();
    }

    public void addItemMore(List<SearchSellerModel.Data> lst) {
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


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_ITEM) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            SearchbuyerDataItemBinding itemCardBinding = SearchbuyerDataItemBinding.inflate(layoutInflater, parent, false);
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

        if (sessionUtil.getUsertype().equals("buyer")) {
            holder.binding.txtImporter.setText("Exporter");
        } else {
            holder.binding.txtImporter.setText("Importer");
        }
        holder.binding.txtCompanyName.setText(mArrayList.get(position).getCompany_name());
        holder.binding.txtName.setText(mArrayList.get(position).getName());
        holder.binding.txtPrice.setText(context.getString(R.string.lbl_dollar_only) + " " + mArrayList.get(position).getPrice() + "(" + mArrayList.get(position).getRemaining_bales() + ")");
        holder.binding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionUtil.getUsertype().equals("buyer")) {
                    SharedPreferences prefs = context.getSharedPreferences("cotton", MODE_PRIVATE);
                    int idName = prefs.getInt("issetup", 0);
                    if (idName == 0) {
                        CompanyDetailDailog();
                    } else if (idName == 1) {
                        context.startActivity(new Intent(context, PostDetailActivity.class)
                                .putExtra("screen", "home")
                                .putExtra("post_id", mArrayList.get(position).getPost_id() + "")
                                .putExtra("post_type", "post"));
                    }
                } else {
                    SharedPreferences prefs = context.getSharedPreferences("cotton", MODE_PRIVATE);
                    int idName = prefs.getInt("issetup", 0);
                    if (idName == 0) {
                        CompanyDetailDailog();
                    } else if (idName == 1) {
                        context.startActivity(new Intent(context, PostDetailActivity.class)
                                .putExtra("screen", "home")
                                .putExtra("post_id", mArrayList.get(position).getPost_id() + "")
                                .putExtra("post_type", "post"));
                    }

                }

            }
        });
    }

    class DataViewHolder extends RecyclerView.ViewHolder {
        public SearchbuyerDataItemBinding binding;

        public DataViewHolder(SearchbuyerDataItemBinding itemView) {
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
    public int getItemCount() {
        return mArrayList.size();
    }

    public void CompanyDetailDailog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.company_details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = (TextView) dialog.findViewById(R.id.txt_ok);

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.startActivity(new Intent(context, UpdateCompanyDetailsActivity.class));
            }
        });

        dialog.show();
    }
}
