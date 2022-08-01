package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;

public class WalletPlansAdapter extends RecyclerView.Adapter<WalletPlansAdapter.PlansViewHolder> {

    Context mcontext;

    public WalletPlansAdapter(Context mcontext) {
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.mywallet_plans,parent,false);
        return new PlansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlansViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class PlansViewHolder extends RecyclerView.ViewHolder {


        TextView validity_txt, amount_txt;
        Button btn_buy;
        public PlansViewHolder(@NonNull View itemView) {
            super(itemView);

            btn_buy = itemView.findViewById(R.id.btn_buy);
            validity_txt = itemView.findViewById(R.id.validity_txt);
            amount_txt = itemView.findViewById(R.id.amount_txt);
        }
    }
}
