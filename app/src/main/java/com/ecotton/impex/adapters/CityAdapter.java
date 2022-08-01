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

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.DistrictHolder> {
    private Context context;
    private List<SearchSellerModel.City> arrayList;
    private OnItemClickListener onItemClickListener;
    private SessionUtil sessionUtil;

    public CityAdapter(Context mcontext, List<SearchSellerModel.City> city) {
        this.arrayList = city;
        this.context = mcontext;
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
        RecyclerView recycler_data;

        public DistrictHolder(@NonNull View itemView) {
            super(itemView);
            txt_district_name = itemView.findViewById(R.id.txt_district_name);
            recycler_data = itemView.findViewById(R.id.recycler_data);
        }
    }

    @NonNull
    @Override
    public DistrictHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distict_item, parent, false);

        return new DistrictHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictHolder holder, int position) {
        holder.txt_district_name.setText(arrayList.get(position).getName() + " " + "(" + arrayList.get(position).getCount() + ")");
        SearchBuyerDataAdapter searchBuyerDataAdapter = new SearchBuyerDataAdapter(context, arrayList.get(position).getData());
        holder.recycler_data.setLayoutManager(new LinearLayoutManager(context));
        holder.recycler_data.setAdapter(searchBuyerDataAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
