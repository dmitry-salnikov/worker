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

    final class Tables {
        static final String PROJECT = "project";
        static final String TIME = "time";

        private Tables() {
        }
    }

    public final class ProjectColumns {
        public static final String NAME = "name";
        static final String DESCRIPTION = "description";
        static final String ARCHIVED = "archived";

        private ProjectColumns() {
        }
    }

    public final class TimeColumns {
        public static final String PROJECT_ID = "project_id";
        public static final String START = "start";
        public static final String STOP = "stop";
        public static final String REGISTERED = "registered";

        private TimeColumns() {
        }
    }

    public final static class Project {
        public static final String ORDER_BY_TIME = TimeColumns.STOP + " ASC," + TimeColumns.START + " ASC";
        public static final String ORDER_BY = BaseColumns._ID + " ASC";

        static final String STREAM_TYPE = "vnd.android.cursor.dir/vnd.me.raatiniemi.worker.project";
        static final String ITEM_TYPE = "vnd.android.cursor.item/vnd.me.raatiniemi.worker.project";

        private static final Uri STREAM_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_PROJECTS);

        private Project() {
        }

        public static String[] getColumns() {
            return new String[]{
                    BaseColumns._ID,
                    ProjectColumns.NAME
            };
        }

        public static Uri getStreamUri() {
            return STREAM_URI;
        }

        public static Uri getItemUri(final long id) {
            return Uri.withAppendedPath(getStreamUri(), String.valueOf(id));
        }

        public static Uri getItemTimeUri(final long id) {
            return Uri.withAppendedPath(getItemUri(id), PATH_TIME);
        }

        public static String getItemId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public final static class Time {
        private static final Uri STREAM_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_TIME);

        static final String STREAM_TYPE = "vnd.android.cursor.dir/vnd.me.raatiniemi.worker.time";
        static final String ITEM_TYPE = "vnd.android.cursor.item/vnd.me.raatiniemi.worker.time";

        private Time() {
        }

        public static String[] getColumns() {
            return new String[]{
                    BaseColumns._ID,
                    TimeColumns.PROJECT_ID,
                    TimeColumns.START,
                    TimeColumns.STOP,
                    TimeColumns.REGISTERED
            };
        }

        public static Uri getStreamUri() {
            return STREAM_URI;
        }

        public static Uri getItemUri(final long id) {
            return Uri.withAppendedPath(getStreamUri(), String.valueOf(id));
        }

        public static String getItemId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public final static class Timesheet {
        public static final String ORDER_BY = TimeColumns.START + " DESC," + TimeColumns.STOP + " DESC";

        static final String GROUP_BY = "strftime('%Y%m%d', " + TimeColumns.START + " / 1000, 'unixepoch')";

        public static String[] getTimesheetColumns() {
            return new String[]{
                    "MIN(" + TimeColumns.START + ") AS date",
                    "GROUP_CONCAT(" + BaseColumns._ID + ")"
            };
        }

        public static Uri getItemTimesheetUri(final long id) {
            return Uri.withAppendedPath(Project.getItemUri(id), PATH_TIMESHEET);
        }
    }
}
