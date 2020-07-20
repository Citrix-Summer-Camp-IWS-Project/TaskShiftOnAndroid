package com.citrix.taskshiftonandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class helper  extends AppCompatActivity {
    private List<Item> Items;
    RecyclerView rv;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        System.out.println("helper success");
    }

//    public void recycleviewBuild() {
//
//        System.out.println("helper success");
//
//        //setContentView(R.layout.recycleviewlisttest);
//
//        //rv = (RecyclerView) findViewById(R.id.tasklist);
//
//        //Items = Item.initializeData();
//
//
//        adapter adapter = new adapter(Items);
//        //这里我们选择创建一个LinearLayoutManager
//        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        //为RecyclerView对象指定我们创建得到的layoutManager
//        //rv.setLayoutManager(layoutManager);
//        rv.setAdapter(adapter);
//
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(rv);


    }
//}
