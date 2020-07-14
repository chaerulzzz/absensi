package com.absensi.alpa.module.request;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.approval.ApprovalService;
import com.absensi.alpa.api.endpoint.approval.process.ApprovalProcessResponse;
import com.absensi.alpa.api.endpoint.request.RequestService;
import com.absensi.alpa.api.endpoint.request.detail.RequestDetailDataResponse;
import com.absensi.alpa.api.endpoint.request.detail.RequestDetailResponse;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.module.login.LoginActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.LoadingDialog;
import com.absensi.alpa.tools.Preferences;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.absensi.alpa.tools.Tools.base64ToBitmap;

public class RequestDetailFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView tvDateNow, tvTitle, tvApprover, tvDateFrom, tvTimeFrom, tvDateTo, tvTimeTo, tvSickLetterDate, etReason, tvNumber, tvSpinnerStatus, tvNotesTitle, tvCategoryTitle, tvCategory;
    private LinearLayout llDateFrom, llTimeFrom, llDateTo, llTimeTo, llSickView, llImage, llSickLetterDate;
    private MaterialButton btnCancel, btnSave;
    private ImageView ivPhoto;
    private EditText etNotes;
    private String id, type;
    private int typeFrom;
    private Spinner spnStatus;
    private ArrayAdapter<String> statusAdapter;
    private String statusSelected;

    public RequestDetailFragment(String type, String id, int typeFrom) {
        this.type = type;
        this.id = id;
        this.typeFrom = typeFrom;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void setTimeVisible(int mode) {
        this.llTimeFrom.setVisibility(mode);
        this.llTimeTo.setVisibility(mode);
    }

    private void init(View view) {
        this.tvDateNow = view.findViewById(R.id.tvDateNow);
        this.tvTitle = view.findViewById(R.id.tvTitle);
        this.tvApprover = view.findViewById(R.id.tvApprover);
        this.tvDateFrom = view.findViewById(R.id.tvDateFrom);
        this.tvTimeFrom = view.findViewById(R.id.tvTimeFrom);
        this.tvDateTo = view.findViewById(R.id.tvDateTo);
        this.tvTimeTo = view.findViewById(R.id.tvTimeTo);
        this.tvSickLetterDate = view.findViewById(R.id.tvSickLetterDate);

        this.llDateFrom = view.findViewById(R.id.llDateFrom);

        this.llTimeFrom = view.findViewById(R.id.llTimeFrom);

        this.llDateTo = view.findViewById(R.id.llDateTo);

        this.llTimeTo = view.findViewById(R.id.llTimeTo);

        this.llSickView = view.findViewById(R.id.llSickView);
        this.llImage = view.findViewById(R.id.llImage);

        this.llSickLetterDate = view.findViewById(R.id.llSickLetterDate);
        this.etReason = view.findViewById(R.id.etReason);

        this.btnCancel = view.findViewById(R.id.btnCancel);
        this.btnCancel.setOnClickListener(this);

        this.btnSave = view.findViewById(R.id.btnSave);
        this.btnSave.setOnClickListener(this);

        this.ivPhoto = view.findViewById(R.id.ivPhoto);

        this.tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);
        this.tvCategory = view.findViewById(R.id.tvCategory);

        tvNumber = view.findViewById(R.id.tvNumber);

        List<String> lstStatus = new ArrayList<>();
        lstStatus.add("New");
        lstStatus.add("Approve");
        lstStatus.add("Reject");

        statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, lstStatus);
        spnStatus = view.findViewById(R.id.spinner);
        spnStatus.setAdapter(statusAdapter);
        spnStatus.setOnItemSelectedListener(this);

        this.tvSpinnerStatus = view.findViewById(R.id.spinnerTitle);
        this.tvNotesTitle = view.findViewById(R.id.notesTitle);
        this.etNotes = view.findViewById(R.id.etNotes);
    }

    @Override
    public void onClick(View v) {
       if (v.equals(btnCancel)) {
            ((HomeActivity)RequestDetailFragment.this.requireActivity()).getSupportFragmentManager().popBackStackImmediate();
        } else if (v.equals(btnSave)) {
            if (isValid()) {
                sendProcess();
            } else {
                Toast.makeText(requireContext(), "Pilih Approve atau reject", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendProcess() {
        LoadingDialog dialog = new LoadingDialog(requireContext());
        dialog.show();

        String url;

        if (type.equalsIgnoreCase("Leave Request")) {
            url = Constant.URL.LEAVE;
        } else if (type.equalsIgnoreCase("Permit Request")) {
            url = Constant.URL.PERMIT;
        } else if (type.equalsIgnoreCase("Sick Request")) {
            url = Constant.URL.SICK;
        } else  {
            url = Constant.URL.OVERTIME;
        }

        if (statusSelected.equalsIgnoreCase("approve")) {
            statusSelected = "263";
        } else {
            statusSelected = "264";
        }

        Call<ApprovalProcessResponse> responseCall = ApprovalService.sendProcess(requireActivity(), url, id, statusSelected, etNotes.getText().toString());

        responseCall.enqueue(new Callback<ApprovalProcessResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApprovalProcessResponse> call, @NotNull Response<ApprovalProcessResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        ApprovalProcessResponse processResponse = response.body();

                        if (processResponse != null) {
                            if (processResponse.getCode().equalsIgnoreCase("200")) {
                                Toast.makeText(RequestDetailFragment.this.getContext(), "Penyetujuan Pengajuan sukses", Toast.LENGTH_SHORT).show();
                                ((HomeActivity)RequestDetailFragment.this.requireActivity()).getSupportFragmentManager().popBackStackImmediate();
                            }
                        } else {
                            Toast.makeText(RequestDetailFragment.this.getContext(), RequestDetailFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(RequestDetailFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();

                            if (jObjError.getString("message").equalsIgnoreCase("Unauthorized")) {
                                Preferences preferences = Preferences.getInstance();
                                preferences.begin();
                                preferences.put(Constant.CREDENTIALS.SESSION, "");
                                preferences.commit();

                                requireActivity().startActivity(new Intent(requireContext(), LoginActivity.class));
                                requireActivity().finish();
                            }
                        } catch (Exception e) {
                            Toast.makeText(RequestDetailFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<ApprovalProcessResponse> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(RequestDetailFragment.this.getContext(), RequestDetailFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid(){
        if (statusSelected != null) {
            return !statusSelected.equalsIgnoreCase("New");
        } else {
            return false;
        }

    }

    private void setData(){
        LoadingDialog dialog = new LoadingDialog(requireContext());
        dialog.show();

        String url;

        if (type.equalsIgnoreCase("Leave Request")) {
            url = Constant.URL.LEAVE;
        } else if (type.equalsIgnoreCase("Permit Request")) {
            url = Constant.URL.PERMIT;
        } else if (type.equalsIgnoreCase("Sick Request")) {
            url = Constant.URL.SICK;
        } else  {
            url = Constant.URL.OVERTIME;
        }

        Call<RequestDetailResponse> responseCall = RequestService.getDetailRequest(requireActivity(), url + "/" + id);

        responseCall.enqueue(new Callback<RequestDetailResponse>() {
            @Override
            public void onResponse(@NotNull Call<RequestDetailResponse> call, @NotNull Response<RequestDetailResponse> response) {
                try {
                    if (response.isSuccessful()) {

                        RequestDetailResponse detailResponse = response.body();

                        if (detailResponse != null) {
                            RequestDetailDataResponse dataResponse = detailResponse.getData();

                            if (dataResponse != null) {

                                if (dataResponse.getCategory() != null) {
                                    if (dataResponse.getCategory().equalsIgnoreCase("1")) {
                                        tvCategory.setText("Tahunan");
                                    } else if (dataResponse.getCategory().equalsIgnoreCase("2")) {
                                        tvCategory.setText("Potong Gaji");
                                    } else if (dataResponse.getCategory().equalsIgnoreCase("3")) {
                                        tvCategory.setText("Melahirkan");
                                    } else if (dataResponse.getCategory().equalsIgnoreCase("4")) {
                                        tvCategory.setText("Perjalanan Dinas");
                                    } else if (dataResponse.getCategory().equalsIgnoreCase("5")) {
                                        tvCategory.setText("Keperluan Pribadi");
                                    } else {
                                        tvCategory.setText("Lainnya");
                                    }
                                } else {
                                    tvCategory.setVisibility(View.GONE);
                                    tvCategoryTitle.setVisibility(View.GONE);
                                }

                                if (type.equalsIgnoreCase("Overtime Request")) {
                                    setTimeVisible(View.VISIBLE);

                                    try {
                                        Date date2 = null;
                                        Date date3 = null;

                                        if (dataResponse.getDateStart() != null) {
                                            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id", "ID")).parse(dataResponse.getDateStart());
                                        }

                                        if (dataResponse.getDateEnd() != null) {
                                            date3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("id", "ID")).parse(dataResponse.getDateEnd());
                                        }

                                        SimpleDateFormat time = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));

                                        if (date2 != null) {
                                            tvTimeFrom.setText(time.format(date2));
                                        } else {
                                            tvTimeFrom.setText("");
                                        }

                                        if (date3 != null) {
                                            tvTimeTo.setText(time.format(date3));
                                        } else {
                                            tvTimeTo.setText("");
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else {
                                    setTimeVisible(View.GONE);
                                }

                                if (type.equalsIgnoreCase("Sick Request")) {
                                    llSickView.setVisibility(View.VISIBLE);

                                    ivPhoto.setImageBitmap(base64ToBitmap(dataResponse.getLetter()));
                                    tvSickLetterDate.setText(dataResponse.getLetterDate());
                                } else {
                                    llSickView.setVisibility(View.GONE);
                                }

                                tvNumber.setText(dataResponse.getNumber());
                                tvDateFrom.setText(dataResponse.getDateStart());
                                tvDateTo.setText(dataResponse.getDateEnd());
                                etReason.setText(dataResponse.getReason());

                                if (!dataResponse.getStatus().equalsIgnoreCase("263")) {
                                    if (typeFrom == 1) {
                                        btnSave.setVisibility(View.VISIBLE);
                                        spnStatus.setVisibility(View.VISIBLE);
                                        tvSpinnerStatus.setVisibility(View.VISIBLE);
                                        tvNotesTitle.setVisibility(View.VISIBLE);
                                        etNotes.setVisibility(View.VISIBLE);
                                    } else {
                                        btnSave.setVisibility(View.GONE);
                                        spnStatus.setVisibility(View.GONE);
                                        tvSpinnerStatus.setVisibility(View.GONE);
                                        tvNotesTitle.setVisibility(View.GONE);
                                        etNotes.setVisibility(View.GONE);
                                    }
                                } else {
                                    btnSave.setVisibility(View.GONE);
                                    spnStatus.setVisibility(View.GONE);
                                    tvSpinnerStatus.setVisibility(View.GONE);
                                    tvNotesTitle.setVisibility(View.GONE);
                                    etNotes.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(RequestDetailFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(RequestDetailFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
            public void onFailure(@NotNull Call<RequestDetailResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
                Toast.makeText(RequestDetailFragment.this.getContext(), RequestDetailFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        statusSelected = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
