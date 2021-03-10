package com.cleanup.todoc.database;


import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

@Database(entities = {Project.class, Task.class}, version = 1)
public abstract class ToDocDatabase extends RoomDatabase {

    private static volatile ToDocDatabase INSTANCE;

    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    public static ToDocDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (ToDocDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDocDatabase.class, "ToDocDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(){
        return new Callback() {

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
}
