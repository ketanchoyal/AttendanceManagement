package com.ketanchoyal.attendancemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout mEmailField;
    private TextInputLayout mPasswordField;
    private TextInputLayout mEnrollmentField;

    private Button mSignupBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private CollectionReference mProfileRef;
    private DocumentReference mUser_idsRef;

    private String email=null, pass=null;
    final String Enrollment = "ENROLLMENT";

    private String field_coll = null;
    private String Type_doc = null;
    private String YearofAdmission_coll = null;
    private String Profile_doc = null;
    private String branch_coll = null;
    private String Enroll_doc =null;

    private String Enroll_coll = "Enroll_no";
    private String User_ids = "User_ids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailField = findViewById(R.id.signup_email_field);
        mPasswordField = findViewById(R.id.signup_password_field);
        mEnrollmentField = findViewById(R.id.signup_enrollment_field);

        mSignupBtn = findViewById(R.id.signup_signup_btn);

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        field_coll = "Engineering";
        YearofAdmission_coll = "2015";
        branch_coll = "CSE";
        Profile_doc = "Profiles";
        Type_doc = "Students";

        mProfileRef = mFireStore.collection(field_coll).document(Type_doc)
                .collection(YearofAdmission_coll).document(Profile_doc).collection(branch_coll);

        mUser_idsRef =  mFireStore.collection(field_coll).document(Type_doc)
                .collection(YearofAdmission_coll).document(User_ids);

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmailField.getEditText().getText().toString().trim();
                pass = mPasswordField.getEditText().getText().toString().trim();
                Enroll_doc = mEnrollmentField.getEditText().getText().toString().trim();

                final DocumentReference documentReference = mProfileRef.document(Enroll_doc);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists())
                            {
                                Toast.makeText(SignupActivity.this, "Enrollment Number Already Registered.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {

                                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(Enroll_doc))
                                {
                                    signup(email, pass, Enroll_doc);
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(SignupActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

    private void signup(final String email, String pass, final String Enroll_doc) {

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Map<String,String> userdata = new HashMap<>();
                    userdata.put("Enroll_doc",Enroll_doc);
                    userdata.put("email",email);
                    userdata.put("name","");
                    userdata.put("mobile_no","");
                    userdata.put("birthdate","");
                    userdata.put("admissionyear","");
                    userdata.put("profile_image","default");
                    userdata.put("profile_icon","default");
                    userdata.put("branch", branch_coll);

                    mProfileRef.document(Enroll_doc).set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            String CurrentUser = mAuth.getUid();

                            Map<String, String> enroll_data = new HashMap<>();
                            enroll_data.put("enroll_no",Enroll_doc);

                            if (CurrentUser != null) {
                                mUser_idsRef.collection(Enroll_coll).document(CurrentUser).set(enroll_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Intent profileintent = new Intent(SignupActivity.this,ProfileSetupActivity.class);
                                        profileintent.putExtra("enroll", Enroll_doc);
                                        startActivity(profileintent);
                                        finish();

                                    }
                                });
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(SignupActivity.this,"Signup Function : "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
