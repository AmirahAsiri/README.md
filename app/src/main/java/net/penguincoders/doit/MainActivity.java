package net.penguincoders.doit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.doit.Adapters.ToDoAdapter;
import net.penguincoders.doit.Model.ToDoModel;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private DatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initializeDatabaseAndViews();
        setUpRecyclerViewAndAdapter();
        setUpItemTouchHelper();

        fab.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));
    }

    private void initializeDatabaseAndViews() {
        db = new DatabaseHandler(this);
        db.openDatabase();
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        fab = findViewById(R.id.fab);
    }

    private void setUpRecyclerViewAndAdapter() {
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter = new ToDoAdapter(db, MainActivity.this, taskList);
        tasksRecyclerView.setAdapter(tasksAdapter);
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}