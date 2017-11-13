package com.taskmanager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taskmanager.database.TaskDbhelper;
import com.taskmanager.webservice.AsyncPublish;
import com.taskmanager.webservice.DataUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddTask extends AppCompatActivity implements AsyncPublish.Publishtask {
//code change
    EditText task,details,summary;
    Button save;
    private ProgressDialog dialog;
    public static String sTask="",sDetail="",sSummary="",sTaskID="";
    TaskDbhelper dbhelper=new TaskDbhelper(AddTask.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        task=(EditText) findViewById(R.id.edt_Task);
        details=(EditText) findViewById(R.id.edt_details);
        summary=(EditText) findViewById(R.id.edt_summary);

        task.setText(sTask);
        details.setText(sDetail);
        summary.setText(sSummary);


        save=(Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=DataUrls.AddNews+task.getText().toString()+"&body="+summary.getText().toString()+"&image="+details.getText().toString()+"&posted_by=1";
                new AsyncHttpTask().execute(url);



            }
        });


    }
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(AddTask.this, null, "Loading data...", true, false);

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
                    int rtrn=processdata(response.toString());
                    Log.d("-----------------------",url.toString());
                    Log.d("-----------------------",response.toString());
                    result = rtrn; // Successful
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result == 1) {
                Toast.makeText(AddTask.this, "Success", Toast.LENGTH_SHORT).show();
                finish();

            } else {


                Toast.makeText(AddTask.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public int processdata(String result){
        int rtrn=0;
        try {
            JSONObject response = new JSONObject(result);
            String status=response.optString("status");
            if(status.equals("success")){
                rtrn=1;
            }else{
                rtrn=0;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rtrn;
    }
    @Override
    public void onBackPressed() {
        sTaskID="";
        sTask="";
        sDetail="";
        sSummary="";
        super.onBackPressed();
    }

    @Override
    public void PublishtaskSuccess(String info) {
        Toast.makeText(AddTask.this,"Succesfully saved",Toast.LENGTH_LONG).show();
        task.setText("");
        details.setText("");
        summary.setText("");
        finish();
    }

    @Override
    public void PublishtaskFailed(String info) {
        Toast.makeText(AddTask.this,"Fail to add!",Toast.LENGTH_LONG).show();

    }
}
