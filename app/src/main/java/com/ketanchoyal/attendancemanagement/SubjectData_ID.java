package com.ketanchoyal.attendancemanagement;

import android.support.annotation.NonNull;

public class SubjectData_ID {

    public String sub_doc_id;

    public <T extends SubjectData_ID> T withId(@NonNull final String id)
    {
        this.sub_doc_id = id;

        return (T) this;
    }
}

