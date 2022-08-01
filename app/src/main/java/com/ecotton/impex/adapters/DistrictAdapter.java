package com.ecotton.impex.adapters;

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
import com.ecotton.impex.utils.SessionUtil;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictHolder> {

    private List<SearchSellerModel.District> modelArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    SessionUtil sessionUtil;

    public DistrictAdapter(Context mcontext, List<SearchSellerModel.District> district) {
        this.context = mcontext;
        this.modelArrayList = district;
        sessionUtil = new SessionUtil(context);
    }


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public class DistrictHolder extends RecyclerView.ViewHolder {

        TextView txt_district_name;
        RecyclerView recycler_data,recycler_city;

        public DistrictHolder(@NonNull View itemView) {
            super(itemView);
            txt_district_name = itemView.findViewById(R.id.txt_district_name);
            recycler_data = itemView.findViewById(R.id.recycler_data);
            recycler_city = itemView.findViewById(R.id.recycler_city);
        }
    }

    @NonNull
    @Override
    public DistrictHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (sessionUtil.getUsertype().equals("buyer")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        } else if (sessionUtil.getUsertype().equals("seller")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distict_item, parent, false);
        }

        return new DistrictHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictHolder holder, int position) {
        holder.txt_district_name.setText(modelArrayList.get(position).getName()+" "+"("+modelArrayList.get(position).getCount()+")");

        if (sessionUtil.getUsertype().equals("buyer")) {
            CityAdapter cityAdapter=new CityAdapter(context,modelArrayList.get(position).getCity());
            holder.recycler_city.setLayoutManager(new LinearLayoutManager(context));
            holder.recycler_city.setAdapter(cityAdapter);
        } else {
            SearchBuyerDataAdapter searchBuyerDataAdapter = new SearchBuyerDataAdapter(context, modelArrayList.get(position).getData());
            holder.recycler_data.setLayoutManager(new LinearLayoutManager(context));
            holder.recycler_data.setAdapter(searchBuyerDataAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }
}
