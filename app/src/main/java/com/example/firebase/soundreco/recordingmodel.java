package com.example.firebase.soundreco;

import java.io.File;

public class recordingmodel {
    File file;
    String starttime,totaltime,date;
    String recordingname;

    public recordingmodel(File file, String starttime, String stoptime, String date) {
        this.file = file;
        this.starttime = starttime;
        totaltime = stoptime;
        this.date = date;
    }

    public recordingmodel(File file, String starttime, String totaltime, String date, String recordingname) {
        this.file = file;
        this.starttime = starttime;
        this.totaltime = totaltime;
        this.date = date;
        this.recordingname = recordingname;
    }


    public File getFile() {
        return file;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public String getDate() {
        return date;
    }


    public String getRecordingname() {
        return recordingname;
    }
}
