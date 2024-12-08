package com.android.attendance.activity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
	private BroadcastReceiver loginReceiver;

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
				((TextView) view).setTextColor(Color.BLACK);
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
						Intent broadcastIntent = new Intent("com.example.LOGIN_SUCCESS");
						broadcastIntent.putExtra("role", "Administrateur");
						broadcastIntent.putExtra("username", user_name);
						sendBroadcast(broadcastIntent);

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
						Intent broadcastIntent = new Intent("com.example.LOGIN_SUCCESS");
						broadcastIntent.putExtra("role", "Enseignant");
						broadcastIntent.putExtra("username", user_name);
						sendBroadcast(broadcastIntent);
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

		loginReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String role = intent.getStringExtra("role");
				String username = intent.getStringExtra("username");

				if (role != null && username != null) {
					// Build and display a notification
					String channelId = "login_notifications";
					String channelName = "Login Notifications";

					// Create the NotificationChannel for Android 8.0 and above
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
						NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
						NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
						notificationManager.createNotificationChannel(channel);
					}

					// Build the notification
					NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
							.setSmallIcon(android.R.drawable.ic_dialog_info)
							.setContentTitle("Connexion Réussie")
							.setContentText("Rôle: " + role + ", Nom d'utilisateur: " + username)
							.setPriority(NotificationCompat.PRIORITY_DEFAULT)
							.setAutoCancel(true);

					// Show the notification
					NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
					notificationManager.notify(1, builder.build());
				}
			}
		};

	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter("com.example.LOGIN_SUCCESS");
		registerReceiver(loginReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(loginReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}