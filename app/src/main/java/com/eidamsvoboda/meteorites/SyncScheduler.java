package com.eidamsvoboda.meteorites;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

/**
 * Created by eidamsvoboda on 09/06/2017.
 */

public class SyncScheduler {
	public static void scheduleSync(Context context) {  // Non waking alarm set on specific hour
		AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(DataManager.getSyncFrequency()), TimeUnit.SECONDS.toMillis(DataManager.getSyncFrequency()), getSyncIntent(context)); // TODO CHANGE TO HOURS AND NON WAKEUP AND INEXACT
	}

	public static PendingIntent getSyncIntent(Context context) {
		Intent intent = new Intent(context, SyncBroadcastReceiver.class);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}

	public static void cancelSchedule(Context context) {
		AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
		alarmManager.cancel(getSyncIntent(context));
	}
}
