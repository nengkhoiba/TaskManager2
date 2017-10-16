package com.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taskmanager.database.TaskDbhelper;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {
    ArrayList<TaskItem> aTask;
    TaskListAdapter adapter;
    ListView list;
    TaskDbhelper dbhelper=new TaskDbhelper(TaskListActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        list=(ListView) findViewById(R.id.listViewTask);
        //populateTaskList(aTask);



    }



    @Override
    protected void onResume() {
        super.onResume();
            aTask=new ArrayList<TaskItem>();

        Cursor disCur = dbhelper.getTasks();

        disCur.moveToFirst();
        while (!disCur.isAfterLast()) {
            TaskItem tItem=new TaskItem();
            tItem.taskId = disCur.getString(0);
            tItem.Task = disCur.getString(1);
            tItem.Details = disCur.getString(2);
            tItem.Summary = disCur.getString(3);

            aTask.add(tItem);

            disCur.moveToNext();

        }
        populateTaskList(aTask);

    }

    public  void repolupate(){
        aTask=new ArrayList<TaskItem>();

        Cursor disCur = dbhelper.getTasks();

        disCur.moveToFirst();
        while (!disCur.isAfterLast()) {
            TaskItem tItem=new TaskItem();
            tItem.taskId = disCur.getString(0);
            tItem.Task = disCur.getString(1);
            tItem.Details = disCur.getString(2);
            tItem.Summary = disCur.getString(3);

            aTask.add(tItem);

            disCur.moveToNext();

        }
        adapter = new TaskListAdapter(TaskListActivity.this,aTask);
        adapter.notifyDataSetInvalidated();

        list.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_add:
                Intent addTask=new Intent("AddTask");
                startActivity(addTask);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

//custom adapter
    public class TaskListAdapter extends BaseAdapter {

        private ArrayList<TaskItem> aLists;
        Context context;

        public TaskListAdapter(Context context,ArrayList<TaskItem> aLists)
        {
            super();
            this.context=context;
            this.aLists =aLists;
        }

        public class ViewHolder {

            TextView TaskID;
            TextView TaskName;
            TextView TaskDesc;
            TextView TaskSummary;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.task_list_item, null);

                holder = new ViewHolder();

                holder.TaskID = (TextView) convertView.findViewById(R.id.txtTaskID);
                holder.TaskName = (TextView) convertView.findViewById(R.id.txt_task_item_name);
                holder.TaskDesc=(TextView) convertView.findViewById(R.id.txt_task_item_description);
                holder.TaskSummary=(TextView) convertView.findViewById(R.id.txt_task_item_summary);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TaskItem tItem = aLists.get(position);

            holder.TaskID.setText(tItem.taskId);
            holder.TaskName.setText(tItem.Task);
            holder.TaskDesc.setText(tItem.Details);
            holder.TaskSummary.setText(tItem.Summary);
             return convertView;

        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return aLists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

    }
    //custom list adapter

    public void populateTaskList(ArrayList<TaskItem> taskData){

        adapter = new TaskListAdapter(TaskListActivity.this,taskData);

        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                    long arg3) {
                TextView tTaskId=(TextView) v.findViewById(R.id.txtTaskID);
                TextView tTask=(TextView) v.findViewById(R.id.txt_task_item_name);
                TextView tDetail=(TextView) v.findViewById(R.id.txt_task_item_description);
                TextView tSummary=(TextView) v.findViewById(R.id.txt_task_item_summary);
                AddTask.sTask=tTask.getText().toString();
                AddTask.sDetail=tDetail.getText().toString();
                AddTask.sSummary=tSummary.getText().toString();
                AddTask.sTaskID=tTaskId.getText().toString();
                Intent addTask=new Intent("AddTask");
                startActivity(addTask);

            }

        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                final TextView ID=(TextView) v.findViewById(R.id.txtTaskID);

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this,R.style.Theme_AppCompat_Light_Dialog);

                builder.setTitle("Delete");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        dbhelper.deleteTaskByID(ID.getText().toString());
                        repolupate();

                    }

                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

    }
}
