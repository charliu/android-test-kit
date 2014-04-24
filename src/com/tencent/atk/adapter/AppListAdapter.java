package com.tencent.atk.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tencent.atk.R;
import com.tencent.atk.model.AppInfo;

public class AppListAdapter extends BaseListAdapter<AppInfo>{
	private int selectedId = -1;
	private Activity activity;
	private String selectItemName;
	
	public AppListAdapter(Activity context) {
		super(context);
		activity = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.appinfo_item, null);
			holder = new ViewHolder();
			holder.radio = (RadioButton) convertView.findViewById(R.id.app_check);
			holder.image = (ImageView) convertView.findViewById(R.id.app_img);
			holder.text = (TextView) convertView.findViewById(R.id.app_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AppInfo info = getItems().get(position);
		holder.text.setText(info.getAppName());
		holder.image.setImageDrawable(info.getIcon());
		holder.radio.setId(position);
		holder.radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (selectedId != -1) {
						RadioButton tempButton = (RadioButton) activity.findViewById(selectedId);
						if (tempButton != null)
							tempButton.setChecked(false);
					}
					selectedId = buttonView.getId();
				}
			}
		});
		
		if (position == selectedId) {
			holder.radio.setChecked(true);
		} else {
			holder.radio.setChecked(false);
		}
		if (info.getPackageName().equals(selectItemName)) {
			holder.radio.setChecked(true);
		}
		return convertView;
	}
	
	
	public int getSelectedId() {
		return selectedId;
	}

	private class ViewHolder {
		RadioButton radio;
		ImageView image;
		TextView text;
	}
	
	public void setSelectItemName(String selectItemName) {
		this.selectItemName = selectItemName;
	}

}
