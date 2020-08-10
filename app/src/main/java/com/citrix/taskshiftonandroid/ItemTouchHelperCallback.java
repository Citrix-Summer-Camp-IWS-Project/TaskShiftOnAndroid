package com.citrix.taskshiftonandroid;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public MainActivity main;
    private final ItemTouchHelperAdapter mAdapter;

    private java.util.Timer timer;
    private TimerTask task;

    //java.util.Timer timer = new java.util.Timer(true);


    public ItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter, RecyclerView recyclerView, MainActivity main) {
        this.mAdapter = mAdapter;
        this.main = main;
        //this.initializeView(recyclerView);
    }

    public void initializeView(RecyclerView recyclerView, float dX) {

       // System.out.println("I am running initialize");
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
        if (holder != null && holder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
            com.citrix.taskshiftonandroid.adapter.CardViewHolder CardViewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) holder;

            CardViewHolder.cv.setTranslationX(dX);


        }
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        //System.out.println("I am running1");
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

        //System.out.println("I am running1");
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

        //float f = Float.parseFloat("25");

        com.citrix.taskshiftonandroid.adapter.CardViewHolder cvh = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) viewHolder;

        //cardview x coordinate of sender
        float f1 = dX + cvh.cv.getLeft();

        float rounddX = (float)(Math.round(f1*100))/100;
        String s = Float.toString(rounddX) + " ";

        System.out.println("I am running " + s);
        OutputStream os = main.os;
        try {
            main.sendTS(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("position0 cardView coordinate: " + dX);

        System.out.println("get left and right" + cvh.cv.getLeft() + cvh.cv.getRight());
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
                    if (holder != null && holder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
                        com.citrix.taskshiftonandroid.adapter.CardViewHolder CardViewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) holder;
                        System.out.println("right" + CardViewHolder.cv.getRight() + CardViewHolder.cv.getLeft());

                        //CardViewHolder.cv.setTranslationX(dX - CardViewHolder.cv.getRight() - CardViewHolder.cv.getLeft());
                        //cardview x coordinate of reciver
                        float f2 =  dX - CardViewHolder.cv.getRight();
                        System.out.println("position2 cardView coordinate: " + f2);
                    }
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {


            }
        };

        timer.schedule(task, 10, 500);
    }
    @Override
    public void onSelectedChanged (RecyclerView.ViewHolder viewHolder,
                                   int actionState) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

        }

        System.out.println("I am running1" + actionState);
        if (timer == null) {


        }
//        System.out.println("onSelectedChanged" + actionState);
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            System.out.println("the method call");
//            timer = new Timer();
//            task = new TimerTask() {
//                public void run() {
//                    if (viewHolder != null && viewHolder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
//                        com.citrix.taskshiftonandroid.adapter.CardViewHolder CardviewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) viewHolder;
//                        CardviewHolder.getAdapterPosition();
//                        int[] location = new int[2];
//                        CardviewHolder.cv.getLocationInWindow(location);
//                        int x=location[0];//获取当前位置的横坐标
//                        int y=location[1];//获取当前位置的纵坐标
//
//                        //System.out.println("cardView coordinate: " + x + "  " + y);
//
//                    }
//                }
//            };
//            timer.schedule(task, 5, 1000);
//            System.out.println("timer start success");
//
//        }
//        if (actionState == 0) {
//            System.out.println("timer stop success");
//            //System.gc();
//            task.cancel();
//            timer.cancel();
//        }

    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        //System.out.println("I am running1");
        System.out.println("onswipe");
        try {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        } catch (IOException e) {
            System.out.println("onswipe");
            e.printStackTrace();
        }
        timer.cancel();
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

        //System.out.println("I am running1");
        return true;
    }

    //enable the card to swipe
    @Override
    public boolean isItemViewSwipeEnabled() {

        //System.out.println("I am running1");
        return true;
    }


}
