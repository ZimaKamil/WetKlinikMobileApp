package com.example.asztar.wetklinikmobileapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class PushReminder {
    //Remember to initialize this activityObj first, by calling initActivityObj(this) from
//your activity
    private static final String DEBUG_TAG = "CalendarActivity";
    private static Activity activityObj;

    public static void initActivityObj(Activity obj) {
        activityObj = obj;
    }

    public static void IcsMakeNewCalendarEntry(String title, String description, String location, long startTime, long endTime, int allDay, int hasAlarm, int calendarId, int selectedReminderValue) {

        ContentResolver cr = activityObj.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);

        if (allDay == 1) {
            values.put(CalendarContract.Events.ALL_DAY, true);
        }

        if (hasAlarm == 1) {
            values.put(CalendarContract.Events.HAS_ALARM, true);
        }

        //Get current timezone
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Log.i(DEBUG_TAG, "Timezone retrieved=>" + TimeZone.getDefault().getID());
        if (ContextCompat.checkSelfPermission(activityObj, Manifest.permission.WRITE_CALENDAR) == 0) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            Log.i(DEBUG_TAG, "Uri returned=>" + uri.toString());
            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());

            if (hasAlarm == 1) {
                ContentValues reminders = new ContentValues();
                reminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, selectedReminderValue);

                Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);
            }
        }
    }
}