package com.absensi.alpa.module.absence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.module.home.HomeActivity;

public class AbsenceFragment extends Fragment implements View.OnClickListener {

    private TextView tvDay, tvDate, btnCheckIn, btnCheckOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absent, container, false);

        this.init(view);
        return view;
    }

    private void init(View view) {
        this.tvDay = view.findViewById(R.id.tvDay);
        this.tvDate = view.findViewById(R.id.tvDate);

        this.btnCheckIn = view.findViewById(R.id.btnCheckIn);
        this.btnCheckIn.setOnClickListener(this);

        this.btnCheckOut = view.findViewById(R.id.btnCheckOut);
        this.btnCheckOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnCheckIn)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new AbsenceDetailFragment(0));
        } else if (v.equals(btnCheckOut)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new AbsenceDetailFragment(1));
        }
    }
}