package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.models.Plan;

import java.util.ArrayList;
import java.util.List;

public class WalletPlansAdapter extends RecyclerView.Adapter<WalletPlansAdapter.PlansViewHolder> {

    Context mcontext;
    int row_index = -1;
    public List<Plan> planList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    public WalletPlansAdapter(Context mcontext, List<Plan> planArrayList) {
        this.mcontext = mcontext;
        this.planList = planArrayList;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    @NonNull
    @Override
    public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.mywallet_plans, parent, false);
        return new PlansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlansViewHolder holder, int position) {

        holder.validity_txt.setText(planList.get(position).getValidity() + " Days");
        holder.amount_txt.setText(planList.get(position).getPrice() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                onItemClickListener.onItemClick(view, position);
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.linMain.setBackgroundResource(R.drawable.spinner_border);
        } else {
            holder.linMain.setBackgroundResource(R.drawable.textview_border);
        }
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class PlansViewHolder extends RecyclerView.ViewHolder {
        TextView validity_txt, amount_txt;
        LinearLayout linMain;

        public PlansViewHolder(@NonNull View itemView) {
            super(itemView);
            linMain = itemView.findViewById(R.id.linMain);
            validity_txt = itemView.findViewById(R.id.validity_txt);
            amount_txt = itemView.findViewById(R.id.txtPrice);
        }
    }
}
