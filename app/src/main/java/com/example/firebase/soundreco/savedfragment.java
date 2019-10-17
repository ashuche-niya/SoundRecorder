package com.example.firebase.soundreco;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class savedfragment extends Fragment {


    public savedfragment() {
        // Required empty public constructor
    }

    SeekBar seekBar;
    ImageView btnplaypause;
    public Thread updateseekbar;
    RecyclerView recyclerView;
    String userinputnamestring, usernewstring;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    recordingadapter recordingadapter;
    File folderpath = new File(Environment.getExternalStorageDirectory() +
            File.separator + "/SoundReco");

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_savedfragment, container, false);
        mediaRecorder = new MediaRecorder();
        recyclerView = view.findViewById(R.id.recordingrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recordingadapter = new recordingadapter(getContext(), recordfragment.staticfilelist);
        recyclerView.setAdapter(recordingadapter);
        recordingadapter.notifyDataSetChanged();
        recordingadapter.setOnItemClickListener(new recordingadapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<recordingmodel> fileArrayList, int position) {
//                SeekBar seekBar;
//                ImageView btnplaypause;
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(getContext(), Uri.fromFile(fileArrayList.get(position).getFile()));
                mediaPlayer.start();
                AlertDialog.Builder alertnew = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.musiccustomdialog, null);
                seekBar = view1.findViewById(R.id.seekbar);
                btnplaypause = view1.findViewById(R.id.btnplaypause);
                alertnew.setView(view1);

                alertnew.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        dialog.dismiss();
                    }
                });
                btnplaypause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            btnplaypause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            mediaPlayer.pause();
                        } else {
                            btnplaypause.setImageResource(R.drawable.ic_pause_black_24dp);
                            mediaPlayer.start();
                        }
                    }
                });
                seekBar.setMax(mediaPlayer.getDuration());
                updateseekbar = new Thread() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            int totalduration = mediaPlayer.getDuration();
                            int currentduration = 0;
                            while (currentduration < totalduration) {
                                try {
                                    sleep(300);
                                    if (mediaPlayer != null && seekBar != null) {
                                        currentduration = mediaPlayer.getCurrentPosition();

                                        seekBar.setProgress(currentduration);
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                };
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                });
                updateseekbar.start();
                alertnew.show();
            }
        });
        recordingadapter.setOnLongClickListener(new recordingadapter.OnLongClickListener() {
            @Override
            public void onLongClick(View view, final ArrayList<recordingmodel> fileArrayList, final int position) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View mview = getLayoutInflater().inflate(R.layout.customdialog, null);
                TextView renamefile, deletefile;
                renamefile = mview.findViewById(R.id.renamefiledialog);
                deletefile = mview.findViewById(R.id.deletefiledialog);
                alert.setView(mview)
                        .setTitle("Options")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                deletefile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileArrayList.get(position).getFile().delete();
                        fileArrayList.remove(position);
//                        recordingadapter.notifyItemRemoved(position);
                        alertDialog.dismiss();
                        recordfragment.staticfilelist.clear();
                        recordfragment.staticfilelist.addAll(fileArrayList);
                        // recordingadapter.notifyItemRemoved(position);
                        recordingadapter.notifyDataSetChanged();
                        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("sharedPre", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(recordfragment.staticfilelist);
                        editor.putString("task list7", json);
                        editor.putInt("text7", recordfragment.staticfileno);
                        editor.apply();

                    }
                });
                renamefile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert1 = new AlertDialog.Builder(getContext());
                        View view1 = getLayoutInflater().inflate(R.layout.renamedialog, null);
                        final EditText userinputname;
                        userinputname = view1.findViewById(R.id.userinputname);
                        alert1.setView(view1)
                                .setTitle("Rename File")
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alert1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userinputnamestring = userinputname.getText().toString();
                                usernewstring = userinputnamestring + ".3gp";
                                dialog.dismiss();
                                alertDialog.dismiss();
                                String newfilename = "/" + userinputnamestring + ".3gp";
                                File file = new File(folderpath, newfilename);
                                fileArrayList.get(position).getFile().renameTo(new File(folderpath, newfilename));
                                fileArrayList.set(position, new recordingmodel(file, fileArrayList.get(position).getStarttime(),
                                        fileArrayList.get(position).getTotaltime(), fileArrayList.get(position).getDate(), usernewstring));
//                                fileArrayList.set(position,new recordingmodel(file,fileArrayList.get(position).getStarttime(),
//                                       fileArrayList.get(position).getTotaltime(),fileArrayList.get(position).getDate(),userinputnamestring));
                                recordfragment.staticfilelist.clear();
                                recordfragment.staticfilelist.addAll(fileArrayList);
                                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("sharedPre", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(recordfragment.staticfilelist);
                                editor.putString("task list7", json);
                                editor.putInt("text7", recordfragment.staticfileno);
                                editor.apply();
                                recordingadapter.notifyItemChanged(position);

                            }
                        });

                        alert1.show();

                    }
                });

                alertDialog.show();


            }
        });
//        seekBar.setMax(mediaPlayer.getDuration());
//        updateseekbar = new Thread() {
//            @Override
//            public void run() {
//                if (mediaPlayer != null) {
//                    int totalduration = mediaPlayer.getDuration();
//                    int currentduration = 0;
//                    while (currentduration < totalduration) {
//                        try {
//                            sleep(300);
//                            if (mediaPlayer != null && seekBar != null) {
//                                currentduration = mediaPlayer.getCurrentPosition();
//
//                                seekBar.setProgress(currentduration);
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//            }
//        };
        return view;
    }

}
