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

package me.raatiniemi.worker.presentation.projects.presenter;

import android.content.Context;
import android.util.Log;

import me.raatiniemi.worker.domain.exception.InvalidProjectNameException;
import me.raatiniemi.worker.domain.exception.ProjectAlreadyExistsException;
import me.raatiniemi.worker.domain.interactor.CreateProject;
import me.raatiniemi.worker.domain.model.Project;
import me.raatiniemi.worker.presentation.presenter.RxPresenter;
import me.raatiniemi.worker.presentation.projects.view.NewProjectFragment;
import me.raatiniemi.worker.presentation.projects.view.NewProjectView;
import rx.Observable;
import rx.Subscriber;

/**
 * Presenter for the {@link NewProjectFragment}.
 */
public class NewProjectPresenter extends RxPresenter<NewProjectView> {
    private static final String TAG = "NewProjectPresenter";

    /**
     * Use case for creating new projects.
     */
    private final CreateProject createProject;

    /**
     * Constructor.
     *
     * @param context       Context used with the presenter.
     * @param createProject Use case for creating projects.
     */
    public NewProjectPresenter(
            final Context context,
            final CreateProject createProject
    ) {
        super(context);

        this.createProject = createProject;
    }

    /**
     * Create new project.
     *
     * @param name Name of the project.
     */
    public void createNewProject(final String name) {
        try {
            Project project = new Project.Builder(name)
                    .build();

            Observable.just(project)
                    .flatMap(this::createProjectViaUseCase)
                    .compose(applySchedulers())
                    .subscribe(new Subscriber<Project>() {
                        @Override
                        public void onNext(Project project) {
                            Log.d(TAG, "createNewProject onNext");

                            // Check that we still have the view attached. Since we're working
                            // with a dialog, we always expect to have the view attached.
                            if (isViewDetached()) {
                                Log.w(TAG, "View is not attached, failed to push project");
                                return;
                            }

                            // Push the project to the view.
                            getView().createProjectSuccessful(project);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "createNewProject onError");

                            // Log the error even if the view have been detached.
                            Log.w(TAG, "Failed to create project", e);

                            // Check that we still have the view attached. Since we're working
                            // with a dialog, we always expect to have the view attached.
                            if (isViewDetached()) {
                                Log.w(TAG, "View is not attached, failed to push error");
                                return;
                            }

                            // Show the proper error message based on the exception.
                            if (e instanceof ProjectAlreadyExistsException) {
                                getView().showDuplicateNameError();
                                return;
                            }

                            getView().showUnknownError();
                        }

                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "createNewProject onCompleted");
                        }
                    });
        } catch (InvalidProjectNameException e) {
            // Check that we still have the view attached. Since we're working
            // with a dialog, we always expect to have the view attached.
            if (isViewDetached()) {
                Log.w(TAG, "View is not attached, failed to push error", e);
                return;
            }

            getView().showInvalidNameError();
        }
    }

    private Observable<Project> createProjectViaUseCase(Project project) {
        try {
            return Observable.just(createProject.execute(project));
        } catch (Exception e) {
            return Observable.error(e);
        }
    }
}
