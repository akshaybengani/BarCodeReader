package com.akshaybengani.barcodereader;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;


public class CameraPreview extends AppCompatActivity {

    SurfaceView surfaceView;
    ImageButton imageButton;
    boolean flashState = false;
    CameraSource cameraSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        surfaceView = findViewById(R.id.cameraView);
        imageButton = findViewById(R.id.flashlight);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLicked","Flashlight Button Clicked");
                toggleFlashLight();
            }
        });
        createCameraSource();

//        Intent intent =new Intent(this,)


    }

    private void toggleFlashLight() {
        android.hardware.Camera camera = null;
        camera = getCamera(cameraSource);
        if (camera != null){
            try{
                android.hardware.Camera.Parameters parameters = camera.getParameters();

                if (!flashState){
                    // State is off and Now turning on
                    parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_ON);
                    camera.setParameters(parameters);
                    flashState = !flashState;
                    imageButton.setBackgroundResource(R.drawable.ic_highlight_white_24dp);

                }else {
                    // State is on and Now turning off
                    parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);                    camera.setParameters(parameters);
                    camera.setParameters(parameters);
                    flashState = !flashState;
                    imageButton.setBackgroundResource(R.drawable.ic_highlight_yellow_24dp);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }



    }

    private static android.hardware.Camera getCamera(CameraSource cameraSource) {

        Field[] declaredFields = CameraSource.class.getDeclaredFields();
        for (Field field : declaredFields){
            if (field.getType() == android.hardware.Camera.class){
                field.setAccessible(true);
                try{
                    android.hardware.Camera camera = (android.hardware.Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;

    }

    private void createCameraSource() {

        // To access Barcode Detection or Google Camera API we need to create an object of the BarcodeDetector class
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        // To build our camera functionality in such a way to access barcode Detection capabilities we need to pass the feature in the Camera Source
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();
        //Add callback to the source view Holder so we can start and stop the camera source
        // .. on SurfaceView surfaceCreated() and surfaceDestroyed()
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
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
                // Here we collect the barcode scanned Data in a array
                // We used a sparse Array to store the data by using the function detections and method getDetectedItems
                final SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
                // Now we have data of our barcode in this array we need to pass this in our next activity or process it

                if (barcodeSparseArray.size()>0)
                {
                        Log.d("Array Size", String.valueOf(barcodeSparseArray.size()));
                        /* + barcodeSparseArray.valueAt(i).displayValue*/
                        Log.d("Data", "Value: " + barcodeSparseArray.valueAt(0).rawValue);

                    Intent intent = new Intent();
                    intent.putExtra("Raw Value",barcodeSparseArray.valueAt(0).rawValue);
                    intent.putExtra("Barcode",barcodeSparseArray.valueAt(0));
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();
                }

            }
        });


    }


}
