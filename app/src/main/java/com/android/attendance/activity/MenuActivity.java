package com.android.attendance.activity;

import java.util.ArrayList;

import com.android.attendance.bean.AttendanceBean;
import com.android.attendance.bean.AttendanceBySubjectBean;
import com.android.attendance.context.ApplicationContext;
import com.android.attendance.db.DBAdapter;
import com.example.androidattendancesystem.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.cardview.widget.CardView;

public class MenuActivity extends Activity {

	CardView addStudent;
	CardView addFaculty;
	CardView viewStudent;
	CardView viewFaculty;
	CardView logout;
	CardView attendancePerStudent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		addStudent =(CardView)findViewById(R.id.cardStudent);
		addFaculty =(CardView)findViewById(R.id.cardTeacher);
		viewStudent =(CardView)findViewById(R.id.cardStudentInfo);
		viewFaculty =(CardView)findViewById(R.id.cardTeacherInfo);
		logout =(CardView)findViewById(R.id.cardLogout);
		
		addStudent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent =new Intent(MenuActivity.this,AddStudentActivity.class);
				startActivity(intent);
			}
		});
		
		addFaculty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent =new Intent(MenuActivity.this,AddFacultyActivity.class);
				startActivity(intent);
			}
		});
		
		viewFaculty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent =new Intent(MenuActivity.this,ViewFacultyActivity.class);
				startActivity(intent);
			}
		});


		viewStudent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent =new Intent(MenuActivity.this,ViewStudentActivity.class);
				startActivity(intent);
			}
		});
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent =new Intent(MenuActivity.this,MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		attendancePerStudent=(CardView)findViewById(R.id.cardAttendance);
		attendancePerStudent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				DBAdapter dbAdapter = new DBAdapter(MenuActivity.this);
				ArrayList<AttendanceBySubjectBean> attendanceBySubjectBeanList=dbAdapter.getAllAttendanceByStudent();
				((ApplicationContext)MenuActivity.this.getApplicationContext()).setAttendanceBySubjectBeans(attendanceBySubjectBeanList);
				
				Intent intent = new Intent(MenuActivity.this,ViewAttendancePerStudentActivity.class);
				startActivity(intent);
				
			}
		});
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
