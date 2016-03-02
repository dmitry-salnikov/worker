/*
 * Copyright (C) 2015-2016 Worker Project
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

import me.raatiniemi.worker.domain.model.CalculatedTime;
import me.raatiniemi.worker.domain.util.CalculateTime;

/**
 * Format time interval.
 */
public class DateIntervalFormat {
    /**
     * Private constructor, preventing instance creation.
     */
    private DateIntervalFormat() {
    }

    private static String fractionHours(CalculatedTime calculatedTime) {
        float fractal = (float) calculatedTime.getMinutes() / (float) 60.0;

        double interval = calculatedTime.getHours() + fractal;
        return String.format("%.2f", interval);
    }

    private static String hoursMinutes(CalculatedTime calculatedTime) {
        String format = "%1$dh %2$dm";

        // If no hours is available, remove it from the format.
        if (0 == calculatedTime.getHours()) {
            format = "%2$dm";
        }

        return String.format(format, calculatedTime.getHours(), calculatedTime.getMinutes());
    }

    public static String format(long milliseconds, Type type) {
        CalculatedTime calculatedTime = CalculateTime.calculateTime(milliseconds);

        // Determined what kind of format type to use.
        switch (type) {
            case FRACTION_HOURS:
                return fractionHours(calculatedTime);
            case HOURS_MINUTES:
            default:
                return hoursMinutes(calculatedTime);
        }
    }

    public static String format(long milliseconds) {
        return format(milliseconds, Type.HOURS_MINUTES);
    }

    /**
     * Available format types.
     */
    public enum Type {
        /**
         * Format the interval with fraction hours, e.g. "8.25" for 8 hours and 15 minutes.
         */
        FRACTION_HOURS,

        /**
         * Format the interval with hours and minutes, e.g. "5h 12m".
         */
        HOURS_MINUTES
    }
}
