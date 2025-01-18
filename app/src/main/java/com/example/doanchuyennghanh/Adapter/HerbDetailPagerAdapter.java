package com.example.doanchuyennghanh.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.Domain.Medicine;
import com.example.doanchuyennghanh.Fragment.GeneralInfoFragment;
import com.example.doanchuyennghanh.Fragment.InteractionsFragment;
import com.example.doanchuyennghanh.Fragment.MoreFragment;

public class HerbDetailPagerAdapter extends FragmentStateAdapter {

    private final Herb selectedMedicine;

    public HerbDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, Herb selectedMedicine) {
        super(fragmentActivity);
        this.selectedMedicine = selectedMedicine;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new GeneralInfoFragment();
                break;
            case 1: {
                fragment = new InteractionsFragment();
                break;
            }
            case 2:
                fragment = new MoreFragment();
                break;
            default:
                throw new IllegalArgumentException("Invalid tab position");
        }

        // Pass the selectedMedicine object to each fragment
        Bundle args = new Bundle();
        args.putSerializable("medicine", selectedMedicine);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

