package me.raatiniemi.worker.model.domain.project;

import android.content.ContentValues;
import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import me.raatiniemi.worker.BuildConfig;
import me.raatiniemi.worker.provider.WorkerContract.ProjectColumns;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ProjectMapperTest {
    @Test
    public void map_CursorToProject_True() {
        Cursor cursor = mock(Cursor.class);

        when(cursor.getColumnIndex(ProjectColumns._ID)).thenReturn(0);
        when(cursor.getLong(0)).thenReturn(1L);

        when(cursor.getColumnIndex(ProjectColumns.NAME)).thenReturn(1);
        when(cursor.getString(1)).thenReturn("Project name");

        when(cursor.getColumnIndex(ProjectColumns.DESCRIPTION)).thenReturn(2);
        when(cursor.getString(2)).thenReturn("Project description");

        when(cursor.getColumnIndex(ProjectColumns.ARCHIVED)).thenReturn(3);
        when(cursor.getLong(3)).thenReturn(1L);

        Project project = ProjectMapper.map(cursor);

        assertEquals(Long.valueOf(1L), project.getId());
        assertEquals("Project name", project.getName());
        assertEquals("Project description", project.getDescription());
        assertEquals(Long.valueOf(1L), project.getArchived());
    }

    @Test
    public void map_ProjectToContentValues_True() {
        Project project = new Project(1L, "Project name");
        project.setDescription("Project description");
        project.setArchived(1L);

        ContentValues contentValues = ProjectMapper.map(project);

        assertNull(contentValues.get(ProjectColumns._ID));
        assertEquals("Project name", contentValues.getAsString(ProjectColumns.NAME));
        assertEquals("Project description", contentValues.getAsString(ProjectColumns.DESCRIPTION));
        assertEquals(Long.valueOf(1L), contentValues.getAsLong(ProjectColumns.ARCHIVED));
    }
}