/*
 * Author: Kristoffer Pedersen
 * Mail: deifyed <Guess (hint: its an @)> gmail.com
 * 
 * License:
 * I take no responsibility what so ever of what you decide to do with this code.
 * You are free to use and/or modify it as you wish. 
 */
 /* 
 * TODO:
 *  * Title label onClick to rename
 *  * Settings
 *  	* Font settings
 *  	* Holo dark choice?
 *  * Undo
 *  * Sync SyncAdapter <-- Not in settings
 *  
 *  Text:
 *  	Add auto-indent OG options (Lese currentLine og telle indent når bruker trykker enter)
 *  	Add auto-bullet-list OG options (Lese første character etter whitespace og evt legge til "* " når bruker trykker enter
 */

package com.cognitiveadventures.note;

import java.io.File;
import java.io.FileWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteV2Activity extends Activity {
	private final String PREFS_NAME = "NotePref";
	
	private final String PREFS_TITLESTRING = "strTitle";
	private final String PREFS_BODYSTRING = "strBody";
	private final String PREFS_NIDLONG = "longNid";
	
	public static final int REQUEST_OPEN = 1;
	
	private long nId = -1;
	
	private EditText body;
	
	private SQLAdapter sql;
	
	/*
	 * OVERRIDES
	 */
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.homeicon);
        
        body = (EditText) findViewById(R.id.txtBody);
        
        body.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(nId == -1)
					getActionBar().setIcon(R.drawable.homeiconunsaved);
			}
        	
        });
        
        sql = new SQLAdapter(this);
    }
    
    /**
     * Called when an activity regains focus.
     */
    @Override
    protected void onResume() {
    	
    	loadNote();
    	
    	super.onResume();
    }
    
    /**
     * Called when an activity lose focus.
     */
    @Override
    protected void onPause() {
    	
    	saveNote(null);
    	
    	super.onPause();
    }

    /** Creates the action bar options menu **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_action_bar, menu);
    	return(true);
    }
    
    /** Enables Action Bar buttons' actions **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case android.R.id.home:
    			renameNote();
    			return(true);
    		case R.id.btnNew:
    			newNote();
    			return(true);
    		case R.id.btnLoad:
    			startActivityForResult(new Intent(this, OpenNoteActivity.class), REQUEST_OPEN);
    			return(true);
    		case R.id.btnExport:
    			export();
    			return(true);
    		case R.id.btnSettings:
    			startActivity(new Intent(this, SettingsActivity.class));
    			return(true);
    		case R.id.btnAbout:
    			about();
    			return(true);
			default:
				return super.onOptionsItemSelected(item);
    	}
    }
    
    /**
     * Listener which fires when an activity started by startActivityForResult() finishes.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Bundle extras = data.getExtras();
    	
    	if(resultCode == Activity.RESULT_OK)
	    	switch(requestCode) {
	    		case REQUEST_OPEN:
        			getActionBar().setTitle(extras.getString(SQLAdapter.KEY_TITLE));
        			body.setText(extras.getString(SQLAdapter.KEY_BODY));
        			nId = extras.getLong(SQLAdapter.KEY_ROWID);
        			body.setSelection(body.length());
        			savePrefs();
	    	}
    }
    
    /*
     * FUNCTIONALITY
     */
    /**
     * Resets the UI and variables for a new note.
     */
    private void newNote() {
    	if(nId != -1) 
    		saveNote(null);
    	
		reset();
    }
    
    /**
     * Gives the user a dialog to rename and save their note.
     */
    private void renameNote() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(R.string.renameTitle);
    	builder.setMessage(R.string.renameMessage);
    	
    	final EditText txtTitleInput = new EditText(builder.getContext());

    	txtTitleInput.setSingleLine();
    	txtTitleInput.setText(body.getText().toString().substring(0, 15));
    		
    	builder.setView(txtTitleInput);
    	
    	builder.setPositiveButton(R.string.renameSave, new Dialog.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				saveNote(txtTitleInput.getText().toString());
				getActionBar().setIcon(R.drawable.homeicon);
			}
    		
    	});
    	builder.setNegativeButton(R.string.cancel, new Dialog.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
    		
    	});
    	
    	builder.show();
    }
    
    /**
     * Calles the About dialog.
     */
    private void about() {
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setIcon(R.drawable.launcher);
    	builder.setTitle(getString(R.string.about) + " " + getString(R.string.app_name));
    	builder.setMessage(R.string.aboutMessage);
    	
    	builder.show();
    }
    
    /**
     * Saves note to application memory or database.
     * @param nId The id of the current note.
     */
    private void saveNote(String title) {
    	if(title != null) {
    		getActionBar().setTitle(title);
    		
    		sql.open();
    		nId = sql.createNote(title, body.getText().toString());
    		sql.close();
    	}
    	else if (nId != -1) {
    		sql.open();
    		sql.updateNote(nId, getActionBar().getTitle().toString(), body.getText().toString());
    		sql.close();
    	}

		savePrefs();
    }
    
    /**
     * Loads note from SharedPreferences.
     */
    private void loadNote() {
    	loadPrefs();
    }
    
    /**
     * Used to export notes to SD-card.
     */
    private void export() {
    	export(getActionBar().getTitle().toString(), body.getText().toString());
    }
    
    /**
     * Main export magic
     * @param The title of the file.
     * @param The content of the file.
     */
    private void export(String title, String body) {
    	try {
    		File exportFile = new File(Environment.getExternalStorageDirectory(), title);
    		
    		FileWriter writer = new FileWriter(exportFile);
    		writer.append(body);
    		writer.flush();
    		writer.close();
    		
    		Toast.makeText(this, R.string.exportSuccess, Toast.LENGTH_SHORT).show();
    	}
    	catch(Exception e) {
    		Toast.makeText(this, R.string.exportError, Toast.LENGTH_SHORT).show();
    	}
    }
    
    /*
     * SUPPORT FUNCTIONS/METHODS
     */
    
    /**
     * Resets UI and nId.
     */
    private void reset() {
    	
		getActionBar().setTitle(getString(R.string.app_name));
		body.setText("");
		nId = -1;
        getActionBar().setIcon(R.drawable.homeicon);
    }
    
    private void loadPrefs() {
    	
    	getActionBar().setTitle(getSharedPreferences(PREFS_NAME, 0).getString(PREFS_TITLESTRING, getString(R.string.app_name)));
    	body.setText(getSharedPreferences(PREFS_NAME, 0).getString(PREFS_BODYSTRING, ""));
    	nId = getSharedPreferences(PREFS_NAME, 0).getLong(PREFS_NIDLONG, -1);
    	body.setSelection(body.length());
    }
    
    private void savePrefs() {
    	
    	SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, 0).edit();
    	editor.putLong(PREFS_NIDLONG, nId);
    	editor.putString(PREFS_TITLESTRING, getActionBar().getTitle().toString());
    	editor.putString(PREFS_BODYSTRING, body.getText().toString());
    	editor.commit();
    }
    
    public long getNId() {
    	
    	return(new Long(nId));
    }
}