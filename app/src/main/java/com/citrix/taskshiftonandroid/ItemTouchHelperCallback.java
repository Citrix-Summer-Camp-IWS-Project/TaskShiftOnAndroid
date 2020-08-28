package com.citrix.taskshiftonandroid;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.TimerTask;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final adapter mAdapter;
    private final int latncyTime = 20;
    private final double swipeThreshold = 0.5;
    public MainActivity main;
    private boolean currentStatus = false;
    private long currentTime;
    private long lastTime;
    private TimerTask task;
    private DecimalFormat df;
    private int flag = 0;
    private boolean swipeAccelaration = false;

    public ItemTouchHelperCallback(adapter mAdapter, RecyclerView recyclerView, MainActivity main) {
        this.mAdapter = mAdapter;
        this.main = main;
        currentTime = 0;
        lastTime = 0;
        flag = 0;
        swipeAccelaration = false;
    }

    public void delete(RecyclerView recyclerView, float dX) {

    }

    //set the card to origin place(fixed)
    public void setOriginX(RecyclerView recyclerView) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
        if (holder != null && holder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
            com.citrix.taskshiftonandroid.adapter.CardViewHolder CardViewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) holder;
            CardViewHolder.cv.setTranslationX(0);
        }
    }

    public void initializeView(RecyclerView recyclerView, float dX) {
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
        if (holder != null && holder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
            com.citrix.taskshiftonandroid.adapter.CardViewHolder CardViewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) holder;
            CardViewHolder.cv.setTranslationX(dX - CardViewHolder.cv.getRight() - CardViewHolder.cv.getLeft());
        }
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
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
    public float getSwipeEscapeVelocity(float defaultValue) {
        System.out.println("swipeVelocity " + defaultValue );
        //the card has reached maximum escape velocity
        swipeAccelaration = true;
        return defaultValue;
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
        currentTime = System.currentTimeMillis();
        if (lastTime == 0) {
            lastTime = currentTime;
        }

        if ((currentStatus == false) && (isCurrentlyActive == true)) {
            //user release card and stop
            System.out.println("User release card");
            flag = 0;
        }

        com.citrix.taskshiftonandroid.adapter.CardViewHolder cvh = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) viewHolder;
        //cardview x coordinate of sender
        float f1 = dX + cvh.cv.getLeft();
        //transform the percentage to String
        String s = swipeLength(cvh, f1);
        float f = Float.parseFloat(s);
        OutputStream os = main.os;
        //avoid bluetooth stiky package
        if ((currentTime - lastTime >= latncyTime) && (flag == 0)) {
            try {
                main.sendTS(s, false, MainActivity.COORLABEL);

                System.out.println("sending x coordinate: " + dX);

            } catch (IOException e) {
                e.printStackTrace();
            }
            lastTime = System.currentTimeMillis();
        }

        System.out.println("position0 cardView coordinate: " + dX);

        System.out.println("get left and right" + cvh.cv.getLeft() + cvh.cv.getRight());
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
                    if (holder != null && holder instanceof com.citrix.taskshiftonandroid.adapter.CardViewHolder) {
                        com.citrix.taskshiftonandroid.adapter.CardViewHolder CardViewHolder = (com.citrix.taskshiftonandroid.adapter.CardViewHolder) holder;
                        System.out.println("right" + CardViewHolder.cv.getRight() + CardViewHolder.cv.getLeft());
                        float f2 =  dX - CardViewHolder.cv.getRight();
                        System.out.println("position2 cardView coordinate: " + f2);
                    }

        //when card stop swipe and release, the received card should be deleted
        if ((currentStatus == true) && (isCurrentlyActive == false) && (f < swipeThreshold) && (swipeAccelaration == false)) {
            System.out.println("onSelectedChanged" + getSwipeThreshold(viewHolder));
            String cardDelete = "card&stop send";
            OutputStream oss = main.os;
            try {
                System.out.println("swipeVelocity sending");
                main.sendTS(cardDelete, true, MainActivity.SENDCANCELLABEL);
                //card release before totally swiped
                flag = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        currentStatus = isCurrentlyActive;
    }

    @Override
    public void onSelectedChanged (RecyclerView.ViewHolder viewHolder,
                                   int actionState) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            try {
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // if onSwiped called, the card was send
        flag = 1;
        swipeAccelaration = false;
        try {
            mAdapter.remove(viewHolder.getAdapterPosition());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //calculate the cardSwipeThreshold
    public String swipeLength(adapter.CardViewHolder cvh, float f1) {
        int length = cvh.cv.getRight() + cvh.cv.getLeft() * 2;
        float totalLength = (float) length;
        //transfer to percentage
        float rounddX = (float) (Math.round(f1 * 100)) / 100;
        df = new DecimalFormat("0.000");
        return df.format((float) rounddX / totalLength);
    }


}
