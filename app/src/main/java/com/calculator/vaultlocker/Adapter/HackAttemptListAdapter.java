package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vaultlocker.Model.HackAttemptEntity;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.XMLParser.HackAttemptMethods;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;

import java.io.File;
import java.util.ArrayList;

public class HackAttemptListAdapter extends ArrayAdapter<HackAttemptEntity> {
    public boolean _isAllCheck;
    public boolean _isEdit;
    public Context con;
    public LayoutInflater layoutInflater;
    public ArrayList<HackAttemptEntity> hackAttemptEntitys;

    public HackAttemptListAdapter(Context context, int i, ArrayList<HackAttemptEntity> arrayList, boolean z, Boolean bool) {
        super(context, i, arrayList);
        this._isAllCheck = false;
        this._isEdit = false;
        this.con = context;
        this.hackAttemptEntitys = arrayList;
        this._isEdit = z;
        this._isAllCheck = bool;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view2;
        if (view == null) {
            view2 = this.layoutInflater.inflate(R.layout.hack_attempt_activity_item, null);
            viewHolder = new ViewHolder();

            viewHolder.lbl_hackattempt_pass_item = view2.findViewById(R.id.lbl_hackattempt_pass_item);
            viewHolder.lbl_hackattempt_description_item = view2.findViewById(R.id.lbl_hackattempt_description_item);
            viewHolder.iv_hackattempt_item = view2.findViewById(R.id.iv_hackattempt_item);
            viewHolder.cb_hackattempt_item = view2.findViewById(R.id.cb_hackattempt_item);

            HackAttemptEntity hackAttemptEntity = this.hackAttemptEntitys.get(i);
            if (SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntity.GetLoginOption())) {
                TextView textView = viewHolder.lbl_hackattempt_pass_item;
                textView.setText("Wrong Password: " + hackAttemptEntity.GetWrongPassword());
            } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntity.GetLoginOption())) {
                TextView textView2 = viewHolder.lbl_hackattempt_pass_item;
                textView2.setText("Wrong PIN: " + hackAttemptEntity.GetWrongPassword());
            } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntity.GetLoginOption())) {
                TextView textView3 = viewHolder.lbl_hackattempt_pass_item;
                textView3.setText("Wrong Pattern: " + hackAttemptEntity.GetWrongPassword());
            }

            viewHolder.lbl_hackattempt_description_item.setText(hackAttemptEntity.GetHackAttemptTime().replace("GMT+05:00", ""));
            viewHolder.cb_hackattempt_item.setChecked(hackAttemptEntity.GetIsCheck());

            if (hackAttemptEntity.GetImagePath().length() > 0) {
                viewHolder.iv_hackattempt_item.setImageBitmap(HackAttemptMethods.DecodeFile(new File(hackAttemptEntity.GetImagePath())));
            }

            if (this._isEdit) {
                viewHolder.cb_hackattempt_item.setVisibility(View.VISIBLE);
            } else {
                viewHolder.cb_hackattempt_item.setVisibility(View.INVISIBLE);
            }

            if (this._isAllCheck && hackAttemptEntity.GetIsCheck()) {
                viewHolder.cb_hackattempt_item.setChecked(true);
            }

            viewHolder.cb_hackattempt_item.setOnCheckedChangeListener((compoundButton, z) -> HackAttemptListAdapter.this.hackAttemptEntitys.get((Integer) compoundButton.getTag()).SetIsCheck(compoundButton.isChecked()));
            view2.setTag(viewHolder);
            view2.setTag(R.id.lbl_hackattempt_pass_item, viewHolder.lbl_hackattempt_pass_item);
            view2.setTag(R.id.lbl_hackattempt_description_item, viewHolder.lbl_hackattempt_description_item);
            view2.setTag(R.id.iv_hackattempt_item, viewHolder.iv_hackattempt_item);
            view2.setTag(R.id.cb_hackattempt_item, viewHolder.cb_hackattempt_item);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            view2 = view;
        }
        viewHolder.cb_hackattempt_item.setTag(i);
        if (this._isEdit) {
            viewHolder.cb_hackattempt_item.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cb_hackattempt_item.setVisibility(View.INVISIBLE);
        }
        if (SecurityLocksCommon.LoginOptions.Password.toString().equals(this.hackAttemptEntitys.get(i).GetLoginOption())) {
            TextView textView4 = viewHolder.lbl_hackattempt_pass_item;
            textView4.setText("Wrong Password: " + this.hackAttemptEntitys.get(i).GetWrongPassword());
        } else if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(this.hackAttemptEntitys.get(i).GetLoginOption())) {
            TextView textView5 = viewHolder.lbl_hackattempt_pass_item;
            textView5.setText("Wrong PIN: " + this.hackAttemptEntitys.get(i).GetWrongPassword());
        } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(this.hackAttemptEntitys.get(i).GetLoginOption())) {
            TextView textView6 = viewHolder.lbl_hackattempt_pass_item;
            textView6.setText("Wrong Pattern: " + this.hackAttemptEntitys.get(i).GetWrongPassword());
        }
        viewHolder.lbl_hackattempt_description_item.setText(this.hackAttemptEntitys.get(i).GetHackAttemptTime().replace("GMT+05:00", ""));
        viewHolder.cb_hackattempt_item.setChecked(this.hackAttemptEntitys.get(i).GetIsCheck());
        view2.startAnimation(AnimationUtils.loadAnimation(this.con, 17432578));
        return view2;
    }


    public static class ViewHolder {
        public CheckBox cb_hackattempt_item;
        public ImageView iv_hackattempt_item;
        public TextView lbl_hackattempt_description_item;
        public TextView lbl_hackattempt_pass_item;

        public ViewHolder() {
        }
    }
}
