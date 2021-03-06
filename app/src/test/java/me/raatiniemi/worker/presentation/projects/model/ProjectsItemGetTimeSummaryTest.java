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

package me.raatiniemi.worker.presentation.projects.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import me.raatiniemi.worker.domain.exception.ClockOutBeforeClockInException;
import me.raatiniemi.worker.domain.exception.InvalidProjectNameException;
import me.raatiniemi.worker.domain.model.Project;
import me.raatiniemi.worker.domain.model.Time;
import me.raatiniemi.worker.factory.TimeFactory;

import static junit.framework.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ProjectsItemGetTimeSummaryTest {
    private final String expected;
    private final Time[] registeredTime;

    public ProjectsItemGetTimeSummaryTest(String expected, Time... registeredTime) {
        this.expected = expected;
        this.registeredTime = registeredTime;
    }

    @Parameters
    public static Collection<Object[]> getParameters()
            throws ClockOutBeforeClockInException {

        return Arrays.asList(
                new Object[][]{
                        {
                                "1h 0m",
                                new Time[]{
                                        TimeFactory.builder()
                                                .stopInMilliseconds(3600000)
                                                .build()
                                }
                        },
                        {
                                "2h 30m",
                                new Time[]{
                                        TimeFactory.builder()
                                                .stopInMilliseconds(9000000)
                                                .build()
                                }
                        },
                        {
                                "3h 30m",
                                new Time[]{
                                        TimeFactory.builder()
                                                .stopInMilliseconds(3600000)
                                                .build(),
                                        TimeFactory.builder()
                                                .stopInMilliseconds(9000000)
                                                .build()
                                }
                        }
                }
        );
    }

    @Test
    public void getTimeSummary() throws InvalidProjectNameException {
        Project project = Project.builder("Project name")
                .build();
        project.addTime(Arrays.asList(registeredTime));
        ProjectsItem projectsItem = new ProjectsItem(project);

        assertEquals(expected, projectsItem.getTimeSummary());
    }
}
