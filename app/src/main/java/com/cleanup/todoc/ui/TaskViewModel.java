package com.cleanup.todoc.ui;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.enums.SortMethod;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {
    private final ProjectDataRepository projectDataSource;
    private final TaskDataRepository taskDataSource;
    private final Executor executor;

    @Nullable
    private LiveData<Project> currentProject;

    public TaskViewModel(ProjectDataRepository projectDataSource, TaskDataRepository taskDataSource, Executor executor) {
        this.projectDataSource = projectDataSource;
        this.taskDataSource = taskDataSource;
        this.executor = executor;
    }

    public void init(long projectId) {
        if (this.currentProject != null)
            return;
        currentProject = projectDataSource.getProject(projectId);
    }

    public LiveData<Project> getProject(long projectId) {
        return this.currentProject;
    }


    public void createTask(final Task task) {
        executor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskDataSource.getAllTasks();
    }


    public void deleteTask(long taskId) {
        executor.execute(() -> {
            taskDataSource.deleteTask(taskId);
        });
    }

    public void updateTask(Task task) {
        executor.execute(() -> {
            taskDataSource.updateTask(task);
        });
    }

    public List<Task> updateListTaskSort(List<Task> originalList, SortMethod sortMethod){
        switch (sortMethod) {
            case ALPHABETICAL:
                Collections.sort(originalList, new Task.TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(originalList, new Task.TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(originalList, new Task.TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(originalList, new Task.TaskOldComparator());
                break;

        }
        return originalList;
    }
}
