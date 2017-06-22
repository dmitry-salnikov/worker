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

package me.raatiniemi.worker.data.repository.mapper;

import android.content.ContentValues;
import android.provider.BaseColumns;

import org.junit.Test;

import me.raatiniemi.worker.RobolectricTestCase;
import me.raatiniemi.worker.data.provider.ProviderContract;
import me.raatiniemi.worker.domain.exception.InvalidProjectNameException;
import me.raatiniemi.worker.domain.model.Project;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class ProjectContentValuesMapperTest extends RobolectricTestCase {
    private static ContentValues createContentValues(final String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProviderContract.COLUMN_PROJECT_NAME, name);

        return contentValues;
    }

    private static Project createProject(
            final String name
    ) throws InvalidProjectNameException {
        return Project.builder(name)
                .build();
    }

    @Test
    public void transform() throws InvalidProjectNameException {
        ProjectContentValuesMapper entityMapper = new ProjectContentValuesMapper();

        ContentValues expected = createContentValues("Name");
        Project project = createProject("Name");
        ContentValues contentValues = entityMapper.transform(project);

        // the id column should not be mapped since that would introduce the
        // possibility of the id being modified.
        assertNull(contentValues.get(BaseColumns._ID));
        assertEquals(expected.get(ProviderContract.COLUMN_PROJECT_NAME), contentValues.get(ProviderContract.COLUMN_PROJECT_NAME));

        expected = createContentValues("Name");
        project = createProject("Name");
        contentValues = entityMapper.transform(project);

        // the id column should not be mapped since that would introduce the
        // possibility of the id being modified.
        assertNull(contentValues.get(BaseColumns._ID));
        assertEquals(expected.get(ProviderContract.COLUMN_PROJECT_NAME), contentValues.get(ProviderContract.COLUMN_PROJECT_NAME));
    }
}
