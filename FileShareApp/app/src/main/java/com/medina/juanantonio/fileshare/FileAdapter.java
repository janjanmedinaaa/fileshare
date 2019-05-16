package com.medina.juanantonio.fileshare;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private List<FileModel> fileModelList;
    private Context context;

    private ImageView fileIcon;
    private TextView fileName;
    private TextView fileLMD;

    private RelativeLayout relativeLayout;

    private final OnItemClickListener listener;

    public FileAdapter(List<FileModel> fileModelList, Context context, OnItemClickListener listener) {
        this.fileModelList = fileModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_layout, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final FileModel fileModel = fileModelList.get(i);

        fileIcon.setImageResource(context.getResources().getIdentifier(fileModel.getFileIcon(), "drawable", context.getPackageName()));

        if(fileModel.getFileType().equals("PREVIOUS")) {
            fileName.setText(fileModel.getFileName());
            fileLMD.setText("Previous URL: " + fileModel.getFileSource());
        } else {
            fileName.setText(fileModel.getFileName() + " (" + fileModel.getFileSize() + ")");
            fileLMD.setText("Last Modified Date: " + fileModel.getFileLMD());
        }

        if(fileModel.getIsFolder())
            relativeLayout.setBackgroundColor(Color.parseColor("#6F93E6"));
        else
            relativeLayout.setBackgroundColor(Color.parseColor("#DD997F"));

        viewHolder.bind(fileModelList.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return fileModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileIcon = (ImageView) itemView.findViewById(R.id.fileIcon);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            fileLMD = (TextView) itemView.findViewById(R.id.lastDateModified);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }

        public void bind(final FileModel fileModel, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(fileModel);
                }
            });
        }
    }
}
