package com.android.attendance.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.TextView;
import java.lang.String;
import com.example.androidattendancesystem.R;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
	private int progress = 0;
	private boolean isPaused = false;
	private Handler handler = new Handler();
	private long startTime = 0;
	TextView txt;
	private final int totalDuration = 20000; // 20 seconds in milliseconds
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ProgressBar bar = (ProgressBar) findViewById(R.id.progress);
		Button start = (Button) findViewById(R.id.buttonstart);
		txt = (TextView) findViewById(R.id.text);
		if (savedInstanceState != null) {
			progress = savedInstanceState.getInt("progress");
			startTime = savedInstanceState.getLong("startTime");
			bar.setProgress(progress);
		} else {
			startTime = System.currentTimeMillis();
		}
		Thread worker = new Thread(new Runnable() {
			@Override
			public void run() {
				while (progress < 100 && !isPaused) {
					try {
						txt.setText(String.valueOf(progress));
						handler.post(new Runnable() {
							@Override
							public void run() {
								bar.setProgress(progress++);
							}
						});

						// Sleep for the remaining duration
						if (progress < 100) {
							txt.setText(String.valueOf(progress));
							Thread.sleep(100);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Intent intent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});

		start.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				start.setVisibility(View.INVISIBLE);
				bar.setVisibility(View.VISIBLE);
				worker.start();

			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isPaused = false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("progress", progress);
		outState.putLong("startTime", startTime);
	}
}