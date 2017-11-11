package com.taskmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taskmanager.database.TaskDbhelper;
import com.taskmanager.webservice.DataUrls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {
    ArrayList<TaskItem> aTask;
    TaskListAdapter adapter;
    ListView list;
    TaskDbhelper dbhelper=new TaskDbhelper(TaskListActivity.this);
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        list=(ListView) findViewById(R.id.listViewTask);
        progress=(ProgressBar) findViewById(R.id.progressBar);
        //populateTaskList(aTask);



    }



    @Override
    protected void onResume() {
        super.onResume();
        new AsyncHttpTask().execute(DataUrls.GetNews);
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            list.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    Log.d("-----------------------",url.toString());
                    Log.d("-----------------------",response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("-----------------------","EROROROROROOR");
                // Log.d(TAG, e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            list.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
            if (result == 1) {

                populateTaskList(aTask);
            } else {


                Toast.makeText(TaskListActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void parseResult(String result) {
        try {
            Log.d("-----------------------",result);
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("news");
            aTask=new ArrayList<TaskItem>();
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);


                TaskItem tItem=new TaskItem();
                tItem.taskId = post.optString("title");
                tItem.Task = post.optString("title");
                tItem.Details = post.optString("image");
                tItem.Summary = post.optString("body");

                aTask.add(tItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
