package contacter.db;

import java.util.ArrayList;
import java.util.HashMap;

import contacter.entity.Friend;
import contacter.entity.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "contact";

    public final static int VERSION = 2;

    private static DBHelper instance = null;

    private SQLiteDatabase db;

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private void openDatabase() {
        if (db == null) {
            db = this.getWritableDatabase();
        }
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuffer tableFriend = new StringBuffer();
        StringBuffer tableUser = new StringBuffer();
        tableUser.append("create table user(account char(20) primary key,")
                .append("name text,")
                .append("password1 text,")
                .append("password2 text )");
        db.execSQL(tableUser.toString());
        tableFriend.append("create table friend(_id integer primary key autoincrement,")
                .append("name text,")
                .append("mobilephone text,")
                .append("address text,")
                .append("position text,")
                .append("company text,")
                .append("email text,")
                .append("account text,")
                .append("imageid int )");
        db.execSQL(tableFriend.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlFriend = "drop table if exists friend";
        db.execSQL(sqlFriend);
        onCreate(db);
        String sqlUser = "drop table if exists user";
        db.execSQL(sqlUser);
        onCreate(db);
    }

    public long saveFriend(Friend friend) {
        openDatabase();
        ContentValues values = new ContentValues();
        values.put("name", friend.name);
        values.put("mobilephone", friend.mobilePhone);
        values.put("position", friend.position);
        values.put("company", friend.company);
        values.put("email", friend.email);
        values.put("imageid", friend.imageId);
        values.put("account", friend.account);
        return db.insert("friend", null, values);
    }

    public long saveUser(User user) {
        openDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.name);
        values.put("account", user.account);
        values.put("password1", user.password1);
        values.put("password2", user.password2);
        return db.insert("user", null, values);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList getFriendList(String account) {
        openDatabase();
        Cursor cursor = db.query("friend", null, "account=?", new String[]{String.valueOf(account)}, null, null, null);//��ѯ���һЩ����
        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("_id", cursor.getInt(cursor.getColumnIndex("_id")));
            map.put("position", cursor.getString(cursor.getColumnIndex("position")));
            map.put("company", cursor.getString(cursor.getColumnIndex("company")));
            map.put("email", cursor.getString(cursor.getColumnIndex("email")));
            map.put("imageid", cursor.getString(cursor.getColumnIndex("imageid")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("mobilephone", cursor.getString(cursor.getColumnIndex("mobilephone")));
            map.put("account", cursor.getString(cursor.getColumnIndex("account")));

            list.add(map);

        }
        return list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList getUserList() {
        openDatabase();
        Cursor cursor = db.query("user", null, null, null, null, null, null);//��ѯ���һЩ����
        ArrayList list = new ArrayList();
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("account", cursor.getString(cursor.getColumnIndex("account")));
            map.put("password1", cursor.getString(cursor.getColumnIndex("password1")));
            map.put("password2", cursor.getString(cursor.getColumnIndex("password2")));
            list.add(map);
        }
        return list;
    }
}
