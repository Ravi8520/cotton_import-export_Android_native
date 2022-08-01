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

import com.ecotton.impex.databinding.FragmentSpinningBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpinningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinningFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public FragmentSpinningBinding binding;
    public double yarnCount = 0, autoCottoRatekg = 0, yield = 0, wasteRecovery = 0, conversionCost = 0, cottonRate = 0;

    public SpinningFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SpinningFragment newInstance(String param1, String param2) {
        SpinningFragment fragment = new SpinningFragment();
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
        binding = FragmentSpinningBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etYarnCount.addTextChangedListener(new TextWatcher() {
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
                    yarnCount = Double.parseDouble(s.toString());
                else
                    yarnCount = 0;
                calculateSpinning();
            }
        });
        binding.etCottonrate.addTextChangedListener(new TextWatcher() {
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
                calculateCottonrate();
            }
        });
        binding.etYield.addTextChangedListener(new TextWatcher() {
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
                    yield = Double.parseDouble(s.toString());
                else
                    yield = 0;
                calculateSpinning();
            }
        });
        binding.etWrecovery.addTextChangedListener(new TextWatcher() {
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
                    wasteRecovery = Double.parseDouble(s.toString());
                else
                    wasteRecovery = 0;
                calculateSpinning();
            }
        });
        binding.etConversioncost.addTextChangedListener(new TextWatcher() {
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
                    conversionCost = Double.parseDouble(s.toString());
                else
                    conversionCost = 0;
                calculateSpinning();
            }
        });
    }

    public void calculateSpinning() {
//formula Cotton one kg rate + ((100-yield)% of One kg cotton rate)+(count \'d7 conversation cost)- waste recovery\
        double firstStep = autoCottoRatekg / (yield / 100);
        double secondStep = firstStep - wasteRecovery;
        double result = Math.abs(secondStep);

        binding.tvYarnCost.setText(result+"" );
    }

    public void calculateCottonrate() {
        double t1 = cottonRate / 356;
        binding.etCottonrateKg.setText(new DecimalFormat("##.##").format(t1));
        autoCottoRatekg = Double.parseDouble(binding.etCottonrateKg.getText().toString());
        calculateSpinning();
    }
}