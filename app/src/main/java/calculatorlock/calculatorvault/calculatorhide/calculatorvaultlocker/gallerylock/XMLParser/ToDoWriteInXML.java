package calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.XMLParser;

import android.app.Activity;
import android.util.Xml;
import android.widget.Toast;

import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.DB.ToDoDAL;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoPojo;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.Model.ToDoTask;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.R;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.common.Constants;
import calculatorlock.calculatorvault.calculatorhide.calculatorvaultlocker.gallerylock.storageoption.StorageOptionsCommon;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class ToDoWriteInXML {
    Constants constants;
    ToDoDAL dal;
    File newFile;
    File oldFile;
    String toDoName;

    public boolean saveToDoList(Activity activity, ToDoPojo toDoPojo, String str, boolean z) {
        this.constants = new Constants();
        this.dal = new ToDoDAL(activity);
        this.toDoName = toDoPojo.getTitle();
        File file = new File(StorageOptionsCommon.STORAGEPATH + StorageOptionsCommon.TODOLIST);
        if (!file.exists()) {
            file.mkdirs();
        }
        String absolutePath = file.getAbsolutePath();
        this.newFile = new File(absolutePath, this.toDoName + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        String absolutePath2 = file.getAbsolutePath();
        this.oldFile = new File(absolutePath2, str + StorageOptionsCommon.NOTES_FILE_EXTENSION);
        if (!z) {
            try {
                if (this.newFile.exists()) {
                    ToDoDAL toDoDAL = this.dal;
                    StringBuilder sb = new StringBuilder();
                    this.constants.getClass();
                    sb.append("SELECT \t     * \t\t\t\t\t\t   FROM  TableToDo WHERE ");
                    this.constants.getClass();
                    sb.append("ToDoName");
                    sb.append("='");
                    sb.append(this.toDoName);
                    sb.append("'");
                    if (toDoDAL.IsFileAlreadyExist(sb.toString())) {
                        Toast.makeText(activity, R.string.toast_exists, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
            try {
                this.newFile.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } else if (!this.toDoName.equals(str)) {
            if (this.oldFile.exists()) {
                this.oldFile.renameTo(this.newFile);
            }
        } else if (!this.newFile.exists()) {
            try {
                this.newFile.createNewFile();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        XmlSerializer newSerializer = Xml.newSerializer();
        StringWriter stringWriter = new StringWriter();
        try {
            newSerializer.setOutput(stringWriter);
            newSerializer.startDocument(null, true);
            newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            newSerializer.startTag(null, Tags.ToDoList.toString());
            newSerializer.startTag(null, Tags.ToDoTitle.toString());
            newSerializer.text(toDoPojo.getTitle());
            newSerializer.endTag(null, Tags.ToDoTitle.toString());
            newSerializer.startTag(null, Tags.ToDoColor.toString());
            newSerializer.text(toDoPojo.getTodoColor());
            newSerializer.endTag(null, Tags.ToDoColor.toString());
            newSerializer.startTag(null, Tags.ToDoDateCreated.toString());
            newSerializer.text(toDoPojo.getDateCreated());
            newSerializer.endTag(null, Tags.ToDoDateCreated.toString());
            newSerializer.startTag(null, Tags.ToDoDateModified.toString());
            newSerializer.text(toDoPojo.getDateModified());
            newSerializer.endTag(null, Tags.ToDoDateModified.toString());
            newSerializer.startTag(null, Tags.ToDoTasks.toString());
            for (ToDoTask next : toDoPojo.getToDoList()) {
                newSerializer.startTag(null, Tags.ToDoTask.toString()).attribute(null, Tags.isCompleted.toString(), String.valueOf(next.isChecked()));
                newSerializer.text(next.getToDo());
                newSerializer.endTag(null, Tags.ToDoTask.toString());
            }
            newSerializer.endTag(null, Tags.ToDoTasks.toString());
            newSerializer.endTag(null, Tags.ToDoList.toString());
            newSerializer.endDocument();
            newSerializer.flush();
            FileOutputStream fileOutputStream = new FileOutputStream(this.newFile);
            fileOutputStream.write(stringWriter.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e5) {
            e5.printStackTrace();
        }
        return true;
    }


    public enum Tags {
        ToDoList,
        ToDoTitle,
        ToDoColor,
        ToDoDateCreated,
        ToDoDateModified,
        ToDoTasks,
        ToDoTask,
        isCompleted
    }
}
