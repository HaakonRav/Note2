package com.cognitiveadventures.note;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class OpenNoteActivity extends ListActivity implements DialogInterface.OnClickListener {
	
	private SQLAdapter sql;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.loadnotelist);
        
        sql = new SQLAdapter(this);
        
        getActionBar().setHomeButtonEnabled(true);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        fillList();
    }
    
    @Override
    public void onBackPressed() {
    	
    	setResult(Activity.RESULT_CANCELED, new Intent());
    	
    	this.finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	getMenuInflater().inflate(R.menu.open_action_bar, menu);
    	
    	return(true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch(item.getItemId()) {
    	
    		case android.R.id.home:
    			setResult(Activity.RESULT_CANCELED, new Intent());
    			this.finish();
    			return(true);
    		case R.id.btnDelete:
    			batchDeleteAlert(((NoteListAdapter) getListAdapter()).getSelectedCount());
    			return(true);
    		default:
    	    	return super.onOptionsItemSelected(item);
    	}
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	sql.open();
    	
    	Cursor c = sql.fetchNote(id);
    	
    	sql.close();
    	
    	if(c.moveToFirst()) {
    		
        	Bundle extras = new Bundle();
        	
    		extras.putString(SQLAdapter.KEY_TITLE, c.getString(c.getColumnIndexOrThrow(SQLAdapter.KEY_TITLE)));
    		extras.putString(SQLAdapter.KEY_BODY, c.getString(c.getColumnIndexOrThrow(SQLAdapter.KEY_BODY)));
    		extras.putLong(SQLAdapter.KEY_ROWID, c.getLong(c.getColumnIndexOrThrow(SQLAdapter.KEY_ROWID)));
    		
    		this.setResult(Activity.RESULT_OK, new Intent().putExtras(extras));
    		
    		finish();
    	}
    	
    	super.onListItemClick(l, v, position, id);
    }
    
    void fillList() {
    	
    	sql.open();
    	
    	Cursor c = sql.fetchAllNotes();
    	
    	startManagingCursor(c);
    	
    	NoteListAdapter adapter = new NoteListAdapter(this, R.layout.loadnotes_item, c, SQLAdapter.KEY_TITLE, R.id.noteTitle);
    	
    	setListAdapter(adapter);
    	
    	sql.close();
    }

    void batchDeleteAlert(int n) {
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Holo_Dialog);
    	
    	builder.setTitle(R.string.delete_notes_dialog_title);
    	builder.setMessage(String.format(getResources().getString(R.string.delete_notes_dialog_message), n));
    	
    	builder.setPositiveButton(R.string.delete_notes, this);
    	
    	builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
    		
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
    	
    	builder.show();
    }

	public void onClick(DialogInterface dialog, int which) {
		
		if(which == Dialog.BUTTON_POSITIVE) {
			
	    	sql.open();
	    	for(long id : ((NoteListAdapter) getListAdapter()).getSelectedItems())
	    		sql.deleteNote(id);
	    	sql.close();
    		
    		fillList();
		}
	}
}
