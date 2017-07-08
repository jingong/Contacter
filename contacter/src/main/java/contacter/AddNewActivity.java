package contacter;

import java.util.HashMap;

import contacter.db.DBHelper;
import contacter.entity.Friend;

import com.example.test_contact00.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

@SuppressWarnings("deprecation")
public class AddNewActivity extends Activity {
    ImageButton btn_img;
    AlertDialog imageChooseDialog;
    Gallery gallery;
    ImageSwitcher is;
    int imagePosition;

    EditText et_name;
    EditText et_mobilePhone;
    EditText et_position;
    EditText et_company;
    EditText et_email;

    Button btn_save;
    Button btn_return;
    HashMap<String, String> map;
    String account;
    private int[] images = {R.drawable.image1, R.drawable.image2,
            R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8,
            R.drawable.image9};

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);
        initWidget();
        Intent intent = getIntent();
        map = (HashMap<String, String>) intent.getSerializableExtra("accountmap");
        account = map.get("account");
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(AddNewActivity.this, "姓名不能为空",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String mobilePhone = et_mobilePhone.getText().toString();
                String position = et_position.getText().toString();
                String company = et_company.getText().toString();
                String email = et_email.getText().toString();
                int imageId = images[imagePosition];

                Friend friend = new Friend();
                friend.company = company;
                friend.email = email;
                friend.imageId = imageId;
                friend.mobilePhone = mobilePhone;
                friend.name = name;
                friend.position = position;
                friend.account = account;
                long success = DBHelper.getInstance(AddNewActivity.this).saveFriend(friend);
                if (success != -1) {
                    Toast.makeText(AddNewActivity.this, "添加成功",
                            Toast.LENGTH_LONG).show();
                    setResult(1);
                    finish();
                } else {
                    Toast.makeText(AddNewActivity.this, "添加失败，请重新操作",
                            Toast.LENGTH_LONG).show();
                    setResult(2);
                    finish();
                }

            }
        });
        btn_img = (ImageButton) this.findViewById(R.id.btn_img);
        btn_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initImageChooseDialog();
                imageChooseDialog.show();
            }
        });
        btn_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
    }

    public void initWidget() {
        et_name = (EditText) this.findViewById(R.id.et_name);
        et_mobilePhone = (EditText) this.findViewById(R.id.et_mobilephone);
        et_position = (EditText) this.findViewById(R.id.et_position);
        et_company = (EditText) this.findViewById(R.id.et_company);
        et_email = (EditText) this.findViewById(R.id.et_email);
        btn_save = (Button) this.findViewById(R.id.btn_save);
        btn_return = (Button) this.findViewById(R.id.btn_return);
    }

    private void initImageChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择头像");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn_img.setImageResource(images[imagePosition % images.length]);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.imageswitch, null);
        gallery = (Gallery) view.findViewById(R.id.img_gallery);
        gallery.setAdapter(new Imageadapter(this));
        gallery.setSelection(2);
        is = (ImageSwitcher) view.findViewById(R.id.image_switcher);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                imagePosition = position;
                is.setImageResource(images[position % images.length]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        is.setFactory(new MyViewFactory(this));
        builder.setView(view);
        imageChooseDialog = builder.create();
    }

    class Imageadapter extends BaseAdapter {
        private Context context;
        public Imageadapter(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(images[position % images.length]);
            iv.setAdjustViewBounds(true);
            iv.setLayoutParams(new Gallery.LayoutParams(80, 80));
            iv.setPadding(15, 10, 15, 10);
            return iv;
        }

    }

    class MyViewFactory implements ViewFactory {
        private Context context;

        public MyViewFactory(Context context) {
            this.context = context;
        }

        @Override
        public View makeView() {
            ImageView iv = new ImageView(context);
            iv.setLayoutParams(new ImageSwitcher.LayoutParams(90, 90));
            return iv;
        }

    }
}
