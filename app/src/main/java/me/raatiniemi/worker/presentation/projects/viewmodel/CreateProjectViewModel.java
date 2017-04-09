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

package me.raatiniemi.worker.presentation.projects.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.raatiniemi.worker.domain.exception.InvalidProjectNameException;
import me.raatiniemi.worker.domain.exception.ProjectAlreadyExistsException;
import me.raatiniemi.worker.domain.interactor.CreateProject;
import me.raatiniemi.worker.domain.model.Project;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static me.raatiniemi.worker.util.NullUtil.nonNull;

public interface CreateProjectViewModel {
    interface Input {
        void projectName(@NonNull String name);

        void createProject();
    }

    interface Output {
        @NonNull
        Observable<Project> createProjectSuccess();

        @NonNull
        Observable<Boolean> isProjectNameValid();
    }

    interface Error {
        @NonNull
        Observable<String> invalidProjectNameError();

        @NonNull
        Observable<String> duplicateProjectNameError();

        @NonNull
        Observable<String> createProjectError();
    }

    final class ViewModel implements Input, Output, Error {
        public final Input input = this;
        public final Output output = this;
        public final Error error = this;

        private final PublishSubject<String> projectName = PublishSubject.create();
        private final BehaviorSubject<Boolean> isProjectNameValid = BehaviorSubject.create(Boolean.FALSE);
        private final PublishSubject<Void> createProject = PublishSubject.create();
        private final PublishSubject<Project> createProjectSuccess = PublishSubject.create();
        private final PublishSubject<Throwable> createProjectError = PublishSubject.create();

        private final CreateProject useCase;

        public ViewModel(@NonNull CreateProject useCase) {
            this.useCase = useCase;

            projectName.map(this::isNameValid)
                    .subscribe(isProjectNameValid);

            createProject.withLatestFrom(projectName, (event, name) -> name)
                    .switchMap(name -> executeUseCase(name)
                            .compose(redirectErrorsToSubject())
                            .compose(hideErrors()))
                    .subscribe(createProjectSuccess);
        }

        private boolean isNameValid(@Nullable String name) {
            return nonNull(name) && !name.isEmpty();
        }

        @NonNull
        private Observable<Project> executeUseCase(@NonNull String name) {
            try {
                Project project = Project.builder(name)
                        .build();

                return Observable.just(useCase.execute(project));
            } catch (Exception e) {
                return Observable.error(e);
            }
        }

        @NonNull
        private Observable.Transformer<Project, Project> redirectErrorsToSubject() {
            return source -> source
                    .doOnError(createProjectError::onNext)
                    .onErrorResumeNext(Observable.empty());
        }

        @NonNull
        private Observable.Transformer<Project, Project> hideErrors() {
            return source -> source
                    .doOnError(e -> {
                    })
                    .onErrorResumeNext(Observable.empty());
        }

        @Override
        public void projectName(@NonNull String name) {
            projectName.onNext(name);
        }

        @Override
        public void createProject() {
            createProject.onNext(null);
        }

        @NonNull
        @Override
        public Observable<Boolean> isProjectNameValid() {
            return isProjectNameValid;
        }

        @NonNull
        @Override
        public Observable<Project> createProjectSuccess() {
            return createProjectSuccess.asObservable();
        }

        @NonNull
        @Override
        public Observable<String> invalidProjectNameError() {
            return createProjectError
                    .filter(this::isInvalidProjectNameError)
                    .map(Throwable::getMessage);
        }

        private boolean isInvalidProjectNameError(@NonNull Throwable e) {
            return e instanceof InvalidProjectNameException;
        }

        @NonNull
        @Override
        public Observable<String> duplicateProjectNameError() {
            return createProjectError
                    .filter(this::isDuplicateProjectNameError)
                    .map(Throwable::getMessage);
        }

        private boolean isDuplicateProjectNameError(@NonNull Throwable e) {
            return e instanceof ProjectAlreadyExistsException;
        }

        @NonNull
        @Override
        public Observable<String> createProjectError() {
            return createProjectError
                    .filter(this::isUnknownError)
                    .map(Throwable::getMessage);
        }

        private boolean isUnknownError(@NonNull Throwable e) {
            return !isInvalidProjectNameError(e) && !isDuplicateProjectNameError(e);
        }
    }
}
