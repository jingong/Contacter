
package contacter;

import java.util.HashMap;

import com.example.test_contact00.R;

import contacter.db.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ViewSwitcher.ViewFactory;

public class DetailActivity extends Activity {

    EditText et_name;
    EditText et_mobilePhone;
    EditText et_familyPhone;
    EditText et_officePhone;
    EditText et_position;
    EditText et_company;
    EditText et_address;
    EditText et_zipCode;
    EditText et_email;
    EditText et_otherContact;
    EditText et_remark;

    ImageButton btn_img;
    AlertDialog imageChooseDialog;
    Gallery gallery;
    ImageSwitcher is;
    @SuppressWarnings("rawtypes")
    HashMap map;

    Button btn_modify;
    Button btn_delete;
    Button btn_return;

    boolean flag = false;
    int count = 0;

    ListView lv_friendList;
    int imagePosition;
    private int[] images = {R.drawable.image1, R.drawable.image2,
            R.drawable.image3, R.drawable.image4, R.drawable.image5,
            R.drawable.image6, R.drawable.image7, R.drawable.image8,
            R.drawable.image9};
    DBHelper dbHelper;
    SQLiteDatabase db;

    @SuppressWarnings("rawtypes")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        initWidget();
        setEditTextDisable();
        Intent intent = getIntent();
        map = (HashMap) intent.getSerializableExtra("friendmap");
        displayData();

        btn_modify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setEditTextAble();
                if (flag) {
                    flag = false;
                    btn_modify.setText("修改");
                    setEditTextDisable();
                } else {
                    flag = true;
                    btn_modify.setText("保存");
                    displayData();
                    setEditTextAble();
                    count++;
                }
            }
        });
        btn_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                db.delete("friend", "_id=?", new String[]{String.valueOf(map.get("_id"))});
                setResult(1);
                finish();
            }
        });

        btn_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String mobilePhone = et_mobilePhone.getText().toString();
                String position = et_position.getText().toString();
                String company = et_company.getText().toString();
                String email = et_email.getText().toString();
                int imageId = images[imagePosition];
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("mobilePhone", mobilePhone);
                values.put("position", position);
                values.put("company", company);
                values.put("email", email);
                values.put("imageId", imageId);
                db.update("friend", values, "_id=?", new String[]{String.valueOf(map.get("_id"))});
                setResult(1);
                Toast.makeText(DetailActivity.this, "修改成功",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btn_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initImageChooseDialog();
                imageChooseDialog.show();
            }
        });
    }

    public void initWidget() {
        et_name = (EditText) this.findViewById(R.id.et_name);
        et_mobilePhone = (EditText) this.findViewById(R.id.et_mobilephone);
        et_position = (EditText) this.findViewById(R.id.et_position);
        et_company = (EditText) this.findViewById(R.id.et_company);
        et_email = (EditText) this.findViewById(R.id.et_email);
        btn_img = (ImageButton) this.findViewById(R.id.btn_img);
        btn_modify = (Button) this.findViewById(R.id.btn_modify);
        btn_delete = (Button) this.findViewById(R.id.btn_delete);
        btn_return = (Button) this.findViewById(R.id.btn_return);
    }

    private void setEditTextDisable() {
        et_name.setEnabled(false);
        et_mobilePhone.setEnabled(false);
        et_position.setEnabled(false);
        et_company.setEnabled(false);
        et_email.setEnabled(false);
        btn_img.setEnabled(false);
    }

    private void setEditTextAble() {
        et_name.setEnabled(true);
        et_mobilePhone.setEnabled(true);
        et_position.setEnabled(true);
        et_company.setEnabled(true);
        et_email.setEnabled(true);
        btn_img.setEnabled(true);
    }

    private void displayData() {
        et_name.setText(String.valueOf(map.get("name")));
        et_mobilePhone.setText(String.valueOf(map.get("mobilephone")));
        et_position.setText(String.valueOf(map.get("position")));
        et_company.setText(String.valueOf(map.get("company")));
        et_email.setText(String.valueOf(map.get("email")));

        btn_img.setImageResource(Integer.parseInt(String.valueOf(map
                .get("imageid"))));

    }

    private void initImageChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择头像");

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
