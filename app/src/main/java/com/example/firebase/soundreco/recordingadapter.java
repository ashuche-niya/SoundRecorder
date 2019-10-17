package com.example.firebase.soundreco;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class recordingadapter extends RecyclerView.Adapter<recordingadapter.recordingviewholder> {
    Context context;
    ArrayList<recordingmodel> fileArrayList=new ArrayList<>();
    LinearLayout linearLayout;
    OnItemClickListener onItemClickListener;
    OnLongClickListener onLongClickListener;
    public interface  OnLongClickListener {
        void onLongClick(View view,ArrayList<recordingmodel> fileArrayList,int position);
    }
    public void setOnLongClickListener(OnLongClickListener onLongClickListener)
    {
        this.onLongClickListener=onLongClickListener;
    }
    public interface  OnItemClickListener {
        void onItemClick(ArrayList<recordingmodel> fileArrayList,int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }

    public recordingadapter(Context context, ArrayList<recordingmodel> fileArrayList) {
        this.context = context;
        this.fileArrayList=fileArrayList;
    }

    public recordingadapter.recordingviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recordinglayout,viewGroup,false);
        return new recordingviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recordingadapter.recordingviewholder recordingviewholder, final int i) {
            recordingviewholder.recordingname.setText(fileArrayList.get(i).getRecordingname());


        recordingviewholder.recordedtime.setText(fileArrayList.get(i).getDate()+"   "+fileArrayList.get(i).getStarttime());
        recordingviewholder.recordingtime.setText(fileArrayList.get(i).getTotaltime()+" seconds");
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(fileArrayList,i);
                }
            }
        });
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickListener.onLongClick(v,fileArrayList,i);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileArrayList.size();
    }
    public class recordingviewholder extends RecyclerView.ViewHolder {
        TextView recordingname,recordingtime,recordedtime;

        public recordingviewholder(@NonNull View itemView) {
            super(itemView);
            recordingname=itemView.findViewById(R.id.recordingname);
            recordingtime=itemView.findViewById(R.id.recordingtotaltime);
            recordedtime=itemView.findViewById(R.id.recordedtime);
            linearLayout=itemView.findViewById(R.id.recordinglayout);


        }
    }
}
