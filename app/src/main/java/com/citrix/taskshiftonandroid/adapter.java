package com.citrix.taskshiftonandroid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.CardViewHolder> implements ItemTouchHelperAdapter {

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        cvh.startBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add code here for startButton

                System.out.println("success start button" + cvh.getAdapterPosition());
            }
        });
        cvh.doneBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add code here for doneButton
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
        Items.remove(position);
        String s = a.toString();
        MainActivity main = MainActivity.getMainActivity();
        OutputStream os = main.os;
        main.sendTS(s);
        main.ChangeIssueAssignee(main.username,main.token, a.taskid, main.AccountID);
        notifyItemRemoved(position);
    }
    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(Items, from, to);
        notifyItemMoved(from, to);
    }

    public void remove(int position) throws IOException {

    }
    public void add(int position,Item Item) {
        Items.add(position,Item);
        notifyItemInserted(position);
    }

    adapter(List<Item> Items){
        this.Items = Items;
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

    //for add item
    //public void remove(int position){
    //    Items.remove(position);
    //    notifyItemRemoved(position);
    //}
    //for delete item
    //public void add(int position,String data) {
    //    Items.add(position,data);
    //    notifyItemInserted(position);
    //}

}

