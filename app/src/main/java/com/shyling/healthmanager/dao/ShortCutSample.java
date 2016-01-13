package com.shyling.healthmanager.dao;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;


public class ShortCutSample {
	public void creatShortCut(Activity activity, String shortCutName,
			int resourceId) {
		Intent intent = new Intent(activity, activity.getClass());
		/**
		 * 卸载应用并删除图标
		 */
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		Intent shortcutintent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复
		shortcutintent.putExtra("duplicate", false);
		// 设置标题
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
		// 设置图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				activity.getApplicationContext(), resourceId);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 点击快捷方式，运行入口
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		activity.sendBroadcast(shortcutintent);

	}

	/**
	 * 删除
	 *
	 * @param activity
	 * @param shortCutName
	 */
	public void deleteShortCut(Activity activity, String shortCutName) {
		Intent shortCut = new Intent(
				"com.android.launcher.permission.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		shortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortCutName);
		Intent intent = new Intent(activity, activity.getClass());
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		shortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		activity.sendBroadcast(shortCut);
	}

	/**
	 * 判断是否存在
	 *
	 * @param activity
	 * @param shortCutName
	 * @return
	 */
	public boolean hasShortCut(Activity activity, String shortCutName) {
		String url = "";
		int systemversion = Integer.parseInt(
				android.os.Build.VERSION.SDK);
		if (systemversion < 8) {
			url = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
				new String[] { shortCutName }, null);
		if (cursor != null && cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}
}
