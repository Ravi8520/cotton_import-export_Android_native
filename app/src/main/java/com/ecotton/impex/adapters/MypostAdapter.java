package com.ecotton.impex.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ecotton.impex.fragments.ActiveFragment;
import com.ecotton.impex.fragments.CompleteFragment;

public class MypostAdapter extends FragmentPagerAdapter {

    int selected_Tab;

    public MypostAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        selected_Tab = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return new ActiveFragment();
            case 1 : return new CompleteFragment();

            default: return null;

        }

    }

    @Override
    public int getCount() {
        return selected_Tab;
    }
}
