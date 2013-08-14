package com.ainur.hidevk.util;

import java.io.IOException;
import java.sql.SQLException;

import com.ainur.hidevk.models.Dialog;
import com.ainur.hidevk.models.User;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	private static final Class<?>[] CLASSES = { Dialog.class,User.class };

	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("hide_vk_config", CLASSES);
	}
}
