package com.absensi.alpa.module.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.GeneralResponse;
import com.absensi.alpa.api.endpoint.logout.LogoutService;
import com.absensi.alpa.api.endpoint.profile.ProfileDataResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileService;
import com.absensi.alpa.module.home.DashboardFragment;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.LoadingDialog;
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
    private ImageView btnLogout;

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

        this.btnLogout = view.findViewById(R.id.btnLogout);
        this.btnLogout.setOnClickListener(this);
    }

    private void setData(){
        LoadingDialog dialog = new LoadingDialog(requireContext());
        dialog.show();

        String userid = Preferences.getInstance().getValue(Constant.CREDENTIALS.USERID, String.class, "");

        Call<ProfileResponse> responseCall = ProfileService.getProfile(requireActivity(), Constant.URL.USER + "/" + userid);

        responseCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProfileResponse> call, @NotNull Response<ProfileResponse> response) {
                try {
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
                                    String dateNow = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"))
                                            .format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID")).parse(dataResponse.getBirthDate())));
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

                            if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.SESSION, "");
                                preferences.commit();

                                requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                requireActivity().finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ProfileFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(requireContext(), requireActivity().getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                } finally {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileResponse> call, @NotNull Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(ProfileFragment.this.getContext(), ProfileFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnEdit)) {
            ProfileEditFragment bottomSheetFragment = new ProfileEditFragment(requireActivity());
            bottomSheetFragment.show (requireActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
        } else if (v.equals(btnLogout)) {
            LoadingDialog dialog = new LoadingDialog(requireContext());
            dialog.show();

            Call<GeneralResponse> responseCall = LogoutService.callLogout(requireActivity(), "/api/logout");
            responseCall.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(@NotNull Call<GeneralResponse> call, @NotNull Response<GeneralResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            GeneralResponse generalResponse = response.body();

                            if (generalResponse != null) {
                                if (generalResponse.getCode().equalsIgnoreCase("200")) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.begin();
                                    preferences.put(Constant.CREDENTIALS.SESSION, "");
                                    preferences.commit();

                                    requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                    requireActivity().finish();
                                }
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                Toast.makeText(ProfileFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                                if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                    Preferences preferences = Preferences.getInstance();
                                    preferences.begin();
                                    preferences.put(Constant.CREDENTIALS.SESSION, "");
                                    preferences.commit();

                                    requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                    requireActivity().finish();
                                }
                            } catch (Exception e) {
                                Toast.makeText(ProfileFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(requireContext(), requireActivity().getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                    } finally {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<GeneralResponse> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(ProfileFragment.this.getContext(), ProfileFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
