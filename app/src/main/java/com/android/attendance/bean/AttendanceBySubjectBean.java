package com.android.attendance.bean;

public class AttendanceBySubjectBean {
    private int attendance_student_id;
    private String attendance_session_subject;
    private int count;

    public int getAttendance_student_id() {
        return attendance_student_id;
    }

    public void setAttendance_student_id(int attendance_student_id) {
        this.attendance_student_id = attendance_student_id;
    }

    public String getAttendance_session_subject() {
        return attendance_session_subject;
    }

    public void setAttendance_session_subject(String attendance_session_subject) {
        this.attendance_session_subject = attendance_session_subject;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
