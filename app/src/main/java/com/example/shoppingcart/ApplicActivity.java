package com.example.shoppingcart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button Add;
    private CheckBox checkBox;
    private Button btn;
    private DatabaseReference dbr;
    private EditText addText;
    Newword word;

    private ImageButton imgbtn;
    private EditText QuantityPlainText;
    private Button Uncheck_all;
    LinearLayout linearMain;
    DatabaseHelper mydb;
    List<CheckBox> CheckBoxList = new ArrayList<CheckBox>();
    List<ImageButton> DeleteButtonList = new ArrayList<ImageButton>();
    List<LinearLayout> LinearLayoutList = new ArrayList<LinearLayout>();
    List<CheckBox> NewCheckBoxList = new ArrayList<CheckBox>();
    boolean stop = false;
    List<AlertDialog> AlertDialogList = new ArrayList<AlertDialog>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applic);
        dbr = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        word = new Newword();
        word.setEnglish_Word("caramela");
        word.setHebrew_Word("artist");
        dbr.child(("suchuser")).setValue(word);
        dbr.push().setValue(word);
        dbr.child("Users").push().setValue(word);
        dbr.push().setValue(word);

        mydb = new DatabaseHelper(ApplicActivity.this);
        linearMain = (LinearLayout) findViewById(R.id.linear_main);
        Add = (Button) findViewById(R.id.AddBtn);
        addText = (EditText) findViewById(R.id.addeditText);
        QuantityPlainText = (EditText)findViewById(R.id.Quantity);
        QuantityPlainText.setInputType(InputType.TYPE_CLASS_NUMBER);
        Uncheck_all = (Button)findViewById(R.id.Uncheck_all);
        Map newPost = new HashMap();
        newPost.put("sss","ddd");
        dbr.setValue(newPost);

        Load_all();
        Add_data();
        clicked();
        Clear_ALL();

        DeleteData();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void Add_data() {
        final Cursor res = mydb.getAllData();
        Add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                TextView r = new TextView(ApplicActivity.this);
                LinearLayout n = new LinearLayout(ApplicActivity.this);
                n.setOrientation(LinearLayout.HORIZONTAL);
                String newtext = addText.getText().toString().trim();
                String newquantity = (QuantityPlainText.getText().toString().trim());
                if (!newtext.trim().equals("")){
                    if (newquantity.trim().equals(""))
                    {
                        newquantity="1";
                    }
                    final Cursor res = mydb.getAllData();
                    TextView space = new TextView(ApplicActivity.this);
                    space.setText("   X");
                    TextView TextViewQuantity = new TextView(ApplicActivity.this);
                    TextViewQuantity.setText(newquantity);
                    while (res.moveToNext()) {
                        if (res.getString(1).equals(newtext)) {
                            stop = true;
                        }
                    }
                    if (stop == false) {
                        boolean isInserted = mydb.insertData(addText.getText().toString().trim(), false, Integer.parseInt(newquantity));
                        if (isInserted == true)
                            Toast.makeText(ApplicActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ApplicActivity.this, "Loading data failed", Toast.LENGTH_LONG).show();
                        checkBox = new CheckBox(ApplicActivity.this);
                        checkBox.setText(newtext);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            checkBox.setButtonTintList(getColorStateList(R.color.colorPrimary));
                        }
                        checkBox.setScaleX(1.1f);
                        checkBox.setScaleY(1.1f);
                        r.setText("     ");
                        CheckBoxList.add(checkBox);
                        ImageButton imgbtn = new ImageButton(ApplicActivity.this);
                        imgbtn.setImageResource(R.drawable.ic_del);
                        DeleteButtonList.add(imgbtn);
                        LinearLayoutList.add(n);
                        n.addView(checkBox);
                        n.addView(space);
                        n.addView(TextViewQuantity);
                        n.addView(r);
                        n.addView(imgbtn);
                        linearMain.addView(n);
                        addText.getText().clear();
                        QuantityPlainText.getText().clear();
                        System.out.println(checkBox.isChecked());
                        DeleteData();
                        clicked();
                    }
                    else{Toast.makeText(ApplicActivity.this, "Product already inserted", Toast.LENGTH_SHORT).show();
                        QuantityPlainText.getText().clear();
                        addText.getText().clear();}
                        stop = false;
                }
                else {
                    Toast.makeText(ApplicActivity.this, "Missing input", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @SuppressLint("ResourceType")
    public void Load_all() {
        Cursor res = mydb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(ApplicActivity.this, "No data found", Toast.LENGTH_LONG).show();
        }
        while (res.moveToNext()) {
            checkBox = new CheckBox(ApplicActivity.this);
            checkBox.setScaleX(1.1f);
            checkBox.setScaleY(1.1f);
            TextView t = new TextView(this);
            TextView space = new TextView(this);
            space.setText("   X");
            TextView qunatitytext = new TextView(this);
            String newtext1 = res.getString(1);
            String newquantity1 = res.getString(3);
            checkBox.setText(newtext1);
            if (res.getString(2).trim().equals("1")){
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);
            }
            t.setText("     ");
            qunatitytext.setText(newquantity1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setButtonTintList(getColorStateList(R.color.colorPrimary));
            }
            CheckBoxList.add(checkBox);
            ImageButton imgbtn = new ImageButton(ApplicActivity.this);
            imgbtn.setImageResource(R.drawable.ic_del);
            DeleteButtonList.add(imgbtn);
            LinearLayout a = new LinearLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutList.add(a);
            checkBox.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            a.addView(checkBox);
            a.addView(space);
            a.addView(qunatitytext);
            a.addView(t);
            a.addView(imgbtn);
            linearMain.addView(a);

            DeleteData();
        }


    }


    public void DeleteData() {
        final Cursor res = mydb.getAllData();
        for (int i = 0; i < CheckBoxList.size(); i++) {
            final int finalI = i;
            DeleteButtonList.get(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    String word = (String) CheckBoxList.get(finalI).getText();
                    mydb.deleteData_2(word);
                    CheckBoxList.get(finalI).setVisibility(View.GONE);
                    DeleteButtonList.get(finalI).setVisibility(View.GONE);
                    LinearLayoutList.get(finalI).setVisibility(View.GONE);
                    Integer deleteRows = mydb.deleteData(word);

                    if (deleteRows > 0) {
                        Toast.makeText(ApplicActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void clicked() {
        for (int i = 0; i < CheckBoxList.size(); i++) {
            final int finalI = i;
            CheckBoxList.get(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    boolean ifchecked = CheckBoxList.get(finalI).isChecked();
                    boolean IsUpdated = mydb.updateData(CheckBoxList.get(finalI).getText().toString(), ifchecked);
                    if (IsUpdated == true) {
                        Toast.makeText(ApplicActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }
    public void dialogbox(){
        Integer count = 0;
        for (int i = 0; i < CheckBoxList.size(); i++) {
            if ((CheckBoxList.get(i).isChecked()) && (View.VISIBLE == CheckBoxList.get(i).getVisibility())) {
                count+=1;
            }
        if (count>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
            builder.setTitle("Alert ! this operation will delete your currently cart");
            builder.setMessage("Confirm ?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    createnewinstance();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            AlertDialogList.add(alert);
        }
        else
            {
                Toast.makeText(ApplicActivity.this, "Nothing selected", Toast.LENGTH_LONG).show();
                for (int j = 0; j < AlertDialogList.size(); j++) {
                    AlertDialogList.get(j).dismiss(); // close the dialog after backpress button ! ! ! ! ! !
                }
        }
    }}

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ApplicActivity.this, MainActivity.class));
    }

    private void createnewinstance() {
        boolean exist = false;
        mydb.Deleta_all_data();
        for (int i = 0; i < CheckBoxList.size(); i++) {
            if ((CheckBoxList.get(i).isChecked()) && (View.VISIBLE == CheckBoxList.get(i).getVisibility())) {

                Cursor cursor = mydb.getSpecificQuantity(CheckBoxList.get(i).getText().toString().trim());
                if (cursor.moveToFirst()) {
                    String InsertData = cursor.getString(0);
                    boolean isInserted = mydb.insertData_2(CheckBoxList.get(i).getText().toString().trim(), false, InsertData);
                    if (isInserted == true)
                        Toast.makeText(ApplicActivity.this, "Data Copied", Toast.LENGTH_LONG).show();
                    exist = true;
                }
            }
        }
        if (exist) {
            movetonew();
            for (int i = 0; i < AlertDialogList.size(); i++) {
                AlertDialogList.get(i).dismiss(); // close the dialog after backpress button ! ! ! ! ! !
            }
        }
    }

        public void Clear_ALL(){
            Uncheck_all.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    for (int i = 0; i<CheckBoxList.size();i++){
                        CheckBoxList.get(i).setChecked(false);
                        boolean IsUpdated = mydb.updateData(CheckBoxList.get(i).getText().toString(), false);
                    if (IsUpdated) {
                        Toast.makeText(ApplicActivity.this, "Uncheked Successfully", Toast.LENGTH_LONG).show();
                        }
                    }
        }});}

    private void movetonew() {
        startActivity(new Intent(ApplicActivity.this, NewShoppingActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.resumeCart: {
                movetonew();
                return true;
            }
            case R.id.createinstance: {
                dialogbox();
                return true;
            }
            case R.id.logoutMenu: {
                Logout();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


