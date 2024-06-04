package sspu.qiu.aichat.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sparkchain.core.LLM;
import com.iflytek.sparkchain.core.LLMCallbacks;
import com.iflytek.sparkchain.core.LLMConfig;
import com.iflytek.sparkchain.core.LLMError;
import com.iflytek.sparkchain.core.LLMEvent;
import com.iflytek.sparkchain.core.LLMResult;
import com.iflytek.sparkchain.core.Memory;
import com.iflytek.sparkchain.core.SparkChain;
import com.iflytek.sparkchain.core.SparkChainConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import sspu.qiu.aichat.Adapter.ChatAdapter;
import sspu.qiu.aichat.Bean.ChatBean;
import sspu.qiu.aichat.Bean.SmsInfo;
import sspu.qiu.aichat.Bean.WeatherInfo;
import sspu.qiu.aichat.R;
import sspu.qiu.aichat.Service.MusicService;
import sspu.qiu.aichat.Utils.ViewUtil;
import sspu.qiu.aichat.Utils.WeatherParsing;

//public class AIChatActivity extends AppCompatActivity {
//    private ListView listView;
//    private ChatAdapter adapter;
//    // 存放所有聊天数据的集合
//    private List<ChatBean> chatBeanList;
//    private EditText et_send_msg;
//    private Button btn_send;
//
//    private RecognizerDialog STT_Dialog;
//
//    private ImageButton start_speaking;
//
//    private Button btn_local_instruct;
//    private String welcome[];// 存储欢迎信息
//
//    // 设定flag，在输出未完成时无法进行发送
//    private boolean sessionFinished = true;
//
//    private String sendMsg, result, getData, code;
//    private static final String TAG = "Chat";
//    private LLM llm;
//
//    private Intent intent;
//
//    private myConn conn;
//
//    private List<Map<String, String>> list;
//
//    private Map<String, String> map;
//    MusicService.MyBinder binder;
//
//    private final StringBuilder resultMessage = new StringBuilder();
//    private View btn_user_information;
//
//    private SpeechRecognizer                STT;                //
//
//    private HashMap<String, String>         STT_Results;        //
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.old.activity_ai_chat);
//        ToastUtils.init(getApplication());
//        ToastUtils.setGravity(Gravity.TOP);
//        intent = new Intent(this, MusicService.class);
//        conn = new myConn();
//        bindService(intent, conn, Context.BIND_AUTO_CREATE);
//        // 获取读取音乐文件的权限
//        XXPermissions.with(this).permission(
////                "android.permission.READ_MEDIA_IMAGES",
////                "android.permission.READ_MEDIA_VIDEO",
//                Permission.READ_MEDIA_AUDIO
//        ).request(new OnPermissionCallback() {
//            @Override
//            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
//                if (!allGranted) {
//                    showToast("获取部分权限成功，但部分权限未正常授予");
//                    return;
//                }
//            }
//
//            @Override
//            public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
//                if (doNotAskAgain) {
//                    showToast("被永久拒绝授权，请手动授予录音和日历权限");
//                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                    XXPermissions.startPermissionActivity(AIChatActivity.this, permissions);
//                } else {
//                    showToast("获取录音和日历权限失败");
//                }
//            }
//        });
//        chatBeanList = new ArrayList<ChatBean>();
//        // 获取内置的欢迎信息
//        welcome = getResources().getStringArray(R.array.welcome);
//
//        initView();
//        initSDK();
//        STT_cfg_init();
//    }
//
//    public void STT_cfg_init(){
//        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=982080ac");                       //Create STT server,where the SDK package is deeply bound to this APPID parameter
//        STT = SpeechRecognizer.createRecognizer(AIChatActivity.this, mInitListener);                  //Initialize STT server
//        STT_params_init();                                                                          //Initialize STT params
//        STT_Results = new LinkedHashMap<String, String>();                                          //Initialize Hashmap
//        STT_Dialog = new RecognizerDialog(AIChatActivity.this, mInitListener);
//    }
//    //STT Param set
//    public void STT_params_init() {
//        STT.setParameter(SpeechConstant.CLOUD_GRAMMAR,  null );
//        STT.setParameter(SpeechConstant.SUBJECT,        null );
//        STT.setParameter(SpeechConstant.PARAMS,         null);
//        STT.setParameter(SpeechConstant.ENGINE_TYPE,    "cloud");
//        STT.setParameter(SpeechConstant.RESULT_TYPE,    "json");
//        STT.setParameter(SpeechConstant.LANGUAGE,       "zh_cn");
//        STT.setParameter(SpeechConstant.ACCENT,         "mandarin");
//        STT.setParameter(SpeechConstant.VAD_BOS,        "4000");
//        STT.setParameter(SpeechConstant.VAD_EOS,        "1000");
//        STT.setParameter(SpeechConstant.ASR_PTT,        "1");
//        STT.setParameter(SpeechConstant.AUDIO_FORMAT,   "wav");
//        STT.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
//    }
//    private InitListener mInitListener = new InitListener() {
//        @Override
//        public void onInit(int code) {
//            if (code != ErrorCode.SUCCESS);
//        }
//    };
//
//    private void start_speaking() {
//        STT_Results.clear();                                    //Clean dat
//        STT_Dialog.setListener(mRecognizerDialogListener);      //set Dialog event Listener
//        STT_Dialog.show();                                      //Show [IFLYTEK] API Interactive animations
//    }
//
//
//    //output the remote server result(iflytek API interface function)
//    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
//
//        public void onResult(RecognizerResult results, boolean isLast) {
//
//            //RX.append(results.getResultString());     //Print Original json data
//            printResult(results);                     //show after analyze data
//
//        }
//
//        public void onError(SpeechError error) {
//        }
//    };
//
//    //JSON data analyze
//    private void printResult(RecognizerResult results) {
////        RX.append("\n");
//        String text = JsonParser.parseIatResult(results.getResultString());
//        String sn = null;
//        // 读取json结果中的sn字段
//        try {
//            JSONObject resultJson = new JSONObject(results.getResultString());
//            sn = resultJson.optString("sn");
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//
//        STT_Results.put(sn, text);
//
//        StringBuffer resultBuffer = new StringBuffer();
//        for (String key : STT_Results.keySet())
//            resultBuffer.append(STT_Results.get(key));
//
//        String res  = resultBuffer.toString();
//        sendData(res);
//        System.out.println(res);
////        STT_RES.setText(res);//听写结果显示
////        SC_LLM_Result = SC_LLM.run(res);
////        if(SC_LLM_Result.getErrCode()==0){
////            RX.append(SC_LLM_Result.getContent());
////        }else{
////            RX.setText("ERROR");
////        }
//    }
//    private void initXML() {
//        try {
//            // 读取weather1.xml文件
//            InputStream is = AIChatActivity.this.getResources().openRawResource(R.raw.weather1);
//            // 把每个城市的天气信息集合存到weatherInfos中
//            List<WeatherInfo> weatherInfos = WeatherParsing.getInfosFromXML(is);
//            // 循环读取weatherInfos中的每一条数据
//            list = new ArrayList<Map<String, String>>();
//            for (WeatherInfo info : weatherInfos) {
//                map = new HashMap<String, String>();
//                map.put("temp", info.getTemp());
//                map.put("weather", info.getWeather());
//                map.put("name", info.getName());
//                map.put("pm", info.getPm());
//                map.put("wind", info.getWind());
//                list.add(map);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToastUtils.show("解析信息失败");
//        }
//    }
//
//    private void getMap(int number) {
//        Map<String, String> cityMap = list.get(number);
//        String temp = cityMap.get("temp");
//        String weather = cityMap.get("weather");
//        String name = cityMap.get("name");
//        String pm = cityMap.get("pm");
//        String wind = cityMap.get("wind");
//        showLocalData(name + "今天" + weather + "\n温度" + temp + "\n风力：" + wind + "\npm：" + pm);
//    }
//
//    private class myConn implements ServiceConnection {
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            binder = (MusicService.MyBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    // 绑定控件
//    public void initView() {
//        btn_local_instruct = findViewById(R.id.btn_local_instruct);
//        listView = findViewById(R.id.list);
//        btn_user_information = findViewById(R.id.btn_user_information);
//        et_send_msg = findViewById(R.id.et_send_msg);
//        btn_send = findViewById(R.id.btn_send);
//        start_speaking = findViewById(R.id.stratSpeaking);
//        adapter = new ChatAdapter(chatBeanList, this);
//        listView.setAdapter(adapter);
//        // 点击发送按钮，发送信息
//        btn_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendDataFromText();
//                ViewUtil.hideOneInputMethod(AIChatActivity.this, et_send_msg); // 隐藏输入法软键盘
//            }
//        });
//
//
//        btn_local_instruct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 显示AlertDialog
//                showAlertDialog();
//            }
//        });
//        btn_user_information.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AIChatActivity.this, UserInformationActivity.class);
//                startActivity(intent);
//            }
//        });
//        // 点击Enter键也可以发送信息
//        et_send_msg.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER &&
//                        keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    sendDataFromText();
//                    ViewUtil.hideOneInputMethod(AIChatActivity.this, et_send_msg); // 隐藏输入法软键盘
//                }
//                return false;
//            }
//        });
//
//        start_speaking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                start_speaking();
//            }
//        });
//        // 获取一个随机数
//        int position = (int) (Math.random() * (welcome.length - 1));
//        // 用随机数获取机器人的首次聊天信息
//        showData(welcome[position]);
//    }
//
//    // 显示AlertDialog的方法
//    private void showAlertDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("本地指令大全");
//        builder.setMessage("1.播放本地音乐 + 手机存储下的音乐文件路径名\n" +
//                "2.停止播放音乐\n" + "3.查询电信卡流量\n" + "4.查询移动卡流量\n" + "5.查询最新短信\n" + "6.查询天气\n" + "7.监听系统分钟广播\n" + "8.停止监听广播");
//
//        // 添加确定按钮
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 用户点击确定按钮时的处理
//                dialog.dismiss();
//            }
//        });
//
//        // 创建并显示AlertDialog
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    LLMCallbacks llmCallbacks = new LLMCallbacks() {
//        @Override
//        public void onLLMResult(LLMResult llmResult, Object usrContext) {
//            Log.d(TAG, "onLLMResult\n");
//            String content = llmResult.getContent();
//            Log.e(TAG, "onLLMResult:" + content);
//            int status = llmResult.getStatus();
//            if (content != null) {
//                // 涉及较多的字符串拼接操作，所以使用StringBuilder
//                resultMessage.append(content);
//            }
//            if (usrContext != null) {
//                String context = (String) usrContext;
//                Log.d(TAG, "context:" + context);
//            }
//            if (status == 2) {
//                // 在主线程上执行与UI相关的操作
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 结束时把所有结果整合输出
//                        showData(resultMessage.toString());
//                        resultMessage.setLength(0);
//                    }
//                });
//                int completionTokens = llmResult.getCompletionTokens();
//                int promptTokens = llmResult.getPromptTokens();//
//                int totalTokens = llmResult.getTotalTokens();
//                Log.e(TAG, "completionTokens:" + completionTokens + "promptTokens:" + promptTokens + "totalTokens:" + totalTokens);
//                sessionFinished = true;
//            }
//        }
//
//        @Override
//        public void onLLMEvent(LLMEvent event, Object usrContext) {
//            Log.d(TAG, "onLLMEvent\n");
//            Log.w(TAG, "onLLMEvent:" + " " + event.getEventID() + " " + event.getEventMsg());
//        }
//
//        @Override
//        public void onLLMError(LLMError error, Object usrContext) {
//            Log.d(TAG, "onLLMError\n");
//            Log.e(TAG, "errCode:" + error.getErrCode() + "errDesc:" + error.getErrMsg());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    showData("错误:" + " err:" + error.getErrCode() + " errDesc:" + error.getErrMsg() + "\n");
//                }
//            });
//            if (usrContext != null) {
//                String context = (String) usrContext;
//                Log.d(TAG, "context:" + context);
//            }
//            sessionFinished = true;
//        }
//    };
//
//    private void initSDK() {
//        // 初始化SDK，Appid等信息在清单中配置
//        SparkChainConfig sparkChainConfig = SparkChainConfig.builder();
//        sparkChainConfig.appID(getString(R.string.spark_app_id))
//                .apiKey(getString(R.string.spark_api_key))
//                .apiSecret(getString(R.string.spark_api_secret))//应用申请的appid三元组
//                .logLevel(0);
//
//        int ret = SparkChain.getInst().init(getApplicationContext(), sparkChainConfig);
//        if (ret == 0) {
//            Log.d(TAG, "SDK初始化成功：" + ret);
//            setLLMConfig();
//        } else {
//            Log.d(TAG, "SDK初始化失败：其他错误:" + ret);
//            showToast("SDK初始化失败-其他错误：" + ret);
//        }
//    }
//
//    private void setLLMConfig() {
//        Log.d(TAG, "setLLMConfig");
//        LLMConfig llmConfig = LLMConfig.builder();
//        llmConfig.domain("generalv3.5");
//        llmConfig.url("ws(s)://spark-api.xf-yun.com/v3.5/chat");
////        llmConfig.maxToken(8192);
//        //memory有两种，windows_memory和tokens_memory，二选一即可
//        Memory window_memory = Memory.windowMemory(5);
//        llm = new LLM(llmConfig, window_memory);
//
////        Memory tokens_memory = Memory.tokenMemory(8192);
////        llm = new LLM(llmConfig,tokens_memory);
//
//        llm.registerLLMCallbacks(llmCallbacks);
//    }
//
//    private void sendData(String sendMsg) {
//        ChatBean chatBean = new ChatBean();
//        chatBean.setMessage(sendMsg);
//        // SEND表示自己发送的信息
//        chatBean.setState(chatBean.SEND);
//        // 将发送的信息添加到数据源列表中
//        chatBeanList.add(chatBean);
//        // 通知适配器更新ListView列表
//        adapter.notifyDataSetChanged();
//        if (sendMsg.startsWith(getString(R.string.play_music))) {
//            String musicFileName = sendMsg.substring(6, sendMsg.length());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                // 如果没有权限，请求权限
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        555);
//            }
//            playLocalMusic(musicFileName);
//            return;
//        } else if (sendMsg.startsWith(getString(R.string.parse_music))) {
//            binder.pause();
//            showLocalData("已停止播放");
//            return;
//        } else if (getString(R.string.query_mobile_traffic).equals(sendMsg) || getString(R.string.query_telecommunication_traffic).equals(sendMsg)) {
//            // 在发送短信之前检查权限
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
//                // 已经有SEND_SMS权限，可以发送短信
//                if (getString(R.string.query_mobile_traffic).equals(sendMsg)) {
//                    sendSmsAuto("10086", "LLCX");
//                } else {
//                    sendSmsAuto("10001", "1081");
//                }
//
//                showLocalData("短信发送成功");
//            } else {
//                // 没有SEND_SMS权限，请求权限
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 9999);
//            }
//            return;
//        } else if (getString(R.string.show_newly_sms).equals(sendMsg)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                    checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//                // 如果没有权限，请求权限
//                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 666);
//            } else {
//                // 如果已有权限，执行你的查询短信的代码
//                readLatestSMS();
//            }
//            return;
//        } else if (getString(R.string.show_weather).equals(sendMsg)) {
//            initXML();
//            getMap(0);
//            getMap(1);
//            getMap(2);
//            return;
//        } else if (getString(R.string.monitor_system_minute_broadcasts).equals(sendMsg)) {
//            timeReceiver = new TimeReceiver(); // 创建一个分钟变更的广播接收器
//            // 创建一个意图过滤器，只处理系统分钟变化的广播
//            IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
//            registerReceiver(timeReceiver, filter); // 注册接收器，注册之后才能正常接收广播
//            showLocalData("开始监听分钟到达广播");
//            return;
//        } else if (getString(R.string.stop_listening_to_broadcasts).equals(sendMsg)) {
//            unregisterReceiver(timeReceiver); // 注销接收器，注销之后就不再接收广播
//            showLocalData("成功停止监听广播");
//            return;
//        }
//        // 从服务器获取机器人回复的信息
//        startChat(sendMsg);
//    }
//    // 用户发送消息
//    private void sendDataFromText() {
//        // 获取你输入的信息
//        sendMsg = et_send_msg.getText().toString();
//        // 判断消息是否为空
//        if (TextUtils.isEmpty(sendMsg)) {
//            Toast.makeText(this, "您还未输入信息哦", Toast.LENGTH_LONG).show();
//            return;
//        }
//        // 清空消息框
//        et_send_msg.setText("");
//        // 替换空格和换行
//        sendMsg = sendMsg.replaceAll(" ", "").
//                replaceAll("\n", "").trim();
//        sendData(sendMsg);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 9999) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 用户授予了SEND_SMS权限，可以发送短信
//                showLocalData("授权成功，请重新查询");
//            } else {
//                // 用户拒绝了SEND_SMS权限，你需要处理相应的逻辑
//                Toast.makeText(this, "用户拒绝了短信权限", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == 666) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 用户授予了SEND_SMS权限，可以发送短信
////                sendSmsAuto("10001", "1081");
//                readLatestSMS();
//            } else {
//                // 用户拒绝了SEND_SMS权限，你需要处理相应的逻辑
//                Toast.makeText(this, "用户拒绝了短信权限", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == 555) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                // 用户授予了读取和写入外部存储权限，执行播放音乐的代码
//                showLocalData("已授权，请重新发送您的播放指令");
//            } else {
//                // 用户拒绝了权限，可以显示一个提示或执行其他逻辑
////                ToastUtils.show("未授予读取和写入外部存储权限，无法播放音乐");
//            }
//        }
//    }
//
//    void playLocalMusic(String pathway) {
//        File SDpath = Environment.getExternalStorageDirectory();
//        File file = new File(SDpath, pathway);
//        String path = file.getAbsolutePath();
//        if (file.exists() && file.length() > 0) {
//            binder.play(path);
//            showLocalData("成功播放" + pathway);
//        } else {
//            showLocalData("找不到音乐文件");
//        }
//    }
//
//    private void startChat(String usrInputText) {
//        if (llm == null) {
//            Log.e(TAG, "startChat failed,please setLLMConfig before!");
//            return;
//        }
//
//        Log.d(TAG, "用户输入：" + usrInputText);
//
//        String myContext = "myContext";
//        // 显示用户友好的提示，提高用户的耐心
//        ChatBean chatBean = new ChatBean();
//        chatBean.setMessage("加载中...");
//        chatBean.setState(ChatBean.RECEIVE);
//        chatBeanList.add(chatBean);
//        int ret = llm.arun(usrInputText, myContext);
//        if (ret != 0) {
//            Log.e(TAG, "SparkChain failed:\n" + ret);
//            return;
//        }
//        sessionFinished = false;
//        return;
//    }
//
//    // 显示本地系统的消息到列表中
//    private void showLocalData(String message) {
//        ChatBean chatBean = new ChatBean();
//        chatBean.setMessage(message);
//        // 本地系统发送的消息
//        chatBean.setState(ChatBean.RECEIVE);
//        // 将本地系统的消息添加到数据源中
//        chatBeanList.add(chatBean);
//        adapter.notifyDataSetChanged();
//    }
//
//    // 显示AI的消息到列表中
//    private void showData(String message) {
//        ChatBean chatBean = new ChatBean();
//        chatBean.setMessage(message);
//        // 机器人发送的消息
//        chatBean.setState(ChatBean.RECEIVE);
//        // 将机器人发送的消息添加到数据源中
//        if (chatBeanList.size() != 0) {
//            chatBeanList.set(chatBeanList.size() - 1, chatBean);
//        } else {
//            chatBeanList.add(chatBean);
//        }
//        adapter.notifyDataSetChanged();
//    }
//
//
//    // 记录第一次点击时的时间
//    protected long exitTime;
//
//    // 双击2次返回键退出程序
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(AIChatActivity.this, "再按一次退出聊天程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                AIChatActivity.this.finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unbindService(conn);
//        unInitSDK();
//    }
//
//    private void unInitSDK() {
//        SparkChain.getInst().unInit();
//    }
//
//    // 短信发送事件
//    private String SENT_SMS_ACTION = "com.example.storage.SENT_SMS_ACTION";
//    // 短信接收事件
//    private String DELIVERED_SMS_ACTION = "com.example.storage.DELIVERED_SMS_ACTION";
//
//
//    public void readLatestSMS() {
//        // 查询系统信息的uri
//        Uri uri = Uri.parse("content://sms/");
//        // 获取ContentResolver对象
//        ContentResolver resolver = getContentResolver();
//        // 通过ContentResolver对象查询系统短信，按日期降序排序，只获取一条
//        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "type", "body", "date"},
//                null, null, "date desc LIMIT 1");
//
//        if (cursor != null && cursor.moveToFirst()) {
//            // 短信信息
//            int _id = cursor.getInt(0);
//            String address = cursor.getString(1);
//            int type = cursor.getInt(2);
//            String body = cursor.getString(3);
//            long date = cursor.getLong(4);
//
//            // 创建短信信息对象
//            SmsInfo smsInfo = new SmsInfo(_id, address, type, body, date);
//
//            // 将查询到的短信内容显示到界面上
//            String text = "手机号码：" + smsInfo.getAddress() + "\n" +
//                    "短信内容：" + smsInfo.getBody() + "\n\n";
//            showLocalData(text);
//
//            // 关闭Cursor
//            cursor.close();
//        } else {
//            // 没有短信时的处理，可以添加相应的逻辑
//            showLocalData("没有找到短信");
//        }
//    }
//
//
//    // 无需用户操作，由App自动发送短信
//    public void sendSmsAuto(String phoneNumber, String message) {
//        // 短信发送是一个可能耗时的操作，将其放在子线程中执行，以避免在主线程中阻塞。
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 以下指定短信发送事件的详细信息
//                Intent sentIntent = new Intent(SENT_SMS_ACTION);
//                sentIntent.putExtra("phone", phoneNumber);
//                sentIntent.putExtra("message", message);
//                PendingIntent sentPI = PendingIntent.getBroadcast(AIChatActivity.this, 0,
//                        sentIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//                // 以下指定短信接收事件的详细信息
//                Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
//                deliverIntent.putExtra("phone", phoneNumber);
//                deliverIntent.putExtra("message", message);
//                PendingIntent deliverPI = PendingIntent.getBroadcast(AIChatActivity.this, 1,
//                        deliverIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//                // 获取默认的短信管理器
//                SmsManager smsManager = SmsManager.getDefault();
//                // 开始发送短信内容。要确保打开发送短信的完全权限，不是那种还需提示的不完整权限
//                smsManager.sendTextMessage(phoneNumber, null, message, sentPI,
//                        deliverPI);
//            }
//        }).start();
//    }
//
//    private TimeReceiver timeReceiver; // 声明一个分钟广播的接收器实例
//
//    // 定义一个分钟广播的接收器
//    private class TimeReceiver extends BroadcastReceiver {
//        // 一旦接收到分钟变更的广播，马上触发接收器的onReceive方法
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null) {
//                Date currentTime = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String formattedTime = sdf.format(currentTime);
//                String localMessage = String.format("%s\n收到一个分钟到达广播%s", formattedTime, intent.getAction());
//                showLocalData(localMessage);
//            }
//        }
//    }

//}
