package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.models.MCXModel;

import java.util.ArrayList;

public class MCXAdapter extends RecyclerView.Adapter<MCXAdapter.McxDataHolder> {

    Context mcontext;
    public ArrayList<MCXModel> mcxdatalist;

    public MCXAdapter(Context mcontext, ArrayList<MCXModel> mcxdatalist) {
        this.mcontext = mcontext;
        this.mcxdatalist = mcxdatalist;
    }

    @NonNull
    @Override
    public McxDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.custome_mcx_layout, parent, false);
        return new McxDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull McxDataHolder holder, int position) {
        MCXModel obj = mcxdatalist.get(position);
        holder.tvName.setText(obj.getName());
        holder.tv_pricetitle.setText(obj.getCurrent_price() + "");
        holder.tv_openprice.setText(obj.getOpen_price() + "");
        holder.tv_highprice.setText(obj.getHigh_price() + "");
        holder.tv_lowprice.setText(obj.getLow_price() + "");
        holder.tv_prev_close_price.setText(obj.getClose_price() + "");
        int updown = obj.getCurrent_price() - obj.getClose_price();
        if (obj.getCurrent_price() < obj.getClose_price()) {
            holder.tv_updown.setTextColor(mcontext.getResources().getColor(R.color.dark_red));
        } else if (obj.getCurrent_price() > obj.getClose_price()) {
            holder.tv_updown.setTextColor(mcontext.getResources().getColor(R.color.colorPrimary));
        }
        holder.tv_updown.setText(updown + "");


    }

    @Override
    public int getItemCount() {
        return mcxdatalist.size();
    }

    public class McxDataHolder extends RecyclerView.ViewHolder {

        TextView tvName, tv_date, tv_pricetitle, tv_updown, tv_openprice, tv_highprice, tv_lowprice, tv_prev_close_price;

        public McxDataHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_pricetitle = itemView.findViewById(R.id.tv_pricetitle);
            tv_updown = itemView.findViewById(R.id.tv_updown);
            tv_openprice = itemView.findViewById(R.id.tv_openprice);
            tv_highprice = itemView.findViewById(R.id.tv_highprice);
            tv_lowprice = itemView.findViewById(R.id.tv_lowprice);
            tv_prev_close_price = itemView.findViewById(R.id.tv_prev_close_price);
        }
    }
}
