package com.example.clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.example.clock.pieView.PhoneCallTime;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
public class MainActivity extends Activity {

	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			"MMM dd, yyyy\n\nhh:mm:ss a");

	private static final DateFormat DATE_FORMATTER2 = new SimpleDateFormat(
			"MMM dd, yyyy\n\nHH:mm:ss a");

	private Timer mTimerCount;
	private long currentTime = 0;

	pieView mPie;

	// Enqueue an action to be performed on a different thread than your own
	private Handler mUiHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPie = (pieView) findViewById(R.id.pie);
	}

	@Override
	protected void onResume() {
		getCallDetails();
		currentTime = System.currentTimeMillis();
		mTimerCount = new Timer();

		mTimerCount.schedule(new TimerTask() {
			@Override
			public void run() {
				currentTime += 1000;
				mUiHandler.post(new Runnable() {
					@Override
					public void run() {
						TextView textView = (TextView) findViewById(R.id.timeTV);
						textView.setText(formatTime(currentTime));

						String dumb = formatTime24Hour(currentTime);
						dumb = dumb.substring(dumb.length() - 11);

						// Log.e("Raja", dumb);//
						// (formatTime(currentTime).split(" "))[2]);
						String[] values = dumb.split(":");
						int hour = Integer.parseInt(values[0]);
						int min = Integer.parseInt(values[1]);
						int sec = Integer.parseInt(values[2].substring(0, 2));

						mPie.updatechar(hour, min, sec);
					}
				});
			}
		}, 0, 1000);

		super.onResume();
	}

	protected void onPause() {
		mTimerCount.cancel();
		super.onPause();
	}

	private static String formatTime(long time) {
		return DATE_FORMATTER.format(new Date(time));
	}

	private static String formatTime24Hour(long time) {
		return DATE_FORMATTER2.format(new Date(time));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	ArrayList<PhoneCallTime> phoneCalls = new ArrayList<PhoneCallTime>();

	private void getCallDetails() {
		StringBuffer sb = new StringBuffer();
		String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
		/* Query the CallLog Content Provider */
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
				null, null, strOrder);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		sb.append("Call Log :");
		while (managedCursor.moveToNext()) {
			String phNum = managedCursor.getString(number);
			String callTypeCode = managedCursor.getString(type);
			String strcallDate = managedCursor.getString(date);
			Date callDate = new Date(Long.valueOf(strcallDate));
			int startHour = callDate.getHours();
			int startMin = callDate.getMinutes();
			int startSec = callDate.getSeconds();

			String callDuration = managedCursor.getString(duration);

			Calendar c = Calendar.getInstance();
			c.setTime(callDate);
			c.add(Calendar.SECOND, Integer.parseInt(callDuration));
			int endHour = c.getTime().getHours();
			int endMin = c.getTime().getMinutes();
			int endSec = c.getTime().getSeconds();

		//	phoneCalls.add(new PhoneCallTime(startHour, startMin, startSec,
			//		endHour, endMin, endSec));

			String callType = null;
			int callcode = Integer.parseInt(callTypeCode);
			switch (callcode) {
			case CallLog.Calls.OUTGOING_TYPE:
				callType = "Outgoing";
				break;
			case CallLog.Calls.INCOMING_TYPE:
				callType = "Incoming";
				break;
			case CallLog.Calls.MISSED_TYPE:
				callType = "Missed";
				break;
			}
			sb.append("\nPhone Number:--- " + phNum + " \nCall Type:--- "
					+ callType + " \nCall Date:--- " + callDate
					+ " \nCall duration in sec :--- " + callDuration);
			sb.append("\n----------------------------------");
			Log.e("Raja", sb.toString());
		}
		managedCursor.close();
	}
}

