package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ImportEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

public class GalleryPhotoAdapter extends ArrayAdapter<ImportEnt> {
    public Context con;
    public ArrayList<ImportEnt> photoImportEntList;
    public LayoutInflater layoutInflater;
    public DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_photo_empty_icon).showImageForEmptyUri(R.drawable.ic_photo_empty_icon).showImageOnFail(R.drawable.ic_photo_empty_icon).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    public GalleryPhotoAdapter(Context context, int i, ArrayList<ImportEnt> arrayList) {
        super(context, i, arrayList);
        this.con = context;
        this.photoImportEntList = arrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        View view2;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = layoutInflater.inflate(R.layout.item_custom_gallery, null);

            viewHolder.ll_custom_gallery = view2.findViewById(R.id.ll_custom_gallery);
            viewHolder.imageview = view2.findViewById(R.id.thumbImage);
            viewHolder.playimageAlbum = view2.findViewById(R.id.playthumbImage);
            viewHolder.ll_dark_on_click = view2.findViewById(R.id.ll_dark_on_click);
            viewHolder.iv_tick = view2.findViewById(R.id.iv_tick);

            viewHolder.imageview.setImageBitmap(photoImportEntList.get(i).GetThumbnail());

            if (photoImportEntList.get(i).GetThumbnailSelection()) {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                viewHolder.iv_tick.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.iv_tick.setVisibility(View.INVISIBLE);
            }

            viewHolder.imageview.setBackgroundColor(0);
            viewHolder.ll_custom_gallery.setId(i);
            viewHolder.imageview.setId(i);
            viewHolder.iv_tick.setId(i);

            viewHolder.imageview.setOnClickListener(view3 -> {
                int intValue = (Integer) view3.getTag();
                if (photoImportEntList.get(intValue).GetThumbnailSelection()) {
                    viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                    photoImportEntList.get(intValue).SetThumbnailSelection(false);
                    viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                    viewHolder.iv_tick.setVisibility(View.INVISIBLE);
                    return;
                }
                viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                photoImportEntList.get(intValue).SetThumbnailSelection(true);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                viewHolder.iv_tick.setVisibility(View.VISIBLE);
            });
            view2.setTag(viewHolder);
            view2.setTag(R.id.thumbImage, viewHolder.imageview);
            view2.setTag(R.id.iv_tick, viewHolder.iv_tick);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            view2 = view;
        }

        viewHolder.imageview.setTag(i);
        viewHolder.iv_tick.setTag(i);

        Common.imageLoader.displayImage("file:///" + photoImportEntList.get(i).GetPath(), viewHolder.imageview, options);

        if (photoImportEntList.get(i).GetThumbnailSelection()) {
            viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
            viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
            viewHolder.iv_tick.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
            viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
            viewHolder.iv_tick.setVisibility(View.INVISIBLE);
        }
        viewHolder.id = i;
        return view2;
    }


    static class ViewHolder {
        public int id;
        public ImageView imageview;
        public ImageView iv_tick;
        public ImageView playimageAlbum;
        public RelativeLayout ll_custom_gallery;
        public LinearLayout ll_dark_on_click;

        ViewHolder() {
        }
    }
}
