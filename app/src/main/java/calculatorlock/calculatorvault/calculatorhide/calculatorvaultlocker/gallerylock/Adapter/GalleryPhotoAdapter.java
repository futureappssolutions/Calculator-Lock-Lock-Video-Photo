package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter;

import android.content.Context;
import android.content.res.Resources;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class GalleryPhotoAdapter extends ArrayAdapter {
    public Context con;
    public ArrayList<ImportEnt> photoImportEntList;
    public LayoutInflater layoutInflater;
    Resources res;
    public DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_photo_empty_icon).showImageForEmptyUri(R.drawable.ic_photo_empty_icon).showImageOnFail(R.drawable.ic_photo_empty_icon).cacheInMemory(false).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    class ViewHolder {
        int id;
        ImageView imageview;
        ImageView iv_tick;
        RelativeLayout ll_custom_gallery;
        LinearLayout ll_dark_on_click;
        ImageView playimageAlbum;

        ViewHolder() {
        }
    }


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
            view2 = this.layoutInflater.inflate(R.layout.item_custom_gallery, (ViewGroup) null);
            viewHolder.ll_custom_gallery = (RelativeLayout) view2.findViewById(R.id.ll_custom_gallery);
            viewHolder.imageview = (ImageView) view2.findViewById(R.id.thumbImage);
            viewHolder.playimageAlbum = (ImageView) view2.findViewById(R.id.playthumbImage);
            viewHolder.ll_dark_on_click = (LinearLayout) view2.findViewById(R.id.ll_dark_on_click);
            viewHolder.iv_tick = (ImageView) view2.findViewById(R.id.iv_tick);
            ImportEnt importEnt = this.photoImportEntList.get(i);
            viewHolder.imageview.setImageBitmap(importEnt.GetThumbnail());
            if (importEnt.GetThumbnailSelection().booleanValue()) {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                viewHolder.iv_tick.setVisibility(0);
            } else {
                viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                viewHolder.iv_tick.setVisibility(4);
            }
            viewHolder.imageview.setBackgroundColor(0);
            viewHolder.ll_custom_gallery.setId(i);
            viewHolder.imageview.setId(i);
            viewHolder.iv_tick.setId(i);
            viewHolder.imageview.setOnClickListener(new View.OnClickListener() {
                /* class com.calculator.vaultlocker.photo.GalleryPhotoAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    if (GalleryPhotoAdapter.this.photoImportEntList.get(intValue).GetThumbnailSelection().booleanValue()) {
                        viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
                        GalleryPhotoAdapter.this.photoImportEntList.get(intValue).SetThumbnailSelection(false);
                        viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
                        viewHolder.iv_tick.setVisibility(4);
                        return;
                    }
                    viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
                    GalleryPhotoAdapter.this.photoImportEntList.get(intValue).SetThumbnailSelection(true);
                    viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
                    viewHolder.iv_tick.setVisibility(0);
                }
            });
            view2.setTag(viewHolder);
            view2.setTag(R.id.thumbImage, viewHolder.imageview);
            view2.setTag(R.id.iv_tick, viewHolder.iv_tick);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.imageview.setTag(Integer.valueOf(i));
        viewHolder.iv_tick.setTag(Integer.valueOf(i));
        ImageLoader imageLoader = Common.imageLoader;
        imageLoader.displayImage("file:///" + this.photoImportEntList.get(i).GetPath().toString(), viewHolder.imageview, this.options);
        if (this.photoImportEntList.get(i).GetThumbnailSelection().booleanValue()) {
            viewHolder.ll_custom_gallery.setBackgroundResource(R.drawable.photo_grid_item_click);
            viewHolder.ll_dark_on_click.setBackgroundResource(R.color.transparent_black_color);
            viewHolder.iv_tick.setVisibility(0);
        } else {
            viewHolder.ll_custom_gallery.setBackgroundResource(R.color.fulltransparent_color);
            viewHolder.ll_dark_on_click.setBackgroundResource(R.color.fulltransparent_color);
            viewHolder.iv_tick.setVisibility(4);
        }
        viewHolder.id = i;
        return view2;
    }
}
