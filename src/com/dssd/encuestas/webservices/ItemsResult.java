package com.dssd.encuestas.webservices;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public abstract class ItemsResult<T> extends Result {
	
	public abstract List<T> getItems();

	public abstract ContentValues getContentValues(T item);
	public abstract String getTableName();
	
	public int deleteAllFromDatabase(SQLiteDatabase db) {
		return db.delete(getTableName(), "1", null);
	}
	
	public long insertIntoDatabase(SQLiteDatabase db) {
		long res = 0;
		for(T item: getItems()) {
			res += db.insert(getTableName(), null, getContentValues(item));
		}
		return res;
	}
}
