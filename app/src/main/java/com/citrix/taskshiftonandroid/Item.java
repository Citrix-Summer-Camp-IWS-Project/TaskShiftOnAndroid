package com.citrix.taskshiftonandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item {
    String taskid;
    String storySummary;
    int jiraLogo;
    int storyType;

    Item(String taskid, String storySummary, int jiraLogo, int storyType) {
        this.taskid = taskid;
        this.storySummary = storySummary;
        this.jiraLogo = jiraLogo;
        this.storyType = storyType;
    }



    //后续改循环
    public static List<Item> initializeData() {
        List<Item> Items;
        Items = new ArrayList<>();
        Items.add(new Item("TS-6", "Investigate how blue tooth works on Android"
                , R.drawable.jira, R.drawable.epic));
        Items.add(new Item("TS-11", "Develop task list UI"
                , R.drawable.jira, R.drawable.story));
        Items.add(new Item("TS-13", "Get tickets from Jira service from Android application"
                , R.drawable.jira, R.drawable.story));
        return Items;
    }
}




