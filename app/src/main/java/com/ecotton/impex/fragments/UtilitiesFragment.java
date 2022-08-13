package com.ecotton.impex.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ecotton.impex.R;
import com.ecotton.impex.activities.BuyerActivity;
import com.ecotton.impex.activities.CalculatorActivity;
import com.ecotton.impex.activities.MCXActivity;
import com.ecotton.impex.activities.NewsActivity;
import com.ecotton.impex.utils.SessionUtil;



public class UtilitiesFragment extends Fragment {
    RelativeLayout rl_buyer_dir, rl_mcx, rl_calculator, rl_news;
    TextView txt_buyer_directory;

    private Context mContext;
    SessionUtil mSessionUtil;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_utilities, container, false);

        rl_buyer_dir = view.findViewById(R.id.rl_buyer_dir);
        txt_buyer_directory = view.findViewById(R.id.txt_buyer_directory);
        rl_mcx = view.findViewById(R.id.rl_mcx);
        rl_calculator = view.findViewById(R.id.rl_calculator);
        rl_news = view.findViewById(R.id.rl_news);
        mSessionUtil = new SessionUtil(mContext);

        if (mSessionUtil.getUsertype().equals("buyer")) {
            txt_buyer_directory.setText(getResources().getString(R.string.lbl_exporter_directory));
        } else {
            txt_buyer_directory.setText(getResources().getString(R.string.lbl_importer_directory));
        }

        rl_buyer_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BuyerActivity.class));
            }
        });

        rl_mcx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MCXActivity.class));
            }
        });

        rl_calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, CalculatorActivity.class));
            }
        });

        rl_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, NewsActivity.class));
            }
        });
        return view;
    }
}