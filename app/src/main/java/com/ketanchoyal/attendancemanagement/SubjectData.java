package com.ketanchoyal.attendancemanagement;

public class SubjectData extends SubjectData_ID {

    String sub_code;
    String sub_teacher;
    String sub_name;
    String sub_qr_code;
    String sub_type;

    public SubjectData(){}

    public SubjectData(String sub_code, String sub_teacher, String sub_name, String sub_qr_code, String sub_type) {
        this.sub_code = sub_code;
        this.sub_teacher = sub_teacher;
        this.sub_name = sub_name;
        this.sub_qr_code = sub_qr_code;
        this.sub_type = sub_type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_teacher() {
        return sub_teacher;
    }

    public void setSub_teacher(String sub_teacher) {
        this.sub_teacher = sub_teacher;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_qr_code() {
        return sub_qr_code;
    }

    public void setSub_qr_code(String sub_qr_code) {
        this.sub_qr_code = sub_qr_code;
    }
}
