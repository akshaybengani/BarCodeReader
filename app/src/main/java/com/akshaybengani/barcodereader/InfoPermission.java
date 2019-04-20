package com.akshaybengani.barcodereader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class InfoPermission extends AppCompatActivity {

    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_permission);

        imageButton = findViewById(R.id.givePermission);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveMeCamera();
            }
        });

    }

    private void giveMeCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.requestPermissions(InfoPermission.this,new String[]{Manifest.permission.CAMERA},54);
        }else{
            Toast.makeText(InfoPermission.this,"You are under Lollipop you have permissions at install time",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case 54 :
                if (grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted)
                    {
                        // Permission Granted now shift to CameraPreview Activity
                        Intent intent = new Intent(InfoPermission.this,CodeResult.class);
                        startActivity(intent);
                        finish();
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                            {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},54);
                            }

                        }
                    }

                }

                break;
        }

    }


}
