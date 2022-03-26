package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.extra;

import java.io.Serializable;

public class AlbumFile implements Serializable {
    public boolean isChecked;
    private String name = "";
    private String path = "";

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }
}
