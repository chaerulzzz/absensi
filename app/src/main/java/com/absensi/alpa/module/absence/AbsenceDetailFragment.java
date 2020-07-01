package com.absensi.alpa.module.absence;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.absensi.alpa.R;
import com.absensi.alpa.api.endpoint.present.PresentResponse;
import com.absensi.alpa.api.endpoint.present.PresentService;
import com.absensi.alpa.module.home.HomeActivity;
import com.absensi.alpa.tools.Constant;
import com.absensi.alpa.tools.Tools;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenceDetailFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private TextView tvTitle;
    private MaterialButton btnAbsence, btnBack;
    String imageValue, latitude, longitude;
    private Integer type;

    public AbsenceDetailFragment(Integer type){
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_absent_detail, container, false);

        this.init(view);
        this.setData();
        return view;
    }

    private void setData(){
        if (type == 0) {
            this.btnAbsence.setText(this.requireActivity().getString(R.string.absent_in_title_activity));
            this.tvTitle.setText(this.requireActivity().getString(R.string.absent_in_title_activity));
        } else {
            this.btnAbsence.setText(this.requireActivity().getString(R.string.absent_out_title_activity));
            this.tvTitle.setText(this.requireActivity().getString(R.string.absent_out_title_activity));
        }
    }

    private void init(View view) {
        this.imageView = view.findViewById(R.id.ivPhoto);
        this.imageView.setOnClickListener(this);

        this.btnAbsence = view.findViewById(R.id.btnAbsent);
        this.btnAbsence.setOnClickListener(this);

        this.btnBack = view.findViewById(R.id.btnBack);
        this.btnBack.setOnClickListener(this);

        this.tvTitle = view.findViewById(R.id.tvTitle);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(imageView)) {
            if (ActivityCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{
                        Manifest.permission.CAMERA
                }, 99);

                return;
            }

            Intent goTo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (goTo.resolveActivity(requireActivity().getPackageManager()) != null) {

                File photoFile = Tools.createTempFile(requireActivity(), "absent_in");
                if (photoFile != null) {
                    imageValue = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                            "com.absensi.alpa.fileprovider",
                            photoFile);
                    goTo.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        goTo.setClipData(ClipData.newRawUri("", photoURI));
                        goTo.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    startActivityForResult(goTo, 99);
                }
            }
        } else if (v.equals(btnAbsence)) {
            sendPresence(type);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                getLastKnownLocation();
            }
        }
    }

    private void getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) this.requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = Objects.requireNonNull(mLocationManager).getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (locationGPS != null) {
            if (!Tools.checkMockApplication(locationGPS)) {
                long lastTimeLocation = locationGPS.getTime() / 1000;
                long currentTime = System.currentTimeMillis() / 1000;
                long timeDifference = currentTime - lastTimeLocation;

                latitude = String.valueOf(locationGPS.getLatitude());
                longitude = String.valueOf(locationGPS.getLongitude());
                requestLocation();
            } else {
                Toast.makeText(this.requireContext(), "Tolong Matikan terlebih dahulu fake GPSnya, terima kasih.", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        LocationRequest locationRequest;
        LocationCallback locationCallback;
        final Location[] location1 = {null};
        final boolean[] isGPSOff = {true};
        final boolean[] isFakeGps = {false};

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        location1[0] = location;
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                    }
                }

                if (latitude != null && longitude != null) {
                    if (location1[0] != null) {
                        isGPSOff[0] = false;
                        if (!Tools.checkMockApplication(location1[0])) {
                            Bitmap bitmap = Tools.getBitmapAbsent(
                                    requireActivity(),
                                    imageValue,
                                    latitude,
                                    longitude);
                            if (bitmap != null) {
                                imageValue = Tools.bitmapToBase64(bitmap);
                                imageView.setImageBitmap(bitmap);
                            }
                            mFusedLocationClient.removeLocationUpdates(this);
                        } else {
                            isFakeGps[0] = true;
                        }
                    }
                } else {
                    isGPSOff[0] = true;
                }

                if (isGPSOff[0]) {
                    if (isFakeGps[0]) {
                        mFusedLocationClient.removeLocationUpdates(this);
                        Toast.makeText(getContext(), "Tolong Matikan terlebih dahulu fake GPSnya, terima kasih.", Toast.LENGTH_SHORT).show();
                    } else {
                        mFusedLocationClient.removeLocationUpdates(this);
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void sendPresence(int type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss", Locale.getDefault());
        Call<PresentResponse> responseCall = PresentService.sendPresence(
                this.requireActivity(),
                Constant.URL.ATTENDANCE,
                type == 0 ? sdf.format(new Date(System.currentTimeMillis())) : "",
                type == 1 ? sdf.format(new Date(System.currentTimeMillis())) : "",
                latitude,
                longitude,
                imageValue
        );

        responseCall.enqueue(new Callback<PresentResponse>() {
            @Override
            public void onResponse(@NotNull Call<PresentResponse> call, @NotNull Response<PresentResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        PresentResponse presentResponse = response.body();

                        if (presentResponse != null) {
                            if (presentResponse.getCode().equalsIgnoreCase("200")) {
                                if (type == 0) {
                                    Toast.makeText(AbsenceDetailFragment.this.getContext(), AbsenceDetailFragment.this.getString(R.string.success_absent_in), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AbsenceDetailFragment.this.getContext(), AbsenceDetailFragment.this.getString(R.string.success_absent_out), Toast.LENGTH_SHORT).show();
                                }
                                ((HomeActivity)AbsenceDetailFragment.this.requireActivity()).loadFragment(new AbsenceFragment());
                            }
                        } else {
                            Toast.makeText(AbsenceDetailFragment.this.getContext(), AbsenceDetailFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(Objects.requireNonNull(response.errorBody()).string());
                            Toast.makeText(AbsenceDetailFragment.this.getContext(), jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(AbsenceDetailFragment.this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(AbsenceDetailFragment.this.getContext(), AbsenceDetailFragment.this.getString(R.string.error_occurred_contact_admin), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<PresentResponse> call, @NotNull Throwable t) {
                Toast.makeText(AbsenceDetailFragment.this.getContext(), AbsenceDetailFragment.this.getString(R.string.error_not_connected_to_server), Toast.LENGTH_SHORT).show();
            }
        });
    }
}