package com.citrix.taskshiftonandroid;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

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

    //after the card has swiped over
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        try {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        } catch (IOException e) {
            System.out.println("assigned problematic");
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


}
