package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.extra;

import java.util.ArrayList;

public class AlbumFolder {
    private ArrayList<AlbumFile> albumFiles = new ArrayList<>();
    private String name = "";

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public ArrayList<AlbumFile> getAlbumFiles() {
        return this.albumFiles;
    }

    public void setAlbumFiles(AlbumFile albumFile) {
        this.albumFiles.add(albumFile);
    }
}
