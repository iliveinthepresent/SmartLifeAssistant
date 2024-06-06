package sspu.qiu.aichat.ui.user;


import static sspu.qiu.aichat.BarColor.setStatusBarColor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;

import java.util.List;

import sspu.qiu.aichat.Activity.AIChatActivity;
import sspu.qiu.aichat.Bean.User;
import sspu.qiu.aichat.Database.UserInfoManager;
import sspu.qiu.aichat.R;
import sspu.qiu.aichat.ui.Side_Menu;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etAccount, etPassword, etConfirm;
    private Button btnRegister;
    private TextView tvReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        ToastUtils.init(getApplication());
        ToastUtils.setGravity(Gravity.TOP);
        if (getSupportActionBar() != null) {//去除默认的ActionBar
            getSupportActionBar().hide();
        }
        //状态栏颜色
        if (!Side_Menu.night_mode) {
            setStatusBarColor(RegisterActivity.this, Color.parseColor("#A09AB4"));
        } else {
            setStatusBarColor(RegisterActivity.this, Color.parseColor("#3b3b3b"));
        }
    }

    private void initView() {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etConfirm = findViewById(R.id.et_confirm);
        btnRegister = findViewById(R.id.btn_register);
        tvReturn = findViewById(R.id.tv_return);
        btnRegister.setOnClickListener(this);
        tvReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register) {
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();
            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
                ToastUtils.show("账号或密码不能为空");
            } else if (!password.equals(confirm)) {
                ToastUtils.show("两次输入的密码不一致");
            } else if (account.length() < 2 || password.length() < 6) {
                ToastUtils.show("账号长度不能小于2，密码长度不能小于6");
            } else {
                List<User> all = UserInfoManager.getInstance(RegisterActivity.this)
                        .queryAll();
                for (User user : all) {
                    String existName = user.getName();
                    if (existName != null && existName.equals(account)) {
                        ToastUtils.show("该用户已存在，无需重复注册");
                        return;
                    }
                }

                User user = new User();
                user.setName(account);
                user.setPassword(password);
                UserInfoManager.getInstance(RegisterActivity.this).insertUser(user);
                // 向上一个活动返回数据，参数1处理结果，参数2意图
                Intent intent = new Intent();
                intent.putExtra("acc", account);
                intent.putExtra("pass", password);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (view.getId() == R.id.tv_return) {
            finish();
        }
    }
}
