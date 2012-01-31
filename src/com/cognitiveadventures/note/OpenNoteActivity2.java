package com.cognitiveadventures.note;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.cognitiveadventures.com.R;

public class OpenNoteActivity2 extends ListActivity {
		
		SQLAdapter sql;
		//NoteListAdapter adapter;
		
		ListView listView;
		
		 @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//adapter = new NoteListAdapter(this, data);
			//setListAdapter(adapter);
			
			listView = getListView();
			listView.setItemsCanFocus(false);
			
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
		public void onBackPressed() {
			
			setResult(Activity.RESULT_CANCELED, new Intent());
			
			super.onBackPressed();
		}
		
		private void showSelectedItems() {
			
			final SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
			
			if(checkedItems == null)
				return;
			
			final int checkedItemsCount = checkedItems.size();
			
			for(int i = 0; i < checkedItemsCount; ++i) {
				
				final int position = checkedItems.keyAt(i);
				
				final boolean isChecked = checkedItems.valueAt(i);
				
				//final Note currentItem = adapter.getNote();
			}
		}
}
