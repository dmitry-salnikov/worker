package me.raatiniemi.worker.model.domain.time;

import android.content.ContentValues;
import android.database.Cursor;

import me.raatiniemi.worker.exception.DomainException;
import me.raatiniemi.worker.provider.WorkerContract.TimeColumns;

public class TimeMapper {
    /**
     * Private constructor, instantiation is not allowed.
     */
    private TimeMapper() {
    }

    /**
     * Map Time from cursor.
     *
     * @param cursor Cursor with data to map to Time.
     * @return Time with data from cursor.
     */
    public static Time map(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(TimeColumns._ID));
        long projectId = cursor.getLong(cursor.getColumnIndex(TimeColumns.PROJECT_ID));
        long start = cursor.getLong(cursor.getColumnIndex(TimeColumns.START));
        long stop = cursor.getLong(cursor.getColumnIndex(TimeColumns.STOP));

        try {
            return new Time(id, projectId, start, stop);
        } catch (DomainException e) {
            // TODO: Handle DomainException properly.
            return null;
        }
    }

    /**
     * Map Time to ContentValues.
     *
     * @param time Time to map to ContentValues.
     * @return Mapped ContentValues.
     */
    public static ContentValues map(Time time) {
        ContentValues values = new ContentValues();
        values.put(TimeColumns.START, time.getStart());
        values.put(TimeColumns.STOP, time.getStop());
        values.put(TimeColumns.PROJECT_ID, time.getProjectId());

        return values;
    }
}