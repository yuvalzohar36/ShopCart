package com.example.shoppingcart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class NewShoppingActivity extends AppCompatActivity {
DatabaseHelper MyDB;
LinearLayout linearMain;
private CheckBox checkBox;
private Button Uncheck_all_Btn;
List<CheckBox> CheckBoxList = new ArrayList<CheckBox>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping);
        MyDB = new DatabaseHelper(NewShoppingActivity.this);
        linearMain = (LinearLayout) findViewById(R.id.linear_main_new);
        Uncheck_all_Btn = findViewById(R.id.Uncheck_all_Btn2);
        Load_all_to_new_page();
        clicked();
        Clear_ALL();
    }
    public void Load_all_to_new_page(){
        Cursor res = MyDB.getAllData_2();
        if (res.getCount() == 0 ){
            Toast.makeText(NewShoppingActivity.this,"No data found",Toast.LENGTH_LONG).show();
        }
        while (res.moveToNext()) {
            TextView t = new TextView(this);
            TextView space = new TextView(this);
            space.setText("   X");
            TextView quantitytextnew = new TextView(this);
            String current_qnuantity = (res.getString(2));
            quantitytextnew.setText(current_qnuantity);
            LinearLayout a = new LinearLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            checkBox = new CheckBox(NewShoppingActivity.this);
            checkBox.setText(res.getString(0));
            checkBox.setScaleX(1.1f);
            checkBox.setScaleY(1.1f);
            t.setText("     ");
            //checkBox.setHeight(154);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setButtonTintList(getColorStateList(R.color.colorPrimary));
            }
            checkBox.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            if (res.getString(1).trim().equals("1")){
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);
            }
            CheckBoxList.add(checkBox);
            a.addView(checkBox);
            a.addView(space);
            a.addView(quantitytextnew);
            a.addView(t);
            linearMain.addView(a);
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
                    boolean IsUpdated = MyDB.updateData_2(CheckBoxList.get(finalI).getText().toString(), ifchecked);
                    if (IsUpdated == true) {
                        Toast.makeText(NewShoppingActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }

    public void Clear_ALL(){
        Uncheck_all_Btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                for (int i = 0; i<CheckBoxList.size();i++){
                    CheckBoxList.get(i).setChecked(false);
                    boolean IsUpdated = MyDB.updateData_2(CheckBoxList.get(i).getText().toString(), false);
                    if (IsUpdated) {
                        Toast.makeText(NewShoppingActivity.this, "Uncheked Successfully", Toast.LENGTH_LONG).show();
                    }
                }
            }});}

    public void Sharing(){
        List<String> NameList = new ArrayList<String>();
        Boolean Share_IT = false;
        Cursor res = MyDB.getAllData_2();
        if (res.getCount() == 0 ){
            Toast.makeText(NewShoppingActivity.this,"No data found for sharing",Toast.LENGTH_LONG).show();
            Share_IT=false;
        }
        while (res.moveToNext()) {
            Share_IT = true;
            String quantity = res.getString(2);
            String product_name = res.getString(0);
            NameList.add(product_name+" X"+quantity);
        }
        String text = "";
        for (int i=0; i<NameList.size();i++){
            text = text + NameList.get(i) +"\n";
        }
        if (Share_IT){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "My Shopping Cart is:");
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "Sharing My Shopping cart");
            startActivity(shareIntent);
    }}
    @Override
    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.menu2, menu2);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.Shareit: {
                Sharing();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }}


