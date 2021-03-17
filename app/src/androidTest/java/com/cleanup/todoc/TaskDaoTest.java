package com.cleanup.todoc;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.ToDocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)

public class TaskDaoTest {

    private ToDocDatabase database;
    private static final long PROJECT_ID = 1L;
    private static final Task TASK_DEMO = new Task(1, PROJECT_ID, "hello world", new Date().getTime(), false);
    private static final Task TASK_DEMO_DELETE = new Task(3, PROJECT_ID, "hello world", new Date().getTime(), false);
    private static final Task TASK_DEMO_UPDATE = new Task(5, PROJECT_ID, "hello world", new Date().getTime(), false);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                ToDocDatabase.class)
                .allowMainThreadQueries()
                .addCallback(prepopulateDatabase())
                .build();
    }

    private static RoomDatabase.Callback prepopulateDatabase(){
        return new RoomDatabase.Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1L);
                contentValues.put("name", "Projet Philippe");
                contentValues.put("color", "0xFFEADAD1");

                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("id", 2L);
                contentValues2.put("name", "Projet Lucidia");
                contentValues2.put("color", "0xFFB4CDBA");

                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("id", 3L);
                contentValues3.put("name", "Projet Circus");
                contentValues3.put("color", "0xFFA3CED2");

                db.insert("Project", OnConflictStrategy.IGNORE, contentValues);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues2);
                db.insert("Project", OnConflictStrategy.IGNORE, contentValues3);
            }
        };
    }

    @Test
    public void addAndGetTask() throws InterruptedException{
        this.database.taskDao().insertTask(TASK_DEMO);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        Task task = tasks.get(0);
        Assert.assertTrue(PROJECT_ID == task.getProject().getId() && task.getName().equals(TASK_DEMO.getName()));
    }

    @Test
    public void deleteTaskWithSuccess() throws InterruptedException{
        this.database.taskDao().insertTask(TASK_DEMO_DELETE);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        Task task = tasks.get(0);
        Assert.assertTrue(PROJECT_ID == task.getProject().getId() && task.getName().equals(TASK_DEMO_DELETE.getName()));
        this.database.taskDao().deleteTask(TASK_DEMO_DELETE.getId());
        tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        Assert.assertFalse(tasks.contains(TASK_DEMO_DELETE));
    }

    @Test
    public void updateTaskWithSuccess() throws InterruptedException {
        this.database.taskDao().insertTask(TASK_DEMO_UPDATE);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        Task task = tasks.get(0);
        task.setDone(true);
        this.database.taskDao().updateTask(task);
        tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        Assert.assertTrue(tasks.get(0).isDone());
    }

    @After
    public void closeDb ()throws Exception{
        database.close();
    }
}
