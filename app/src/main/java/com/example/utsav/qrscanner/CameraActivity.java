package com.example.utsav.qrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class CameraActivity extends Activity {

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

            cameraView = (SurfaceView) findViewById(R.id.camera_view);

            barcodeDetector = new BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.QR_CODE)
                    .build();

            cameraSource = new CameraSource
                    .Builder(this, barcodeDetector)
                    .setAutoFocusEnabled(true)
                    .setRequestedPreviewSize(640, 480)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (SecurityException | IOException se) {
                        Log.e("CAMERA SOURCE", se.getMessage());
                    }
                    catch (RuntimeException rte) {
                        CameraActivity.this.finish();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barCodes = detections.getDetectedItems();

                    if (barCodes.size() != 0) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("code", barCodes.valueAt(0).displayValue);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }
}