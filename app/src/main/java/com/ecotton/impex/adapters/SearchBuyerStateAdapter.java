package com.ecotton.impex.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.models.SearchSellerModel;

import java.util.List;

public class SearchBuyerStateAdapter extends RecyclerView.Adapter<SearchBuyerStateAdapter.StateHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<SearchSellerModel.State>arrayList;
    public SearchBuyerStateAdapter(Context mcontext, List<SearchSellerModel.State> stateModelList) {
        this.context=mcontext;
        this.arrayList=stateModelList;
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public class StateHolder extends RecyclerView.ViewHolder {
        TextView txt_state_name;
        RecyclerView recycler_district;

        public StateHolder(@NonNull View itemView) {
            super(itemView);
            txt_state_name = itemView.findViewById(R.id.txt_state_name);
            recycler_district = itemView.findViewById(R.id.recycler_district);
        }
    }

    @NonNull
    @Override
    public StateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item, parent, false);
        return new StateHolder(view);
    }

    @Override
    public void onBindViewHolder(StateHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txt_state_name.setText(arrayList.get(position).getName()+" "+"("+arrayList.get(position).getCount()+")");

        DistrictAdapter districtAdapter = new DistrictAdapter(context, arrayList.get(position).getDistrict());
        holder.recycler_district.setLayoutManager(new LinearLayoutManager(context));
        holder.recycler_district.setAdapter(districtAdapter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
