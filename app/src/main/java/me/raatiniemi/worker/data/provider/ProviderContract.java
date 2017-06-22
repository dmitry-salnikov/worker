/*
 * Copyright (C) 2017 Worker Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.raatiniemi.worker.data.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ProviderContract {
    public static final String AUTHORITY = "me.raatiniemi.worker";
    private static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private static final String PATH_PROJECTS = "projects";
    private static final String PATH_TIMESHEET = "timesheet";
    private static final String PATH_TIME = "time";

    static final String TABLE_PROJECT = "project";
    static final String TABLE_TIME = "time";

    public static final String COLUMN_PROJECT_NAME = "name";
    static final String COLUMN_PROJECT_DESCRIPTION = "description";
    static final String COLUMN_PROJECT_ARCHIVED = "archived";

    public static final String COLUMN_TIME_PROJECT_ID = "project_id";
    public static final String COLUMN_TIME_START = "start";
    public static final String COLUMN_TIME_STOP = "stop";
    public static final String COLUMN_TIME_REGISTERED = "registered";

    public static final String PROJECT_ORDER_BY = BaseColumns._ID + " ASC";
    public static final String PROJECT_ORDER_BY_TIME = COLUMN_TIME_STOP + " ASC," + COLUMN_TIME_START + " ASC";

    static final String PROJECT_STREAM_TYPE = "vnd.android.cursor.dir/vnd.me.raatiniemi.worker.project";
    static final String PROJECT_ITEM_TYPE = "vnd.android.cursor.item/vnd.me.raatiniemi.worker.project";

    private static final Uri PROJECT_STREAM_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_PROJECTS);

    public static String[] getProjectColumns() {
        return new String[]{
                BaseColumns._ID,
                COLUMN_PROJECT_NAME
        };
    }

    public static Uri getProjectStreamUri() {
        return PROJECT_STREAM_URI;
    }

    public static Uri getProjectItemUri(final long id) {
        return Uri.withAppendedPath(getProjectStreamUri(), String.valueOf(id));
    }

    public static Uri getProjectItemTimeUri(final long id) {
        return Uri.withAppendedPath(getProjectItemUri(id), PATH_TIME);
    }

    public static String getProjectItemId(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    static final String TIME_STREAM_TYPE = "vnd.android.cursor.dir/vnd.me.raatiniemi.worker.time";
    static final String TIME_ITEM_TYPE = "vnd.android.cursor.item/vnd.me.raatiniemi.worker.time";

    private static final Uri TIME_STREAM_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_TIME);

    public static String[] getTimeColumns() {
        return new String[]{
                BaseColumns._ID,
                COLUMN_TIME_PROJECT_ID,
                COLUMN_TIME_START,
                COLUMN_TIME_STOP,
                COLUMN_TIME_REGISTERED
        };
    }

    public static Uri getTimeStreamUri() {
        return TIME_STREAM_URI;
    }

    public static Uri getTimeItemUri(final long id) {
        return Uri.withAppendedPath(getTimeStreamUri(), String.valueOf(id));
    }

    public static String getTimeItemId(Uri uri) {
        return uri.getPathSegments().get(1);
    }

    public static final String TIMESHEET_ORDER_BY = COLUMN_TIME_START + " DESC," + COLUMN_TIME_STOP + " DESC";
    static final String TIMESHEET_GROUP_BY = "strftime('%Y%m%d', " + COLUMN_TIME_START + " / 1000, 'unixepoch')";

    public static String[] getTimesheetStreamColumns() {
        return new String[]{
                "MIN(" + COLUMN_TIME_START + ") AS date",
                "GROUP_CONCAT(" + BaseColumns._ID + ")"
        };
    }

    public static Uri getTimesheetStreamUri(final long id) {
        return Uri.withAppendedPath(getProjectItemUri(id), PATH_TIMESHEET);
    }
}
