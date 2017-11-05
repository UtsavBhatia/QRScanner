package com.example.utsav.qrscanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final Integer CAMERA = 0x5;
    TextView QrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QrCode = (TextView) findViewById(R.id.QrCode);
        askForPermission(Manifest.permission.CAMERA, CAMERA);

        final Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v)
            {
                askForPermission(Manifest.permission.CAMERA, CAMERA);
                try{
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivityForResult(intent,0);
                }
                catch(RuntimeException rte){
                    showAlertBox("Permission denied");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            QrCode.setText(data.getStringExtra("code"));
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        else
            showAlertBox("Permission denied");
    }

    public void showAlertBox(String s)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage(s);
        alertDialog.show();
    }
}