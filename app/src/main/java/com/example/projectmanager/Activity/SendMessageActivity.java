package com.example.projectmanager.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.projectmanager.R;
import com.example.projectmanager.Utils.FilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.projectmanager.Activity.LoginActivity.HOST_NAME;
import static com.example.projectmanager.Activity.LoginActivity.USER_ID;
import static com.example.projectmanager.Activity.LoginActivity.USER_LEVEL;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = "SendMessageActivity";
    private static final int USER_RESULT_CODE = 20;
    private String selectedFilePath, FILE_NAME;
    private String SERVER_URL = HOST_NAME + "mitra/upload.php?";
    private LinearLayout layoutAttachmentBtn, layoutUploaded, layoutAttachBox;
    private Button bUpload, btnSubmit, btnDeleteFile, btnSelectUser;
    private TextView tvFileName, tvUploadedFileName, tvGetterUserName;
    private ProgressDialog dialog;
    private String mes;
    private String getterUserID = "0";
    private EditText edtMessageBox;
    private boolean file_exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        layoutAttachmentBtn = findViewById(R.id.attack_img_view);
        layoutAttachBox = findViewById(R.id.layout_attach);
        bUpload = findViewById(R.id.btn_upload_file);
        tvFileName = findViewById(R.id.tv_selected_file_name);
        layoutAttachmentBtn.setOnClickListener(this);
        bUpload.setOnClickListener(this);
        btnSubmit = findViewById(R.id.btn_send_message_submit);
        edtMessageBox = findViewById(R.id.edt_send_message_box);
        btnDeleteFile = findViewById(R.id.btn_delete_uploaded);
        tvUploadedFileName = findViewById(R.id.tv_uploaded_filename);
        layoutUploaded = findViewById(R.id.layout_uploaded_file);
        btnSelectUser = findViewById(R.id.btn_message_user_select);
        tvGetterUserName = findViewById(R.id.tv_send_message_title);

        if (getIntent().hasExtra("getterId")) {
            getterUserID = getIntent().getStringExtra("getterId");
            tvGetterUserName.setText("ارسال پیام به : " + getterUserID);
            btnSelectUser.setVisibility(View.GONE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtMessageBox.getText().length() < 2)
                    Toast.makeText(SendMessageActivity.this, "لطفا متن پیام را وارد کنید!", Toast.LENGTH_SHORT).show();
                else {
                    if (USER_LEVEL.equals("0") & getterUserID.equals("0"))
                        Toast.makeText(SendMessageActivity.this, "شما قادر به ارسال پیام عمومی نیستید لطفا یکی از مدیران را انتخاب کنید", Toast.LENGTH_LONG).show();
                    else
                        SendMessage();
                }
            }
        });

        btnDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteFile();
            }
        });

        btnSelectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SendMessageActivity.this, SelectUserActivity.class), USER_RESULT_CODE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == layoutAttachmentBtn) {

            if (ContextCompat.checkSelfPermission(SendMessageActivity.this
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SendMessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            } else
                showFileChooser();
        }
        if (v == bUpload) {

            //on upload button Click
            if (selectedFilePath != null) {
                dialog = ProgressDialog.show(SendMessageActivity.this, "", "Uploading File...", true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(selectedFilePath);
                    }
                }).start();
            } else {
                Toast.makeText(SendMessageActivity.this, "لطفا یک فایل انتخاب کنید", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void SendMessage() {
        if (file_exist) {
            AndroidNetworking.get(HOST_NAME + "mitra/AddMessage.php?userID={personnelCode}&message={messageString}" +
                    "&file_name={fileName}&getterID={getterId}")
                    .addPathParameter("personnelCode", USER_ID)
                    .addPathParameter("messageString", edtMessageBox.getText().toString())
                    .addPathParameter("fileName", FILE_NAME)
                    .addPathParameter("getterId", getterUserID)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("success").equals("1")) {
                                    Toast.makeText(SendMessageActivity.this, " " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("sendMessage", "onError: " + anError);
                            Log.e("sendMessage", "onError: " + anError.getResponse());

                        }
                    });
        } else {
            AndroidNetworking.get(HOST_NAME + "mitra/AddMessage.php?userID={personnelCode}&message={messageString}" +
                    "&getterID={getterId}")
                    .addPathParameter("personnelCode", USER_ID)
                    .addPathParameter("messageString", edtMessageBox.getText().toString())
                    .addPathParameter("getterId", getterUserID)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("success").equals("1")) {
                                    Toast.makeText(SendMessageActivity.this, " " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("sendMessage", "onError: " + anError);
                            Log.e("sendMessage", "onError: " + anError.getResponse());

                        }
                    });
        }
    }

    private void DeleteFile() {
        AndroidNetworking.get(HOST_NAME + "mitra/DeleteFile.php?file_name={filename}")
                .addPathParameter("filename", FILE_NAME)
                .setTag(this)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SendMessageActivity.this
                                , "فایل حذف شد.", Toast.LENGTH_SHORT).show();
                        layoutAttachBox.setVisibility(View.VISIBLE);
                        tvUploadedFileName.setVisibility(View.VISIBLE);
                        layoutUploaded.setVisibility(View.GONE);
                        tvFileName.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onResponse: " + response);
                        file_exist = false;
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("sendMessage", "onError: " + anError);
                        Log.e("sendMessage", "onError: " + anError.getResponse());

                    }
                });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    return;
                }

                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                Log.i(TAG, "Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    tvFileName.setText(selectedFilePath);
                } else {
                    Toast.makeText(this, "خطا در آپلود فایل", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == USER_RESULT_CODE) {
                String username = data.getStringExtra("username");
                getterUserID = data.getStringExtra("userId");
                tvGetterUserName.setText("ارسال پیام به : " + username);
                btnSelectUser.setVisibility(View.GONE);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }

    public int uploadFile(final String FilePath) {

        int serverResponseCode = 0;
        mes = "";
        file_exist = false;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(FilePath);


        String[] parts = FilePath.split("/");
        final String fileName = parts[parts.length - 1];
        FILE_NAME = fileName;

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvFileName.setText("Source File Doesn't Exist: " + FilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", FilePath);

                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + FilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
                BufferedReader buff = new BufferedReader(in);
                String line = null;

                do {
                    line = buff.readLine();
                    if (line != null)
                        mes += line;
                } while (line != null);
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SendMessageActivity.this, "File Upload completed ...", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "run: " + mes);
                            tvFileName.setVisibility(View.GONE);
                            layoutAttachBox.setVisibility(View.GONE);
                            tvUploadedFileName.setText(FILE_NAME);
                            layoutUploaded.setVisibility(View.VISIBLE);
                            selectedFilePath = "";
                            tvFileName.setText("");
                            file_exist = true;

                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(TAG, "uploadFile: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SendMessageActivity.this, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(SendMessageActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SendMessageActivity.this, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
