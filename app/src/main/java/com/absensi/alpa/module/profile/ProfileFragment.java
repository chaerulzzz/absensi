package com.absensi.alpa.module.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.profile.ProfileDataResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileService;
import com.absensi.alpa.module.home.DashboardFragment;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Preferences;
import com.absensi.alpa.tools.Tools;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tvName, tvEmail, tvBirthPlace, tvBirthDate;
    private MaterialButton btnEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void init(View view) {
        this.tvName = view.findViewById(R.id.tvName);
        this.tvEmail = view.findViewById(R.id.tvEmail);
        this.tvBirthDate = view.findViewById(R.id.tvBirthDate);
        this.tvBirthPlace = view.findViewById(R.id.tvBirthPlace);
        this.btnEdit = view.findViewById(R.id.btnEdit);
        this.btnEdit.setOnClickListener(this);
    }

    private void setData(){
        String userid = Preferences.getInstance().getValue(Constant.CREDENTIALS.USERID, String.class, "");

        Call<ProfileResponse> responseCall = ProfileService.getProfile(requireActivity(), Constant.URL.USER + "/2");

        responseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProfileResponse> call, @NotNull Response<ProfileResponse> response) {
                if (response.isSuccessful()) {
                    ProfileResponse profileResponse = response.body();

                    if (profileResponse != null) {
                        ProfileDataResponse dataResponse = profileResponse.getData();
                        if (dataResponse != null) {
                            Preferences preferences = Preferences.getInstance();
                            preferences.begin();
                            preferences.put(Constant.CREDENTIALS.EMAIL, dataResponse.getEmail());
                            preferences.put(Constant.CREDENTIALS.NAME, dataResponse.getName());
                            preferences.put(Constant.CREDENTIALS.BIRTH_DATE, dataResponse.getBirthDate());
                            preferences.put(Constant.CREDENTIALS.USERID, dataResponse.getId());
                            preferences.put(Constant.CREDENTIALS.BIRTH_PLACE, dataResponse.getBirthPlace());
                            preferences.commit();

                            tvName.setText(dataResponse.getName());
                            tvEmail.setText(dataResponse.getEmail());
                            tvBirthPlace.setText(dataResponse.getBirthPlace());

                            try {
                                String dateNow = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
                                        .format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dataResponse.getBirthDate())));
                                tvBirthDate.setText(dateNow);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                        Toast.makeText(ProfileFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ProfileFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                Toast.makeText(ProfileFragment.this.getContext(), ProfileFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnEdit)) {
            ProfileEditFragment bottomSheetFragment = new ProfileEditFragment(requireActivity());
            bottomSheetFragment.show (requireActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
        }
    }
}
