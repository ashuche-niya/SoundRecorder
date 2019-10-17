package com.example.firebase.soundreco;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class recordfragment extends Fragment {


    public recordfragment() {
        // Required empty public constructor
    }
    private Chronometer mChronometer;
    String recordingname,recordingnewname;
    private long lastPause;
    String starttime,date,stoptime;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    FloatingActionButton btnrecord;
    TextView frontshowntext;
    Button btnstoprecord,btnplay,btnstop;
    //ArrayList<String> pathsave=new ArrayList<>();
//    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPre";
    //public static final String TEXT = "text";
    public static final String TEXT = "text7";
    String pathsave;
    public static final int RECORD_AUDIO = 0;
   // File folderpath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//    File file=new File(folderpath,"/recording1.3gp");
     File folderpath = new File(Environment.getExternalStorageDirectory() +
        File.separator + "/SoundReco");
    boolean success = true;

    public static int staticfileno=0;
    ArrayList<recordingmodel> fileArrayList=new ArrayList<>();
    public static ArrayList<recordingmodel> staticfilelist=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.recordfragment, container, false);
        btnrecord=view.findViewById(R.id.recordbutton);
        mChronometer =view.findViewById(R.id.chronometer);
        frontshowntext=view.findViewById(R.id.frontshowntext);
        if (!folderpath.exists()) {
            folderpath.mkdirs();
        }

//        btnplay=view.findViewById(R.id.playrecording);
//        btnstop=view.findViewById(R.id.stopplaying);
//        btnstoprecord=view.findViewById(R.id.stoprcording);
        //loaddata();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        staticfileno=sharedPreferences.getInt(TEXT,0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list7", null);
        Type type = new TypeToken<ArrayList<recordingmodel>>() {}.getType();
        staticfilelist = gson.fromJson(json, type);
        if(staticfilelist==null)
        {
            staticfilelist=new ArrayList<>();
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);

        btnrecord.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(mediaRecorder==null) {
                    recordvoice();
                    frontshowntext.setText("Recording...");
                    Toast.makeText(getContext(), "recording...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    stoprecording();
                    frontshowntext.setText("Tab button to start Recording");
                    Toast.makeText(getContext(), "recordingstop..", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        btnstoprecord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stoprecording();
//                Toast.makeText(getContext(), "recordingstop..", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //savedata();
//        SharedPreferences sharedPreferences1 =getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences1.edit();
//        editor.putInt(TEXT,staticfileno);
//        editor.apply();

        return view;
    }

@RequiresApi(api = Build.VERSION_CODES.O)
public void recordvoice()
{
    mediaRecorder=new MediaRecorder();
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//    File folderpath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//    File file=new File(folderpath,"/recording1.3gp");
    recordingname="/recording"+(staticfileno+1)+".3gp";
    recordingnewname="recording"+(staticfileno+1)+".3gp";
    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
    starttime=simpleDateFormat.format(calendar.getTime());
    date= DateFormat.getDateInstance().format(calendar.getTime());
    fileArrayList.add(new recordingmodel(new File(folderpath,recordingname),starttime,stoptime,date));
//    staticfilelist.add(new recordingmodel(new File(folderpath,recordingname),starttime,stoptime,date));
    mediaRecorder.setOutputFile(fileArrayList.get(fileArrayList.size()-1).getFile());
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    try {
        mediaRecorder.prepare();

        mediaRecorder.start();
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    } catch (IOException e) {
        e.printStackTrace();
    }
    staticfileno++;
//    SharedPreferences sharedPreferences1 =getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPreferences1.edit();
//    Gson gson = new Gson();
//    String json = gson.toJson(staticfilelist);
//    editor.putString("task list", json);
//    editor.putInt(TEXT,staticfileno);
//    editor.apply();
}
public void stoprecording()
{
//    Calendar calendar=Calendar.getInstance();
//    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
//    stoptime=simpleDateFormat.format(calendar.getTime());
    if (mediaRecorder!=null) {
        lastPause = SystemClock.elapsedRealtime() - mChronometer.getBase();
        mChronometer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
    }
    double timeinsecond=lastPause/1000;
    double timeinminute;
    String totoaltime=String.valueOf(timeinsecond);
    staticfilelist.add(new recordingmodel(new File(folderpath,recordingname),starttime,totoaltime,date,recordingnewname));
    SharedPreferences sharedPreferences1 =getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences1.edit();
    Gson gson = new Gson();
    String json = gson.toJson(staticfilelist);
    editor.putString("task list7", json);
    editor.putInt(TEXT,staticfileno);
    editor.apply();
    btnrecord.setImageResource(R.drawable.ic_stop_black_24dp);
}
//public void savedata()
//{
//    SharedPreferences sharedPreferences =getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPreferences.edit();
//    editor.putInt(TEXT,staticfileno);
//    editor.apply();
//}
//public void loaddata()
//{
//    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//    staticfileno=sharedPreferences.getInt(TEXT,0);
//}
}
