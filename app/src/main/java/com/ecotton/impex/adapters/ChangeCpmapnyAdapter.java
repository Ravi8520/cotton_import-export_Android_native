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
import com.ecotton.impex.models.companylist.CompanyListModel;
import com.ecotton.impex.utils.SessionUtil;

import java.util.List;

public class ChangeCpmapnyAdapter extends RecyclerView.Adapter<ChangeCpmapnyAdapter.ChangeCpmapnyHolder> {

    Context context;
    public List<CompanyListModel> companyListModels;
    private OnItemClickListener onItemClickListener;
    SessionUtil sessionUtil;

    public ChangeCpmapnyAdapter(Context mContext, List<CompanyListModel> companyListModelList) {
        this.context = mContext;
        this.companyListModels = companyListModelList;
        sessionUtil = new SessionUtil(context);
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener myClickListener) {
        this.onItemClickListener = myClickListener;
    }

    public class ChangeCpmapnyHolder extends RecyclerView.ViewHolder {
        TextView txt_company_name, txt_use_name, txt_buyer, txt_seller, firsLetter, secondLetter;
        LinearLayout layout_login_as;

        public ChangeCpmapnyHolder(@NonNull View itemView) {
            super(itemView);
            txt_company_name = itemView.findViewById(R.id.txt_company_name);
            txt_buyer = itemView.findViewById(R.id.txt_buyer);
            txt_seller = itemView.findViewById(R.id.txt_seller);
            layout_login_as = itemView.findViewById(R.id.layout_login_as);
            firsLetter = itemView.findViewById(R.id.firs_letter);
            secondLetter = itemView.findViewById(R.id.second_letter);
        }
    }

    @NonNull
    @Override
    public ChangeCpmapnyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.change_company_itme, parent, false);
        return new ChangeCpmapnyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeCpmapnyHolder holder, int position) {

        CompanyListModel companyListModel = companyListModels.get(position);
        holder.txt_company_name.setText(companyListModel.getCompany_name());

        String test = companyListModel.getCompany_name();
        String first = String.valueOf(test.charAt(0));

        String str = test;
        String separator = " ";
        int sepPos = str.indexOf(separator);
        if (sepPos == -1) {
            System.out.println("");
        }
        String data = str.substring(sepPos + separator.length());

        String second = String.valueOf(data.charAt(0));

        holder.firsLetter.setText(first);
        holder.secondLetter.setText(second);

        if (companyListModel.getCompany_name().equals(sessionUtil.getCompanyName())) {

            if (sessionUtil.getUsertype().equals("buyer")) {
                holder.txt_buyer.setBackground(context.getDrawable(R.drawable.white_bg));
                holder.txt_buyer.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (sessionUtil.getUsertype().equals("seller")) {
                holder.txt_seller.setBackground(context.getDrawable(R.drawable.white_bg));
                holder.txt_seller.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        holder.txt_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
        holder.txt_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyListModels.size();
    }
}

