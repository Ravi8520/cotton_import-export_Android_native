package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.models.AttributeModelFromTo;

import java.util.List;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.AttributeHolder> {
    Context context;
    List<AttributeModelFromTo> attributeModelList;

    public AttributeAdapter(Context mContext, List<AttributeModelFromTo> attributeRequestModels) {
        this.context = mContext;
        this.attributeModelList = attributeRequestModels;
    }

    public class AttributeHolder extends RecyclerView.ViewHolder {

        TextView attribute, to, from;

        public AttributeHolder(@NonNull View itemView) {
            super(itemView);
            attribute = itemView.findViewById(R.id.txt_attribute);
            to = itemView.findViewById(R.id.txt_to);
            from = itemView.findViewById(R.id.txt_from);
        }
    }

    @NonNull
    @Override
    public AttributeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attribute_horizontal_item, parent, false);

        return new AttributeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttributeHolder holder, int position) {

        AttributeModelFromTo attributeModel = attributeModelList.get(position);

        holder.attribute.setText(attributeModel.getAttribute());
        holder.to.setText(attributeModel.getTo());
        holder.from.setText(attributeModel.getFrom());
    }

    @Override
    public int getItemCount() {
        return attributeModelList.size();
    }
}
