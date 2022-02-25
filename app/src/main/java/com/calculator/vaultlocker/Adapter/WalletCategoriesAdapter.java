package com.calculator.vaultlocker.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vaultlocker.DB.WalletEntriesDAL;
import com.calculator.vaultlocker.Model.WalletCategoriesFileDB_Pojo;
import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Constants;

import java.util.List;

public class WalletCategoriesAdapter extends BaseAdapter {
    public int categoriesCount;
    public int[] entryCount;
    public int focusedPosition = 0;
    public boolean isEdit = false;
    public boolean isGridView = true;
    public Context context;
    public LayoutInflater inflater;
    public List<WalletCategoriesFileDB_Pojo> walletCategoriesFileInfoPojoList;
    public Constants constants = new Constants();

    public WalletCategoriesAdapter(Context context, List<WalletCategoriesFileDB_Pojo> list) {
        int i = 0;
        WalletEntriesDAL walletEntriesDAL = new WalletEntriesDAL(context);
        this.context = context;
        this.walletCategoriesFileInfoPojoList = list;
        this.categoriesCount = list.size();
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.entryCount = new int[this.walletCategoriesFileInfoPojoList.size()];
        while (true) {
            int[] iArr = this.entryCount;
            if (i < iArr.length) {
                this.constants.getClass();
                StringBuilder sb = new StringBuilder();
                this.constants.getClass();
                sb.append("WalletCategoriesFileId");
                sb.append(" = ");
                sb.append(list.get(i).getCategoryFileId());
                iArr[i] = walletEntriesDAL.getEntriesCount("SELECT \t COUNT(*)\t\t\t\t\t   FROM TableWalletEntries WHERE ".concat(sb.toString()));
                i++;
            } else {
                return;
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setFocusedPosition(int i) {
        focusedPosition = i;
    }

    public void setIsEdit(boolean z) {
        isEdit = z;
    }

    public void setIsGridview(boolean z) {
        isGridView = z;
    }

    @Override
    public int getCount() {
        return walletCategoriesFileInfoPojoList.size();
    }

    @Override
    public Object getItem(int i) {
        return walletCategoriesFileInfoPojoList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (categoriesCount > 0) {
            if (view == null) {
                viewHolder = new ViewHolder();
                view2 = inflater.inflate(R.layout.item_wallet_gridview, viewGroup, false);
                viewHolder.tv_WalletCategoriesName = view2.findViewById(R.id.tv_WalletTitle);
                viewHolder.tv_WalletCategoriesEntryCount = view2.findViewById(R.id.tv_WalletCategoriesEntryCount);
                viewHolder.iv_walletCategoriesIcon = view2.findViewById(R.id.iv_walletCategoriesIcon);
                viewHolder.ll_selection = view2.findViewById(R.id.ll_selection);
                view2.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
                view2 = view;
            }
            TypedArray obtainTypedArray = context.getResources().obtainTypedArray(R.array.wallet_drawables_list);
            int resourceId = obtainTypedArray.getResourceId(walletCategoriesFileInfoPojoList.get(i).getCategoryFileIconIndex(), -1);
            viewHolder.tv_WalletCategoriesEntryCount.setText(String.valueOf(entryCount[i]));
            viewHolder.tv_WalletCategoriesName.setText(walletCategoriesFileInfoPojoList.get(i).getCategoryFileName());
            viewHolder.iv_walletCategoriesIcon.setBackgroundResource(resourceId);
            obtainTypedArray.recycle();
            if (focusedPosition != i || !isEdit) {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_unselect);
            } else {
                viewHolder.ll_selection.setBackgroundResource(R.drawable.album_grid_item_boarder_select);
            }
            view = view2;
        } else {
            view.setVisibility(View.GONE);
        }
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.anim_fade_in));
        return view;
    }

    public static class ViewHolder {
        public ImageView iv_walletCategoriesIcon;
        public LinearLayout ll_selection;
        public TextView tv_WalletCategoriesEntryCount;
        public TextView tv_WalletCategoriesName;

        public ViewHolder() {
        }
    }
}
