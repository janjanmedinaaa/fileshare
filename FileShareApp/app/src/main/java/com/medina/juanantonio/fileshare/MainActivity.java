package com.medina.juanantonio.fileshare;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;

    private ImageButton restartButton;
    private ImageButton homeButton;
    private EditText searchInput;

    public static EditText editText;

    private ProgressBar progressBar;

    private String base_url;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private ArrayList<FileModel> fileList = new ArrayList<>();
    private ArrayList<FileModel> rawFileList = new ArrayList<>();

    public static String CLI_URL = "https://github.com/janjanmedinaaa/fileshare";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        restartButton = (ImageButton) findViewById(R.id.restartButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        searchInput = (EditText) findViewById(R.id.searchInput);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(new Wave());
        progressBar.setVisibility(View.INVISIBLE);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(base_url != null && !base_url.equals(""))
                    getFiles(base_url);
            }
        });
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<FileModel> results = Tools.searchResults(rawFileList, s.toString());

                if(fileList.size() != 0)
                    fileList.clear();

                fileList.clear();
                renderItems(results);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        showInputDialog();
        requestCamera();
    }

    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void getFiles(final String url) {
        progressBar.setVisibility(View.VISIBLE);

        FileShareRequestClient.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                fileList.clear();

                try {
                    fileList.clear();

//                    if(!url.equalsIgnoreCase(base_url)) {
//                        String previousURL = Tools.previousURL(url);
//                        String previousFolder = Tools.lastFolder(previousURL);
//
//                        fileList.add(new FileModel(
//                                "Back to " + previousFolder,
//                                "PREVIOUS","","",
//                                previousURL, true
//                        ));
//                    }

                    for (int a = 0; a < response.length(); a++) {
                        JSONObject file = response.getJSONObject(a);

                        String fileName = file.optString("name");
                        String fileType = file.optString("type");
                        String fileLDM = file.optString("modified");
                        String fileSource = file.optString("source");

                        int fileSize = file.optInt("size");

                        Boolean isFolder = file.optBoolean("folder");

                        FileModel fileModel = new FileModel(
                                fileName,
                                fileType,
                                Tools.formatDate(fileLDM),
                                Tools.getFileSize(fileSize),
                                fileSource,
                                isFolder
                        );

                        if(fileName.charAt(0) != '.') {
                            fileList.add(fileModel);
                            rawFileList.add(fileModel);
                        }
                    }

                    renderItems(fileList);
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("HTTP", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("HTTP", responseString);
            }
        });
    }

    private void renderItems(ArrayList<FileModel> fileItems) {
        fileAdapter = new FileAdapter(fileItems, getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItemClick(FileModel fileModel) {
                String fileURL = base_url + "/" + fileModel.getFileSource();

                if(fileModel.getIsFolder())
                    getFiles(fileURL);
                else if(fileModel.getFileIcon().equalsIgnoreCase("ic_image_white"))
                    showImageViewDialog(fileURL);
                else
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fileURL)));
            }
        });

        recyclerView.setAdapter(fileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        editText = (EditText) promptView.findViewById(R.id.textInput);
        final ImageButton qrButton = (ImageButton) promptView.findViewById(R.id.qrButton);
        final Button installButton = (Button) promptView.findViewById(R.id.installButton);

        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QRScanner.class));
            }
        });

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CLI_URL)));
            }
        });

        String lastURL = preferences.getString("lastURL", "");

        editText.setText(lastURL);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(Tools.urlCheck(editText.getText().toString())) {
                            base_url = editText.getText().toString();
                            getFiles(base_url);

                            editor.putString("lastURL", base_url);
                            editor.apply();

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid URL!", Toast.LENGTH_SHORT).show();
                            showInputDialog();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void showImageViewDialog(String imageUrl) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.image_dialog, null);

        final WebView webView = (WebView) dialogView.findViewById(R.id.webView);

        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadUrl(imageUrl);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}
