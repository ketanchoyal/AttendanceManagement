package com.ketanchoyal.attendancemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetupActivity extends AppCompatActivity {

    private static final String TAG = "Profile Data";
    private EditText mBirthdate;
    private EditText mAdmissionYear;

    private TextInputLayout mName;
    private EditText mMobileno;
    private TextInputLayout mEmail;

    private Button mUpdateProfilebtn;

    private CircleImageView mProfileimage;

    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private CollectionReference mProfileRef;
    private CollectionReference mUser_idsRef;

    private ImageButton mHomeBtn;

    private String field_coll = null;
    private String Type_doc = null;
    private String YearofAdmission_coll = null;
    private String Profile_doc = null;
    private String branch_coll = null;
    private String Enroll_doc = null;

    private String Enroll_coll = "Enroll_no";

    private String User_ids = "User_ids";

    @Override
    protected void onStart() {
        super.onStart();

        setdata();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        mBirthdate = findViewById(R.id.birthdatee);
        mAdmissionYear = findViewById(R.id.admission_year);

        mName = findViewById(R.id.user_name);
        mEmail = findViewById(R.id.user_email);
        mMobileno = findViewById(R.id.user_mobile_noo);

        mProfileimage = findViewById(R.id.profile_image);

        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        field_coll = "Engineering";
        YearofAdmission_coll = "2015";
        branch_coll = "CSE";
        Profile_doc = "Profiles";
        Type_doc = "Students";

        Enroll_doc = getIntent().getStringExtra("enroll");

        mProfileRef = mFireStore.collection(field_coll).document(Type_doc)
                .collection(YearofAdmission_coll).document(Profile_doc).collection(branch_coll);

        mUser_idsRef =  mFireStore.collection(field_coll).document(Type_doc)
                .collection(YearofAdmission_coll).document(User_ids).collection(Enroll_coll);

        mUpdateProfilebtn = findViewById(R.id.update_profile_btn);
        mHomeBtn = findViewById(R.id.home_btn);

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(ProfileSetupActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        });

        new TextViewDatePicker(this, mBirthdate);

        final YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year,int month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                mAdmissionYear.setText(dateFormat.format(calendar.getTime()));
            }
        });

        mAdmissionYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                yearMonthPickerDialog.show();

            }
        });

        mUpdateProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getEditText().getText().toString().trim();
                String name = mName.getEditText().getText().toString().trim();
                String birthdate = mBirthdate.getText().toString().trim();
                String mobile_no = mMobileno.getText().toString().trim();
                String admissionyear = mAdmissionYear.getText().toString().trim();
                String profile_image = "";
                String profile_icon = "";

                Map<String,Object> profiledata = new HashMap<>();

                if(TextUtils.isEmpty(email))
                {
                    profiledata.put("email","");
                }
                else
                {
                    profiledata.put("email",email);
                }

                if(TextUtils.isEmpty(name))
                {
                    profiledata.put("name","");
                }
                else
                {
                    profiledata.put("name",name);
                }

                if(TextUtils.isEmpty(birthdate))
                {
                    profiledata.put("birthdate","");
                }
                else
                {
                    profiledata.put("birthdate",birthdate);
                }

                if(TextUtils.isEmpty(mobile_no))
                {
                    profiledata.put("mobile_no","");
                }
                else
                {
                    profiledata.put("mobile_no",mobile_no);
                }

                if(TextUtils.isEmpty(admissionyear))
                {
                    profiledata.put("admissionyear","");
                }
                else
                {
                    profiledata.put("admissionyear",admissionyear);
                }

                if(TextUtils.isEmpty(profile_image))
                {
                    profiledata.put("profile_image","default");
                }
                else
                {
                    profiledata.put("profile_image","default");
                }

                if(TextUtils.isEmpty(profile_icon))
                {
                    profiledata.put("profile_icon","default");
                }
                else
                {
                    profiledata.put("profile_icon","default");
                }

                mProfileRef.document(Enroll_doc).update(profiledata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileSetupActivity.this,"Profile Updated Succesfully",Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            Toast.makeText(ProfileSetupActivity.this,"Try Again, there was some error.",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });

    }

    private void setdata() {

        mProfileRef.document(Enroll_doc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {

                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());

                        HashMap<String,Object> profiledata = new HashMap<>(document.getData());

                        String email = profiledata.get("email").toString();
                        String name = profiledata.get("name").toString();
                        String birthdate = profiledata.get("birthdate").toString();
                        String mobile_no = profiledata.get("mobile_no").toString();
                        String admissionyear = profiledata.get("admissionyear").toString();
                        String profile_image = profiledata.get("profile_image").toString();;
                        String profile_icon = profiledata.get("profile_icon").toString();

                        if(!email.equals(""))
                        {
                            mEmail.getEditText().setText(email);
                        }

                        if(!name.equals(""))
                        {
                            mName.getEditText().setText(name);
                        }

                        if(!birthdate.equals(""))
                        {
                            mBirthdate.setText(birthdate);
                        }

                        if(!mobile_no.equals(""))
                        {
                            mMobileno.setText(mobile_no);
                        }

                        if(!admissionyear.equals(""))
                        {
                            mAdmissionYear.setText(admissionyear);
                        }

                        if(!profile_icon.equals(""))
                        {

                        }
                        else
                        {

                        }

                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

}
