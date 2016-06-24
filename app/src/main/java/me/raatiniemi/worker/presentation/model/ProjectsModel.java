/*
 * Copyright (C) 2016 Worker Project
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

package me.raatiniemi.worker.presentation.model;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import me.raatiniemi.worker.R;
import me.raatiniemi.worker.domain.model.Project;
import me.raatiniemi.worker.presentation.util.DateIntervalFormat;
import me.raatiniemi.worker.presentation.util.HoursMinutesIntervalFormat;

public class ProjectsModel {
    private static final DateIntervalFormat sIntervalFormat;

    static {
        sIntervalFormat = new HoursMinutesIntervalFormat();
    }

    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final Project mProject;

    public ProjectsModel(Project project) {
        mProject = project;
    }

    public Project asProject() {
        return mProject;
    }

    public String getTitle() {
        return mProject.getName();
    }

    private static void showTextView(TextView descriptionView) {
        descriptionView.setVisibility(View.VISIBLE);
    }

    private static void hideTextView(TextView descriptionView) {
        descriptionView.setVisibility(View.GONE);
    }

    public String getTimeSummary() {
        return sIntervalFormat.format(mProject.summarizeTime());
    }

    public String getHelpTextForClockActivityToggle(Resources resources) {
        if (isActive()) {
            return resources.getString(R.string.fragment_projects_item_clock_out);
        }

        return resources.getString(R.string.fragment_projects_item_clock_in);
    }

    public String getHelpTextForClockActivityAt(Resources resources) {
        if (isActive()) {
            return resources.getString(R.string.fragment_projects_item_clock_out_at);
        }

        return resources.getString(R.string.fragment_projects_item_clock_in_at);
    }

    public String getClockedInSince(Resources resources) {
        if (!isActive()) {
            return null;
        }

        return String.format(
                getClockedInSinceFormatTemplate(resources),
                getFormattedClockedInSince(),
                getFormattedElapsedTime()
        );
    }

    private boolean isActive() {
        return mProject.isActive();
    }

    private static String getClockedInSinceFormatTemplate(Resources resources) {
        return resources.getString(R.string.fragment_projects_item_clocked_in_since);
    }

    private String getFormattedClockedInSince() {
        // TODO: Handle if the time session overlap days.
        // The timestamp should include the date it was
        // checked in, e.g. 21 May 1:06PM.
        return mTimeFormat.format(mProject.getClockedInSince());
    }

    private String getFormattedElapsedTime() {
        return sIntervalFormat.format(mProject.getElapsed());
    }

    public void setVisibilityForClockedInSinceView(TextView clockedInSinceView) {
        if (isActive()) {
            showTextView(clockedInSinceView);
            return;
        }

        hideTextView(clockedInSinceView);
    }
}
