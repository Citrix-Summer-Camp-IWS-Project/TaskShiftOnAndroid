package com.citrix.taskshiftonandroid;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.TimerTask;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    private java.util.Timer timer;
    private TimerTask task;

    //java.util.Timer timer = new java.util.Timer(true);


    public ItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //up and down drag
        //int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //(left and) right swipe
        //
        // code for left swipe : ItemTouchHelper.START |
        int swipeFlags =  ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDraw (Canvas c,
                             RecyclerView recyclerView,
                             RecyclerView.ViewHolder viewHolder,
                             float dX,
                             float dY,
                             int actionState,
                             boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);


        //TimerTask task = new TimerTask() {
            //public void run() {
                if (viewHolder != null && viewHolder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
//                    com.citrix.taskshiftonandroid.adapter.CardViewHolder CardviewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) viewHolder;
//                    CardviewHolder.getAdapterPosition();
//                    int[] location = new int[2];
//                    CardviewHolder.cv.getLocationInWindow(location);
//                    int x=location[0];//获取当前位置的横坐标
//                    int y=location[1];//获取当前位置的纵坐标

                   // System.out.println("cardView coordinate: " + dX + "  " + dY);

                }
            //}
        //};

        //timer.schedule(task, 10, 500);
    }
    @Override
    public void onSelectedChanged (RecyclerView.ViewHolder viewHolder,
                                   int actionState) {
        if (timer == null) {
            timer = new java.util.Timer(true);
            task = new TimerTask() {
                public void run() {
                    if (viewHolder != null && viewHolder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
                        com.citrix.taskshiftonandroid.adapter.CardViewHolder CardviewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) viewHolder;
                        CardviewHolder.getAdapterPosition();
                        int[] location = new int[2];
                        CardviewHolder.cv.getLocationInWindow(location);
                        int x=location[0];//获取当前位置的横坐标
                        int y=location[1];//获取当前位置的纵坐标

                        System.out.println("cardView coordinate: " + x + "  " + y);

                    }
                }
            };
        }
        System.out.println("onSelectedChanged" + actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            System.out.println("the method call");

            timer.schedule(task, 5, 500);
            System.out.println("timer start success");

        }
        if (actionState == 0) {
            System.out.println("timer stop success");
            //System.gc();
            task.cancel();
            timer.cancel();
        }

    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        System.out.println("onswipe");
        try {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        } catch (IOException e) {
            System.out.println("onswipe");
            e.printStackTrace();
        }
//        java.util.Timer timer = new java.util.Timer(true);
//        TimerTask task = new TimerTask() {
//            public void run() {
//                if (viewHolder != null && viewHolder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
//                    com.citrix.taskshiftonandroid.adapter.CardViewHolder CardviewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) viewHolder;
//                    CardviewHolder.getAdapterPosition();
//                    int[] location = new int[2];
//                    CardviewHolder.cv.getLocationInWindow(location);
//                    int x=location[0];//获取当前位置的横坐标
//                    int y=location[1];//获取当前位置的纵坐标
//
//                    System.out.println("cardView coordinate: " + x + "  " + y);
//
//                }
//            }
//        };

        //timer.schedule(task, 10, 500);

    }

    //long press enable the card to drag(up and down)
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //enable the card to swipe
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


}
