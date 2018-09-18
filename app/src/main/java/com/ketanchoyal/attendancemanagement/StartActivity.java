package com.ketanchoyal.attendancemanagement;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button mSignin_Btn;
    private Button mSignUp_Btn;

    private Apppermissions apppermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSignin_Btn = findViewById(R.id.signin_signin_btn);
        mSignUp_Btn = findViewById(R.id.signup_btn);

        mSignin_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(StartActivity.this,SigninActivity.class);
                startActivity(signinIntent);
            }
        });

        mSignUp_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signupIntent = new Intent(StartActivity.this,SignupActivity.class);
                startActivity(signupIntent);

            }
        });

        apppermissions = new Apppermissions(this);

        if (!apppermissions.checkPermissionForExternalStorage()) {
            apppermissions.requestPermissionForExternalStorage(Apppermissions.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE_BY_GALLERY);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Apppermissions.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE_BY_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (!apppermissions.checkPermissionForCamera())
                    {
                        apppermissions.requestPermissionForCamera();
                        if(!apppermissions.checkLocationPermission())
                        {
                            apppermissions.requestPermissionForLocation();
                        }
                    }

                } else {
                    if (!apppermissions.checkPermissionForCamera())
                    {
                        apppermissions.requestPermissionForCamera();
                        if(!apppermissions.checkLocationPermission())
                        {
                            apppermissions.requestPermissionForLocation();
                        }
                    }
                }
                break;
            case Apppermissions.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE_BY_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //granted

                } else {
                    //denied
                }
                break;
            case Apppermissions.LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //granted

                } else {
                    //denied
                }
                break;
        }

    }

}
