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

import com.ecotton.impex.databinding.FragmentGinningBinding;
import com.ecotton.impex.utils.AppUtil;

import org.jetbrains.annotations.NotNull;

public class GinningFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public FragmentGinningBinding binding;
    public double kapas = 0, expenses = 0, cottonSeed = 0, outTurn = 0, shortage = 0;

    public GinningFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GinningFragment newInstance(String param1, String param2) {
        GinningFragment fragment = new GinningFragment();
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
        binding = FragmentGinningBinding.inflate(getLayoutInflater());

        binding.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.hideSoftKeyboard(getActivity());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etKapas.addTextChangedListener(new TextWatcher() {
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
                    kapas = Double.parseDouble(s.toString());
                else
                    kapas = 0;
                calculateGinning();
            }
        });
        binding.etExpenses.addTextChangedListener(new TextWatcher() {
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
        binding.etCottonSeed.addTextChangedListener(new TextWatcher() {
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
                    cottonSeed = Double.parseDouble(s.toString());
                else
                    cottonSeed = 0;
                calculateGinning();
            }
        });
        binding.etOurTurn.addTextChangedListener(new TextWatcher() {
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
                    outTurn = Double.parseDouble(s.toString());
                else
                    outTurn = 0;
                calculateGinning();
            }
        });
        binding.etSortage.addTextChangedListener(new TextWatcher() {
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
                    shortage = Double.parseDouble(s.toString());
                else
                    shortage = 0;
                calculateGinning();
            }
        });


    }


    public void calculateGinning() {
        double t1 = cottonSeed * (100 - (outTurn + shortage));
        double t2 = (kapas + expenses) * 100;
        double t3 = t1 - t2;
        t3 = Math.abs(t3);
        double t4 = t3 * 355.6;
        double t5 = 20 * outTurn;
        double result = t4 / t5;
        binding.cocPrice.setText(Math.round(result) + "");
    }
}