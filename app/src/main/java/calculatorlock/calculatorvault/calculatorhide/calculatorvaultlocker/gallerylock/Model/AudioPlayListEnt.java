package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model;


public class AudioPlayListEnt {
    private int _fileCount;
    private int _id;
    private String _modifiedDateTime;
    private String _playlistLocation;
    private String _playlistName;

    public int getId() {
        return this._id;
    }

    public void setId(int i) {
        this._id = i;
    }

    public String getPlayListName() {
        return this._playlistName;
    }

    public void setPlayListName(String str) {
        this._playlistName = str;
    }

    public String getPlayListLocation() {
        return this._playlistLocation;
    }

    public void setPlayListLocation(String str) {
        this._playlistLocation = str;
    }

    public int get_fileCount() {
        return this._fileCount;
    }

    public void set_fileCount(int i) {
        this._fileCount = i;
    }

    public String get_modifiedDateTime() {
        return this._modifiedDateTime;
    }

    public void set_modifiedDateTime(String str) {
        this._modifiedDateTime = str;
    }
}
