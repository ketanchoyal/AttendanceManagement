package com.ketanchoyal.attendancemanagement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {

    public List<SubjectData> subList;
    public Context context;
    public String Date;
    public String Month;
    public String Year;

    public FirebaseFirestore mFireStore;
    public DocumentReference mPresentAbsentRef;

    public int a;
    public String CurrentUser;
    public String field_coll = null;
    public String Type_doc = null;
    public String YearofAdmission_coll = null;
    public String Attendance_doc = null;
    public String Profile_doc = null;
    public String branch_coll = null;
    public String Enroll_doc = "Enrollment No Loading..";
    public String Enroll_coll = "Enroll_no";
    public String Sem_coll = null;

    public SubjectListAdapter(String Date,int a,Context context,List<SubjectData> subList,String Enroll_doc)
    {
        this.subList = subList;
        this.context = context;
        this.Enroll_coll = Enroll_doc;
        this.a = a;
        this.Date = Date;
        mFireStore = FirebaseFirestore.getInstance();
    }

    public SubjectListAdapter(String Date,int a,Context context,List<SubjectData> subList,String Enroll_doc,
                              String branch_coll, String YearofAdmission_coll, String Sem_coll, String Month, String Year)
    {
        this.subList = subList;
        this.context = context;
        this.Enroll_coll = Enroll_doc;
        this.a = a;
        this.Date = Date;
        this.branch_coll = branch_coll;
        this.YearofAdmission_coll = YearofAdmission_coll;
        this.Sem_coll = Sem_coll;
        this.Month = Month;
        this.Year = Year;

        mFireStore = FirebaseFirestore.getInstance();

        field_coll = "Engineering";
        Profile_doc = "Profiles";
        Attendance_doc = "Attendance";
        Type_doc = "Students";
    }

    @Override
    public SubjectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubjectListAdapter.ViewHolder holder, int position) {

        final String sub_name =  subList.get(position).getSub_name();
        final String sub_code = subList.get(position).getSub_code();
        final String sub_doc_id = subList.get(position).sub_doc_id;
        final String sub_qr_code = subList.get(position).getSub_qr_code();
        final String sub_teacher = subList.get(position).getSub_teacher();
        final String sub_type = subList.get(position).getSub_type();

        String Subject = sub_name+"("+sub_code+")";

        holder.mSub_name_code.setText(Subject);

        holder.mSub_teacher.setText(subList.get(position).getSub_teacher());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a == 1) {
                    Intent presentabsentIntent = new Intent(context, PresentAbsentActivity.class);
                    presentabsentIntent.putExtra("sub_code", sub_code);
                    presentabsentIntent.putExtra("sub_name", sub_name);
                    presentabsentIntent.putExtra("enroll", Enroll_coll);
                    presentabsentIntent.putExtra("sub_doc_id", sub_doc_id);
                    presentabsentIntent.putExtra("sub_qr_code", sub_qr_code);
                    presentabsentIntent.putExtra("sub_teacher", sub_teacher);
                    presentabsentIntent.putExtra("sub_type", sub_type);
                    context.startActivity(presentabsentIntent);
                }
            }
        });

        if(a == 0 && !Date.equals(""))
        {
            //Toast.makeText(context,Date,Toast.LENGTH_LONG).show();

            mPresentAbsentRef = mFireStore.collection(field_coll).document(Type_doc).collection(YearofAdmission_coll)
                    .document(Attendance_doc).collection(branch_coll).document("SEM").collection(Sem_coll)
                    .document(sub_doc_id).collection(Enroll_coll).document(Year).collection(Month).document(Date);

            mPresentAbsentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            HashMap<String, Object> attendancedata = new HashMap<>(document.getData());

                            String p_or_a = attendancedata.get("p_or_a").toString();

                            if (p_or_a.equals("1") || p_or_a.equals("2")) {
                                holder.mSub_attendance.setText("P");
                            }
                            if(p_or_a.equals("0"))
                            {
                                holder.mSub_attendance.setText("A");
                            }

                        }
                        else
                        {
                            holder.mSub_attendance.setText("Pending");
                        }
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return subList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public TextView mSub_name_code;
        public TextView mSub_teacher;
        public TextView mSub_attendance;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mSub_name_code = mView.findViewById(R.id.sub_name);
            mSub_teacher = mView.findViewById(R.id.sub_teacher);
            mSub_attendance = mView.findViewById(R.id.sub_attendance);

        }
    }

}
