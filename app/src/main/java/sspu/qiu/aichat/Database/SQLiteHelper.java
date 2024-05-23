package sspu.qiu.aichat.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table secret (id integer primary key autoincrement, name varchar(20), password varchar(20), phone varchar(20)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        super.onDowngrade(db, oldVersion, newVersion);
        // 在这里处理降级逻辑，例如删除表或修改模式。
        db.execSQL("DROP TABLE IF EXISTS secret");
        onCreate(db);
    }

}
