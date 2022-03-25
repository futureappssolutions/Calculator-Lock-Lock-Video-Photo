package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Flaes;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.AudioEnt;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.AudioDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.securitylocks.SecurityLocksCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.utilities.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AudioPlayerActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private final Handler mHandler = new Handler();
    private boolean IsStop = false;
    private ArrayList<AudioEnt> audioEntList = new ArrayList<>();
    private ProgressDialog AudioDecrytionDialog;
    private SeekBar audioProgressBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private TextView txtSongName;
    private LinearLayout btnPlayerPlayPause;
    private String AppPath = "";

    Handler handle = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                hideAudioDecrytionProgress();
                File file = new File(AppPath);
                DecrpytAudioFile(file, new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.AUDIOS_TEMP_FOLDER + audioEntList.get(Common.CurrentTrackIndex).getAudioName()), Common.CurrentTrackIndex);
            }
            super.handleMessage(message);
        }
    };

    private final Runnable mUpdateTimeTask = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            long duration = Common.mediaplayer.getDuration();
            long currentPosition = Common.mediaplayer.getCurrentPosition();
            songTotalDurationLabel.setText("" + Common.milliSecondsToTimer(duration));
            songCurrentDurationLabel.setText("" + Common.milliSecondsToTimer(currentPosition));
            int progressPercentage = Common.getProgressPercentage(currentPosition, duration);
            audioProgressBar.setProgress(progressPercentage);
            if (progressPercentage != 100) {
                mHandler.postDelayed(this, 100);
            } else if (Common.CurrentTrackIndex < audioEntList.size() - 1) {
                PlaySong(Common.CurrentTrackIndex + 1);
                Common.CurrentTrackIndex++;
                if (audioEntList.get(Common.CurrentTrackIndex).getAudioName().length() > 20) {
                    txtSongName.setText(audioEntList.get(Common.CurrentTrackIndex).getAudioName().substring(0, 19));
                } else {
                    txtSongName.setText(audioEntList.get(Common.CurrentTrackIndex).getAudioName());
                }
            } else {
                PlaySong(0);
                Common.CurrentTrackIndex = 0;
                if (audioEntList.get(Common.CurrentTrackIndex).getAudioName().length() > 20) {
                    txtSongName.setText(audioEntList.get(Common.CurrentTrackIndex).getAudioName().substring(0, 19));
                } else {
                    txtSongName.setText(audioEntList.get(Common.CurrentTrackIndex).getAudioName());
                }
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
    }

    private void showAudioDecryptionProgress() {
        AudioDecrytionDialog = ProgressDialog.show(this, null, "Audio Decryption, \nPlease wait your audio file is being decrypted...", true);
    }

    public void hideAudioDecrytionProgress() {
        if (AudioDecrytionDialog != null && AudioDecrytionDialog.isShowing()) {
            AudioDecrytionDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_audioplayer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Audio Player");
        toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);

        txtSongName = findViewById(R.id.txtSongTitle);
        btnPlayerPlayPause = findViewById(R.id.llPlayerPlayPause);
        audioProgressBar = findViewById(R.id.audioProgressbar);
        songCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = findViewById(R.id.songTotalDurationLabel);

        SecurityLocksCommon.IsAppDeactive = true;

        if (Common.voiceplayer.isPlaying()) {
            Common.voiceplayer.stop();
        }

        audioProgressBar.setOnSeekBarChangeListener(this);
        Common.mediaplayer.setOnCompletionListener(this);

        AudioDAL audioDAL = new AudioDAL(this);
        audioDAL.OpenRead();
        audioEntList = (ArrayList) audioDAL.GetAudiosByAlbumId(Common.FolderId, Common.sortType);
        audioDAL.close();

        if (Common.CurrentTrackId == audioEntList.get(Common.CurrentTrackNextIndex).getId() && Common.mediaplayer.isPlaying() && Common.CurrentTrackIndex == Common.CurrentTrackNextIndex) {
            if (audioEntList.get(Common.CurrentTrackIndex).getAudioName().length() > 20) {
                txtSongName.setText(audioEntList.get(Common.CurrentTrackIndex).getAudioName().substring(0, 19));
            } else {
                txtSongName.setText(audioEntList.get(Common.CurrentTrackIndex).getAudioName());
            }
            updateProgressBar();
            return;
        }

        Common.CurrentTrackIndex = Common.CurrentTrackNextIndex;
        PlaySong(Common.CurrentTrackIndex);
    }

    public void btnPlayerPlayPauseClick(View view) {
        if (Common.mediaplayer.isPlaying()) {
            Common.mediaplayer.pause();
            btnPlayerPlayPause.setBackgroundResource(R.drawable.btn_play);
        } else if (Common.mediaplayer.isPlaying() || !IsStop) {
            Common.mediaplayer.start();
            btnPlayerPlayPause.setBackgroundResource(R.drawable.btn_pause);
        } else {
            IsStop = false;
            PlaySong(Common.CurrentTrackIndex);
            btnPlayerPlayPause.setBackgroundResource(R.drawable.btn_pause);
        }
    }

    public void btnPlayerPreviousTrackClick(View view) {
        btnPlayerPlayPause.setBackgroundResource(R.drawable.btn_pause);
        if (Common.CurrentTrackIndex > 0) {
            PlaySong(Common.CurrentTrackIndex - 1);
            Common.CurrentTrackIndex--;
            return;
        }
        PlaySong(audioEntList.size() - 1);
        Common.CurrentTrackIndex = audioEntList.size() - 1;
    }

    public String FileName(String str) {
        for (int length = str.length() - 1; length > 0; length--) {
            if (str.charAt(length) == " /".charAt(1)) {
                return str.substring(length + 1);
            }
        }
        return "";
    }

    public void btnPlayerForwardTrackClick(View view) {
        btnPlayerPlayPause.setBackgroundResource(R.drawable.btn_pause);
        if (Common.CurrentTrackIndex < audioEntList.size() - 1) {
            PlaySong(Common.CurrentTrackIndex + 1);
            Common.CurrentTrackIndex++;
            return;
        }
        PlaySong(0);
        Common.CurrentTrackIndex = 0;
    }

    public void PlaySong(int i) {
        AppPath = audioEntList.get(i).getFolderLockAudioLocation();
        final File file = new File(AppPath);
        final File file2 = new File(StorageOptionsCommon.STORAGEPATH + "/" + StorageOptionsCommon.AUDIOS_TEMP_FOLDER + audioEntList.get(i).getAudioName());
        File file3 = new File(Objects.requireNonNull(file2.getParent()));
        if (!file3.exists()) {
            file3.mkdirs();
        }

        if (file.exists()) {
            if (!file2.exists()) {
                try {
                    file2.createNewFile();
                    showAudioDecryptionProgress();
                    new Thread() {
                        @Override
                        public void run() {
                            Flaes.decryptUsingCipherStream_AES128(file, file2);
                            Message message = new Message();
                            message.what = 1;
                            handle.sendMessage(message);
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file2);
                    Common.mediaplayer.stop();
                    Common.mediaplayer.reset();
                    Common.mediaplayer.setDataSource(fileInputStream.getFD());
                    Common.mediaplayer.prepare();
                    Common.mediaplayer.start();
                    updateProgressBar();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void DecrpytAudioFile(File file, File file2, int i) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file2);
            Common.mediaplayer.stop();
            Common.mediaplayer.reset();
            Common.mediaplayer.setDataSource(fileInputStream.getFD());
            Common.mediaplayer.prepare();
            Common.mediaplayer.start();

            if (audioEntList.get(i).getAudioName().length() > 20) {
                txtSongName.setText(audioEntList.get(i).getAudioName().substring(0, 19));
            } else {
                txtSongName.setText(audioEntList.get(i).getAudioName());
            }

            Common.CurrentTrackId = audioEntList.get(i).getId();
            audioProgressBar.setProgress(0);
            audioProgressBar.setMax(100);
            updateProgressBar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (Common.CurrentTrackIndex < audioEntList.size() - 1) {
            PlaySong(Common.CurrentTrackIndex + 1);
            Common.CurrentTrackIndex++;
            return;
        }
        PlaySong(0);
        Common.CurrentTrackIndex = 0;
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        Common.mediaplayer.seekTo(Common.progressToTimer(seekBar.getProgress(), Common.mediaplayer.getDuration()));
        updateProgressBar();
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            Back();
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        Back();
        return true;
    }

    private void Back() {
        SecurityLocksCommon.IsAppDeactive = false;
        startActivity(new Intent(this, AudioActivity.class));
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (SecurityLocksCommon.IsAppDeactive) {
            finish();
            System.exit(0);
        }
    }
}