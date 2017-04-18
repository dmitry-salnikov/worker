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

package me.raatiniemi.worker.domain.interactor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.raatiniemi.worker.domain.model.Time;
import me.raatiniemi.worker.domain.repository.TimesheetRepository;

/**
 * Use case for getting segment from project timesheet.
 */
public class GetTimesheet {
    private final TimesheetRepository repository;

    /**
     * Constructor.
     *
     * @param repository Timesheet repository.
     */
    public GetTimesheet(TimesheetRepository repository) {
        this.repository = repository;
    }

    /**
     * Get segment from project timesheet.
     *
     * @param projectId          Id for project.
     * @param offset             Offset for segment.
     * @param hideRegisteredTime Should registered time be hidden.
     * @return Segment of project timesheet.
     */
    public Map<Date, List<Time>> execute(
            final Long projectId,
            final int offset,
            boolean hideRegisteredTime
    ) {
        if (hideRegisteredTime) {
            return repository.getTimesheetWithoutRegisteredEntries(projectId, offset);
        }

        return repository.getTimesheet(projectId, offset);
    }
}
