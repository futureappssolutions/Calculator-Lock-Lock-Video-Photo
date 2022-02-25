package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vaultlocker.Activity.ToDoActivity;
import com.calculator.vaultlocker.Activity.ViewToDoActivity;
import com.calculator.vaultlocker.Model.ToDoDB_Pojo;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.utilities.Common;
import com.calculator.vaultlocker.utilities.Utilities;

import java.util.ArrayList;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {
    public int ToDofilesCount;
    public int viewBy = 0;
    public Activity mContext;
    public ToDoActivity fragment;
    public LayoutInflater inflater;
    public ArrayList<ToDoDB_Pojo> toDoList;

    public ToDoListAdapter(Activity activity, ArrayList<ToDoDB_Pojo> arrayList, ToDoActivity toDoActivity) {
        this.ToDofilesCount = 0;
        this.mContext = activity;
        this.fragment = toDoActivity;
        this.toDoList = arrayList;
        this.ToDofilesCount = arrayList.size();
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void setViewBy(int i) {
        viewBy = i;
    }

    public void setAdapterData(ArrayList<ToDoDB_Pojo> arrayList) {
        toDoList = arrayList;
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        if (ToDofilesCount > 0) {
            String str = toDoList.get(i).getToDoFileModifiedDate().split(" ")[0];
            String str2 = toDoList.get(i).getToDoFileModifiedDate().split(" ")[1];
            boolean isToDoFileTask1IsChecked = toDoList.get(i).isToDoFileTask1IsChecked();
            boolean isToDoFileTask2IsChecked = toDoList.get(i).isToDoFileTask2IsChecked();
            boolean isToDoFinished = toDoList.get(i).isToDoFinished();
            Utilities.strikeTextview(viewHolder.tv_ToDoName, toDoList.get(i).getToDoFileName(), isToDoFinished);
            if (isToDoFinished) {
                viewHolder.tv_ToDoName.append("  ✓");
            }

            viewHolder.tv_ToDoDate.setText("Date: " + str);
            viewHolder.tv_ToDoTime.setText("Time: " + str2);
            viewHolder.tv_ToDoDate.setSelected(true);
            viewHolder.tv_ToDoTime.setSelected(true);
            String trim = toDoList.get(i).getToDoFileTask1().trim();
            String trim2 = toDoList.get(i).getToDoFileTask2().trim();
            Utilities.strikeTextview(viewHolder.tv_task1, "• " + trim, isToDoFileTask1IsChecked);

            if (!trim2.equals("")) {
                Utilities.strikeTextview(viewHolder.tv_task2, "• " + trim2, isToDoFileTask2IsChecked);
                viewHolder.tv_task2.setVisibility(View.VISIBLE);
                viewHolder.tv_task2c.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_task2c.setVisibility(View.GONE);
            }

            int i2 = -16711936;
            viewHolder.tv_task1c.setTextColor(isToDoFileTask1IsChecked ? -16711936 : mContext.getResources().getColor(R.color.Color_Secondary_Font));

            if (!isToDoFileTask2IsChecked) {
                i2 = mContext.getResources().getColor(R.color.Color_Secondary_Font);
            }
            viewHolder.tv_task2c.setTextColor(i2);

            try {
                String substring = toDoList.get(i).getToDoFileColor().substring(3);
                viewHolder.colorBorder.setBackgroundColor(Color.parseColor("#E6" + substring));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_to_do_list, viewGroup, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView colorBorder;
        public TextView tv_ToDoDate;
        public TextView tv_ToDoName;
        public TextView tv_ToDoTime;
        public TextView tv_task1;
        public TextView tv_task1c;
        public TextView tv_task2;
        public TextView tv_task2c;

        public ViewHolder(View view) {
            super(view);
            tv_ToDoName = view.findViewById(R.id.tv_ToDoName);
            tv_ToDoDate = view.findViewById(R.id.tv_ToDoDate);
            tv_ToDoTime = view.findViewById(R.id.tv_ToDoTime);
            tv_task1 = view.findViewById(R.id.tv_task1);
            tv_task2 = view.findViewById(R.id.tv_task2);
            tv_task1c = view.findViewById(R.id.tv_task1c);
            tv_task2c = view.findViewById(R.id.tv_task2c);
            colorBorder = view.findViewById(R.id.colorBorder);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.setSelected(true);
            int adapterPosition = getAdapterPosition();
            SecurityLocksCommon.IsAppDeactive = false;
            Common.ToDoListEdit = false;
            Common.ToDoListName = toDoList.get(adapterPosition).getToDoFileName();
            Common.ToDoListId = toDoList.get(adapterPosition).getToDoId();
            mContext.startActivity(new Intent(mContext, ViewToDoActivity.class));
            mContext.finish();
            mContext.overridePendingTransition(17432576, 17432577);
        }
    }
}
