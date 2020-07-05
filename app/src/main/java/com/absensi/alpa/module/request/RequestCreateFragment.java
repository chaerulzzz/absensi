package com.absensi.alpa.module.request;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.request.RequestService;
import com.absensi.alpa.api.endpoint.request.insert.RequestInsertRequest;
import com.absensi.alpa.api.endpoint.request.insert.RequestInsertResponse;
import com.absensi.alpa.module.absence.AbsenceDetailFragment;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Tools;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.absensi.alpa.tools.Tools.createTempFile;
import static com.absensi.alpa.tools.Tools.rotateImage;
import static com.absensi.alpa.tools.Tools.updateLabel;
import static com.absensi.alpa.tools.Tools.updateTimeLabel;

public class RequestCreateFragment extends Fragment implements View.OnClickListener {

    private TextView tvDateNow, tvTitle, tvApprover, tvLeaveTotalTitle , tvLeaveTotal, tvDateFrom, tvTimeFrom, tvDateTo, tvTimeTo, tvSickLetterDate;
    private LinearLayout llDateFrom, llTimeFrom, llDateTo, llTimeTo, llSickView, llImage, llSickLetterDate;
    private EditText etReason;
    private MaterialButton btnCancel, btnSave;
    private Integer type;
    private Calendar calendarFrom, calendarTo, clTimeFrom, clTimeTo, clSickLetterDate;
    private DatePickerDialog.OnDateSetListener dateTo, dateFrom, dateLetterDate;
    private TimePickerDialog.OnTimeSetListener timeFrom, timeTo;
    private ImageView ivPhoto;
    private String imageValue, pathImage;

    public RequestCreateFragment(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_create, container, false);

        this.init(view);
        this.setView();
        this.setTime();
        this.setData();
        return view;
    }

    private void setView(){
        if (type == 0) {
            this.tvTitle.setText(this.requireActivity().getString(R.string.leave_form_title));
            this.setLeaveTotalVisible(View.VISIBLE);
            this.setTimeVisible(View.GONE);
            this.tvLeaveTotal.setText(String.valueOf(10));
            this.llSickView.setVisibility(View.GONE);
        } else if (type == 1) {
            this.tvTitle.setText(this.requireActivity().getString(R.string.sick_form_title));
            this.setLeaveTotalVisible(View.GONE);
            this.setTimeVisible(View.GONE);
            this.llSickView.setVisibility(View.VISIBLE);
        } else if (type == 2) {
            this.tvTitle.setText(this.requireActivity().getString(R.string.permit_form_title));
            this.setLeaveTotalVisible(View.GONE);
            this.setTimeVisible(View.GONE);
            this.llSickView.setVisibility(View.GONE);
        } else if (type == 3) {
            this.tvTitle.setText(this.requireActivity().getString(R.string.overtime_form_title));
            this.setTimeVisible(View.VISIBLE);
            this.setLeaveTotalVisible(View.GONE);
            this.llSickView.setVisibility(View.GONE);
        }
    }

    private void setTimeVisible(int mode) {
        this.llTimeFrom.setVisibility(mode);
        this.llTimeTo.setVisibility(mode);
    }

    private void setLeaveTotalVisible(int mode) {
        this.tvLeaveTotal.setVisibility(mode);
        this.tvLeaveTotalTitle.setVisibility(mode);
    }

    private void init(View view) {
        this.tvDateNow = view.findViewById(R.id.tvDateNow);
        this.tvTitle = view.findViewById(R.id.tvTitle);
        this.tvApprover = view.findViewById(R.id.tvApprover);
        this.tvLeaveTotal = view.findViewById(R.id.tvLeaveTotal);
        this.tvDateFrom = view.findViewById(R.id.tvDateFrom);
        this.tvTimeFrom = view.findViewById(R.id.tvTimeFrom);
        this.tvDateTo = view.findViewById(R.id.tvDateTo);
        this.tvTimeTo = view.findViewById(R.id.tvTimeTo);
        this.tvSickLetterDate = view.findViewById(R.id.tvSickLetterDate);
        this.tvLeaveTotalTitle = view.findViewById(R.id.tvLeaveTotalTitle);

        this.llDateFrom = view.findViewById(R.id.llDateFrom);
        this.llDateFrom.setOnClickListener(this);

        this.llTimeFrom = view.findViewById(R.id.llTimeFrom);
        this.llTimeFrom.setOnClickListener(this);

        this.llDateTo = view.findViewById(R.id.llDateTo);
        this.llDateTo.setOnClickListener(this);

        this.llTimeTo = view.findViewById(R.id.llTimeTo);
        this.llTimeTo.setOnClickListener(this);

        this.llSickView = view.findViewById(R.id.llSickView);
        this.llImage = view.findViewById(R.id.llImage);
        this.llImage.setOnClickListener(this);

        this.llSickLetterDate = view.findViewById(R.id.llSickLetterDate);
        this.llSickLetterDate.setOnClickListener(this);

        this.etReason = view.findViewById(R.id.etReason);
        this.btnCancel = view.findViewById(R.id.btnCancel);
        this.btnCancel.setOnClickListener(this);

        this.btnSave = view.findViewById(R.id.btnSave);
        this.btnSave.setOnClickListener(this);

        this.ivPhoto = view.findViewById(R.id.ivPhoto);

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();
        clTimeFrom = Calendar.getInstance();
        clTimeTo = Calendar.getInstance();
        clSickLetterDate = Calendar.getInstance();
    }

    private void setTime(){
        dateFrom = (datePicker, i, i1, i2) -> {
            calendarFrom.set(Calendar.YEAR, i);
            calendarFrom.set(Calendar.MONTH, i1);
            calendarFrom.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(tvDateFrom, calendarFrom);
        };

        dateTo = (datePicker, i, i1, i2) -> {
            calendarTo.set(Calendar.YEAR, i);
            calendarTo.set(Calendar.MONTH, i1);
            calendarTo.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(tvDateTo, calendarTo);
        };

        dateLetterDate = (datePicker, i, i1, i2) -> {
            clSickLetterDate.set(Calendar.YEAR, i);
            clSickLetterDate.set(Calendar.MONTH, i1);
            clSickLetterDate.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(tvSickLetterDate, clSickLetterDate);
        };

        timeFrom = (timePicker, i, i1) -> {
            clTimeFrom.set(Calendar.HOUR_OF_DAY, i);
            clTimeFrom.set(Calendar.MINUTE, i1);
            updateTimeLabel(tvTimeFrom, clTimeFrom);
        };

        timeTo = (timePicker, i, i1) -> {
            clTimeTo.set(Calendar.HOUR_OF_DAY, i);
            clTimeTo.set(Calendar.MINUTE, i1);
            updateTimeLabel(tvTimeTo, clTimeTo);
        };

        updateLabel(tvDateFrom, calendarFrom);
        updateLabel(tvDateTo, calendarTo);
        updateLabel(tvSickLetterDate, clSickLetterDate);
        updateTimeLabel(tvTimeFrom, clTimeFrom);
        updateTimeLabel(tvTimeTo, clTimeTo);
        updateLabel(tvDateNow);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(llDateFrom)) {
            new DatePickerDialog(requireActivity(), dateFrom, calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.equals(llDateTo)) {
            new DatePickerDialog(requireActivity(), dateTo, calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.equals(llTimeFrom)) {
            new TimePickerDialog(getActivity(), timeFrom, clTimeFrom.get(Calendar.HOUR_OF_DAY), clTimeFrom.get(Calendar.MINUTE), true).show();
        } else if (v.equals(llTimeTo)) {
            new TimePickerDialog(getActivity(), timeTo, clTimeTo.get(Calendar.HOUR_OF_DAY), clTimeTo.get(Calendar.MINUTE), true).show();
        } else if (v.equals(btnCancel)) {
            ((HomeActivity)RequestCreateFragment.this.requireActivity()).getSupportFragmentManager().popBackStackImmediate();
        } else if (v.equals(btnSave)) {
            if (isValid()) {
                insertRequest();
            }
        } else if (v.equals(llImage)) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals("Take Photo")) {
                    onCameraUsing(0);
                } else if (options[item].equals("Choose from Gallery")) {
                    onCameraUsing(1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (v.equals(llSickLetterDate)) {
            new DatePickerDialog(requireActivity(), dateLetterDate, clSickLetterDate.get(Calendar.YEAR), clSickLetterDate.get(Calendar.MONTH), clSickLetterDate.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private boolean isValid(){
        if (etReason.getText().length() < 1) {
            return false;
        }

        if (!etReason.getText().toString().matches("[a-zA-Z0-9.? ]*")) {
            return false;
        }

        if (type == 1) {
            return imageValue != null;
        } else if (type == 3 || type == 2) {
            String dateFrom = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarFrom.getTime()) + " " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(clTimeFrom.getTime());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarTo.getTime()) + " " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(clTimeTo.getTime());

            if (!Tools.checkDates(dateFrom, dateTo, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()))) {
                Toast.makeText(RequestCreateFragment.this.getContext(), "Tanggal mulai tidak boleh lebih besar dari tanggal akhir", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void setData(){

    }

    private void insertRequest(){
        String url;
        if (type == 0) {
            url = Constant.URL.LEAVE;
        } else if (type == 1) {
            url = Constant.URL.SICK;
        } else if (type == 2) {
            url = Constant.URL.PERMIT;
        } else {
            url = Constant.URL.OVERTIME;
        }

        Call<RequestInsertResponse> responseCall = RequestService.insertRequest(
                requireActivity(),
                url,
                etReason.getText().toString(),
                imageValue,
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(clSickLetterDate.getTime()),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarFrom.getTime()),
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendarTo.getTime())
        );

        responseCall.enqueue(new Callback<RequestInsertResponse>() {
            @Override
            public void onResponse(@NotNull Call<RequestInsertResponse> call, @NotNull Response<RequestInsertResponse> response) {
                if (response.isSuccessful()) {

                    RequestInsertResponse requestInsertResponse = response.body();

                    if (requestInsertResponse != null) {
                        if (requestInsertResponse.getCode().equalsIgnoreCase("200")) {
                            Toast.makeText(RequestCreateFragment.this.getContext(), "Pembuatan Pengajuan sukses", Toast.LENGTH_SHORT).show();
                            ((HomeActivity)RequestCreateFragment.this.requireActivity()).getSupportFragmentManager().popBackStackImmediate();
                        }
                    } else {
                        Toast.makeText(RequestCreateFragment.this.getContext(), RequestCreateFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                        Toast.makeText(RequestCreateFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(RequestCreateFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RequestInsertResponse> call, @NotNull Throwable t) {
                Toast.makeText(RequestCreateFragment.this.getContext(), RequestCreateFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onCameraUsing(int type) {
        if (type != -1) {
            switch (type) {
                case 0:
                    if (ActivityCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                                Manifest.permission.CAMERA
                        }, 99);

                        return;
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = createTempFile(requireActivity(), "sick_image");
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            pathImage = photoFile.getAbsolutePath();
                            Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                                    "com.absensi.alpa.fileprovider",
                                    photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                                intent.setClipData(ClipData.newRawUri("", photoURI));
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                            startActivityForResult(intent, 99);
                        }
                    }
                    break;
                case 1:
                    if (ActivityCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(requireActivity(),
                                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 100);

                        return;
                    }

                    Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.setType("image/*");
                    if (intent1.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivityForResult(intent1, 100);
                    }
                    break;
            }
        }
    }

    @SuppressLint("Recycle")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    File filePath = new File(pathImage);

                    OutputStream outFile;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inJustDecodeBounds = false;
                    bitmapOptions.inSampleSize = 8;
                    bitmapOptions.inMutable = true;
                    System.gc();
                    Bitmap bitmap = null;
                    Bitmap result;
                    ExifInterface exif = null;
                    bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath(), bitmapOptions);
                    exif = new ExifInterface(filePath.getAbsolutePath());

                    if (exif != null) {
                        result = bitmap;

                        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

                        int rotationAngle = 0;
                        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
                        if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                            rotationAngle = 180;
                        if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                            rotationAngle = 270;

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_NORMAL:
                                bitmap = result;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                bitmap = rotateImage(result, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                bitmap = rotateImage(result, 180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                bitmap = rotateImage(result, 270);
                                break;
                            default:
                                bitmap = result;
                                break;
                        }
                    }

                    if (bitmap != null) {
                        ivPhoto.setImageBitmap(bitmap);
                        imageValue = Tools.bitmapToBase64(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = null;
                int sizeFile = 0;
                String extFile = null;
                if (data != null) {
                    uri = data.getData();
                    if (uri != null) {
                        Cursor cursor = null;
                        try {
                            cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null) {
                                /*
                                 * Get the column indexes of the data in the Cursor,
                                 * move to the first row in the Cursor, get the data,
                                 * and display it.
                                 */
                                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                                cursor.moveToFirst();
                                sizeFile = Integer.parseInt(cursor.getString(sizeIndex));
                                extFile = cursor.getString(2).substring(cursor.getString(2).lastIndexOf(".") + 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (sizeFile > 2048000) {
                            Toast.makeText(requireContext(), "upload gambar maksimal 1 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            if (extFile != null) {
                                try {
                                    Bitmap bitmap = BitmapFactory.decodeStream(requireActivity().getContentResolver().openInputStream(uri));
                                    if (bitmap != null) {
                                        ivPhoto.setImageBitmap(bitmap);
                                        imageValue = Tools.bitmapToBase64(bitmap);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
