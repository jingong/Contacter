package contacter;

import contacter.db.DBHelper;
import contacter.entity.User;

import com.example.test_contact00.R;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
    ImageView register_imageView_back;
    EditText registerName, registerId, registerPassword, registerPasswordAffirm;
    Button btn_register;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        initView();
        setClickLisenter();
    }

    private void initView() {
        //ImageView
        register_imageView_back = (ImageView) findViewById(R.id.register_imageView_back);
        //EditText
        registerName = (EditText) findViewById(R.id.register_name);
        registerId = (EditText) findViewById(R.id.register_id);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerPasswordAffirm = (EditText) findViewById(R.id.register_password_affirm);
        //Button
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    private void setClickLisenter() {
        //ImageView
        register_imageView_back.setOnClickListener(this);
        //EditText
        registerName.setOnClickListener(this);
        registerId.setOnClickListener(this);
        registerPassword.setOnClickListener(this);
        registerPasswordAffirm.setOnClickListener(this);
        //Button
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId) {
            case R.id.register_imageView_back:
                finish();
                break;
            case R.id.btn_register:
                String name = registerName.getText().toString();
                String account = registerId.getText().toString();
                String password1 = registerPassword.getText().toString();
                String password2 = registerPasswordAffirm.getText().toString();
                User user = new User();
                user.name = name;
                user.account = account;
                user.password1 = password1;
                user.password2 = password2;

                if (name.length() != 0 && account.length() != 0) {
                    if (password1.length() != 0 && password2.length() != 0) {
                        if (password1.equals(password2)) {
                            long s = DBHelper.getInstance(RegisterActivity.this).saveUser(user);
                            if (s != -1) {
                                Toast.makeText(RegisterActivity.this, "注册成功,请登录...", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "两次密码不相同，请重新注册！", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } else {
                        if (password1.length() == 0 || password2.length() == 0) {
                            Toast.makeText(RegisterActivity.this, "密码不能为空，请重新注册！", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                } else {
                    if (name.length() == 0) {
                        Toast.makeText(RegisterActivity.this, "姓名不能为空，请重新注册！", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    if (account.length() == 0) {
                        Toast.makeText(RegisterActivity.this, "账号不能为空，请重新注册！", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

}