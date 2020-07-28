package com.citrix.taskshiftonandroid;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.Paths.get;

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

    public static List<Item> initializeFromDictionary(List<Dictionary> itemDict) {
        List<Item> Items;
        Items = new ArrayList<>();
        for (Dictionary<String, String> a: itemDict){
            int type = 0;
            if (a.get("Type") == "Epic") {
                type = R.drawable.epic;
            } else {
                type = R.drawable.story;
            }
            Items.add(new Item(a.get("Task"), a.get("Summary"), R.drawable.icons8_jira_240, type));
        }
        return Items;
    }


    //后续改循环
    public static List<Item> initializeData() {
        List<Item> Items;
        Items = new ArrayList<>();
        Items.add(new Item("TS-6", "Investigate how blue tooth works on Android"
                , R.drawable.icons8_jira_240, R.drawable.epic));
        Items.add(new Item("TS-11", "Develop task list UI"
                , R.drawable.icons8_jira_240, R.drawable.story));
        Items.add(new Item("TS-13", "Get tickets from Jira service from Android application"
                , R.drawable.icons8_jira_240, R.drawable.story));
        Items.add(new Item("TS-6", "Investigate how blue tooth works on Android"
                , R.drawable.icons8_jira_240, R.drawable.epic));
        Items.add(new Item("TS-11", "Develop task list UI"
                , R.drawable.icons8_jira_240, R.drawable.story));
        return Items;
    }

    public String toString() {
        return this.taskid + "//" + storySummary + "//" + String.valueOf(jiraLogo) + "//" + String.valueOf(storyType) + "//";
    }

    public static Item toItem(String s) {
        String id = s.substring(0, s.indexOf("//"));
        s = s.substring(s.indexOf("//") + 2);
        String summary = s.substring(0, s.indexOf("//"));
        s = s.substring(s.indexOf("//") + 2);
        int Logo = Integer.parseInt(s.substring(0, s.indexOf("//")));
        s = s.substring(s.indexOf("//") + 2);
        int type = Integer.parseInt(s.substring(0, s.indexOf("//")));
        return new Item(id, summary, Logo, type);
    }

}




