package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model;

import android.widget.EditText;
import android.widget.TextView;


public class WalletEntryItemsPojo {
    private EditText editText;
    private boolean isSecured;
    private TextView textView;

    public boolean isSecured() {
        return this.isSecured;
    }

    public void setSecured(boolean z) {
        this.isSecured = z;
    }

    public EditText getEditText() {
        return this.editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
