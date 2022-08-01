package com.ecotton.impex.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecotton.impex.R;
import com.ecotton.impex.activities.LoginAsActivity;
import com.ecotton.impex.models.companylist.CompanyListModel;

import java.util.List;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.CompanyHolder> {
    Context context;
    public List<CompanyListModel> companyListModels;

    public CompanyListAdapter(Context mContext, List<CompanyListModel> companyListModelList) {
        this.context = mContext;
        this.companyListModels = companyListModelList;
    }

    public class CompanyHolder extends RecyclerView.ViewHolder {
        TextView txt_company_name,firsLetter, secondLetter;

        public CompanyHolder(@NonNull View itemView) {
            super(itemView);
            txt_company_name = itemView.findViewById(R.id.txt_company_name);
            firsLetter = itemView.findViewById(R.id.firs_letter);
            secondLetter = itemView.findViewById(R.id.second_letter);
        }
    }

    @NonNull
    @Override
    public CompanyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_itme, parent, false);

        return new CompanyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyHolder holder, int position) {

        CompanyListModel companyListModel = companyListModels.get(position);
        holder.txt_company_name.setText(companyListModel.getCompany_name());
       // holder.txt_use_name.setText(companyListModel.getCompany_type());

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("cotton", MODE_PRIVATE).edit();
                editor.putInt("issetup", companyListModel.getIs_setupped());
                editor.apply();
                Intent intent = new Intent(context, LoginAsActivity.class);
                intent.putExtra(LoginAsActivity.COMPANY_ID, companyListModel.getCompany_id());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("company_id", "company_id==" + companyListModels.size());
        return companyListModels.size();
    }
}
