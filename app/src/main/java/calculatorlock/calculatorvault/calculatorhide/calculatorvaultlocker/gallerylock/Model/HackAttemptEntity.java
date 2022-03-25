package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model;


public class HackAttemptEntity {
    private String _LoginOption;
    private String _hackAttemptTime;
    private String _imagePath;
    private boolean _isCheck;
    private String _wrongPassword;

    public String GetLoginOption() {
        return this._LoginOption;
    }

    public void SetLoginOption(String str) {
        this._LoginOption = str;
    }

    public String GetWrongPassword() {
        return this._wrongPassword;
    }

    public void SetWrongPassword(String str) {
        this._wrongPassword = str;
    }

    public String GetImagePath() {
        return this._imagePath;
    }

    public void SetImagePath(String str) {
        this._imagePath = str;
    }

    public String GetHackAttemptTime() {
        return this._hackAttemptTime;
    }

    public void SetHackAttemptTime(String str) {
        this._hackAttemptTime = str;
    }

    public boolean GetIsCheck() {
        return this._isCheck;
    }

    public void SetIsCheck(Boolean bool) {
        this._isCheck = bool.booleanValue();
    }
}
