package com.ecotton.impex.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.activities.PostDetailActivity;
import com.ecotton.impex.models.SearchSellerModel;
import com.ecotton.impex.utils.SessionUtil;

import java.util.List;

public class SearchBuyerDataAdapter extends RecyclerView.Adapter<SearchBuyerDataAdapter.DataHolder> {
    private Context context;
    private List<SearchSellerModel.Data> arrayList;
    private OnItemClickListener onItemClickListener;
    private SessionUtil sessionUtil;

    public SearchBuyerDataAdapter(Context mcontext, List<SearchSellerModel.Data> data) {
        this.context = mcontext;
        this.arrayList = data;
        sessionUtil = new SessionUtil(context);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        TextView txt_company_name, txt_buyer_name, txt_price, txt_beale;
        Button btn_view;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            btn_view = itemView.findViewById(R.id.btn_view);
            txt_company_name = itemView.findViewById(R.id.txt_company_name);
            txt_buyer_name = itemView.findViewById(R.id.txt_buyer_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_beale = itemView.findViewById(R.id.txt_beale);
        }
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchbuyer_data_item, parent, false);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (sessionUtil.getUsertype().equals("buyer")) {
            holder.txt_company_name.setText(arrayList.get(position).getCompany_name());
            holder.txt_buyer_name.setText(arrayList.get(position).getName());
            holder.txt_price.setText(context.getResources().getString(R.string.lbl_rupees_symbol_only) + arrayList.get(position).getPrice());
            holder.txt_beale.setText("(" + arrayList.get(position).getRemaining_bales() + ")");
        } else if (sessionUtil.getUsertype().equals("seller")) {
            holder.txt_company_name.setText(arrayList.get(position).getCompany_name());
            holder.txt_buyer_name.setText(arrayList.get(position).getName());
            holder.txt_price.setText(context.getResources().getString(R.string.lbl_rupees_symbol_only) + arrayList.get(position).getPrice());
            holder.txt_beale.setText("(" + arrayList.get(position).getRemaining_bales() + ")");
        }

        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionUtil.getUsertype().equals("buyer")) {
                    context.startActivity(new Intent(context, PostDetailActivity.class)
                            .putExtra("screen", "home")
                            .putExtra("post_id", arrayList.get(position).getPost_id() + "")
                            .putExtra("post_type", "post"));
                } else {
                    context.startActivity(new Intent(context, PostDetailActivity.class)
                            .putExtra("screen", "home")
                            .putExtra("post_id", arrayList.get(position).getPost_id() + "")
                            .putExtra("post_type", "post"));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
