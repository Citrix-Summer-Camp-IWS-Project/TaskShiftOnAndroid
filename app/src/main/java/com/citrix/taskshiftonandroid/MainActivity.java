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
        map.put("storyType", "epic");
        map.put("storySummary", "Investigate how blue tooth works on Android");
        map.put("storyOperater", "Li Tian");
        list.add(map);

        map = new HashMap<String,Object>();
        map.put("logo",R.drawable.jira);
        map.put("storyType", "story");
        map.put("storySummary", "Develop task list UI");
        map.put("storyOperater", "Yuhan Lu");
        list.add(map);

        map = new HashMap<String,Object>();
        map.put("logo",R.drawable.jira);
        map.put("storyType", "story");
        map.put("storySummary", "Get tickets from Jira service from Android application");
        map.put("storyOperater", "Haoran Li");
        list.add(map);

        //adapter
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.singletask,
                new String[] {"logo", "storyType","storySummary","storyOperater"},
                new int[] {R.id.taskPicture,R.id.taskType, R.id.taskSummary, R.id.taskOperater}
                );

        taskList.setAdapter(adapter);
    }
}