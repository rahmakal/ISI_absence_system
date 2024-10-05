package com.android.attendance.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.attendance.bean.FacultyBean;
import com.android.attendance.context.ApplicationContext;
import com.android.attendance.db.DBAdapter;
import com.example.androidattendancesystem.R;

public class LoginActivity extends Activity {

	Button login;
	EditText username, password;
	Spinner spinnerloginas;
	String userrole;
	private String[] userRoleString = new String[]{"Espace Enseignant", "Administrateur"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		login = findViewById(R.id.buttonlogin);
		username = findViewById(R.id.editTextusername);
		password = findViewById(R.id.editTextpassword);
		spinnerloginas = findViewById(R.id.spinnerloginas);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				R.layout.spinner_item, userRoleString);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerloginas.setAdapter(adapter);

		spinnerloginas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				userrole = (String) spinnerloginas.getSelectedItem();
				((TextView) view).setTextColor(Color.WHITE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String user_name = username.getText().toString();
				String pass_word = password.getText().toString();

				if (TextUtils.isEmpty(user_name)) {
					username.setError("Vérifiez Nom Utilisateur");
					return;
				}
				if (TextUtils.isEmpty(pass_word)) {
					password.setError("Entrer mot de passe");
					return;
				}

				if (userrole.equals("Administrateur")) {
					if (user_name.equals("ISI") && pass_word.equals("ISI2@24")) {
						Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
						startActivity(intent);
						Toast.makeText(getApplicationContext(), "Login ISI :-)", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "Login ISI :-(", Toast.LENGTH_SHORT).show();
					}

				} else if (userrole.equals("Espace Enseignant")) {
					DBAdapter dbAdapter = new DBAdapter(LoginActivity.this);
					FacultyBean facultyBean = dbAdapter.validateFaculty(user_name, pass_word);
					if (facultyBean != null) {
						Intent intent = new Intent(LoginActivity.this, AddAttandanceSessionActivity.class);
						startActivity(intent);
						((ApplicationContext) LoginActivity.this.getApplicationContext()).setFacultyBean(facultyBean);
						Toast.makeText(getApplicationContext(), "Login Enseignant :-)", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "Login Enseignant :-(", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
