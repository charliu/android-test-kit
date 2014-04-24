package com.vimc.atk.view;

import com.vimc.atk.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatView extends LinearLayout implements View.OnClickListener {
	static final String SEND = "T:";
	static final String RCV = "R:";
	static final String ALL = "A:";
	private TextView trafficView;
	private Button clearButton;
	private ResetTrafficListener resetTrafficListener;
	
	public FloatView(Context context) {
		super(context);
		init();
	}
	
	public FloatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		LayoutInflater flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = flater.inflate(R.layout.floating, this);
		trafficView = (TextView) view.findViewById(R.id.traffic);
		clearButton = (Button) view.findViewById(R.id.clear);
		clearButton.setOnClickListener(this);
	}
	
	public void updateTraffic(long t, long r, String percent) {
//		trafficView.setText(SEND + t + " b\n" + RCV + r + " b\n" + ALL + (t + r) + " b");
		trafficView.setText(formatTraffic(t, r, percent));
	}
	
	private String formatTraffic(long t, long r, String percent) {
		StringBuffer sb = new StringBuffer();
		sb.append(SEND);
		sb.append(t);
		sb.append(" b\n");
		sb.append(RCV);
		sb.append(r);
		sb.append(" b\n");
		sb.append(ALL);
		sb.append(t + r);
		sb.append(" b\n");
		sb.append(String.format("%.2f", (t + r)/1024.0));
		sb.append(" Kb");
		sb.append("\n");
		sb.append("CPU%:");
		sb.append(percent);
		return sb.toString();
	}
	
	public void setText(String str) {
		trafficView.setText(str);
	}
	
	public interface ResetTrafficListener {
		void onResetListener();
	}
	
	public void setResetTrafficListener(ResetTrafficListener listener) {
		this.resetTrafficListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == clearButton.getId()) {
			if (resetTrafficListener != null) {
				resetTrafficListener.onResetListener();
			}
		}
	}
}
