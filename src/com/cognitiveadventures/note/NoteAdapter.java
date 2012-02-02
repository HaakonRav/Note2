package com.cognitiveadventures.note;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
	
	private int id;
	private int title;
	private long[] idsToDelete;
	
	private Context mContext;
	private Cursor mCursor;
	
	NoteAdapter(Context ctx, Cursor c) {
		
		mContext = ctx;
		mCursor = c;
		
		id = mCursor.getColumnIndex(SQLAdapter.KEY_ROWID);
		title = mCursor.getColumnIndex(SQLAdapter.KEY_TITLE);
	}

	public int getCount() {
		
		int sum = 0;
		
		mCursor.moveToFirst();
		
		while(mCursor.moveToNext())
			sum++;
		
		return(sum);
	}

	public Note getItem(int position) {
		
		mCursor.moveToPosition(position);
		
		return(new Note(mCursor.getLong(id), mCursor.getString(title)));
	}

	public long getItemId(int position) {
		
		mCursor.moveToPosition(position);
		
		return(mCursor.getLong(id));
	}
	
	public long[] getDeleteIds() {
		ArrayList<Long> noteIds = new ArrayList<Long>();
		
		for(int i = 0; i < getCount(); i++) {
			Note n = getItem(i);
			if(n.isChecked())
				noteIds.add(n.getId());
		}
		
		long[] returner = new long[noteIds.size()];
		
		int i = 0;
		for(long l : noteIds)
			returner[i++] = l;
		
		return(returner);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		View v;
		
		if(convertView == null)
			v = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loadnotes_item, null);
		else
			v = convertView;
		
		mCursor.moveToPosition(position);
		
		((TextView) v.findViewById(R.id.noteTitle)).setText(mCursor.getString(title));
		((CheckBox) v.findViewById(android.R.id.checkbox)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if(((CheckBox) v).isChecked())
					getItem(position).setChecked(true);
				else
					getItem(position).setChecked(false);
			}
		});
		
		return v;
	}
}
