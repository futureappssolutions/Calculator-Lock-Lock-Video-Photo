package com.calculator.vaultlocker.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.Model.FeatureActivityEnt;

import java.util.ArrayList;

public class MainAdapter extends ArrayAdapter<FeatureActivityEnt> {
    private final ArrayList<FeatureActivityEnt> featureEntList;
    private final LayoutInflater layoutInflater;

    public MainAdapter(Context context, int i, ArrayList<FeatureActivityEnt> arrayList) {
        super(context, i, arrayList);
        this.featureEntList = arrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View inflate = this.layoutInflater.inflate(R.layout.item_main_lay, null);
        ((AppCompatTextView) inflate.findViewById(R.id.tv_text)).setText(featureEntList.get(i).get_featureName());
        ((AppCompatImageView) inflate.findViewById(R.id.img_grid)).setImageResource(featureEntList.get(i).get_feature_Icon());
        return inflate;
    }
}
