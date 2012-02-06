package com.cognitiveadventures.note;

import android.app.ActionBar;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

public class BodyTextWatcher implements TextWatcher {
	
	private Context mContext;
	private ActionBar mActionBar;
	
	public BodyTextWatcher(Context ctx, ActionBar ab) {
		
		mContext = ctx;
		mActionBar = ab;
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		//if( == -1)
			mActionBar.setIcon(R.drawable.homeiconunsaved);
		
	}
}
