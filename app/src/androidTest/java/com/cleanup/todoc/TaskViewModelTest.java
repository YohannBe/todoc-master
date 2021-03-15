package com.cleanup.todoc;


import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cleanup.todoc.injection.Injection;
import com.cleanup.todoc.injection.ViewModelFactory;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(JUnit4.class)

public class TaskViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    private TaskViewModel viewModel;
    private TaskDataRepository taskDataRepository;
    private ProjectDataRepository projectDataRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Context context;


    @Before
    public void init(){
        taskDataRepository = mock(TaskDataRepository.class);
        projectDataRepository = mock(ProjectDataRepository.class);
        context = mock(Context.class);
        this.viewModel = new TaskViewModel(projectDataRepository, taskDataRepository, executor);
    }

    @Test
    public void empty() {
        Observer <List<Task>> result = mock(Observer.class);
        viewModel.getAllTasks().observeForever(result);
    }
}
