package com.cleanup.todoc;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.enums.SortMethod;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.cleanup.todoc.ui.TaskViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class TaskViewModelTest {

    private TaskViewModel viewModel;
    private TaskDataRepository taskDataRepository;
    private ProjectDataRepository projectDataRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    private MutableLiveData<List<Task>> taskLiveData = new MutableLiveData<>();

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    private Observer<List<Task>> observer;

    List<Task> tasks = Arrays.asList(
            new Task(0, 1, "zzz", 123, true),
            new Task(1, 1, "aaa", 124, true),
            new  Task(2, 1, "bbb", 125, true)
    );

    @Before
    public void init(){
        taskDataRepository = mock(TaskDataRepository.class);
        projectDataRepository = mock(ProjectDataRepository.class);
        taskLiveData.postValue(tasks);
        when(taskDataRepository.getAllTasks()).thenReturn(taskLiveData);

        this.viewModel = new TaskViewModel(projectDataRepository, taskDataRepository, executor);
        observer = mock(Observer.class);
    }

    @Test
    public void verifyLiveDataValueChangeOnEvent(){
        viewModel.getAllTasks().observeForever(observer);
        verify(observer).onChanged(taskLiveData.getValue());
    }

    @Test
    public void VerifySortAz(){
        List<Task> sortedList = viewModel.updateListTaskSort(tasks, SortMethod.ALPHABETICAL);
        assertEquals(sortedList.get(0).getName(), "aaa");
        assertEquals(sortedList.get(1).getName(), "bbb");
        assertEquals(sortedList.get(2).getName(), "zzz");
    }

    @Test
    public void test_za_comparator() {
        List<Task> sortedList = viewModel.updateListTaskSort(tasks, SortMethod.ALPHABETICAL_INVERTED);
        assertEquals(sortedList.get(0).getName(), "zzz");
        assertEquals(sortedList.get(1).getName(), "bbb");
        assertEquals(sortedList.get(2).getName(), "aaa");
    }

    @Test
    public void test_recent_comparator() {
        List<Task> sortedList = viewModel.updateListTaskSort(tasks, SortMethod.OLD_FIRST);
        assertEquals(sortedList.get(0).getCreationTimestamp(), 123);
        assertEquals(sortedList.get(1).getCreationTimestamp(), 124);
        assertEquals(sortedList.get(2).getCreationTimestamp(), 125);
    }

    @Test
    public void test_old_comparator() {
        List<Task> sortedList = viewModel.updateListTaskSort(tasks, SortMethod.RECENT_FIRST);
        assertEquals(sortedList.get(0).getCreationTimestamp(), 125);
        assertEquals(sortedList.get(1).getCreationTimestamp(), 124);
        assertEquals(sortedList.get(2).getCreationTimestamp(), 123);
    }

}
