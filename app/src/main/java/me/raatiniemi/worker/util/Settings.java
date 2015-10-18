/*
 * Copyright (C) 2015 Worker Project
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

package me.raatiniemi.worker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Communicate with the shared preferences.
 */
public class Settings {
    /**
     * Preference key for hiding registered time.
     */
    private static final String PREF_HIDE_REGISTERED_TIME = "pref_hide_registered_time";

    /**
     * Check if the registered time should be hidden.
     *
     * @param context Context to be used to lookup the {@link android.content.SharedPreferences}.
     * @return 'true' if registered time should be hidden, otherwise 'false'.
     */
    public static boolean shouldHideRegisteredTime(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(PREF_HIDE_REGISTERED_TIME, false);
    }

    /**
     * Set the prefererence indicating whether the registered time should be hidden.
     *
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     * @param newValue 'true' if registered should be hidden, otherwise 'false'.
     */
    public static void setHideRegisteredTime(final Context context, boolean newValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_HIDE_REGISTERED_TIME, newValue).apply();
    }
}
