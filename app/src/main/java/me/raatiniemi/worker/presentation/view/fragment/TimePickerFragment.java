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

package me.raatiniemi.worker.presentation.view.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;

import static me.raatiniemi.util.NullUtil.isNull;

public class TimePickerFragment extends BaseDialogFragment {
    private static final String TAG = "TimePickerFragment";

    /**
     * The "OnTimeSetListener" for the TimePickerDialog.
     */
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    protected boolean isStateValid() {
        if (isNull(onTimeSetListener)) {
            Log.w(TAG, "No OnTimeSetListener have been supplied");
            return false;
        }

        return true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        return new TimePickerDialog(
                getActivity(),
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity())
        );
    }

    /**
     * Set the "OnTimeSetListener" for the TimePickerDialog.
     *
     * @param listener "OnTimeSetListener" for the TimePickerDialog.
     */
    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
        onTimeSetListener = listener;
    }
}
