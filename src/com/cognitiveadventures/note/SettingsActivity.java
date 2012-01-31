package com.cognitiveadventures.note;

import com.cognitiveadventures.com.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends Activity {
	/*
	 * OVERRIDES
	 */
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.settings);
        
        getActionBar().setHomeButtonEnabled(true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case android.R.id.home:
    			finishActivity(-1);
    			return(true);
			default:
		    	return super.onOptionsItemSelected(item);
    	}
    }
}
