package com.template.videotrimmer;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.template.R;
import com.template.databinding.ActivityTrimmerBinding;
import com.template.videotrimmer.interfaces.OnHgLVideoListener;
import com.template.videotrimmer.interfaces.OnTrimVideoListener;


public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnHgLVideoListener {

    //    private VideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;
    private static final int REQUEST_VIDEO_TRIMMER = 0x01;
    private static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    public static final String VIDEO_TOTAL_DURATION = "VIDEO_TOTAL_DURATION";
    public static final String FILE_PATH = "FILE_PATH";
    ActivityTrimmerBinding trimmerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trimmerBinding = ActivityTrimmerBinding.inflate(LayoutInflater.from(this));
        setContentView(trimmerBinding.getRoot());
        int maxDuration = 30;

        Intent extraIntent = getIntent();
        String path = "";

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(EXTRA_VIDEO_PATH);
            maxDuration = extraIntent.getIntExtra(VIDEO_TOTAL_DURATION, 30);
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.trimming_progress));

        if (trimmerBinding != null) {
            /**
             * get total duration of video file
             */
            Log.e("tg", "maxDuration = " + maxDuration);
            //mVideoTrimmer.setMaxDuration(maxDuration);
            trimmerBinding.timeLine.setMaxDuration(26);
            trimmerBinding.timeLine.setOnTrimVideoListener(this);
            trimmerBinding.timeLine.setOnHgLVideoListener(this);
            //trimmerBinding.timeLine.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            trimmerBinding.timeLine.setVideoURI(Uri.parse(path));
            trimmerBinding.timeLine.setVideoInformationVisibility(true);
        }

    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri contentUri) {
        mProgressDialog.cancel();

        runOnUiThread(() -> {
            // Toast.makeText(TrimmerActivity.this, getString(R.string.video_saved_at, contentUri.getPath()), Toast.LENGTH_SHORT).show();

        });
        try {
            String path = contentUri.getPath();
            Intent intent = new Intent();
            intent.putExtra(FILE_PATH, path);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(TrimmerActivity.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void playUriOnVLC(Uri uri) {

        int vlcRequestCode = 42;
        Intent vlcIntent = new Intent(Intent.ACTION_VIEW);
        vlcIntent.setPackage("org.videolan.vlc");
        vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
        vlcIntent.putExtra("title", "Kung Fury");
        vlcIntent.putExtra("from_start", false);
        vlcIntent.putExtra("position", 90000l);
        startActivityForResult(vlcIntent, vlcRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("tg", "resultCode = " + resultCode + " data " + data);
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        trimmerBinding.timeLine.destroy();
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(() -> {
            Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(() -> {
            // Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
        });
    }
}
