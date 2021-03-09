package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.ToDocDatabase;
import com.cleanup.todoc.model.Project;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class TaskDaoTest {

    private ToDocDatabase database;
    // DATA SET FOR TEST
    private static long PROJECT_ID = 1L;

    private static Project PROJECT_DEMO = Project.getProjectById(PROJECT_ID);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                ToDocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @Test
    public void getProject() throws InterruptedException{
        Project project = LiveDataTestUtil.getValue(this.database.projectDao().getProjectById(PROJECT_ID));
        Assert.assertTrue(PROJECT_ID == project.getId() && project.getName().equals(PROJECT_DEMO.getName()));
    }

    @After
    public void closeDb ()throws Exception{
        database.close();
    }
}
