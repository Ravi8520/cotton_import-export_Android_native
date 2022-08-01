package com.ecotton.impex.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.ecotton.impex.R;


public class ContractFragment extends Fragment {

    Button pick_date_button, btn_sellerwise, btn_brokerwise, btn_productwise;
    TextView seller_wise_date, seller_name_txt, broker_Title, companyname_lbl, companyname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_contract, container, false);

        pick_date_button = view.findViewById(R.id.pick_date_button);
        seller_wise_date = view.findViewById(R.id.seller_wise_date);
        btn_sellerwise = view.findViewById(R.id.btn_sellerwise);
        btn_brokerwise = view.findViewById(R.id.btn_brokerwise);
        btn_productwise = view.findViewById(R.id.btn_productwise);
        seller_name_txt = view.findViewById(R.id.seller_name_txt);
        broker_Title = view.findViewById(R.id.broker_Title);
        companyname_lbl = view.findViewById(R.id.companyname_lbl);
        companyname = view.findViewById(R.id.companyname);


        btn_sellerwise.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                companyname_lbl.setVisibility(View.VISIBLE);
                companyname.setVisibility(View.VISIBLE);
                seller_name_txt.setText("John Deo");
                broker_Title.setText("Seller");
                if (view.getId() == R.id.btn_sellerwise) {

                    btn_sellerwise.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_sellerwise.setTextColor(getResources().getColor(R.color.white));

                    btn_brokerwise.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_brokerwise.setTextColor(getResources().getColor(R.color.black));

                    btn_productwise.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_productwise.setTextColor(getResources().getColor(R.color.black));
                }


            }
        });
        btn_brokerwise.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                companyname_lbl.setVisibility(View.VISIBLE);
                companyname.setVisibility(View.VISIBLE);
                seller_name_txt.setText("All");
                broker_Title.setText("Broker");
                if (view.getId() == R.id.btn_brokerwise) {

                    btn_brokerwise.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_brokerwise.setTextColor(getResources().getColor(R.color.white));

                    btn_sellerwise.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_sellerwise.setTextColor(getResources().getColor(R.color.black));

                    btn_productwise.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_productwise.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        btn_productwise.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                companyname_lbl.setVisibility(View.GONE);
                companyname.setVisibility(View.GONE);
                seller_name_txt.setText("Maria Smith");
                broker_Title.setText("Product");

                if (view.getId() == R.id.btn_productwise) {
                    btn_productwise.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btn_productwise.setTextColor(getResources().getColor(R.color.white));

                    btn_brokerwise.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_brokerwise.setTextColor(getResources().getColor(R.color.black));

                    btn_sellerwise.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btn_sellerwise.setTextColor(getResources().getColor(R.color.black));
                }

            }
        });

        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().
                setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();


        pick_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        pick_date_button.setText(materialDatePicker.getHeaderText());
                        seller_wise_date.setText(materialDatePicker.getHeaderText());
                    }
                });
            }
        });
        return view;
    }
}