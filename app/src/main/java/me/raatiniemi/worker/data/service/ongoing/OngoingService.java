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

package me.raatiniemi.worker.data.service.ongoing;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import me.raatiniemi.worker.WorkerApplication;
import me.raatiniemi.worker.data.provider.ProviderContract;
import me.raatiniemi.worker.domain.repository.ProjectRepository;
import me.raatiniemi.worker.domain.repository.TimeRepository;
import me.raatiniemi.worker.presentation.model.OngoingNotificationActionEvent;
import me.raatiniemi.worker.presentation.util.OngoingNotificationPreferences;

public abstract class OngoingService extends IntentService {
    @SuppressWarnings("WeakerAccess")
    @Inject
    ProjectRepository projectRepository;

    @SuppressWarnings("WeakerAccess")
    @Inject
    TimeRepository timeRepository;

    @SuppressWarnings("WeakerAccess")
    @Inject
    OngoingNotificationPreferences ongoingNotificationPreferences;

    OngoingService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ((WorkerApplication) getApplication()).getDataComponent()
                .inject(this);
    }

    private static String buildNotificationTag(long projectId) {
        return String.valueOf(projectId);
    }

    long getProjectId(Intent intent) {
        String itemId = ProviderContract.getProjectItemId(intent.getData());
        long projectId = Long.parseLong(itemId);
        if (0 == projectId) {
            throw new IllegalArgumentException("Unable to extract project id from URI");
        }

        return projectId;
    }

    ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    TimeRepository getTimeRepository() {
        return timeRepository;
    }

    void sendNotification(long projectId, Notification notification) {
        NotificationManager manager = getNotificationManager();
        manager.notify(
                buildNotificationTag(projectId),
                WorkerApplication.NOTIFICATION_ON_GOING_ID,
                notification
        );
    }

    void dismissNotification(long projectId) {
        NotificationManager manager = getNotificationManager();
        manager.cancel(
                buildNotificationTag(projectId),
                WorkerApplication.NOTIFICATION_ON_GOING_ID
        );
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    boolean isOngoingNotificationEnabled() {
        return ongoingNotificationPreferences.isOngoingNotificationEnabled();
    }

    boolean isOngoingNotificationChronometerEnabled() {
        return ongoingNotificationPreferences.isOngoingNotificationChronometerEnabled();
    }

    void updateUserInterface(long projectId) {
        getEventBus().post(
                new OngoingNotificationActionEvent(projectId)
        );
    }

    EventBus getEventBus() {
        return EventBus.getDefault();
    }
}
