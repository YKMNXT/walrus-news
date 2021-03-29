package com.walrus.news.utility;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.walrus.news.R;


public class CustomProgressDialog extends Dialog {

	TextView tvMessage;
	
	public CustomProgressDialog(Context context) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_progress_lyt);

		tvMessage = (TextView) findViewById(R.id.message);
		
	}
	
	public void setMessage(String message){
		tvMessage.setText(message);
	}
}
