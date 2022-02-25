package com.calculator.vaultlocker.XMLParser;

import com.calculator.vaultlocker.Model.ToDoPojo;
import com.calculator.vaultlocker.Model.ToDoTask;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ToDoReadFromXML {
    String CurrentTag = "";
    Boolean inDataTag = false;
    ToDoPojo toDoPojo;
    ToDoTask toDoTask;
    ArrayList<ToDoTask> toDoTasksList;

    public ToDoPojo ReadToDoList(String str) {
        try {
            File file = new File(str);
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new InputStreamReader(new ByteArrayInputStream(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8).getBytes())));
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2) {
                    String name = newPullParser.getName();
                    this.CurrentTag = name;
                    if (name.equals(Tags.ToDoList.toString())) {
                        this.inDataTag = true;
                        this.toDoPojo = new ToDoPojo();
                    } else if (this.CurrentTag.equals(Tags.ToDoTasks.toString())) {
                        this.toDoTasksList = new ArrayList<>();
                    } else if (this.CurrentTag.equals(Tags.ToDoTask.toString())) {
                        ToDoTask toDoTask = new ToDoTask();
                        this.toDoTask = toDoTask;
                        toDoTask.setChecked(Boolean.parseBoolean(newPullParser.getAttributeValue(null, Tags.isCompleted.toString())));
                    }
                } else if (eventType == 3) {
                    if (newPullParser.getName().equals(Tags.ToDoList.toString())) {
                        this.inDataTag = false;
                    }
                    this.CurrentTag = "";
                } else if (eventType == 4) {
                    if (this.inDataTag && this.toDoPojo != null) {
                        String str2 = this.CurrentTag;
                        switch (str2) {
                            case "ToDoTask":
                                this.toDoTask.setToDo(newPullParser.getText());
                                this.toDoTasksList.add(this.toDoTask);
                                this.toDoTask = null;
                                break;
                            case "ToDoTitle":
                                this.toDoPojo.setTitle(newPullParser.getText());
                                break;
                            case "ToDoDateModified":
                                this.toDoPojo.setDateModified(newPullParser.getText());
                                break;
                            case "ToDoDateCreated":
                                this.toDoPojo.setDateCreated(newPullParser.getText());
                                break;
                            case "ToDoColor":
                                this.toDoPojo.setTodoColor(newPullParser.getText());
                                break;
                        }
                    }
                }
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        this.toDoPojo.setToDoList(this.toDoTasksList);
        return this.toDoPojo;
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
