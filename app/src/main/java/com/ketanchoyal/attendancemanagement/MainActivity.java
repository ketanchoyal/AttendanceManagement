package com.ketanchoyal.attendancemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;
    private FirebaseUser firebaseUser;

    private CollectionReference mProfileRef;
    private CollectionReference mUser_idsRef;

    private TextView mEnrollmentview;
    private TextView mName;
    private TextView mBranch;

    private Button mAddendancebtn;
    private Button mAddendanceHistorybtn;
    private Button mLogoutbtn;

    private CircleImageView mProfileicon;

    private String CurrentUser;
    private String field_coll = null;
    private String Type_doc = null;
    private String YearofAdmission_coll = null;
    private String Attendance_doc = null;
    private String Profile_doc = null;
    private String branch_coll = null;
    private String Enroll_doc = "Enrollment No Loading..";

    private String Enroll_coll = "Enroll_no";
    private String User_ids = "User_ids";

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null)
        {
            Intent intent = new Intent(this,StartActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            getdata();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        mEnrollmentview = findViewById(R.id.mainenrollmentno);

        mProfileicon = findViewById(R.id.mainprofile_icon);

        mName = findViewById(R.id.mainuser_name);
        mBranch = findViewById(R.id.mainbranch);

        mAddendancebtn = findViewById(R.id.mainattendance_btn);
        mAddendanceHistorybtn = findViewById(R.id.mainattendancehistory_btn);
        mLogoutbtn = findViewById(R.id.mainlogout_btn);

        CurrentUser = mAuth.getUid();

        field_coll = "Engineering";
        YearofAdmission_coll = "2015";
        branch_coll = "CSE";
        Profile_doc = "Profiles";
        Attendance_doc = "Attendance";
        Type_doc = "Students";

        mFireStore.collection(field_coll).document(Type_doc).collection(YearofAdmission_coll)
                .document(Attendance_doc).collection(branch_coll).document("SEM").collection("SEM_1").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange Doc : documentSnapshots.getDocumentChanges())
                {
                    if(Doc.getType() == DocumentChange.Type.ADDED)
                    {
                        String Sub_code = Doc.getDocument().getString("sub_code");
                        String teacher = Doc.getDocument().getString("sub_teacher");

                        Log.d("Subcode","Code : "+Sub_code);
                        Log.d("Subcode","Teacher : "+teacher);

                    }
                }

            }
        });

        mProfileRef = mFireStore.collection(field_coll).document(Type_doc)
                .collection(YearofAdmission_coll).document(Profile_doc).collection(branch_coll);

        mUser_idsRef =  mFireStore.collection(field_coll).document(Type_doc)
                .collection(YearofAdmission_coll).document(User_ids).collection(Enroll_coll);

        if(CurrentUser != null) {
            getdata();

            mProfileicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mUser_idsRef.document(CurrentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                Enroll_doc = documentSnapshot.getData().get("enroll_no").toString();

                                Intent profileIntent = new Intent(MainActivity.this, ProfileSetupActivity.class);
                                profileIntent.putExtra("enroll", Enroll_doc);
                                startActivity(profileIntent);

                            }
                        }
                    });

                }
            });

            mAddendancebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mUser_idsRef.document(CurrentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                Enroll_doc = documentSnapshot.getData().get("enroll_no").toString();

                                Intent AttendanceIntent = new Intent(MainActivity.this,AttendanceActivity.class);
                                AttendanceIntent.putExtra("enroll", Enroll_doc);
                                startActivity(AttendanceIntent);

                            }
                        }
                    });

                }
            });

            mAddendanceHistorybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mUser_idsRef.document(CurrentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();

                                Enroll_doc = documentSnapshot.getData().get("enroll_no").toString();

                                Intent calenderIntent = new Intent(MainActivity.this,AttendanceCalenderActivity.class);
                                calenderIntent.putExtra("enroll", Enroll_doc);
                                startActivity(calenderIntent);

                            }
                        }
                    });

                }
            });

            mLogoutbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                    finish();
                }
            });

        }
    }

    private void getdata() {

        mUser_idsRef.document(CurrentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    Enroll_doc = documentSnapshot.getData().get("enroll_no").toString();
                    mEnrollmentview.setText(Enroll_doc);

                    mProfileRef.document(Enroll_doc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {

                                    HashMap<String, Object> user_data = new HashMap<>(document.getData());

                                    String Name = user_data.get("name").toString();
                                    String Branch = user_data.get("branch").toString();

                                    mName.setText(Name);
                                    mBranch.setText(" | "+Branch);

                                }
                            } else {

                            }
                        }
                    });

                }
            }
        });

    }

    /*private void getdata() {

        mUser_idsRef.document(CurrentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {

                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());

                        HashMap<String,Object> enroll_data = new HashMap<>(document.getData());

                        Enroll_no = enroll_data.get("enroll_no").toString();
                        mEnrollmentview.setText(Enroll_no);

                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }*/

}

