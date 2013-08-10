package com.ainur.hidevk.util;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore.Audio;

import com.ainur.hidevk.HideVkApp;
import com.ainur.hidevk.models.Dialogs;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static class Holder {
		private static final DatabaseHelper INSTANCE = new DatabaseHelper(
				HideVkApp.getContext());
	}

	public static DatabaseHelper getInstance() {
		return Holder.INSTANCE;
	}

	public static final String DATABASE_NAME = "hide_vk.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				new File("a"));
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(arg1, Dialogs.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.dropTable(arg1, Dialogs.class, true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		onCreate(arg0, arg1);
	}

	public Dao<Audio, Integer> getMessagesDao() {
		try {
			return getDao(Dialogs.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
