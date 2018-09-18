package com.ketanchoyal.attendancemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    private TextInputLayout mEmail;
    private TextInputLayout mPassword;

    private Button mSigninBtn;

    private TextView mPasswordReset;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;

    private String CurrentUser;
    public String Enroll_no;
    final String Enrollment = "ENROLLMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mPasswordReset = findViewById(R.id.password_reset);

        mEmail = findViewById(R.id.signin_signin_email_field);
        mPassword = findViewById(R.id.signin_signin_password_field);

        mSigninBtn = findViewById(R.id.signin_signin_btn);

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getEditText().getText().toString().trim();
                if(!TextUtils.isEmpty(email))
                {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(SigninActivity.this,"Password Reset Link has been sent to your Email-Id.",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getEditText().getText().toString().trim();
                String pass = mPassword.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
                {
                    signin(email,pass);
                }

            }
        });

    }

    private void signin(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    CurrentUser = mAuth.getCurrentUser().getUid();

                    Intent mainIntent = new Intent(SigninActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        });

    }


}
