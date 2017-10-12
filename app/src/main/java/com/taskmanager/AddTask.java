package com.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
//code change
    EditText task,details,summary;
    Button save;
    public static String sTask="",sDetail="",sSummary="";

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

                TaskItem tItem=new TaskItem();

                tItem.Task=task.getText().toString();
                tItem.Details=details.getText().toString();
                tItem.Summary=summary.getText().toString();
                TaskListActivity.aTask.add(tItem);
                Toast.makeText(AddTask.this,"Succesfully saved",Toast.LENGTH_LONG).show();
                task.setText("");
                details.setText("");
                summary.setText("");
                finish();

            }
        });


    }
}
