package com.ketanchoyal.attendancemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView mSubListRec;

    private List<SubjectData> SubList;

    private FirebaseFirestore mFireStore;
    private CollectionReference mAttendanceRef;

    private SubjectListAdapter subjectListAdapter;

    private String CurrentUser;
    private String field_coll = null;
    private String Type_doc = null;
    private String YearofAdmission_coll = null;
    private String Attendance_doc = null;
    private String Profile_doc = null;
    private String branch_coll = null;
    private String Enroll_doc = "Enrollment No Loading..";
    private String Enroll_coll = "Enroll_no";
    private String Sem_coll = null;

    private String User_ids = "User_ids";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        mFireStore = FirebaseFirestore.getInstance();

        field_coll = "Engineering";
        YearofAdmission_coll = "2015";
        branch_coll = "CSE";
        Profile_doc = "Profiles";
        Attendance_doc = "Attendance";
        Type_doc = "Students";
        Sem_coll = "SEM_1";

        Enroll_coll = getIntent().getStringExtra("enroll");

        mAttendanceRef = mFireStore.collection(field_coll).document(Type_doc).collection(YearofAdmission_coll)
                .document(Attendance_doc).collection(branch_coll).document("SEM").collection(Sem_coll);

        SubList = new ArrayList<>();

        subjectListAdapter = new SubjectListAdapter("",1,getApplicationContext(),SubList,Enroll_coll);
        mSubListRec = findViewById(R.id.sub_list);
        mSubListRec.setHasFixedSize(true);
        mSubListRec.setLayoutManager(new LinearLayoutManager(this));
        mSubListRec.setAdapter(subjectListAdapter);

        mAttendanceRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc : documentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        String sub_doc_id = doc.getDocument().getId();

                        SubjectData subjectData = doc.getDocument().toObject(SubjectData.class).withId(sub_doc_id);
                        SubList.add(subjectData);

                        subjectListAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }
}
