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

package me.raatiniemi.worker.domain.model;

public class CalculatedTime {
    private static final int MINUTES_IN_HOUR = 60;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLISECONDS_IN_SECOND = 1000;
    private final long hours;
    private final long minutes;

    public CalculatedTime(long hours, long minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long asMilliseconds() {
        return calculateSeconds() * MILLISECONDS_IN_SECOND;
    }

    private long calculateSeconds() {
        return calculateMinutes() * SECONDS_IN_MINUTE;
    }

    private long calculateMinutes() {
        final long hoursInMinutes = getHours() * MINUTES_IN_HOUR;

        return hoursInMinutes + getMinutes();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CalculatedTime)) {
            return false;
        }

        CalculatedTime calculatedTime = (CalculatedTime) o;
        return calculatedTime.getHours() == getHours()
                && calculatedTime.getMinutes() == getMinutes();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (int) (getHours() ^ (getHours() >>> 32));
        result = 31 * result + (int) (getMinutes() ^ (getMinutes() >>> 32));
        return result;
    }
}
