package sspu.qiu.aichat.Database;


import android.content.Context;
import android.content.DialogInterface;

import java.lang.reflect.Field;
import java.util.List;

import sspu.qiu.aichat.Bean.User;
import sspu.qiu.aichat.Dao.UserDao;

public class UserInfoManager {
    private static UserInfoManager instance;
    private Context context;
    private static UserDao dao;
    public static String PASSWORD = "password";
    public static String PHONE = "phone";

    public static UserInfoManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserInfoManager(context);
        }
        return instance;
    }

    private UserInfoManager(Context context) {
        this.context = context;
        dao = new UserDao(context);
    }

    public void insertUser(User user) {
        dao.insert(user);
    }

    public String findData(String name, String data) {
        return dao.findData(name, data);
    }

    public void updateUser(User user) {
        dao.update(user);
    }

    public void delete(String name) {
        dao.delete(name);
    }

    public List<User> queryAll() {
        return dao.queryAll();
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    /**
     * 由于对话框无论点击哪个系统按钮都会自动关闭,通过mShowing判断
     * 设置为false不关闭,设置为true关闭对话框
     */
    public static void dialogState(DialogInterface dialog, boolean flag) {
        try {
            Field field = dialog.getClass().getSuperclass().
                    getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}