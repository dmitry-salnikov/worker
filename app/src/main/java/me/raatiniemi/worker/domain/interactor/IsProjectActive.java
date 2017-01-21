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

import me.raatiniemi.worker.domain.exception.DomainException;
import me.raatiniemi.worker.domain.model.Time;
import me.raatiniemi.worker.domain.repository.TimeRepository;

import static me.raatiniemi.util.NullUtil.nonNull;

public class IsProjectActive {
    private TimeRepository timeRepository;

    public IsProjectActive(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public boolean execute(long projectId) throws DomainException {
        Time time = timeRepository.getActiveTimeForProject(projectId);

        return nonNull(time);
    }
}
