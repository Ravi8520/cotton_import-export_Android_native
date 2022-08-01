package com.ecotton.impex.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ecotton.impex.fragments.ContractFragment;
import com.ecotton.impex.fragments.PostFragment;

public class ReportsAdapter extends FragmentPagerAdapter {


    int selected_Tab;

    public ReportsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        selected_Tab = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return new PostFragment();
            case 1 : return new ContractFragment();

            default: return null;

        }
    }

    @Override
    public int getCount() {
        return selected_Tab;
    }
}
