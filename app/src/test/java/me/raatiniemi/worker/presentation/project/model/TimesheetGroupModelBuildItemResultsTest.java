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

package me.raatiniemi.worker.presentation.project.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import me.raatiniemi.worker.domain.exception.ClockOutBeforeClockInException;
import me.raatiniemi.worker.domain.model.Time;

import static junit.framework.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TimesheetGroupModelBuildItemResultsTest {
    private String message;
    private List<TimeInAdapterResult> expected;
    private int groupIndex;
    private TimesheetGroupModel timesheetGroupModel;

    public TimesheetGroupModelBuildItemResultsTest(
            String message,
            TimeInAdapterResult[] expected,
            int groupIndex,
            TimesheetGroupModel timesheetGroupModel
    ) {
        this.message = message;
        this.expected = Arrays.asList(expected);
        this.groupIndex = groupIndex;
        this.timesheetGroupModel = timesheetGroupModel;
    }

    private static TimesheetGroupModel buildTimesheetGroupWithNumberOfChildItems(
            int numberOfChildItems
    ) throws ClockOutBeforeClockInException {
        TimesheetGroupModel groupModel = new TimesheetGroupModel(new Date());
        if (0 == numberOfChildItems) {
            return groupModel;
        }

        for (int i = 0; i < numberOfChildItems; i++) {
            groupModel.add(new TimesheetChildItem(buildTime()));
        }

        return groupModel;
    }

    private static Time buildTime() throws ClockOutBeforeClockInException {
        return new Time.Builder(1)
                .build();
    }

    @Parameters
    public static Collection<Object[]> getParameters()
            throws ClockOutBeforeClockInException {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Without items",
                                new TimeInAdapterResult[]{
                                },
                                0,
                                buildTimesheetGroupWithNumberOfChildItems(0)
                        },
                        {
                                "With one item",
                                new TimeInAdapterResult[]{
                                        TimeInAdapterResult.build(1, 0, buildTime())
                                },
                                1,
                                buildTimesheetGroupWithNumberOfChildItems(1)
                        },
                        {
                                "With multiple items",
                                new TimeInAdapterResult[]{
                                        TimeInAdapterResult.build(2, 0, buildTime()),
                                        TimeInAdapterResult.build(2, 1, buildTime()),
                                        TimeInAdapterResult.build(2, 2, buildTime()),
                                        TimeInAdapterResult.build(2, 3, buildTime()),
                                        TimeInAdapterResult.build(2, 4, buildTime()),
                                        TimeInAdapterResult.build(2, 5, buildTime()),
                                },
                                2,
                                buildTimesheetGroupWithNumberOfChildItems(6)
                        }
                }
        );
    }

    @Test
    public void buildItemResultsWithGroupIndex() {
        List<TimeInAdapterResult> actual =
                timesheetGroupModel.buildItemResultsWithGroupIndex(groupIndex);

        assertEquals(message, expected, actual);
    }
}
