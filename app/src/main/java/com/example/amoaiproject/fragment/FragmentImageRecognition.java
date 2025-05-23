package com.example.amoaiproject.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.example.amoaiproject.R;
import com.example.amoaiproject.database.AppDatabase;
import com.example.amoaiproject.database.History;
import com.example.amoaiproject.utils.CustomLoadingDialog;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.example.amoaiproject.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.P)
public class FragmentImageRecognition extends Fragment {

    ImageView ivSearch;
    MaterialButton btnCapture;
    TextView tvResult;
    CustomLoadingDialog progressDialog;
    int REQ_CAMERA = 100;
    String imageFilePath, timeStamp, imageName;
    File fileDirectory, imageFilename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_recognition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new CustomLoadingDialog(getContext());

        ivSearch = view.findViewById(R.id.ivSearch);
        btnCapture = view.findViewById(R.id.btnCapture);
        tvResult = view.findViewById(R.id.tvResult);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }

        btnCapture.setOnClickListener(view1 -> takeCameraImage());
    }

    // Ambil gambar dari kamera
    private void takeCameraImage() {
        Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            try {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        FileProvider.getUriForFile(getContext(),
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                createImageFile()));
                                startActivityForResult(intent, REQ_CAMERA);
                            } catch (IOException ex) {
                                Toast.makeText(getContext(), "Gagal membuka kamera!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    // Method untuk membuat file
    private File createImageFile() throws IOException {
        timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        imageName = "IMG_";
        fileDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFilename = File.createTempFile(imageName, ".jpg", fileDirectory);
        imageFilePath = imageFilename.getAbsolutePath();
        return imageFilename;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
            convertImage(imageFilePath);
        }
    }

    // Method untuk convert file ke bitmap
    private void convertImage(String urlImg) {
        File imgFile = new File(urlImg);
        if (imgFile.exists()) {
            progressDialog.show();
            Bitmap bitmap = decodeFile(imgFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
    }

    // Method untuk merubah rotasi gambar dan menampilkan gambar ke ImageView
    public Bitmap decodeFile(File file) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            options.inSampleSize = calculateInSampleSize(options, 612, 816);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);

            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            ivSearch.setImageBitmap(bitmap);
            buttonImageRecognitionGemini(bitmap);

            int resizeImage = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            return Bitmap.createScaledBitmap(bitmap, 512, resizeImage, true);
        } catch (OutOfMemoryError | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // Method untuk memanggil function Gemini AI dan menyimpan riwayat jawaban ke database
    public void buttonImageRecognitionGemini(Bitmap bitmap) {
        GenerativeModel generativeModel = new GenerativeModel("gemini-1.5-flash", "AIzaSyAnJ26ep51hVCLmVvltWRRhRcZ57a9MGJE");
        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(generativeModel);
        Content content = new Content.Builder()
                .addText("Ini gambar apa?")
                .addImage(bitmap)
                .build();
        ListenableFuture<GenerateContentResponse> responseFuture = modelFutures.generateContent(content);
        Futures.addCallback(responseFuture, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                tvResult.setText(result.getText());
                progressDialog.dismiss();

                // Simpan riwayat ke database di background thread
                new Thread(() -> {
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    History history = new History(result.getText(), currentTime);
                    AppDatabase.getInstance(getContext()).historyDao().insertHistory(history);
                }).start();
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                tvResult.setText(t.toString());
                progressDialog.dismiss();
            }
        }, getContext().getMainExecutor());
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                101);
    }
}
