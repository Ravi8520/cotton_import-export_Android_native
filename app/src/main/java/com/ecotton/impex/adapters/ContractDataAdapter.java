package com.ecotton.impex.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.models.MyContractModel;
import com.google.gson.Gson;
import com.ecotton.impex.R;
import com.ecotton.impex.activities.MyContractDetailsActivity;
import com.ecotton.impex.utils.SessionUtil;

import java.util.List;

public class ContractDataAdapter extends RecyclerView.Adapter<ContractDataAdapter.DataHolder> {
    private Context context;
    private List<MyContractModel.DealDetails> dealDetailsList;
    private OnItemClickListener onItemClickListener;
    private SessionUtil sessionUtil;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public ContractDataAdapter(Context mcontext, List<MyContractModel.DealDetails> deal_details) {
        this.context = mcontext;
        this.dealDetailsList = deal_details;
        sessionUtil = new SessionUtil(context);
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        TextView txt_product_name, txt_company_name, txt_seller_name, txt_amount,sellertitle_txt;
        Button btn_download;
        RelativeLayout layout_main;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_company_name = itemView.findViewById(R.id.txt_company_name);
            txt_seller_name = itemView.findViewById(R.id.txt_seller_name);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            btn_download = itemView.findViewById(R.id.btn_download);
            layout_main = itemView.findViewById(R.id.layout_main);
            sellertitle_txt = itemView.findViewById(R.id.sellertitle_txt);
        }
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contract_data_item, parent, false);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {

        holder.txt_product_name.setText(dealDetailsList.get(position).getProduct_name());
        holder.txt_amount.setText(context.getResources().getString(R.string.lbl_rupees_symbol_only) + " " + dealDetailsList.get(position).getPost_price());


        if (sessionUtil.getUsertype().equals("buyer")) {
            holder.txt_company_name.setText(dealDetailsList.get(position).getSeller_company_name());
            holder.txt_seller_name.setText(dealDetailsList.get(position).getSeller_name());
            holder.sellertitle_txt.setText("Exporter");
        } else {
            holder.txt_company_name.setText(dealDetailsList.get(position).getBuyer_company_name());
            holder.txt_seller_name.setText(dealDetailsList.get(position).getBuyer_name());
            holder.sellertitle_txt.setText("Importer");
        }

        if (sessionUtil.getUsertype().equals("buyer")) {
            if (dealDetailsList.get(position).getIs_buyer_otp_verify() == 0) {
                holder.btn_download.setText(context.getResources().getString(R.string.lbl_pending));
                holder.btn_download.setEnabled(true);
                holder.btn_download.setBackground(context.getResources().getDrawable(R.drawable.custome_button));
            } else if (dealDetailsList.get(position).getIs_buyer_otp_verify() == 1) {
                holder.btn_download.setText(context.getResources().getString(R.string.lbl_pending));
                holder.btn_download.setEnabled(false);
                holder.btn_download.setBackground(context.getResources().getDrawable(R.drawable.custome_button1));
            }
            if (dealDetailsList.get(position).getIs_buyer_otp_verify() == 1 && dealDetailsList.get(position).getIs_seller_otp_verify() == 1) {
                holder.btn_download.setText(context.getResources().getString(R.string.download));
                holder.btn_download.setEnabled(true);
                holder.btn_download.setBackground(context.getResources().getDrawable(R.drawable.custome_button));
            }
        }

        if (sessionUtil.getUsertype().equals("seller")) {
            if (dealDetailsList.get(position).getIs_seller_otp_verify() == 0) {
                holder.btn_download.setText(context.getResources().getString(R.string.lbl_pending));
                holder.btn_download.setEnabled(true);
                holder.btn_download.setBackground(context.getResources().getDrawable(R.drawable.custome_button));
            } else if (dealDetailsList.get(position).getIs_seller_otp_verify() == 1) {
                holder.btn_download.setText(context.getResources().getString(R.string.lbl_pending));
                holder.btn_download.setEnabled(false);
                holder.btn_download.setBackground(context.getResources().getDrawable(R.drawable.custome_button1));
            }
            if (dealDetailsList.get(position).getIs_buyer_otp_verify() == 1 && dealDetailsList.get(position).getIs_seller_otp_verify() == 1) {
                holder.btn_download.setText(context.getResources().getString(R.string.download));
                holder.btn_download.setEnabled(true);
                holder.btn_download.setBackground(context.getResources().getDrawable(R.drawable.custome_button));
            }
        }
        holder.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String attribute = gson.toJson(dealDetailsList.get(position).getAttribute_array());
                if (dealDetailsList.get(position).getIs_buyer_otp_verify() == 1 && dealDetailsList.get(position).getIs_seller_otp_verify() == 1) {
                    Intent intent = new Intent(context, MyContractDetailsActivity.class);
                    intent.putExtra("deal_id", dealDetailsList.get(position).getDeal_id());
                    intent.putExtra("attribute", attribute);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dealDetailsList.size();
    }
}
