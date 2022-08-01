package com.ecotton.impex.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.models.MyPostActiveModel;

import java.util.List;

public class ActivePostAttributeAdapter extends RecyclerView.Adapter<ActivePostAttributeAdapter.AttributeHolder> {
    private Context context;
    List<MyPostActiveModel.Attribute> myPostActiveModelArrayList;

    public ActivePostAttributeAdapter(Context mcontext, List<MyPostActiveModel.Attribute> mArrayList) {
        this.context = mcontext;
        this.myPostActiveModelArrayList = mArrayList;
    }

    public class AttributeHolder extends RecyclerView.ViewHolder {

        TextView txt_attribute, txt_from;

        public AttributeHolder(@NonNull View itemView) {
            super(itemView);
            txt_attribute = itemView.findViewById(R.id.txt_attribute);
            txt_from = itemView.findViewById(R.id.txt_from);
        }
    }

    @NonNull
    @Override
    public AttributeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypost_attribute_horizontal_item, parent, false);

        return new AttributeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttributeHolder holder, int position) {
        if (myPostActiveModelArrayList != null) {
            holder.txt_attribute.setText(myPostActiveModelArrayList.get(position).getAttribute());
            holder.txt_from.setText(myPostActiveModelArrayList.get(position).getAttribute_value());
            holder.txt_from.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public int getItemCount() {

        return myPostActiveModelArrayList.size();
    }
}
