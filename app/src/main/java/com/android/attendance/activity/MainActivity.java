package com.android.attendance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidattendancesystem.R;

public class MainActivity extends Activity {
	private int progress = 0;
	private boolean isPaused = true;
	private boolean isClicked = false;
	private Handler handler = new Handler();
	private ProgressBar bar;
	private TextView txt;
	private Button startButton;
	private Button quizButton;
	private Runnable progressRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		startService(new Intent(this,Reminder.class));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = findViewById(R.id.progress);
		startButton = findViewById(R.id.buttonstart);
		quizButton = findViewById(R.id.buttonquiz);
		txt = findViewById(R.id.text);

		if (savedInstanceState != null) {
			progress = savedInstanceState.getInt("progress");
			bar.setProgress(progress);
		}
		progressRunnable = new Runnable() {
			@Override
			public void run() {
				if (!isPaused && progress < 100) {
					progress++;
					bar.setProgress(progress);
					txt.setText(String.valueOf(progress)+"%");
					handler.postDelayed(this, 10);
				} else if (progress >= 100) {
					Intent intent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};

		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isClicked = true;
				startButton.setVisibility(View.INVISIBLE);
				quizButton.setVisibility(View.INVISIBLE);
				bar.setVisibility(View.VISIBLE);
				isPaused = false;
				handler.post(progressRunnable);
			}
		});
		quizButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Replace "com.example.quizapp" with the actual package name of the Quiz app
				String packageName = "com.example.quizapp";
				Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

				if (launchIntent != null) {
					startActivity(launchIntent); // Launch the Quiz app
				} else {
					// Notify user if the Quiz app is not installed
					Toast.makeText(getApplicationContext(), "Quiz app not installed", Toast.LENGTH_SHORT).show();
				}
			}
		});


	}

	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
		handler.removeCallbacks(progressRunnable);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(isClicked){
			isPaused = false;
		}
		if (progress < 100 && !isPaused) {
			handler.post(progressRunnable);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("progress", progress);
	}
}
