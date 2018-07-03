package com.example.zz.chilq.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.zz.chilq.R;
import com.example.zz.chilq.model.task_model;

import java.util.ArrayList;
import java.util.List;

public class parent_task_list_adapter extends RecyclerView.Adapter<parent_task_recycler_view_holder> {

    private List<task_model> mDataset= new ArrayList<>();
    private int lastPosition = -1;

    public parent_task_list_adapter(List<task_model> dataset) {
        this.mDataset = dataset;
    }

    @Override
    public parent_task_recycler_view_holder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parent_tasks_card_view, parent, false);
        parent_task_recycler_view_holder vh = new parent_task_recycler_view_holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final parent_task_recycler_view_holder holder, final int position) {

        task_model taskModel=mDataset.get(position);

        holder.mImageView.setImageResource(R.drawable.ic_launcher_background);
        holder.mTitleView.setText(taskModel.getName_task());
        holder.mDescriptionView.setText(taskModel.getDesc_task());
        holder.mRewardTextView.setText("Баллы: " + taskModel.getReward());

        // description expand
        holder.mDescriptionView.setVisibility(View.GONE);
        holder.mRewardTextView.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mDescriptionView.isShown()) {
                    holder.mDescriptionView.setVisibility(View.GONE);
                    holder.mRewardTextView.setVisibility(View.GONE);
                } else {
                    holder.mDescriptionView.setVisibility(View.VISIBLE);
                    holder.mRewardTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

            }
        });

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Activity activity = (Activity) view.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.dialog_text_delete_title);

                String message = activity.getResources().getString(R.string.dialog_text_delete_message);
                message = message.replace("%task_name%", holder.mTitleView.getText());

                builder.setMessage(message)
                        .setPositiveButton(R.string.dialog_text_delete_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Animation animation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_out_right);
                                animation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        mDataset.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, getItemCount());
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                                holder.itemView.startAnimation(animation);
                            }
                        })
                        .setNegativeButton(R.string.dialog_text_delete_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.create().show();
            }
        });

        // play slide animation
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
            animation.setDuration(300);
            animation.setStartOffset((position - lastPosition) * 100);
            holder.itemView.startAnimation(animation);
            this.lastPosition = position;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}