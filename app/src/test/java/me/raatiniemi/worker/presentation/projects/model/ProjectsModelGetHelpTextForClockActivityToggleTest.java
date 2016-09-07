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

package me.raatiniemi.worker.presentation.projects.model;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.test.mock.MockResources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import me.raatiniemi.worker.R;
import me.raatiniemi.worker.domain.model.Project;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ProjectsModelGetHelpTextForClockActivityToggleTest {
    private static final Resources resources = new MockResources() {
        @NonNull
        @Override
        public String getString(int id) throws NotFoundException {
            switch (id) {
                case R.string.fragment_projects_item_clock_in:
                    return "Clock in now";
                case R.string.fragment_projects_item_clock_out:
                    return "Clock out now";
            }

            return super.getString(id);
        }
    };

    private String expected;
    private Project project;

    public ProjectsModelGetHelpTextForClockActivityToggleTest(
            String expected,
            Project project
    ) {
        this.expected = expected;
        this.project = project;
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(
                new Object[][]{
                        {
                                "Clock in now",
                                mockProjectWithActiveIndicator(Boolean.FALSE)
                        },
                        {
                                "Clock out now",
                                mockProjectWithActiveIndicator(Boolean.TRUE)
                        }
                }
        );
    }

    private static Project mockProjectWithActiveIndicator(boolean isProjectActive) {
        Project project = mock(Project.class);
        when(project.isActive()).thenReturn(isProjectActive);

        return project;
    }

    @Test
    public void getHelpTextForClockActivityToggle() {
        ProjectsModel model = new ProjectsModel(project);

        assertEquals(expected, model.getHelpTextForClockActivityToggle(resources));
    }
}
