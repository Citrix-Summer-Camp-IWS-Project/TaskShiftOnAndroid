package com.citrix.taskshiftonandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Item> Items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleviewlisttest);

//        //creat listview object
//        ListView taskList = (ListView) findViewById(R.id.lv_main);
//        //LinearLayout.LayoutParams params = tasklist.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 60);
//
//
//        //hardcode task data to show
//        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//        Map<String, Object> map = new HashMap<String,Object>();
//        map.put("logo",R.drawable.jira);
//        map.put("ticketId","TS-6");
//        map.put("storyType", R.drawable.epic);
//        map.put("storySummary", "Investigate how blue tooth works on Android");
//        list.add(map);
//
//        map = new HashMap<String,Object>();
//        map.put("logo",R.drawable.jira);
//        map.put("ticketId","TS-11");
//        map.put("storyType", R.drawable.story);
//        map.put("storySummary", "Develop task list UI");
//        list.add(map);
//
//        map = new HashMap<String,Object>();
//        map.put("logo",R.drawable.jira);
//        map.put("ticketId","TS-13");
//        map.put("storyType", R.drawable.story);
//        map.put("storySummary", "Get tickets from Jira service from Android application");
//        list.add(map);
//
//        //card adapter test
//        //CardsAdapter adapter = new CardsAdapter(this);
//        //lvCards.setAdapter(adapter);
//
//        //adapter
//        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.test,
//                new String[] {"logo","ticketId", "storyType","storySummary"},
//                new int[] {R.id.jira,R.id.TS,R.id.storyType, R.id.summary}
//                );
//        //set
//        taskList.setAdapter(adapter);

        //button function
        //Button button = findViewById(R.id.startButton);
        //button.setOnClickListener(new View.OnClickListener() {
       // public void onClick(View v) {
//                // your handler code here
//                Items.add(new Item("TS-6", "Investigate how blue tooth works on Android"
//                        , R.drawable.icons8_jira_240, R.drawable.epic));
//                Items.add(new Item("TS-11", "Develop task list UI"
//                        , R.drawable.icons8_jira_240, R.drawable.story));
         //   }
        //});


        //data
        //Items = new ArrayList<>();
        Items = Item.initializeData();




        //initiate recycle view
        RecyclerView rv = (RecyclerView) findViewById(R.id.tasklist);
        //define the width of divider
        int space = 2;
        rv.addItemDecoration(new SpacesItemDecoration(space));

        //这里我们选择创建一个LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //为RecyclerView对象指定我们创建得到的layoutManager
        rv.setLayoutManager(layoutManager);
        adapter adapter = new adapter(Items);
        rv.setAdapter(adapter);
        //PagerSnapHelper snapHelper = new PagerSnapHelper();
        //snapHelper.attachToRecyclerView(rv);

        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);


    }




    // for the divider width
    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != parent.getChildCount() ) {
                outRect.bottom = space;
            }
        }
    }
}