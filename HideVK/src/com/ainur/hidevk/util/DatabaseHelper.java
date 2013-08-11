package com.ainur.hidevk.util;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ainur.hidevk.HideVkApp;
import com.ainur.hidevk.R;
import com.ainur.hidevk.models.Dialog;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
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
	private static final int DATABASE_VERSION = 2;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.hide_vk_config);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(arg1, Dialog.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.dropTable(arg1, Dialog.class, true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		onCreate(arg0, arg1);
	}

	public Dao<Dialog, Integer> getDialogsDao() {
		try {
			return getDao(Dialog.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static interface ErrorListener {
		public void onError(Exception e);
	}

	public void runTransactionInBg(final Callable<?> callable,
			final ErrorListener errorListener) {
		new Thread() {

			@Override
			public void run() {
				try {
					getDialogsDao().callBatchTasks(callable);
				} catch (Exception e) {
					if (errorListener != null) {
						errorListener.onError(e);
					}
				}
			};
		}.start();
	}

	public List<Dialog> getDialogs() {
		try {
			QueryBuilder<Dialog, Integer> queryBuilder = getDialogsDao()
					.queryBuilder();
			return queryBuilder.orderBy(Dialog.DATE, false).query();
		} catch (Exception e) {
			Log.d(e);
			// TODO
		}
		return null;
	}

	public void clearAll() {
		Dao<Dialog, Integer> audioDao = getDialogsDao();
		try {
			audioDao.deleteBuilder().delete();
			Log.d("Table cleared");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
