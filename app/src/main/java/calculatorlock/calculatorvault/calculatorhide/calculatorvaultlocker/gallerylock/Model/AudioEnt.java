package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model;

import android.graphics.Bitmap;

import java.io.File;

public class AudioEnt {
    private int _ISDCard;
    private String _audioName;
    private File _file;
    private int _folderId;
    private String _folderLockAudioLocation;
    private int _id;
    private Bitmap _image;
    private Boolean _isCheck;
    private String _modifiedDateTime;
    private String _originalAudioLocation;

    public int getId() {
        return this._id;
    }

    public void setId(int i) {
        this._id = i;
    }

    public String getAudioName() {
        return this._audioName;
    }

    public void setAudioName(String str) {
        this._audioName = str;
    }

    public String getFolderLockAudioLocation() {
        return this._folderLockAudioLocation;
    }

    public void setFolderLockAudioLocation(String str) {
        this._folderLockAudioLocation = str;
    }

    public String getOriginalAudioLocation() {
        return this._originalAudioLocation;
    }

    public void setOriginalAudioLocation(String str) {
        this._originalAudioLocation = str;
    }

    public boolean GetFileCheck() {
        return this._isCheck;
    }

    public void SetFileCheck(boolean z) {
        this._isCheck = z;
    }

    public File GetFile() {
        return this._file;
    }

    public void SetFile(File file) {
        this._file = file;
    }

    public int getPlayListId() {
        return this._folderId;
    }

    public void setPlayListId(int i) {
        this._folderId = i;
    }

    public Bitmap GetFileImage() {
        return this._image;
    }

    public void SetFileImage(Bitmap bitmap) {
        this._image = bitmap;
    }

    public int getISDCard() {
        return this._ISDCard;
    }

    public void setISDCard(int i) {
        this._ISDCard = i;
    }

    public String get_modifiedDateTime() {
        return this._modifiedDateTime;
    }

    public void set_modifiedDateTime(String str) {
        this._modifiedDateTime = str;
    }
}
