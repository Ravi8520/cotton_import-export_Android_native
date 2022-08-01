package com.ecotton.impex.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecotton.impex.databinding.FragmentExportsBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;


public class ExportsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public FragmentExportsBinding binding;
    public double cottonRate = 0, expenses = 0, exchangeRate = 0;

    public ExportsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ExportsFragment newInstance(String param1, String param2) {
        ExportsFragment fragment = new ExportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExportsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etCotonratecandy.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    cottonRate = Double.parseDouble(s.toString());
                else
                    cottonRate = 0;
                calculateGinning();
            }
        });
        binding.etExportExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    expenses = Double.parseDouble(s.toString());
                else
                    expenses = 0;
                calculateGinning();
            }
        });
        binding.etExcgRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0)
                    exchangeRate = Double.parseDouble(s.toString());
                else
                    exchangeRate = 0;
                calculateGinning();
            }
        });
    }
    public void calculateGinning() {
        double t1 = cottonRate + expenses;
        double t2 = t1 * 100;
        double t3 = 355.6 * exchangeRate;
        double t4 = t3 * 2.205;

        double result = t2 / t4;
        binding.tvExportrate.setText(new DecimalFormat("##.##").format(result) );
    }
}