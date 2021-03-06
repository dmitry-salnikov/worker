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

package me.raatiniemi.worker.presentation.project.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import me.raatiniemi.worker.domain.model.Time;
import me.raatiniemi.worker.domain.model.TimesheetItem;
import me.raatiniemi.worker.factory.TimeFactory;

import static junit.framework.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TimesheetGroupIsRegisteredTest {
    private static final TimesheetItem NOT_REGISTERED_TIME;
    private final static TimesheetItem REGISTERED_TIME;

    static {
        Time notRegistered = TimeFactory.builder().build();
        NOT_REGISTERED_TIME = TimesheetItem.with(notRegistered);

        Time registered = TimeFactory.builder().register().build();
        REGISTERED_TIME = TimesheetItem.with(registered);
    }

    private final boolean expected;
    private final TimesheetGroup item;

    public TimesheetGroupIsRegisteredTest(
            boolean expected,
            TimesheetItem... timesheetItems
    ) {
        this.expected = expected;

        TreeSet<TimesheetItem> items = new TreeSet<>(Arrays.asList(timesheetItems));
        item = TimesheetGroup.build(new Date(), items);
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(
                new Object[][]{
                        {
                                Boolean.TRUE,
                                new TimesheetItem[]{
                                        REGISTERED_TIME
                                }
                        },
                        {
                                Boolean.FALSE,
                                new TimesheetItem[]{
                                        NOT_REGISTERED_TIME
                                }
                        },
                        {
                                Boolean.FALSE,
                                new TimesheetItem[]{
                                        NOT_REGISTERED_TIME,
                                        REGISTERED_TIME
                                }
                        },
                        {
                                Boolean.TRUE,
                                new TimesheetItem[]{
                                        REGISTERED_TIME,
                                        REGISTERED_TIME
                                }
                        }
                }
        );
    }

    @Test
    public void isRegistered() {
        assertEquals(expected, item.isRegistered());
    }
}
