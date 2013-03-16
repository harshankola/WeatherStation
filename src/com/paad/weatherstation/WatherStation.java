package com.paad.weatherstation;

import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class WatherStation extends Activity {

	private SensorManager sensorManager;
	private TextView temperatureTextView;
	private TextView pressureTextView;
	private TextView lightTextView;

	private float currentTemperature = Float.NaN;
	private float currentPressure = Float.NaN;
	private float currentLight = Float.NaN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		temperatureTextView = (TextView) findViewById(R.id.temperature);
		pressureTextView = (TextView) findViewById(R.id.pressure);
		lightTextView = (TextView) findViewById(R.id.light);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		Timer updateTimer=new Timer("weatherUpdate");
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateGUI();
			}
		}, 0, 1000);
	}

	private final SensorEventListener tempSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			currentTemperature = event.values[0];
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	private final SensorEventListener pressureSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			currentPressure = event.values[0];
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	private final SensorEventListener lightSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			currentLight = event.values[0];
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		sensorManager.unregisterListener(pressureSensorEventListener);
		sensorManager.unregisterListener(tempSensorEventListener);
		sensorManager.unregisterListener(lightSensorEventListener);
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		if (lightSensor != null) {
			sensorManager.registerListener(lightSensorEventListener,
					lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			lightTextView.setText("Light Sensor Unavailable");
		}

		Sensor pressureSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_PRESSURE);
		if (pressureSensor != null) {
			sensorManager.registerListener(pressureSensorEventListener,
					pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			pressureTextView.setText("Barometer Unavailable");
		}

		Sensor temperatureSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		if (temperatureSensor != null) {
			sensorManager.registerListener(tempSensorEventListener,
					temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			temperatureTextView.setText("Thermometer Unavailable");
		}
	}
	
	private void updateGUI() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!Float.isNaN(currentPressure)) {
					pressureTextView.setText(currentPressure+"hPa");
					pressureTextView.invalidate();
				}
				if (!Float.isNaN(currentLight)) {
					String lightStr="Sunny";
					if (currentLight<=SensorManager.LIGHT_CLOUDY) {
						lightStr="Night";
					} else if (currentLight<=SensorManager.LIGHT_OVERCAST) {
						lightStr="Cloudy";
					} else if (currentLight<=SensorManager.LIGHT_SUNLIGHT) {
						lightStr="Overcast";
					}
					lightTextView.setText(lightStr);
					lightTextView.invalidate();
				}
				if (!Float.isNaN(currentTemperature)) {
					temperatureTextView.setText(currentTemperature+"C");
					temperatureTextView.invalidate();
				}
			}
		});
	}
}
