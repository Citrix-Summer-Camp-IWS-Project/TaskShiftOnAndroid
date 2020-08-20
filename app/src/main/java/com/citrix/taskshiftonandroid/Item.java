package com.citrix.taskshiftonandroid;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.Paths.get;

public class Item implements Serializable {
    String taskid;
    String storySummary;
    int jiraLogo;
    int storyType;
    String emailAddress;
    Item(String taskid, String storySummary, int jiraLogo, int storyType, String emailAddress) {
        this.taskid = taskid;
        this.storySummary = storySummary;
        this.jiraLogo = jiraLogo;
        this.storyType = storyType;
        this.emailAddress = emailAddress;
    }

    Item(JSONObject jsonObj, String username) throws JSONException {
        this.emailAddress = "";
        JSONObject fields = (JSONObject) jsonObj.get("fields");

        String tKey = (String) jsonObj.get("key");

        String summary = (String) fields.get("summary");

        JSONObject issueType = (JSONObject) fields.get("issuetype");
        String name = (String) issueType.get("name");
        int tName = 0;
        if (name.equals("Story")) {
            tName = R.drawable.story;
        } else if (name.equals("Epic")) {
            tName = R.drawable.epic;
        }

        JSONObject status = (JSONObject) fields.get("status");
        String statusName = (String) status.get("name");

        try {
            JSONObject assignee = (JSONObject) fields.get("assignee");
            emailAddress = (String) assignee.get("emailAddress");
        } catch (Exception e) {
            emailAddress = "No assignee";
        }

        if (!statusName.equals("Done")) {
            this.taskid = tKey;
            this.storySummary =summary;
            this.storyType =tName;
            this.jiraLogo = R.drawable.icons8_jira_240;
        }
    }

    public String toString() {
        return this.taskid + "//" + storySummary + "//" + String.valueOf(jiraLogo) + "//" + String.valueOf(storyType) + "//" + String.valueOf(emailAddress) + "//";
    }

    public static Item toItem(String s) {
        String id = s.substring(0, s.indexOf("//"));
        s = s.substring(s.indexOf("//") + 2);
        String summary = s.substring(0, s.indexOf("//"));
        s = s.substring(s.indexOf("//") + 2);
        int Logo = Integer.parseInt(s.substring(0, s.indexOf("//")));
        s = s.substring(s.indexOf("//") + 2);
        int type = Integer.parseInt(s.substring(0, s.indexOf("//")));
        s = s.substring(s.indexOf("//") + 2);
        String emailAddress = s.substring(0, s.indexOf("//"));
        return new Item(id, summary, Logo, type, emailAddress);
    }

}




