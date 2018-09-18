package com.ketanchoyal.attendancemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private String Date = "";
    private String Month = "";
    private String Year = "";

    private TextView mHeading;
    private RecyclerView mHistroy_sub_list;

    private List<SubjectData> History_SubList;

    private FirebaseFirestore mFireStore;
    private CollectionReference mAttendanceRef;

    private SubjectListAdapter history_subjectListAdapter;

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
        setContentView(R.layout.activity_attendance_history);

        Date = getIntent().getStringExtra("date");
        Month = getIntent().getStringExtra("Month");
        Year = getIntent().getStringExtra("Year");

        mHeading = findViewById(R.id.attendancedetailheading);

        mHeading.setText(Date);

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

        History_SubList = new ArrayList<>();

        history_subjectListAdapter = new SubjectListAdapter(Date,0,getApplicationContext(),History_SubList,
                Enroll_coll,branch_coll,YearofAdmission_coll,Sem_coll,Month,Year);
        mHistroy_sub_list = findViewById(R.id.history_sub_list);
        mHistroy_sub_list.setHasFixedSize(true);
        mHistroy_sub_list.setLayoutManager(new LinearLayoutManager(this));
        mHistroy_sub_list.setAdapter(history_subjectListAdapter);

        mAttendanceRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc : documentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        String sub_doc_id = doc.getDocument().getId();

                        SubjectData subjectData = doc.getDocument().toObject(SubjectData.class).withId(sub_doc_id);
                        History_SubList.add(subjectData);

                        history_subjectListAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }
}
