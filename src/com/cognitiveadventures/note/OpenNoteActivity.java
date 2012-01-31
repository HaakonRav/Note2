package com.cognitiveadventures.note;

import com.cognitiveadventures.com.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class OpenNoteActivity extends ListActivity {
		private final int DELETE_ID = 1;
		
		//private ArrayList<Long> selectedItems = new ArrayList<Long>();
	
		SQLAdapter sql;
		Cursor mNotesCursor;
		
		 @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.loadnotelist);
			
			sql = new SQLAdapter(this);
			
			fillData();
			
			registerForContextMenu(getListView());
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
		}
		 
	    /** Creates the action bar options menu **/
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	MenuInflater inflater = getMenuInflater();
	    	inflater.inflate(R.menu.open_action_bar, menu);
	    	return(true);
	    }
		 
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		
			switch(item.getItemId()) {
				case android.R.id.home:
					setResult(Activity.RESULT_CANCELED, new Intent());
					finish();
				case R.id.btnDelete:
					Toast.makeText(this, "Count: " + getListView().getCheckedItemCount(), Toast.LENGTH_SHORT).show();
				default:
					return super.onOptionsItemSelected(item);
			}
		}
		 
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			
			super.onCreateContextMenu(menu, v, menuInfo);
			
			menu.add(0, DELETE_ID, 0, R.string.delete_notes);
		}
		
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			switch(item.getItemId()) {
				case DELETE_ID:
					AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
					sql.open();
					sql.deleteNote(info.id);
					sql.close();
					fillData();
					return(true);
			}
			
			
			return super.onContextItemSelected(item);
		}
		
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
		
			super.onListItemClick(l, v, position, id);
			
			Cursor c = mNotesCursor;
			c.moveToPosition(position);
			
			Bundle i = new Bundle();
			i.putLong(SQLAdapter.KEY_ROWID, id);
			i.putString(SQLAdapter.KEY_TITLE, c.getString(c.getColumnIndexOrThrow(SQLAdapter.KEY_TITLE)));
			i.putString(SQLAdapter.KEY_BODY, c.getString(c.getColumnIndexOrThrow(SQLAdapter.KEY_BODY)));
			
			setResult(Activity.RESULT_OK, new Intent().putExtras(i));
			
			finish();
		}
		
		@Override
		public void onBackPressed() {
			
			setResult(Activity.RESULT_CANCELED, new Intent());
			
			super.onBackPressed();
		}
		
		private void fillData() {
			sql.open();
			mNotesCursor = sql.fetchAllNotes();
			
			startManagingCursor(mNotesCursor);
			
			String[] from = new String[] { SQLAdapter.KEY_TITLE };
			int[] to = new int[] { R.id.noteTitle };
			
			SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.loadnotes_item, mNotesCursor, from, to);
			//SimpleCursorAdapter notes = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, mNotesCursor, from, to);
			
			setListAdapter(notes);
			sql.close();
		}
}
