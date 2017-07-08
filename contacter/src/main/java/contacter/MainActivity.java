package contacter;

import java.util.ArrayList;
import java.util.HashMap;

import contacter.db.DBHelper;

import com.example.test_contact00.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

    GridView gv_button_menu;
    ListView lv_friendList;
    SimpleAdapter adapter;
    ImageButton add;
    ImageButton search;
    ImageButton delete;
    ImageButton quit;
    AlertDialog dialog1;
    AlertDialog dialog2;
    SQLiteDatabase db;
    DBHelper dbHelper;
    static String account;
    HashMap<String, String> map;

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        this.setContentView(R.layout.main);
        loadUserList();
        imageButton();
        Intent intent = getIntent();
        map = (HashMap<String, String>) intent.getSerializableExtra("accountmap");
        account = map.get("account");
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        search = (ImageButton) this.findViewById(R.id.search);
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchPerson();
                dialog1.show();
            }
        });

        delete = (ImageButton) this.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "请点击您要删除的联系人",
                        Toast.LENGTH_LONG).show();
            }
        });

        quit = (ImageButton) this.findViewById(R.id.quit);
        quit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                quit();
                dialog2.show();
            }
        });

    }

    private void searchPerson() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("查找联系人");
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.alert_enter, null);
        builder.setPositiveButton("查找", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText sEdit = (EditText) view.findViewById(R.id.search_edit);
                String keyword = sEdit.getText().toString();
                Cursor cursor = db.query("friend", null, "name=?",
                        new String[]{String.valueOf(keyword)}, null, null,
                        null);
                String message = "";
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        String name = cursor.getString(cursor
                                .getColumnIndex("name"));
                        message += "姓名："
                                + cursor.getString(cursor
                                .getColumnIndex("name"))
                                + "\n"
                                + "电话："
                                + cursor.getString(cursor
                                .getColumnIndex("mobilephone")) + "\n"
                                + "ְ职务职称："
                                + cursor.getString(cursor
                                .getColumnIndex("position")) + "\n"
                                + "公司："
                                + cursor.getString(cursor
                                .getColumnIndex("company")) + "\n"
                                + "邮箱："
                                + cursor.getString(cursor
                                .getColumnIndex("email")) + "\n";
                        if (name.equals(keyword)) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("查找结果")
                                    .setMessage(message)
                                    .setPositiveButton(
                                            "关闭",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int whichButton) {
                                                }
                                            }).show();
                        }
                    }
                    cursor.close();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(view);
        dialog1 = builder.create();
    }

    private void quit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定退出？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog2 = builder.create();
    }

    @SuppressWarnings("unchecked")
    private void loadUserList() {

        lv_friendList = (ListView) this.findViewById(R.id.lv_friendlist);
        @SuppressWarnings("rawtypes")
        ArrayList data = DBHelper.getInstance(this).getFriendList(account);
        adapter = new SimpleAdapter(this, data, R.layout.list_item,
                new String[]{"imageid", "name", "mobilephone"}, new int[]{
                R.id.user_image, R.id.tv_showname,
                R.id.tv_showmobilephone});
        lv_friendList.setAdapter(adapter);

        lv_friendList.setOnItemClickListener(new OnItemClickListener() {// ȡ������Ҫ��ʾ�ĵڼ�����ϵ�˵���Ϣ
            @SuppressWarnings("rawtypes")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap map = (HashMap) parent
                        .getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,
                        DetailActivity.class);
                intent.putExtra("friendmap", map);
                startActivityForResult(intent, 3);
            }
        });
    }

    public void imageButton() {
        add = (ImageButton) this.findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        AddNewActivity.class);
                HashMap<String, String> accountMap = new HashMap<String, String>();
                accountMap.put("account", account);
                intent.putExtra("accountmap", accountMap);
                startActivityForResult(intent, 0);
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            ArrayList friendData = DBHelper.getInstance(this).getFriendList(account);
            adapter = new SimpleAdapter(this, friendData, R.layout.list_item,
                    new String[]{"imageid", "name", "mobilephone"},
                    new int[]{R.id.user_image, R.id.tv_showname,
                            R.id.tv_showmobilephone});
            lv_friendList.setAdapter(adapter);
        } else if (resultCode == 2) {
        }
        if (requestCode == 1) {
            ArrayList friendData = DBHelper.getInstance(this).getFriendList(account);
            adapter = new SimpleAdapter(this, friendData, R.layout.list_item,
                    new String[]{"imageid", "name", "mobilephone"},
                    new int[]{R.id.user_image, R.id.tv_showname,
                            R.id.tv_showmobilephone});
            lv_friendList.setAdapter(adapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
