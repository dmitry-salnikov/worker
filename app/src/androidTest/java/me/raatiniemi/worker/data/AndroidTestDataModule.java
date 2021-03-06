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

package me.raatiniemi.worker.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.dagger.Module;

import me.raatiniemi.worker.data.provider.WorkerDatabase;

@Module
public class AndroidTestDataModule extends DataModule {
    public AndroidTestDataModule(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    WorkerDatabase providesWorkerDatabase() {
        return WorkerDatabase.inMemory(context);
    }
}
