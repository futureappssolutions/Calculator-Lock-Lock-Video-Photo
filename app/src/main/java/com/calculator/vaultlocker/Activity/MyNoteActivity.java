package com.calculator.vaultlocker.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.calculator.vaultlocker.R;
import com.calculator.vaultlocker.common.Constants;
import com.calculator.vaultlocker.notes.AudioRecorder;
import com.calculator.vaultlocker.notes.LinedEditText;
import com.calculator.vaultlocker.notes.NotesCommon;
import com.calculator.vaultlocker.Model.NotesFileDB_Pojo;
import com.calculator.vaultlocker.DB.NotesFilesDAL;
import com.calculator.vaultlocker.XMLParser.ReadNoteFromXML;
import com.calculator.vaultlocker.panicswitch.AccelerometerListener;
import com.calculator.vaultlocker.panicswitch.AccelerometerManager;
import com.calculator.vaultlocker.panicswitch.PanicSwitchActivityMethods;
import com.calculator.vaultlocker.panicswitch.PanicSwitchCommon;
import com.calculator.vaultlocker.securitylocks.SecurityLocksCommon;
import com.calculator.vaultlocker.storageoption.StorageOptionsCommon;
import com.calculator.vaultlocker.utilities.Utilities;
import com.flask.colorpicker.ColorPickerView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MyNoteActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener, EasyPermissions.PermissionCallbacks {
    private final String[] PERMISSIONS = {"android.permission.RECORD_AUDIO"};
    private String NotesContent;
    private String notesTitle;
    private HashMap<String, String> NotesHashMap;
    private AudioRecorder audioRecorder;
    private Chronometer chronometer;
    private Constants constants;
    private LinedEditText et_NoteContent;
    private EditText et_Notetitle;
    private ImageView iv_NotesRecordAudio;
    private ImageView iv_NotesplayAudio;
    private ImageView iv_play;
    private ScrollView ll_main;
    private LinearLayout ll_notesRecordingPlayer;
    private Handler mHandler;
    private MediaPlayer mPlayer;
    private NotesCommon notesCommon;
    private NotesFilesDAL notesFilesDAL;
    private ReadNoteFromXML readNoteFromXML;
    private SeekBar seekbar;
    private TextView tv_recEndTime;
    private TextView tv_recStartTime;

    public Runnable mUpdateTimeTask = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            long totalDuration = mPlayer.getDuration();
            long currentDuration = mPlayer.getCurrentPosition();
            seekbar.setProgress(notesCommon.getProgressPercentage(currentDuration, totalDuration));
            tv_recEndTime.setText("" + notesCommon.milliSecondsToTimer(totalDuration));
            tv_recStartTime.setText("" + notesCommon.milliSecondsToTimer(currentDuration));
            mHandler.postDelayed(this, 100);
        }
    };
    private String audioString = "";
    private boolean hasInsertedLines = false;
    private boolean hasModified = false;
    private boolean isPlayerVisible = false;
    private boolean isPlaying = false;
    private boolean isRecording = false;
    private ProgressDialog myProgressDialog = null;
    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                et_Notetitle.setText(NotesHashMap.get("Title"));
                et_NoteContent.setText(NotesHashMap.get("Text"));
                if (audioString.length() > 0) {
                    iv_NotesplayAudio.setVisibility(View.VISIBLE);
                }
                hideProgress();
            } else if (message.what == 2) {
                hideProgress();
            } else if (message.what == 3) {
                Toast.makeText(MyNoteActivity.this, getResources().getString(R.string.note_file_exists), Toast.LENGTH_SHORT).show();
                Utilities.hideKeyboard(ll_main, MyNoteActivity.this);
            }
            super.handleMessage(message);
        }
    };
    private String noteColor = "#33aac0ff";
    private String oldNoteTitle = "";
    private String recordingFilePath = "";
    private SensorManager sensorManager;
    private Toolbar toolbar;

    @Override
    public void onAccelerationChanged(float f, float f2, float f3) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
    }

    private void showProgress() {
        myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setTitle(getResources().getString(R.string.please_wait));
        myProgressDialog.setMessage(getResources().getString(R.string.processing));
        myProgressDialog.show();
    }

    public void hideProgress() {
        if (myProgressDialog != null) {
            myProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_note);

        ImageView iv_previous = findViewById(R.id.iv_previous);
        ImageView iv_next = findViewById(R.id.iv_next);

        toolbar = findViewById(R.id.toolbar);
        et_Notetitle = findViewById(R.id.et_Notetitle);
        et_NoteContent = findViewById(R.id.et_NoteContent);
        tv_recStartTime = findViewById(R.id.tv_recStartTime);
        tv_recEndTime = findViewById(R.id.tv_recEndTime);
        iv_NotesplayAudio = findViewById(R.id.iv_NotesplayAudio);
        iv_NotesRecordAudio = findViewById(R.id.iv_NotesRecordAudio);
        iv_play = findViewById(R.id.iv_play);
        seekbar = findViewById(R.id.seekbar);
        ll_notesRecordingPlayer = findViewById(R.id.ll_notesRecordingPlayer);
        chronometer = findViewById(R.id.chronometer);
        ll_main = findViewById(R.id.ll_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        NotesHashMap = new HashMap<>();
        readNoteFromXML = new ReadNoteFromXML();
        notesCommon = new NotesCommon();
        audioRecorder = new AudioRecorder(this);
        mHandler = new Handler();
        notesFilesDAL = new NotesFilesDAL(this);
        constants = new Constants();

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorAppTheme));
        toolbar.setNavigationIcon(R.drawable.ic_top_back_icon);

        SecurityLocksCommon.IsAppDeactive = true;
        iv_NotesplayAudio.setVisibility(View.GONE);

        iv_NotesplayAudio.setOnClickListener(new PlayAudioListeners());
        iv_NotesRecordAudio.setOnClickListener(new RecordAudioListeners());
        iv_play.setOnClickListener(new AudioPlayerListener());
        iv_next.setOnClickListener(new AudioPlayerListener());
        iv_previous.setOnClickListener(new AudioPlayerListener());
        seekbar.setOnSeekBarChangeListener(new SeekBarListener());

        if (NotesCommon.isEdittingNote) {
            showProgress();
            new Thread() {
                @Override
                public void run() {
                    try {
                        initViewNote();
                        Message message = new Message();
                        message.what = 1;
                        handle.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            initAddNote();
        }

        new Handler().postDelayed(() -> {
            try {
                et_NoteContent.addTextChangedListener(new EditTextChangeListener());
                et_Notetitle.addTextChangedListener(new EditTextChangeListener());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500);
        requestPermission(PERMISSIONS);
    }

    public void initAddNote() {
        et_NoteContent = notesCommon.setEditTextFullPage(et_NoteContent);
        audioRecorder.hasRecording = false;
    }

    public void initViewNote() {
        HashMap<String, String> ReadNote = readNoteFromXML.ReadNote(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.NOTES + NotesCommon.CurrentNotesFolder + File.separator + NotesCommon.CurrentNotesFile + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        NotesHashMap = ReadNote;
        audioString = ReadNote.get("audioData");
        oldNoteTitle = NotesHashMap.get("Title");
        if (audioString.length() > 0) {
            audioRecorder.createRecordingFolder();
            audioRecorder.createFirstRecording();
            String absolutePath = audioRecorder.firstRecordingFile.getAbsolutePath();
            notesCommon.getDecodedAudio(audioString, absolutePath);
            recordingFilePath = absolutePath;
            audioRecorder.hasRecording = true;
            iv_NotesplayAudio.setVisibility(View.VISIBLE);
        }
        try {
            ll_main.setBackgroundColor(Color.parseColor(NotesHashMap.get("NoteColor")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RecordOrStop() {
        if (!isRecording) {
            isRecording = true;
            iv_NotesRecordAudio.setImageResource(R.drawable.recorder_active_icon);
            audioRecorder.StartRecording();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setVisibility(View.VISIBLE);
            chronometer.start();
            Toast.makeText(this, getResources().getString(R.string.recording_started), Toast.LENGTH_SHORT).show();
            return;
        }
        isRecording = false;
        hasModified = true;
        iv_NotesRecordAudio.setImageResource(R.drawable.ic_recorder_icon);
        recordingFilePath = audioRecorder.StopRecording();
        chronometer.stop();
        chronometer.setVisibility(View.INVISIBLE);
        iv_NotesplayAudio.clearAnimation();
        iv_NotesplayAudio.setVisibility(View.VISIBLE);
        Toast.makeText(this, getResources().getString(R.string.recording_stoped), Toast.LENGTH_SHORT).show();
        iv_NotesplayAudio.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_bounce));
    }

    public void DeleteExistingRecording() {
        File file = new File(recordingFilePath);
        if (!file.exists() || !file.delete()) {
            Toast.makeText(this, getResources().getString(R.string.recording_not_deleted), Toast.LENGTH_SHORT).show();
            return;
        }
        audioRecorder.hasRecording = false;
        recordingFilePath = "";
        iv_NotesplayAudio.clearAnimation();
        iv_NotesplayAudio.setVisibility(View.GONE);
        RecordOrStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_color, menu);
        toolbar.setTitle("");
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            switch (itemId) {
                case R.id.action_menu_add:
                    NotesContent = Objects.requireNonNull(et_NoteContent.getText()).toString();
                    notesTitle = et_Notetitle.getText().toString().trim();
                    if (!notesTitle.trim().equals("") && !notesCommon.hasNoData(NotesContent)) {
                        final String encodedAudio = notesCommon.getEncodedAudio(recordingFilePath);
                        final String currentDate = notesCommon.getCurrentDate();
                        if (notesTitle.equals("")) {
                            String[] split = NotesContent.trim().replaceAll("[!?,]", "").split("\\s+");
                            if (split.length > 0) {
                                notesTitle = split[0];
                            }
                            if (split.length > 1) {
                                notesTitle = notesTitle.concat(" " + split[1]);
                            }
                        }
                        if (!notesTitle.equals("") && notesCommon.isNoSpecialCharsInName(notesTitle)) {
                            showProgress();
                            new Thread() {
                                @Override
                                public void run() {
                                    if (NotesCommon.isEdittingNote) {
                                        StringBuilder sb = new StringBuilder();
                                        constants.getClass();
                                        sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ");
                                        constants.getClass();
                                        sb.append("NotesFileName");
                                        sb.append(" = '");
                                        sb.append(oldNoteTitle);
                                        sb.append("' AND ");
                                        constants.getClass();
                                        sb.append("NotesFileIsDecoy");
                                        sb.append(" = ");
                                        sb.append(SecurityLocksCommon.IsFakeAccount);
                                        NotesFileDB_Pojo notesFileInfoFromDatabase = notesFilesDAL.getNotesFileInfoFromDatabase(sb.toString());

                                        StringBuilder sb2 = new StringBuilder();
                                        constants.getClass();
                                        sb2.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ");
                                        constants.getClass();
                                        sb2.append("NotesFileName");
                                        sb2.append(" = '");
                                        sb2.append(notesTitle);
                                        sb2.append("' AND ");
                                        constants.getClass();
                                        sb2.append("NotesFileIsDecoy");
                                        sb2.append(" = ");
                                        sb2.append(SecurityLocksCommon.IsFakeAccount);
                                        NotesFileDB_Pojo notesFileInfoFromDatabase2 = notesFilesDAL.getNotesFileInfoFromDatabase(sb2.toString());

                                        int notesFileId = notesFileInfoFromDatabase.getNotesFileId();
                                        int notesFileId2 = notesFileInfoFromDatabase2.getNotesFileId();

                                        if (oldNoteTitle.equals(notesTitle)) {
                                            notesCommon.createNote(MyNoteActivity.this, noteColor, NotesContent, notesTitle, oldNoteTitle, encodedAudio, NotesHashMap.get("note_datetime_c"), currentDate, true);
                                        } else if (notesFileId == notesFileId2 || notesFileInfoFromDatabase2.getNotesFileName() == null) {
                                            NotesCommon notesCommon2 = notesCommon;
                                            notesCommon2.createNote(MyNoteActivity.this, noteColor, NotesContent, notesTitle, oldNoteTitle, encodedAudio, NotesHashMap.get("note_datetime_c"), currentDate, true);
                                        } else {
                                            Message message = new Message();
                                            message.what = 3;
                                            handle.sendMessage(message);
                                        }
                                    } else {
                                        StringBuilder sb3 = new StringBuilder();
                                        constants.getClass();
                                        sb3.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableNotesFile WHERE ");
                                        constants.getClass();
                                        sb3.append("NotesFileName");
                                        sb3.append(" = '");
                                        sb3.append(notesTitle);
                                        sb3.append("' AND ");
                                        constants.getClass();
                                        sb3.append("NotesFileIsDecoy");
                                        sb3.append(" = ");
                                        sb3.append(SecurityLocksCommon.IsFakeAccount);
                                        if (!notesFilesDAL.IsFileAlreadyExist(sb3.toString())) {
                                            notesCommon.createNote(MyNoteActivity.this, noteColor, NotesContent, notesTitle, oldNoteTitle, encodedAudio, currentDate, currentDate, false);
                                        } else {
                                            Message message2 = new Message();
                                            message2.what = 3;
                                            handle.sendMessage(message2);
                                        }
                                    }
                                    Message message3 = new Message();
                                    message3.what = 2;
                                    handle.sendMessage(message3);
                                }
                            }.start();
                        } else {
                            Utilities.hideKeyboard(ll_main, this);
                            Toast.makeText(this, R.string.empty_note_name, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Utilities.hideKeyboard(ll_main, this);
                        Toast.makeText(this, R.string.empty_note, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.action_menu_color:
                    setNoteColor();
                    break;
            }
        } else if (audioRecorder.hasRecording && isPlaying && isPlayerVisible) {
            ll_notesRecordingPlayer.setVisibility(View.GONE);
            isPlayerVisible = false;
            isPlaying = false;
            mPlayer.stop();
            mPlayer.release();
            mHandler.removeCallbacks(mUpdateTimeTask);
        } else if (isRecording || hasModified) {
            showDiscardDialog();
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, NotesFilesActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void setNoteColor() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.dialog_note_color_picker);
        final ColorPickerView colorPickerView = dialog.findViewById(R.id.color_picker_view);
        colorPickerView.setAlpha(0.5f);
        colorPickerView.setDensity(12);

        dialog.findViewById(R.id.tv_no).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.tv_yes).setOnClickListener(view -> {
            try {
                int selectedColor = colorPickerView.getSelectedColor();
                String str = "#33" + Integer.toHexString(selectedColor).substring(2);
                ll_main.setBackgroundColor(Color.parseColor(str));
                noteColor = str;
                hasModified = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public void showDiscardDialog() {
        final Dialog dialog = new Dialog(this, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.confirmation_message_box);
        ((TextView) dialog.findViewById(R.id.tvmessagedialogtitle)).setText(R.string.discard_changes);

        dialog.findViewById(R.id.ll_Ok).setOnClickListener(view -> {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(MyNoteActivity.this, NotesFilesActivity.class));
            finish();
            dialog.cancel();
        });

        dialog.findViewById(R.id.ll_Cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!NotesCommon.isEdittingNote && !hasInsertedLines) {
            notesCommon.setEditTextFullPage(et_NoteContent);
            hasInsertedLines = true;
        }
    }

    public void PlayPause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            iv_play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
        } else if (!mPlayer.isPlaying()) {
            mPlayer.start();
            iv_play.setBackgroundResource(R.drawable.pause);
        } else {
            mPlayer.start();
        }
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public void showRecordingOverrideDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.overwrite_audio));
        builder.setCancelable(true);

        builder.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
            iv_NotesplayAudio.clearAnimation();
            iv_NotesplayAudio.setVisibility(View.GONE);
            DeleteExistingRecording();
            dialogInterface.cancel();
        });

        builder.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> {
            RecordOrStop();
            dialogInterface.cancel();
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (audioRecorder.hasRecording && isPlaying && isPlayerVisible) {
            ll_notesRecordingPlayer.setVisibility(View.GONE);
            isPlayerVisible = false;
            isPlaying = false;
            mPlayer.stop();
            mPlayer.release();
            mHandler.removeCallbacks(mUpdateTimeTask);
        } else if (isRecording || hasModified) {
            showDiscardDialog();
        } else {
            SecurityLocksCommon.IsAppDeactive = false;
            startActivity(new Intent(this, NotesFilesActivity.class));
            finish();
            overridePendingTransition(17432576, 17432577);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();
        }
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(8), 3);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 8 && sensorEvent.values[0] == 0.0f && PanicSwitchCommon.IsPalmOnFaceOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @Override
    public void onShake(float f) {
        if (PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
            PanicSwitchActivityMethods.SwitchApp(this);
        }
    }

    @AfterPermissionGranted(123)
    private void requestPermission(String[] strArr) {
        SecurityLocksCommon.IsAppDeactive = false;
        if (!EasyPermissions.hasPermissions(this, strArr)) {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, 123, strArr).setRationale("For the best Calculator Vault, please Allow Permission").setPositiveButtonText("ok").setNegativeButtonText("").build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (iArr.length > 0 && iArr[0] == 0) {
            Toast.makeText(getApplicationContext(), "Permission is granted ", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.RECORD_AUDIO")) {
            String[] strArr2 = {"android.permission.RECORD_AUDIO"};
            if (EasyPermissions.hasPermissions(this, strArr2)) {
                Toast.makeText(this, "Permission  again...", Toast.LENGTH_SHORT).show();
            } else {
                EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, 123, strArr2).setRationale("For the best Calculator Vault experience, please Allow Permission").setPositiveButtonText("ok").setNegativeButtonText("").build());
            }
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
        }
    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public class AudioPlayerListener implements View.OnClickListener {
        public AudioPlayerListener() {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_next:
                    mPlayer.seekTo(notesCommon.progressToTimer(seekbar.getProgress(), mPlayer.getDuration()) + 2000);
                    updateProgressBar();
                    return;
                case R.id.iv_notesFolder:
                default:
                    return;
                case R.id.iv_play:
                    PlayPause();
                    return;
                case R.id.iv_previous:
                    mPlayer.seekTo(notesCommon.progressToTimer(seekbar.getProgress(), mPlayer.getDuration()) + NotificationManagerCompat.IMPORTANCE_UNSPECIFIED);
                    updateProgressBar();
            }
        }
    }

    public class EditTextChangeListener implements TextWatcher {
        public EditTextChangeListener() {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            hasModified = true;
        }
    }

    public class PlayAudioListeners implements View.OnClickListener {
        public PlayAudioListeners() {
        }

        @Override
        public void onClick(View view) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et_NoteContent.getWindowToken(), 0);
            if (!audioRecorder.hasRecording) {
                Toast.makeText(MyNoteActivity.this, "No recording to play", Toast.LENGTH_SHORT).show();
                Utilities.hideKeyboard(ll_main, MyNoteActivity.this);
            } else if (isPlaying || isPlayerVisible) {
                ll_notesRecordingPlayer.setVisibility(View.GONE);
                isPlayerVisible = false;
                isPlaying = false;
                mPlayer.stop();
                mPlayer.release();
                mHandler.removeCallbacks(mUpdateTimeTask);
            } else {
                try {
                    isPlaying = true;
                    iv_play.setBackgroundResource(R.drawable.pause);
                    ll_notesRecordingPlayer.setVisibility(View.VISIBLE);
                    isPlayerVisible = true;
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(recordingFilePath);
                    mPlayer.prepare();
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new RecordingCompleteListener());
                    seekbar.setProgress(0);
                    seekbar.setMax(100);
                    updateProgressBar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class RecordAudioListeners implements View.OnClickListener {
        public RecordAudioListeners() {
        }

        @Override
        public void onClick(View view) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et_NoteContent.getWindowToken(), 0);
            if (!audioRecorder.hasRecording || isRecording) {
                RecordOrStop();
            } else {
                showRecordingOverrideDialog();
            }
        }
    }

    public class RecordingCompleteListener implements MediaPlayer.OnCompletionListener {
        public RecordingCompleteListener() {
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            isPlaying = false;
            iv_play.setBackgroundResource(R.drawable.ic_play_circle_outline_black_24dp);
        }
    }

    public class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public SeekBarListener() {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(mUpdateTimeTask);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            mPlayer.seekTo(notesCommon.progressToTimer(seekBar.getProgress(), mPlayer.getDuration()));
            updateProgressBar();
        }
    }
}
