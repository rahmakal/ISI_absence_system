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
import com.example.androidattendancesystem.R;

public class MainActivity extends Activity {
	private int progress = 0;
	private boolean isPaused = true;
	private boolean isClicked = false;
	private Handler handler = new Handler();
	private ProgressBar bar;
	private TextView txt;
	private Button startButton;
	private Runnable progressRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = findViewById(R.id.progress);
		startButton = findViewById(R.id.buttonstart);
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
					handler.postDelayed(this, 50);
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
				bar.setVisibility(View.VISIBLE);
				isPaused = false;
				handler.post(progressRunnable);
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
