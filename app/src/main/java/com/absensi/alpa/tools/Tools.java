package com.absensi.alpa.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.absensi.alpa.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static boolean checkMockApplication(Location location){
        return location.isFromMockProvider();
    }

    public static File createTempFile(Activity activity, String nameFile) {
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File filePath = null;
        try {
            filePath = File.createTempFile(
                    nameFile,
                    "." + Bitmap.CompressFormat.JPEG.toString().toLowerCase(),
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public static Bitmap getBitmapAbsent(Activity activity, String pathFile, String latitude, String longitude) {
        try {
            File filePath = new File(pathFile);

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
                if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
                if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

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
                try {
                    SimpleDateFormat dateImage = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    String dateTime = dateImage.format(Calendar.getInstance().getTime());
                    Canvas canvas;
                    float scale = activity.getResources().getDisplayMetrics().density;
                    Paint paint = new Paint();
                    String message;
                    if (latitude != null && longitude != null) {
                        message = "LAT : " + latitude
                                + " LONG : " + longitude
                                + " DATE : " + dateTime;
                    } else {
                        message = "LAT : " + ""
                                + " LONG : " + ""
                                + " DATE : " + dateTime;
                    }
                    paint.setColor(ContextCompat.getColor(activity.getApplicationContext(),
                            android.R.color.white));

                    canvas = new Canvas(bitmap);

                    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                    textPaint.setColor(Color.rgb(255, 255, 255));
                    if (bitmap.getHeight() > bitmap.getWidth()) {
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textPaint.setTextSize((Float.parseFloat("3.5"))*scale);
                        } else {
                            textPaint.setTextSize(5*scale);
                        }*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textPaint.setTextSize((Float.parseFloat("4.5")) * scale);
                        } else {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                textPaint.setTextSize(1 * scale);
                            } else {
                                textPaint.setTextSize(6 * scale);
                            }
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            textPaint.setTextSize(5 * scale);
                        } else {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                                textPaint.setTextSize(3 * scale);
                            } else {
                                textPaint.setTextSize(Float.parseFloat("6.5") * scale);
                            }
                        }
                    }
                    textPaint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

                    int textWidth = canvas.getWidth() - (int) (1.6 * scale);

                    StaticLayout staticLayout;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        StaticLayout.Builder sb = StaticLayout.Builder.obtain(message, 0, message.length(), textPaint, textWidth)
                                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                                .setLineSpacing(1.0f, 1.0f)
                                .setIncludePad(false);
                        staticLayout = sb.build();
                    } else {
                        staticLayout = new StaticLayout(
                                message, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL,
                                1.0f, 1.0f, false
                        );
                    }

                    Paint paintBackground = new Paint();
                    paintBackground.setColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorPrimaryDark));

                    canvas.drawRect(0, 0, bitmap.getWidth(), staticLayout.getHeight() + 10, paintBackground);
                    canvas.save();
                    canvas.translate(5, 5);
                    staticLayout.draw(canvas);
                    canvas.restore();

                    activity.deleteFile(filePath.getName());

                    outFile = new FileOutputStream(filePath);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, false);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,
                            50, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Uri uri = null;
                if (filePath.isFile()) {
                    uri = Uri.fromFile(filePath);
                    bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
                    activity.deleteFile(filePath.getName());
                    return bitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("bitmap size : ", String.valueOf((base64.length() / (1024))));
        Log.d("bitmap size : ", String.valueOf((byteArray.length / (1024))));
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static boolean checkDates(String fromDate, String toDate , SimpleDateFormat dfDate)   {
        boolean b = false;
        try {
            //If start date is after the end date
            if(Objects.requireNonNull(dfDate.parse(fromDate)).before(dfDate.parse(toDate))) {
                b = true;//If start date is before end date
            } else b = Objects.equals(dfDate.parse(fromDate), dfDate.parse(toDate));//If two dates are equal
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void updateLabel(TextView textView, Calendar calendar) {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(calendar.getTime()));
    }

    public static void updateLabel(TextView textView) {
        String myFormat = "EEE, dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        textView.setText(sdf.format(new Date()));
    }

    public static void updateTimeLabel(TextView tv1, Calendar calendar) {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv1.setText(sdf.format(calendar.getTime()));
    }

    public static String generateHashedPass(String pass) {
        return Bcrypt.hashpw(pass, Bcrypt.gensalt());
    }

    public static boolean generalValidation(@NonNull View mParentView, View mContentView,
                                            @NonNull String mMessage, @NonNull Integer mType) {

        if (mParentView instanceof TextInputLayout) {
            TextInputLayout mInputLayout = (TextInputLayout) mParentView;

            if (mContentView != null) {
                if (mContentView instanceof TextInputEditText) {
                    TextInputEditText mEdtText = (TextInputEditText) mContentView;

                    if (TextUtils.isEmpty(mEdtText.getText().toString())) {
                        mEdtText.requestFocus();
                        mEdtText.setError(null);
                        mInputLayout.setErrorEnabled(true);
                        mInputLayout.setError(mMessage);

                        return false;
                    } else {
                        if (mType == 1) {
                            if (!emailValidation(mEdtText.getText().toString().trim())) {
                                mEdtText.setError(null);
                                mInputLayout.setErrorEnabled(true);
                                mInputLayout.setError(mMessage);

                                return false;
                            }
                        } else if (mType == 2) {
                            if (mEdtText.getText().toString().length() < 6) {
                                mEdtText.setError(null);
                                mInputLayout.setErrorEnabled(true);
                                mInputLayout.setError(mMessage);

                                return false;
                            }
                        } else if (mType == 3) {
                            if (mEdtText.getText().toString().length() > 15 || mEdtText.getText().toString().length() < 11) {
                                mEdtText.setError(null);
                                mInputLayout.setErrorEnabled(true);
                                mInputLayout.setError(mMessage);

                                return false;
                            }
                        }
                    }
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private static boolean emailValidation(String mEmail) {

        if (!TextUtils.isEmpty(mEmail)) {
            String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(mEmail);
            return matcher.matches();
        }

        return false;
    }
}
