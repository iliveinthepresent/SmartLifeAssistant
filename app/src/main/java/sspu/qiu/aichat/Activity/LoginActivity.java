package sspu.qiu.aichat.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;

import sspu.qiu.aichat.Database.UserInfoManager;
import sspu.qiu.aichat.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private CheckBox rememberPass;
    // 读取数据和存储数据的对象
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        // 由于Toast在某些安卓版本的机器上无法显示，这里使用github上的一个Toast框架来解决不同安卓机型的适配问题
        ToastUtils.init(getApplication());
        ToastUtils.setGravity(Gravity.TOP);
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        rememberPass = findViewById(R.id.rememberPass);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

        pref = getSharedPreferences("data", MODE_PRIVATE);
        boolean isRemember = pref.getBoolean("remember", false);
        // 如果记住密码
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            // 将账号和密码设置到文本框中
            etAccount.setText(account);
            etPassword.setText(password);
            rememberPass.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            // 文本框中的内容
            String acc = etAccount.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            // 逻辑判断
            if (TextUtils.isEmpty(acc) || TextUtils.isEmpty(pass)) {
                ToastUtils.show("账号或密码不能为空");
                return;
            }
            // 存储的数据
            String password = UserInfoManager.getInstance(this).findData(acc,
                    UserInfoManager.PASSWORD);
            // 如果密码正确
            if (password.equals(pass)) {
                // 获取editor实例
                editor = pref.edit();
                // 如果勾选了记住密码，就把账号密码保存到共享参数里，并且设置remember为true。下次创建这个活动时就会自动填写用户名和密码
                if (rememberPass.isChecked()) {
                    editor.putBoolean("remember", true);
                    editor.putString("account", acc);
                    editor.putString("password", pass);
                } else {
                    editor.putBoolean("remember", false);
                }
                editor.apply();
                ToastUtils.show("欢迎登录");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                ToastUtils.show("账号或密码不正确");
                return;
            }
        } else if (view.getId() == R.id.tv_register) {
            // 跳转到注册活动
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 1);
        }
    }


    // 得到注册活动返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String acc = data.getStringExtra("acc");
                    String pass = data.getStringExtra("pass");
                    // 将账号和密码设置到文本框中
                    etAccount.setText(acc);
                    etPassword.setText(pass);
                }
                break;
            default:
                break;
        }

    }
}