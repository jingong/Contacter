package contacter;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
    private TextView mRegister;
    private ImageView mBack;
    private Button mLoginButton;
    private EditText mIdText, mPasswText;
    DBHelper dbHelper;
    SQLiteDatabase db;
    AlertDialog dialog2;
    String account;
    String password;
    SimpleAdapter adapter;
    ListView lv_friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        initView();
        setViewClickListener();

    }

    private void initView() {
        mRegister = (TextView) findViewById(R.id.register_textView);
        mBack = (ImageView) findViewById(R.id.login_imageView_back);
        mIdText = (EditText) findViewById(R.id.login_id_text);
        mPasswText = (EditText) findViewById(R.id.login_passw_text);
        mLoginButton = (Button) findViewById(R.id.btn_login);
    }

    private void setViewClickListener() {
        mRegister.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId) {
            case R.id.register_textView:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.login_imageView_back:
                quit();
                dialog2.show();
                break;
            case R.id.btn_login:
                account = mIdText.getText().toString();
                password = mPasswText.getText().toString();
                MainActivity.account = account;
                Cursor cursor = db.query("user", null, "account=?",
                        new String[]{String.valueOf(account)}, null, null, null);
                String accounter = "";
                String passworder = "";
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        accounter += cursor.getString(cursor.getColumnIndex("account"));
                        passworder += cursor.getString(cursor.getColumnIndex("password1"));
                    }
                }
                cursor.close();
                //Toast.makeText(LoginActivity.this,accounter,Toast.LENGTH_LONG).show();
                if (account.equals(accounter) && password.equals(passworder)) {
                    Intent j = new Intent(LoginActivity.this, MainActivity.class);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("account", account);
                    j.putExtra("accountmap", map);
                    startActivityForResult(j, 1);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "请重新登录！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
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
}
