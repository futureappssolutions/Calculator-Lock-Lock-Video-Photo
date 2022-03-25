package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.notes;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.Objects;

public class AudioRecorder {
    public static boolean isRecordStarted = false;
    public boolean hasRecording = false;
    public MediaRecorder recorder;
    public File firstRecordingFile;
    public File recordingfolder;
    public File secondRecordingFile;
    public File tempRecordingFile;
    public String Recording = "Recordings/";
    public String RecordingFileExtension = ".m4a";
    public String firstRecording = "firstRecording";
    public String recordingFolderPath = "";
    public String secondRecording = "secondRecording";
    public String tempRecording = "tempRecording";
    public Context context;

    public AudioRecorder(Context context) {
        this.context = context;
        createRecordingFolder();
    }

    public void StartRecording() {
        try {
            this.recorder = new MediaRecorder();
            recorder.reset();
            this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            if (!this.hasRecording) {
                createFirstRecording();
                this.recorder.setOutputFile(this.firstRecordingFile.getAbsolutePath());
            } else {
                createSecondRecording();
                this.recorder.setOutputFile(this.secondRecordingFile.getAbsolutePath());
            }
            this.recorder.prepare();
            this.recorder.start();
            isRecordStarted = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("masl", e.toString());
        }
    }

    public String StopRecording() {
        if (isRecordStarted) {
            isRecordStarted = false;
            this.recorder.stop();
            this.recorder.reset();
            this.recorder.release();
            this.recorder = null;
            if (!this.hasRecording) {
                this.hasRecording = true;
            } else if (this.firstRecordingFile.exists() && this.firstRecordingFile != null && this.secondRecordingFile.exists() && this.secondRecordingFile != null) {
                return mergeM4Afiles(this.context);
            }
        }
        return Objects.requireNonNull(this.firstRecordingFile).getAbsolutePath();
    }

    public String mergeM4Afiles(Context context) {
        try {
            Movie movie = new Movie();
            Movie[] movieArr = {MovieCreator.build(this.firstRecordingFile.getAbsolutePath()), MovieCreator.build(this.secondRecordingFile.getAbsolutePath())};
            LinkedList<Track> linkedList = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                for (Track track : movieArr[i].getTracks()) {
                    if (track.getHandler().equals("soun")) {
                        linkedList.add(track);
                    }
                }
            }
            if (linkedList.size() > 0) {
                movie.addTrack(new AppendTrack(linkedList.toArray(new Track[0])));
            }
            Container build = new DefaultMp4Builder().build(movie);
            createTempRecording();
            FileChannel channel = new RandomAccessFile(this.tempRecordingFile, "rw").getChannel();
            build.writeContainer(channel);
            channel.close();
            deleteFirstRecording();
            createFirstRecording();
            FileUtils.copyFile(this.tempRecordingFile, this.firstRecordingFile);
            deleteTempRecording();
            return this.firstRecordingFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void createRecordingFolder() {
        this.recordingFolderPath = this.context.getFilesDir() + File.separator + this.Recording;
        this.recordingfolder = new File(this.context.getFilesDir(), this.Recording);
        this.recordingFolderPath = recordingfolder.getAbsolutePath();
        if (!this.recordingfolder.exists()) {
            this.recordingfolder.mkdirs();
        }
    }

    public void createFirstRecording() {
        try {
            this.firstRecordingFile = new File(recordingFolderPath, this.firstRecording + this.RecordingFileExtension);
            if (!firstRecordingFile.exists()) {
                this.firstRecordingFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSecondRecording() {
        try {
            this.secondRecordingFile = new File(recordingFolderPath, this.secondRecording + this.RecordingFileExtension);
            if (!secondRecordingFile.exists()) {
                this.secondRecordingFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTempRecording() {
        try {
            this.tempRecordingFile = new File(recordingFolderPath, this.tempRecording + this.RecordingFileExtension);
            if (tempRecordingFile.exists()) {
                this.tempRecordingFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFirstRecording() {
        if (firstRecordingFile != null && firstRecordingFile.exists()) {
            this.firstRecordingFile.delete();
        }
    }

    public void deleteSecondRecording() {
        if (secondRecordingFile != null && secondRecordingFile.exists()) {
            this.secondRecordingFile.delete();
        }
    }

    public void deleteTempRecording() {
        if (tempRecordingFile != null && tempRecordingFile.exists()) {
            this.tempRecordingFile.delete();
        }
    }
}
