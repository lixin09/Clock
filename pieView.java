package com.example.clock;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

public class pieView extends View {
	private Paint pie = new Paint();
	private Paint chart = new Paint();
	private Paint log = new Paint();

	public pieView(Context context) {
		super(context);
		init(null, 0);
	}

	public pieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public pieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		pie.setColor(Color.BLACK);
		chart.setColor(Color.RED);
		log.setColor(Color.BLUE);

	}

	class PhoneCallTime {
		int startHour, endHour;
		int startMin, endMin;
		int startSec, endSec;
	}

	ArrayList<PhoneCallTime> phoneCalls = new ArrayList<PhoneCallTime>();
	

	float angle, startAngle, endAngle;
	float max = 24 * 3600;
	final RectF oval = new RectF();
	void someFunction(ArrayList<PhoneCallTime> phoneCalls){
		for (PhoneCallTime time: phoneCalls){
			int start_Hour = time.startHour;
			int start_Min = time.startMin;
			int start_Sec = time.startSec;
			int end_Hour = time.endHour;
			int end_Min = time.endMin;
			int end_Sec = time.endSec;
			startAngle = ((start_Hour*3600 + start_Min*60 + start_Sec)/max) * 360;
			endAngle = ((end_Hour*3600 + end_Min*60 + end_Sec)/max)*360;	
		}
	}
	
	public void updatechar(int hour, int min, int sec) {
		int var = sec + 60 * min + 3600 * hour;
		int max = 24 * 3600;
		angle = ((float) var / (float) max) * 360;
		postInvalidate();
	}

	/*
	 * public void log(){ Uri allCalls = Uri.parse("content://call_log/calls");
	 * Cursor c = managedQuery(allCalls, null, null, null, null); String num=
	 * c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)); String name=
	 * c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)); String duration
	 * = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
	 * 
	 * }
	 */

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float center_x, center_y;
		float radius = 200f;
		center_x = getWidth() / 2;
		center_y = getHeight() / 2;
		RectF oval = new RectF();
		oval.set(center_x - radius, center_y - radius, center_x + radius,
				center_y + radius);
		pie.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, 200f, pie);
		canvas.drawArc(oval, 270, angle, true, chart);
		canvas.drawArc(oval, 270+startAngle, endAngle - startAngle, true, log);
		// Log.e("Raja", "Something has been drawn");
		// canvas.drawArc(oval,0,)
	}

}
