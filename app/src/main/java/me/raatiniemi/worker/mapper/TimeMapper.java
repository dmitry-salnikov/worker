package me.raatiniemi.worker.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.raatiniemi.worker.domain.Project;
import me.raatiniemi.worker.domain.Time;
import me.raatiniemi.worker.exception.DomainException;
import me.raatiniemi.worker.provider.ExpandableDataProvider.*;
import me.raatiniemi.worker.provider.TimesheetExpandableDataProvider.*;
import me.raatiniemi.worker.provider.WorkerContract.*;
import me.raatiniemi.worker.provider.WorkerDatabase.*;

public class TimeMapper extends AbstractMapper<Time>
{
    public TimeMapper()
    {
        super();
    }

    protected String getTable()
    {
        return Tables.TIME;
    }

    protected String[] getColumns()
    {
        return new String[]{
            TimeColumns.ID,
            TimeColumns.PROJECT_ID,
            TimeColumns.START,
            TimeColumns.STOP
        };
    }

    protected Time load(Cursor row)
    {
        long id = row.getLong(row.getColumnIndex(TimeColumns.ID));
        long projectId = row.getLong(row.getColumnIndex(TimeColumns.PROJECT_ID));
        long start = row.getLong(row.getColumnIndex(TimeColumns.START));
        long stop = row.getLong(row.getColumnIndex(TimeColumns.STOP));

        try {
            return new Time(id, projectId, start, stop);
        } catch (DomainException e) {
            // TODO: Handle DomainException properly.
            return null;
        }
    }

    public ArrayList<Time> findTimeByProject(Project project)
    {
        ArrayList<Time> result = new ArrayList<>();

        // Check that the project actually exists, i.e. it has an value for id.
        if (project != null && project.getId() != null) {
            String selection = TimeColumns.PROJECT_ID + "=" + project.getId();
            String orderBy = TimeColumns.STOP + " DESC," + TimeColumns.START + " ASC";

            Cursor rows = mDatabase.query(getTable(), getColumns(), selection, null, null, null, orderBy);
            if (rows.moveToFirst()) {
                do {
                    Time time = load(rows);
                    if (time != null) {
                        result.add(time);
                    }
                } while (rows.moveToNext());
            }
        }

        return result;
    }

    public List<Groupable> findTime(Project project)
    {
        List<Groupable> result = new ArrayList<>();

        String[] columns = new String[] {
            "MIN(start) AS date",
            "GROUP_CONCAT(" + TimeColumns.ID + ")"
        };

        String selection = TimeColumns.PROJECT_ID + "=" + project.getId();
        String[] selectionArgs;

        String groupBy = "strftime('%Y%m%d', start / 1000, 'unixepoch')";
        String orderBy = TimeColumns.START + " DESC," + TimeColumns.STOP + " DESC";

        Cursor interval = mDatabase.query(getTable(), columns, selection, null, groupBy, null, orderBy);
        if (interval.moveToFirst()) {
            selection = TimeColumns.ID + "= ?";

            do {
                TimeGroup group = new TimeGroup(
                    interval.getPosition(),
                    new Date(interval.getLong(0))
                );

                List<Child> children = new ArrayList<>();

                String grouped = interval.getString(1);
                String[] rows = grouped.split(",");
                for (String id : rows) {
                    selectionArgs = new String[] { id };

                    Cursor row = mDatabase.query(getTable(), getColumns(), selection, selectionArgs, null, null, null);
                    if (row.moveToFirst()) {
                        do {
                            Time time = load(row);
                            if (null != time) {
                                children.add(
                                    new TimeChild(row.getPosition(), time)
                                );
                            }
                        } while (row.moveToNext());
                    }
                    row.close();
                }
                Groupable groupable = new Groupable(group, children);
                result.add(groupable);
            } while (interval.moveToNext());
        }
        interval.close();

        return result;
    }

    public Time insert(Time time)
    {
        // TODO: Check if timer is already active for project, throw exception.

        ContentValues values = new ContentValues();
        values.put(TimeColumns.PROJECT_ID, time.getProjectId());
        values.put(TimeColumns.START, time.getStart());
        values.put(TimeColumns.STOP, time.getStop());

        long id = mDatabase.insert(getTable(), null, values);
        return find(id);
    }

    public Time update(Time time)
    {
        ContentValues values = new ContentValues();
        values.put(TimeColumns.START, time.getStart());
        values.put(TimeColumns.STOP, time.getStop());

        String where = TimeColumns.ID + "=" + time.getId();

        mDatabase.update(getTable(), values, where, null);
        return find(time.getId());
    }

    public boolean remove(Time time)
    {
        String where = TimeColumns.ID + "=" + time.getId();

        return 0 < mDatabase.delete(getTable(), where, null);
    }
}
