package com.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taskmanager.database.TaskDbhelper;
import com.taskmanager.webservice.AsyncPublish;
import com.taskmanager.webservice.DataUrls;

public class AddTask extends AppCompatActivity implements AsyncPublish.Publishtask {
//code change
    EditText task,details,summary;
    Button save;
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


                new AsyncPublish(AddTask.this, DataUrls.AddNews+task.getText().toString()+"&body="+summary.getText().toString()+"&image="+details.getText().toString()+"&posted_by=1",AddTask.this)
                        .execute();


            }
        });


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
