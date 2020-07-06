package com.absensi.alpa.module.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.profile.ProfileDataResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileEditResponse;
import com.absensi.alpa.api.endpoint.profile.ProfileService;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Preferences;
import com.absensi.alpa.tools.Tools;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.absensi.alpa.tools.Tools.updateLabel;

public class ProfileEditFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private LinearLayout llBirthDate;
    private TextView tvBirthDate;
    private EditText etBirthPlace;
    private TextInputLayout tilOldPassword, tilNewPassword;
    private TextInputEditText tietOldPassword, tietNewPassword;
    private String password;
    private Activity activity;
    private DatePickerDialog.OnDateSetListener dateBirth;
    private Calendar calendarBirth;
    private MaterialButton btnSave;

    public ProfileEditFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_profile_edit, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void init(View view) {
        this.calendarBirth = Calendar.getInstance();

        this.llBirthDate = view.findViewById(R.id.llBirthDate);
        this.llBirthDate.setOnClickListener(this);

        this.tvBirthDate = view.findViewById(R.id.tvBirthDate);
        this.etBirthPlace = view.findViewById(R.id.etBirthPlace);
        this.tilOldPassword = view.findViewById(R.id.tilOldPassword);
        this.tilOldPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.tilNewPassword = view.findViewById(R.id.tilNewPassword);
        this.tilNewPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());

        this.tietNewPassword = view.findViewById(R.id.tietNewPassword);
        this.tietNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tietNewPassword.setError(null);
                    tilNewPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    tilNewPassword.setEndIconTintList(ContextCompat.getColorStateList(activity, android.R.color.black));
                } else {
                    tilNewPassword.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.tietOldPassword = view.findViewById(R.id.tietOldPassword);
        this.tietOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tietOldPassword.setError(null);
                    tilOldPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    tilOldPassword.setEndIconTintList(ContextCompat.getColorStateList(activity, android.R.color.black));
                } else {
                    tilOldPassword.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateBirth = (datePicker, i, i1, i2) -> {
            calendarBirth.set(Calendar.YEAR, i);
            calendarBirth.set(Calendar.MONTH, i1);
            calendarBirth.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(tvBirthDate, calendarBirth);
        };

        updateLabel(tvBirthDate, calendarBirth);

        this.btnSave = view.findViewById(R.id.btnSave);
        this.btnSave.setOnClickListener(this);
    }

    private void setData() {
        if (!Preferences.getInstance().getValue(Constant.CREDENTIALS.USERID, String.class, "").equalsIgnoreCase("")){
            String birthDate = Preferences.getInstance().getValue(Constant.CREDENTIALS.BIRTH_DATE, String.class, "");
            String birthPlace = Preferences.getInstance().getValue(Constant.CREDENTIALS.BIRTH_PLACE, String.class, "");
            tvBirthDate.setText(birthDate);
            etBirthPlace.setText(birthPlace);
        }
    }

    private boolean isValid() {
        if (etBirthPlace.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Tempat lahir tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tietNewPassword.getText().toString().trim().length() > 0) {
            if (tietNewPassword.getText().toString().length() < 1) {
                tietNewPassword.setError(null);
                tilNewPassword.setErrorEnabled(true);
                tilNewPassword.setError("Password baru tidak boleh kosong");

                return false;
            }
        }

        if (tietOldPassword.getText().toString().trim().length() > 0) {
            if (tietOldPassword.getText().toString().length() < 1) {
                tietOldPassword.setError(null);
                tilOldPassword.setErrorEnabled(true);
                tilOldPassword.setError("Password lama tidak boleh kosong");

                return false;
            }

            String mOldPassword = Preferences.getInstance().getValue(Constant.CREDENTIALS.PASSWORD, String.class, "");
            String mOldPassword1 = tietOldPassword.getText().toString();

            if (mOldPassword.equals(mOldPassword1)) {
                password = Tools.generateHashedPass(tietNewPassword.getText().toString().trim());
            } else {
                tietOldPassword.setError(null);
                tilOldPassword.setErrorEnabled(true);
                tilOldPassword.setError("Password lama tidak sesuai");

                return false;
            }
        }

        return true;
    }

    private void sendData() {
        if (isValid()) {
            String mOldPassword = Preferences.getInstance().getValue(Constant.CREDENTIALS.PASSWORD, String.class, "");
            String userid = Preferences.getInstance().getValue(Constant.CREDENTIALS.USERID, String.class, "");

            Call<ProfileEditResponse> responseCall = ProfileService.sendProfile(activity, Constant.URL.USER + "/" + userid,
                    etBirthPlace.getText().toString(),
                    tvBirthDate.getText().toString(),
                    password,
                    mOldPassword);

            responseCall.enqueue(new Callback<ProfileEditResponse>() {
                @Override
                public void onResponse(@NotNull Call<ProfileEditResponse> call, @NotNull Response<ProfileEditResponse> response) {
                    if (response.isSuccessful()) {
                        ProfileEditResponse editResponse = response.body();

                        if (editResponse != null) {
                            ProfileDataResponse dataResponse = editResponse.getData();
                            if (dataResponse != null) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.EMAIL, dataResponse.getEmail());
                                preferences.put(Constant.CREDENTIALS.NAME, dataResponse.getName());
                                preferences.put(Constant.CREDENTIALS.USERID, dataResponse.getId());
                                preferences.put(Constant.CREDENTIALS.PASSWORD, tietNewPassword.getText().toString());
                                preferences.put(Constant.CREDENTIALS.BIRTH_PLACE, dataResponse.getBirthPlace());
                                preferences.put(Constant.CREDENTIALS.BIRTH_DATE, dataResponse.getBirthDate());
                                preferences.commit();

                                ((HomeActivity)activity).getSupportFragmentManager().popBackStackImmediate();
                            }
                        } else {
                            Toast.makeText(ProfileEditFragment.this.getContext(), activity.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(ProfileEditFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(ProfileEditFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileEditResponse> call, Throwable t) {
                    Toast.makeText(ProfileEditFragment.this.getContext(), activity.getString(R.string.error_not_connected_to_server), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(llBirthDate)) {
            new DatePickerDialog(activity, dateBirth, calendarBirth.get(Calendar.YEAR), calendarBirth.get(Calendar.MONTH), calendarBirth.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.equals(btnSave)){
            sendData();
        }
    }
}
