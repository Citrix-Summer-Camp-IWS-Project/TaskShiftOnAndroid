package com.citrix.taskshiftonandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.CardViewHolder> implements ItemTouchHelperAdapter {

    public enum TaskState {
        toDo ("11"),
        inProgress("21"),
        done("31");
        private String stateNum;
        TaskState(String stateNum){
            this.stateNum = stateNum;
        }
        public String getStateNum() {
            return stateNum;
        }
    }

    public MainActivity main;

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        cvh.startBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = cvh.getAdapterPosition();
                Item a = Items.get(position);
                try {
                    main.ChangeIssueStatus(main.getmAccount().getUsername(),main.getmAccount().getToken(), a.taskid, TaskState.inProgress.getStateNum());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("success start button");
            }
        });
        cvh.doneBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = cvh.getAdapterPosition();
                Item a = Items.get(position);
                try {
                    main.ChangeIssueStatus(main.getmAccount().getUsername(),main.getmAccount().getToken(), a.taskid, TaskState.done.getStateNum());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Items.remove(position);
                notifyItemRemoved(position);
                System.out.println("success done button");

            }
        });
        return cvh;
    }

    @NonNull
    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.taskId.setText(Items.get(i).taskid);
        cardViewHolder.taskSummary.setText(Items.get(i).storySummary);
        cardViewHolder.jira.setImageResource(Items.get(i).jiraLogo);
        cardViewHolder.storyType.setImageResource(Items.get(i).storyType);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    List<Item> Items;

    //try to implement interface (listener)

    @Override
    public void onItemDismiss(int position) throws IOException {
        Item a = Items.get(position);
        String s = a.toString();
        main.sendTS(s, true, MainActivity.ITEMLABEL);
        main.ChangeIssueAssignee(main.getmAccount().getUsername(), main.getmAccount().getToken(), a.taskid, main.getmAccount().getAccountID());
    }
    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(Items, from, to);
        notifyItemMoved(from, to);
    }

    public void remove(int position) throws IOException {
        Items.remove(position);
        notifyItemRemoved(position);
    }
    public void add(int position,Item Item) {
        Items.add(position,Item);
        notifyItemInserted(position);
    }

    adapter(List<Item> Items, MainActivity main){
        this.Items = Items;
        this.main = main;
    }
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskId;
        TextView taskSummary;
        ImageView jira;
        ImageView storyType;

        Button startBut;
        Button doneBut;
        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card);
            taskId = (TextView)itemView.findViewById(R.id.TS);
            taskSummary = (TextView)itemView.findViewById(R.id.summary);
            jira = (ImageView)itemView.findViewById(R.id.jira);
            storyType = (ImageView)itemView.findViewById(R.id.storyType);
            startBut = (Button) itemView.findViewById(R.id.startButton);
            doneBut = (Button) itemView.findViewById(R.id.doneButton);
        }
    }

}

