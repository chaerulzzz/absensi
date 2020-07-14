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
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AbsenceFragment extends Fragment implements View.OnClickListener {

    private TextView tvDay, tvDate;
    private MaterialButton btnCheckIn, btnCheckOut, btnAbsentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absent, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void init(View view) {
        this.tvDay = view.findViewById(R.id.tvDay);
        this.tvDate = view.findViewById(R.id.tvDate);

        this.btnCheckIn = view.findViewById(R.id.btnCheckIn);
        this.btnCheckIn.setOnClickListener(this);

        this.btnCheckOut = view.findViewById(R.id.btnCheckOut);
        this.btnCheckOut.setOnClickListener(this);

        this.btnAbsentList = view.findViewById(R.id.btnLisAbsent);
        this.btnAbsentList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnCheckIn)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new AbsenceDetailFragment(0));
        } else if (v.equals(btnCheckOut)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new AbsenceDetailFragment(1));
        } else if (v.equals(btnAbsentList)) {
            ((HomeActivity)this.requireActivity()).loadFragment(new AbsenceListFragment());
        }
    }

    private void setData(){
        Date date = new Date();
        SimpleDateFormat day = new SimpleDateFormat("EEEE", new Locale("id", "ID"));
        SimpleDateFormat dateNow = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));

        this.tvDay.setText(day.format(date));
        this.tvDate.setText(dateNow.format(date));
    }
}