package sspu.qiu.aichat.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


//Spark-v3.5
import com.iflytek.sparkchain.core.LLM;
import com.iflytek.sparkchain.core.LLMConfig;
import com.iflytek.sparkchain.core.LLMOutput;
import com.iflytek.sparkchain.core.SparkChain;
import com.iflytek.sparkchain.core.SparkChainConfig;
import com.iflytek.sparkchain.utils.AESUtil;

import java.security.PrivilegedAction;
import java.util.ArrayList;


//TTS
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import kotlinx.coroutines.sync.Mutex;
import sspu.qiu.aichat.R;


public class SpeechRecognitionActivity extends AppCompatActivity {

    private SpeechRecognizer                STT;                //
    private RecognizerDialog                STT_Dialog;         //
    private HashMap<String, String>         STT_Results;        //

    public SparkChainConfig                 SC_USER_cfg;        //
    public LLMConfig                        SC_LLM_cfg;         //
    public LLMOutput                        SC_LLM_Result;      //
    public LLM                              SC_LLM;             //

    private TextView                        TX;
    private TextView                        RX;
    private EditText                        ETX;
    private TextView                        STT_RES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);

        TX  = findViewById(R.id.TX);
        RX  = findViewById(R.id.RX);
        ETX = findViewById(R.id.ETX);
        STT_RES = findViewById(R.id.STT_RES);

        Android_Permission_init();
//        SC_cfg_init();
        STT_cfg_init();
    }



    //Start Spark demo
    public void B_1_C(View view){
        RX.append("\n");
        String text = ETX.getText().toString();
        SC_LLM_Result = SC_LLM.run(text);
        if(SC_LLM_Result.getErrCode()==0){
            RX.append(SC_LLM_Result.getContent());
        }else{
            RX.setText("ERROR");
        }
    }

    //Android permission ask for
    private void Android_Permission_init(){
        ArrayList<String> NOPER_List = new ArrayList<String>();
        String              tempList[];
        String              Perm[]     = {android.Manifest.permission.RECORD_AUDIO,              //录音权限
                android.Manifest.permission.ACCESS_NETWORK_STATE,      //络连接信息权限
                android.Manifest.permission.INTERNET,                  //连网权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};   //应用写入设备的外部存储

        //is permission granted ? otherwise add to 'List'
        for(String P : Perm)
            if(PackageManager.PERMISSION_GRANTED!= ContextCompat.checkSelfPermission(this,P))
                NOPER_List.add(P);

        tempList = new String[NOPER_List.size()];
        if(!NOPER_List.isEmpty())
            ActivityCompat.requestPermissions(this,NOPER_List.toArray(tempList),123);

    }


    //TTS part
    //STT cfg init
    public void STT_cfg_init(){
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=982080ac");                       //Create STT server,where the SDK package is deeply bound to this APPID parameter
        STT = SpeechRecognizer.createRecognizer(SpeechRecognitionActivity.this, mInitListener);                  //Initialize STT server
        STT_params_init();                                                                          //Initialize STT params
        STT_Results = new LinkedHashMap<String, String>();                                          //Initialize Hashmap
        STT_Dialog = new RecognizerDialog(SpeechRecognitionActivity.this, mInitListener);
    }

    //STT start button
    public void B_2_C(View v) {
        STT_Results.clear();                                    //Clean dat
        STT_Dialog.setListener(mRecognizerDialogListener);      //set Dialog event Listener
        STT_Dialog.show();                                      //Show [IFLYTEK] API Interactive animations
    }

    //Initialize listener(only show ERROR messages)
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS)
                RX.setText("ERROR");
        }
    };

    //output the remote server result(iflytek API interface function)
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {

        public void onResult(RecognizerResult results, boolean isLast) {

            //RX.append(results.getResultString());     //Print Original json data
            printResult(results);                     //show after analyze data

        }

        public void onError(SpeechError error) {
        }
    };

    //JSON data analyze
    private void printResult(RecognizerResult results) {
        RX.append("\n");
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e){
            e.printStackTrace();
        }

        STT_Results.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : STT_Results.keySet())
            resultBuffer.append(STT_Results.get(key));

        String res  = resultBuffer.toString();
        STT_RES.setText(res);//听写结果显示
        SC_LLM_Result = SC_LLM.run(res);
        if(SC_LLM_Result.getErrCode()==0){
            RX.append(SC_LLM_Result.getContent());
        }else{
            RX.setText("ERROR");
        }
    }

    //STT Param set
    public void STT_params_init() {
        STT.setParameter(SpeechConstant.CLOUD_GRAMMAR,  null );
        STT.setParameter(SpeechConstant.SUBJECT,        null );
        STT.setParameter(SpeechConstant.PARAMS,         null);
        STT.setParameter(SpeechConstant.ENGINE_TYPE,    "cloud");
        STT.setParameter(SpeechConstant.RESULT_TYPE,    "json");
        STT.setParameter(SpeechConstant.LANGUAGE,       "zh_cn");
        STT.setParameter(SpeechConstant.ACCENT,         "mandarin");
        STT.setParameter(SpeechConstant.VAD_BOS,        "4000");
        STT.setParameter(SpeechConstant.VAD_EOS,        "1000");
        STT.setParameter(SpeechConstant.ASR_PTT,        "1");
        STT.setParameter(SpeechConstant.AUDIO_FORMAT,   "wav");
        STT.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

}