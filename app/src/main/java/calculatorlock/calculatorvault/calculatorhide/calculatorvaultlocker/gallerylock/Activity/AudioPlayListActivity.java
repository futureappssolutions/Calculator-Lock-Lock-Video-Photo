package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Ads.Advertisement;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Adapter.AudioPlayListAdapter;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.AudioPlayListDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioPlayListEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser.AudioPlaylistGalleryMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.AccelerometerManager;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchActivityMethods;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.panicswitch.PanicSwitchCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksSharedPreferences;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Utilities;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AudioPlayListActivity extends BaseActivity {
    public static int albumPosition = 0;
    public static boolean isEditAudioAlbum = false;
    public static boolean isGridView = true;

    private ProgressBar Progress;

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Progress.setVisibility(View.GONE);
            }
            super.handleMessage(message);
        }
    };
    private AudioPlayListAdapter adapter;
    private ArrayList<AudioPlayListEnt> audioAlbums;
    private GridView gridView;
    private LinearLayout ll_EditAlbum;
    private int position;
    private boolean IsMoreDropdown = false;
    private String albumName = "";
    private SensorManager sensorManager;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_audios_test);

        FloatingActionButton btn_Add_Album = findViewById(R.id.btn_Add_Album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        RelativeLayout ll_background = findViewById(R.id.ll_background);
        LinearLayout ll_rename_btn = findViewById(R.id.ll_rename_btn);
        LinearLayout ll_delete_btn = findViewById(R.id.ll_delete_btn);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        Advertisement.showBannerAds(AudioPlayListActivity.this, ll_banner);

        gridView = findViewById(R.id.AlbumsGalleryGrid);
        Progress = findViewById(R.id.prbLoading);
        ll_EditAlbum = findViewById(R.id.ll_EditAlbum);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.lblFeature9);
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
        toolbar.setNavigationOnClickListener(view -> btnBackonClick());

        Common.IsPhoneGalleryLoad = true;
        SecurityLocksCommon.IsAppDeactive = true;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        ll_background.setOnTouchListener((view, motionEvent) -> {
            if (IsMoreDropdown) {
                IsMoreDropdown = false;
            }
            return false;
        });

        Progress.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            GetAlbumsFromDatabase();
            Message message = new Message();
            message.what = 1;
            handle.sendMessage(message);
        }, 300);

        btn_Add_Album.setOnClickListener(view -> {
            if (!AudioPlayListActivity.isEditAudioAlbum) {
                AddAlbumPopup();
            }
        });

        gridView.setOnItemClickListener((adapterView, view, i, j) -> {
            AudioPlayListActivity.albumPosition = gridView.getFirstVisiblePosition();
            if (AudioPlayListActivity.isEditAudioAlbum) {
                AudioPlayListActivity.isEditAudioAlbum = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, i, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            Common.FolderId = audioAlbums.get(i).getId();
            startActivity(new Intent(AudioPlayListActivity.this, AudioActivity.class));
            finish();
        });

        gridView.setOnItemLongClickListener((adapterView, view, i, j) -> {
            AudioPlayListActivity.albumPosition = gridView.getFirstVisiblePosition();
            if (AudioPlayListActivity.isEditAudioAlbum) {
                AudioPlayListActivity.isEditAudioAlbum = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, i, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else {
                AudioPlayListActivity.isEditAudioAlbum = true;
                ll_EditAlbum.setVisibility(View.VISIBLE);
                position = i;
                albumName = audioAlbums.get(position).getPlayListName();
                adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, i, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            if (AudioPlayListActivity.albumPosition != 0) {
                gridView.setSelection(AudioPlayListActivity.albumPosition);
            }
            return true;
        });

        ll_rename_btn.setOnClickListener(view -> {
            if (audioAlbums.get(position).getId() != 1) {
                EditAlbumPopup(audioAlbums.get(position).getId(), audioAlbums.get(position).getPlayListName(), audioAlbums.get(position).getPlayListLocation());
                return;
            }
            Toast.makeText(AudioPlayListActivity.this, R.string.lbl_default_album_notrenamed, Toast.LENGTH_SHORT).show();
            AudioPlayListActivity.isEditAudioAlbum = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, 0, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        ll_delete_btn.setOnClickListener(view -> {
            if (audioAlbums.get(position).getId() != 1) {
                DeleteALertDialog(audioAlbums.get(position).getId(), audioAlbums.get(position).getPlayListName(), audioAlbums.get(position).getPlayListLocation());
                return;
            }
            Toast.makeText(AudioPlayListActivity.this, R.string.lbl_default_album_notdeleted, Toast.LENGTH_SHORT).show();
            AudioPlayListActivity.isEditAudioAlbum = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, 0, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        int i = albumPosition;
        if (i != 0) {
            gridView.setSelection(i);
            albumPosition = 0;
        }
    }

    public void btnBackonClick() {
        if (isEditAudioAlbum) {
            SecurityLocksCommon.IsAppDeactive = false;
            isEditAudioAlbum = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new AudioPlayListAdapter(this, 17367043, audioAlbums, 0, isEditAudioAlbum, isGridView);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    public void GetAlbumsFromDatabase() {
        isEditAudioAlbum = false;
        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
        try {
            audioPlayListDAL.OpenRead();
            audioAlbums = (ArrayList) audioPlayListDAL.GetPlayLists();
            adapter = new AudioPlayListAdapter(this, 17367043, audioAlbums, 0, isEditAudioAlbum, isGridView);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            audioPlayListDAL.close();
            throw th;
        }
    }

    public void AddAlbumPopup() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.album_add_edit_popup, null);
        bottomSheetDialog.setContentView(inflate);
        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            Advertisement.getInstance((AudioPlayListActivity.this)).showFull((AudioPlayListActivity.this), () -> {
                if (editText.getEditableText().toString().length() <= 0 || editText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AudioPlayListActivity.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                    return;
                }
                albumName = editText.getEditableText().toString();
                File file = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.AUDIOS + albumName);
                if (file.exists()) {
                    Toast.makeText(AudioPlayListActivity.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                } else {
                    file.mkdirs();
                    AudioPlaylistGalleryMethods audioPlaylistGalleryMethods = new AudioPlaylistGalleryMethods();
                    audioPlaylistGalleryMethods.AddPlaylistToDatabase(AudioPlayListActivity.this, albumName);
                    Toast.makeText(AudioPlayListActivity.this, R.string.lbl_Photos_Album_Create_Album_Success, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase();
                    bottomSheetDialog.dismiss();
                }
            });
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    public void EditAlbumPopup(final int i, final String str, final String str2) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.album_add_edit_popup, null);
        bottomSheetDialog.setContentView(inflate);

      //  final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
     //   dialog.setContentView(R.layout.album_add_edit_popup);
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        TextView textView = inflate.findViewById(R.id.lbl_Create_Edit_Album);
        textView.setTypeface(createFromAsset);
        textView.setText(R.string.lbl_Photos_Album_Rename_Album);
        ((TextView) inflate.findViewById(R.id.lbl_Ok)).setTypeface(createFromAsset);
        ((TextView) inflate.findViewById(R.id.lbl_Cancel)).setTypeface(createFromAsset);
        final EditText editText = inflate.findViewById(R.id.txt_AlbumName);
        if (str.length() > 0) {
            editText.setText(str);
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            if (editText.getEditableText().toString().length() <= 0 || editText.getText().toString().trim().isEmpty()) {
                Toast.makeText(AudioPlayListActivity.this, R.string.lbl_Photos_Album_Create_Album_please_enter, Toast.LENGTH_SHORT).show();
                return;
            }
            albumName = editText.getEditableText().toString();
            if (new File(str2).exists()) {
                File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + albumName);
                if (file.exists()) {
                    Toast.makeText(AudioPlayListActivity.this, "\"" + albumName + "\" already exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file2 = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.AUDIOS + str);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (file2.renameTo(file)) {
                    AudioPlaylistGalleryMethods audioPlaylistGalleryMethods = new AudioPlaylistGalleryMethods();
                    audioPlaylistGalleryMethods.UpdatePlaylistInDatabase(AudioPlayListActivity.this, i, albumName);
                    Toast.makeText(AudioPlayListActivity.this, R.string.lbl_Photos_Album_Create_Album_Success_renamed, Toast.LENGTH_SHORT).show();
                    GetAlbumsFromDatabase();
                    bottomSheetDialog.dismiss();
                    ll_EditAlbum.setVisibility(View.GONE);
                }
            }
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            ll_EditAlbum.setVisibility(View.GONE);
            AudioPlayListActivity.isEditAudioAlbum = false;
            adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, position, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void DeleteALertDialog(final int i, final String str, final String str2) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.confirmation_message_box, null);
        bottomSheetDialog.setContentView(inflate);
        TextView textView = inflate.findViewById(R.id.tvmessagedialogtitle);
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "ebrima.ttf"));
        if (str.length() > 9) {
            textView.setText("Are you sure you want to delete " + str.substring(0, 8) + ".. including its data?");
        } else {
            textView.setText("Are you sure you want to delete " + str + " including its data?");
        }

        inflate.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            DeleteAlbum(i, str, str2);
            bottomSheetDialog.dismiss();
            ll_EditAlbum.setVisibility(View.GONE);
        });

        inflate.findViewById(R.id.ll_Cancel).setOnClickListener(view -> {
            AudioPlayListActivity.isEditAudioAlbum = false;
            ll_EditAlbum.setVisibility(View.GONE);
            adapter = new AudioPlayListAdapter(AudioPlayListActivity.this, 17367043, audioAlbums, position, AudioPlayListActivity.isEditAudioAlbum, AudioPlayListActivity.isGridView);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    public void DeleteAlbum(int i, String str, String str2) {
        File file = new File(str2);
        DeletePlayListFromDatabase(i);
        try {
            Utilities.DeleteAlbum(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DeletePlayListFromDatabase(int i) {
        AudioPlayListDAL audioPlayListDAL = new AudioPlayListDAL(this);
        try {
            audioPlayListDAL.OpenWrite();
            audioPlayListDAL.DeletePlayListById(i);
            Toast.makeText(this, R.string.lbl_delete_success, Toast.LENGTH_SHORT).show();
            GetAlbumsFromDatabase();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } catch (Throwable th) {
            audioPlayListDAL.close();
            throw th;
        }
        audioPlayListDAL.close();
    }

    @Override
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onPause() {
        ll_EditAlbum.setVisibility(View.GONE);
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            SecurityLocksSharedPreferences.GetObject(this).SetIsCameraOpenFromInApp(false);
            finish();
            System.exit(0);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        ll_EditAlbum.setVisibility(View.GONE);
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (isEditAudioAlbum) {
                SecurityLocksCommon.IsAppDeactive = false;
                isEditAudioAlbum = false;
                ll_EditAlbum.setVisibility(View.GONE);
                adapter = new AudioPlayListAdapter(this, 17367043, audioAlbums, 0, isEditAudioAlbum, isGridView);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, ActivityMain.class));
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
