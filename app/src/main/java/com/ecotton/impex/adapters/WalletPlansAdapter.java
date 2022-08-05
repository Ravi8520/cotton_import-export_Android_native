package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.google.android.material.card.MaterialCardView;

public class WalletPlansAdapter extends RecyclerView.Adapter<WalletPlansAdapter.PlansViewHolder> {

    Context mcontext;
    int row_index = -1;

    public WalletPlansAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.mywallet_plans, parent, false);
        return new PlansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlansViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.linMain.setBackgroundResource(R.drawable.spinner_border);
        }else{
            holder.linMain.setBackgroundResource(R.drawable.textview_border);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class PlansViewHolder extends RecyclerView.ViewHolder {


        LinearLayout linMain;

        public PlansViewHolder(@NonNull View itemView) {
            super(itemView);

            linMain = itemView.findViewById(R.id.linMain);
        }
    }
}
