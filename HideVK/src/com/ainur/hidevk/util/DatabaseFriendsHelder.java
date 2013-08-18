package com.ainur.hidevk.util;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ainur.hidevk.HideVkApp;
import com.ainur.hidevk.R;
import com.ainur.hidevk.models.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseFriendsHelder extends OrmLiteSqliteOpenHelper {

	private static class Holder {
		private static final DatabaseFriendsHelder INSTANCE = new DatabaseFriendsHelder(
				HideVkApp.getContext());
	}

	public static DatabaseFriendsHelder getInstance() {
		return Holder.INSTANCE;
	}

	public static final String DATABASE_NAME = "hide_vk_friends.db";
	private static final int DATABASE_VERSION = 8;

	public DatabaseFriendsHelder(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.hide_vk_config);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(arg1, User.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.dropTable(arg1, User.class, true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		onCreate(arg0, arg1);
	}

	public Dao<User, Integer> getFriendsDao() {
		try {
			return getDao(User.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static interface ErrorListener {
		public void onError(Exception e);
	}

	public void runTransactionInBg(final Callable<?> callable,
			final DatabaseDialogHelper.ErrorListener errorListener) {
		new Thread() {

			@Override
			public void run() {
				try {
					getFriendsDao().callBatchTasks(callable);
				} catch (Exception e) {
					if (errorListener != null) {
						errorListener.onError(e);
					}
				}
			};
		}.start();
	}

	

	public void clearAll() {
		try {
			Dao<User, Integer> friendsDao = getFriendsDao();
			friendsDao.deleteBuilder().delete();
			Log.d("Table cleared");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String getUserName(int uid){
		try {
			Dao<User, Integer> friendsDao = getFriendsDao();
			QueryBuilder<User, Integer> queryBuilder = friendsDao.queryBuilder();
			queryBuilder.where().eq(User.USER_ID, uid);
			User finded = queryBuilder.queryForFirst();
			String name = finded.firstName+" "+finded.lastName;
			return name;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.d("error userName getting: "+uid);
		}
		return null;
	}
	
	public boolean isEmptyDB(){
		long c=0;
		try {
			Dao<User, Integer> friendsDao = getFriendsDao();
			QueryBuilder<User, Integer> queryBuilder = friendsDao.queryBuilder();
			queryBuilder.setCountOf(true);
			c = queryBuilder.countOf();
			Log.d("Count of friends: "+c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c==0;
	}
	
	public String getFriendImageURL(int uid){
		try {
			Dao<User, Integer> friendsDao = getFriendsDao();
			QueryBuilder<User, Integer> queryBuilder = friendsDao.queryBuilder();
			queryBuilder.where().eq(User.USER_ID, uid);
		 List<User> query = queryBuilder.query();
		 for(User u: query){
			 Log.d("Finded user: "+u.toString());
		 }
			return query.get(0).photoUrl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.d("error friend image URL GETTING:"+uid);
		}
		return null;
	}
	
	public boolean addFriend(User user){
		try {
			Dao<User, Integer> friendsDao = getFriendsDao();
			friendsDao.create(user);
			Log.d("New user created");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("error friend adding: "+ user.toString()+"; "+e.getMessage());
		}
		return false;
	}
}
