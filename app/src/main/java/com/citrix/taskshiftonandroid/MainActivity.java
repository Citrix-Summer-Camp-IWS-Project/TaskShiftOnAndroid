package com.citrix.taskshiftonandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creat listview object
        ListView taskList = (ListView) findViewById(R.id.lv_main);

        //hardcode task data to show
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("logo",R.drawable.jira);
        map.put("ticketId","TS-6");
        map.put("storyType", R.drawable.epic);
        map.put("storySummary", "Investigate how blue tooth works on Android");
        list.add(map);

        map = new HashMap<String,Object>();
        map.put("logo",R.drawable.jira);
        map.put("ticketId","TS-11");
        map.put("storyType", R.drawable.story);
        map.put("storySummary", "Develop task list UI");
        list.add(map);

        map = new HashMap<String,Object>();
        map.put("logo",R.drawable.jira);
        map.put("ticketId","TS-13");
        map.put("storyType", R.drawable.story);
        map.put("storySummary", "Get tickets from Jira service from Android application");
        list.add(map);

        //adapter
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.singletask,
                new String[] {"logo","ticketId", "storyType","storySummary"},
                new int[] {R.id.taskPicture,R.id.taskTicketId,R.id.taskType, R.id.taskSummary}
                );

        taskList.setAdapter(adapter);
    }
}