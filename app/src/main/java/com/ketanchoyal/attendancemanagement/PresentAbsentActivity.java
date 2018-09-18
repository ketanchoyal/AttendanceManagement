package com.ketanchoyal.attendancemanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Long.parseLong;
import static java.lang.Long.toUnsignedString;

public class PresentAbsentActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, QRCodeReaderView.OnQRCodeReadListener {

    private QRCodeReaderView qrCodeView;
    private TextView attendanceview;

    private TextView mSubjectName;

    private FloatingActionButton mFlashbtn;
    private FloatingActionButton mCamerabtn;
    private FloatingActionsMenu mMainFlotingbtn;

    private FirebaseFirestore mFireStore;
    private DocumentReference mPresentAbsentRef;

    private String CurrentUser;
    private String field_coll = null;
    private String Type_doc = null;
    private String YearofAdmission_coll = null;
    private String Attendance_doc = null;
    private String branch_coll = null;
    private String Sem_coll = null;
    private String Sub_doc_id = null;
    private String Enroll_coll = null;

    private String Enroll_doc = null;
    private String Profile_doc = null;

    private String User_ids = "User_ids";

    private String Sub_code = null;
    private String Sub_qr_code = null;
    private String Sub_type = null;
    private String Sub_name = null;
    private String Sub_teacher = null;

    private String Camera_qr_code = null;

    private Boolean torch = false;
    private Boolean cam = false;

    private PointsOverlayView pointsOverlayView;

    private final String lec = "lec";
    private final String lab = "lab";

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationFunction locationFunction;

    private Apppermissions apppermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_absent);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            apppermissions.requestPermissionForLocation();
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.d("GPS : ", "" + location.getLatitude() + "  " + location.getLongitude());
                            locationFunction = new LocationFunction(new LatLng(location.getLatitude(), location.getLongitude()));
                        } else {



                        }
                    }
                });

        mSubjectName = findViewById(R.id.subjectname);

        attendanceview = findViewById(R.id.attendance_resultview);
        pointsOverlayView = findViewById(R.id.qrpoint_view);
        qrCodeView = findViewById(R.id.qrdecodeview);

        mFlashbtn = findViewById(R.id.qr_flash_btn);
        mCamerabtn = findViewById(R.id.qr_camera_btn);
        mMainFlotingbtn = findViewById(R.id.qr_floating_btn);
        mMainFlotingbtn.setLongClickable(true);

        mFireStore = FirebaseFirestore.getInstance();

        Enroll_coll = getIntent().getStringExtra("enroll");

        field_coll = "Engineering";
        YearofAdmission_coll = "2015";
        branch_coll = "CSE";
        Profile_doc = "Profiles";
        Attendance_doc = "Attendance";
        Type_doc = "Students";
        Sem_coll = "SEM_1";
        Sub_doc_id = getIntent().getStringExtra("sub_doc_id");

        Sub_code = getIntent().getStringExtra("sub_code");
        Sub_qr_code = getIntent().getStringExtra("sub_qr_code");
        Sub_name = getIntent().getStringExtra("sub_name");
        Sub_type = getIntent().getStringExtra("sub_type");
        Sub_teacher = getIntent().getStringExtra("sub_teacher");

        mSubjectName.setText(Sub_name);

        qrCodeView.setOnQRCodeReadListener(this);

        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date(System.currentTimeMillis()));

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(System.currentTimeMillis()));

        //TODO 1: add another string to get day name from date and check if its sunday or not

        mPresentAbsentRef = mFireStore.collection(field_coll).document(Type_doc).collection(YearofAdmission_coll)
                .document(Attendance_doc).collection(branch_coll).document("SEM").collection(Sem_coll)
                .document(Sub_doc_id).collection(Enroll_coll).document(year).collection(month).document(date);

        mPresentAbsentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        HashMap<String, Object> attendancedata = new HashMap<>(document.getData());

                        String p_or_a = attendancedata.get("p_or_a").toString();

                        if (p_or_a.equals("1") || p_or_a.equals("2")) {
                            attendanceview.setText("Attendance registered already");
                        }
                        if(p_or_a.isEmpty())
                        {
                            attendanceview.setText("Attendance Pending");
                        }

                    }
                }
            }
        });

        mFlashbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                torch = !torch;

                qrCodeView.setTorchEnabled(torch);

            }
        });

        mCamerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cam) {
                    cam = true;
                    resume();
                } else {
                    cam = false;
                    onPause();
                    torch = false;
                    qrCodeView.setTorchEnabled(false);
                }

            }
        });

        mMainFlotingbtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                qrCodeView.forceAutoFocus();

                return false;
            }
        });

        qrCodeView.setQRDecodingEnabled(true);
        qrCodeView.setFrontCamera();
        qrCodeView.setBackCamera();
        qrCodeView.forceAutoFocus();

    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        Camera_qr_code = text;
        pointsOverlayView.setPoints(points);

        if (Camera_qr_code.equals(Sub_qr_code)) {
            if (locationFunction.getloc()) {
                checkqrcode();
            } else {
                Toast.makeText(PresentAbsentActivity.this, "Please go to College and then Try to do Attendance", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(PresentAbsentActivity.this, "QR Code doesn't match!", Toast.LENGTH_LONG).show();
        }
    }

    private void checkqrcode() {

        Number lec = 1;

        if (Sub_type.equals(lab)) {
            lec = 2;
        }

        Map<String, Number> presencemap = new HashMap<>();
        presencemap.put("p_or_a", lec);

        mPresentAbsentRef.set(presencemap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {
                    Toast.makeText(PresentAbsentActivity.this, "Your attendance is updated succesfully", Toast.LENGTH_LONG).show();
                    attendanceview.setText("Attendance registered already");
                } else {
                    Log.d("ERROR 2 : ", task.getException().getMessage());
                    Toast.makeText(PresentAbsentActivity.this, "There was some error, Please try again", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void resume() {
        qrCodeView.startCamera();
        pointsOverlayView.setVisibility(View.VISIBLE);
        mFlashbtn.setVisibility(View.VISIBLE);
        qrCodeView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeView.startCamera();
        pointsOverlayView.setVisibility(View.GONE);
        mFlashbtn.setVisibility(View.INVISIBLE);
        qrCodeView.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cam = false;
        qrCodeView.stopCamera();
        pointsOverlayView.setVisibility(View.GONE);
        mFlashbtn.setVisibility(View.INVISIBLE);
        qrCodeView.setVisibility(View.GONE);
    }

}
