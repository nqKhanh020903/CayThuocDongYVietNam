package com.example.doanchuyennghanh.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doanchuyennghanh.Activity.HerbDetail_Activity;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.Domain.Medicine;
import com.example.doanchuyennghanh.R;

import java.util.List;

// GeneralInfoFragment.java
public class GeneralInfoFragment extends Fragment {

    private Medicine medicine;
    private Herb herb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_info, container, false);

        // Retrieve the Medicine object from the arguments
        if (getArguments() != null) {
            herb = (Herb) getArguments().getSerializable("medicine");
        }

        // Initialize views and display data
        if (herb != null) {
            setText(view, R.id.tvName, herb.getTenThuoc());
            setText(view, R.id.tvScientificName, herb.getTenKhoaHoc());
            setText(view, R.id.tvFamily, herb.getHo());
            setText(view, R.id.tvPartsUsed, formatList(herb.getBoPhanDung()));
            setText(view, R.id.tvChemicalComponents, formatList(herb.getThanhPhanHoaHocChinh()));
            setText(view, R.id.tvUsage, herb.getCongDung());
            setText(view, R.id.tvDosage, herb.getCachDungVaLieuLuong());
            setText(view, R.id.tvDistribute, herb.getPhanBo());
            setText(view, R.id.tvDescrip, herb.getMoTaCay());
        }

        return view;
    }
    private void setText(View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        if (text == null || text.trim().isEmpty()) {
            textView.setText("Đang cập nhật");
        } else {
            textView.setText(text);
        }
    }
    // Helper method to convert List<String> to a comma-separated String
    private String formatList(List<String> list) {
        if (list == null || list.isEmpty()) return "N/A";
        return String.join(", ", list);
    }

}
