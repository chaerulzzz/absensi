package com.absensi.alpa.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.login.LoginDataResponse;
import com.absensi.alpa.api.endpoint.login.LoginResponse;
import com.absensi.alpa.api.endpoint.login.LoginService;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.DeviceUtil;
import com.absensi.alpa.tools.LoadingDialog;
import com.absensi.alpa.tools.Preferences;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText tietUsername, tietPassword;
    //private TextInputLayout tilUsername, tielPassword;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        if (!Preferences.getInstance().getValue(Constant.CREDENTIALS.SESSION, String.class, "").equalsIgnoreCase("")){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        this.init();
        this.setData();
    }

    private void init() {
        this.tietUsername = findViewById(R.id.tietUsername);
        this.tietPassword = findViewById(R.id.tietPassword);

        this.btnLogin = findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(this);
    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnLogin)) {
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
            if (tietUsername.getText() != null && tietPassword.getText() != null){
                if (!tietUsername.getText().toString().isEmpty() && !tietPassword.getText().toString().isEmpty()) {
                    Call<LoginResponse> responseCall = LoginService.sendLogin(
                            this,
                            Constant.URL.LOGIN,
                            tietUsername.getText().toString(),
                            tietPassword.getText().toString(),
                            DeviceUtil.getAndroidId(this));

                    responseCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                            try {
                                if (response.isSuccessful()) {
                                    LoginResponse loginResponse = response.body();

                                    if (loginResponse != null) {
                                        if (loginResponse.getCode().equalsIgnoreCase("200")) {
                                            LoginDataResponse dataResponse = loginResponse.getData();

                                            Preferences preferences = Preferences.getInstance();
                                            preferences.begin();
                                            preferences.put(Constant.CREDENTIALS.EMAIL, dataResponse.getEmail());
                                            preferences.put(Constant.CREDENTIALS.NAME, dataResponse.getName());
                                            preferences.put(Constant.CREDENTIALS.SESSION, dataResponse.getToken());
                                            preferences.put(Constant.CREDENTIALS.USERID, dataResponse.getId());
                                            preferences.commit();

                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.error_username_password_incorrect), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    try {
                                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                                        Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception ex) {
                                Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                            } finally {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                            Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(this, LoginActivity.this.getString(R.string.error_username_password_not_empty), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            } else {
                Toast.makeText(this, LoginActivity.this.getString(R.string.error_username_password_not_empty), Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        }
    }
}
