package example.pnc.msshin.transferftpsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private ProcessFTP mProcessFTP;
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mSendBtn = findViewById(R.id.send);
        mSendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                transferFTP(
                        "",
                        "",
                        "",
                        "",
                        "");
                break;
        }
    }

    private void transferFTP(String url, String id, String pw, final String transFilePath, final String toFolderPath){
        if (mProcessFTP == null || !mProcessFTP.isProcessing()) {
            mProcessFTP = new ProcessFTP(url, id, pw);
            mProcessFTP.connect(new ProcessFTP.OnEventListener() {
                @Override
                public void onResult(int resultCode, String addString) {
                    Log.d(TAG, "connect() onResult - resultCode : " + resultCode + ", addString : " + addString);
                    if (resultCode == ProcessFTP.EVENT_SUCCESS) {
                        if (mProcessFTP != null) {
                            mProcessFTP.upload(new ProcessFTP.OnEventListener() {
                                @Override
                                public void onResult(int resultCode, String addString) {
                                    Log.d(TAG, "upload() onResult - resultCode : " + resultCode + ", addString : " + addString);
                                    if (resultCode == ProcessFTP.EVENT_SUCCESS) {
                                        Log.d(TAG, "transfer success");
                                    }
                                    if (mProcessFTP != null) {
                                        mProcessFTP.disconnect(new ProcessFTP.OnEventListener() {
                                            @Override
                                            public void onResult(int resultCode, String addString) {
                                                Log.d(TAG, "disconnect() onResult - resultCode : " + resultCode + ", addString : " + addString);
                                            }
                                        });
                                    }
                                }
                            }, transFilePath, toFolderPath);
                        }
                    }
                }
            });
        }
    }
}