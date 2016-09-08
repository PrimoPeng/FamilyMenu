package com.example.android.family.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.family.menu.db.FamilyMenuDbHelper;
import com.example.android.family.menu.model.FamilyMenu;


/**
 * Created by Administrator on 2016/9/7.
 */
public class FamilyMenuOperatorActivity extends BaseToolbarActivity {

    private EditText etDishName;
    private Button btnAdd;
    private FamilyMenuDbHelper familyMenuDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_menu_operator);
        familyMenuDbHelper = FamilyMenuDbHelper.getInstance(this);
        etDishName = (EditText) findViewById(R.id.etDishName);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dishName = etDishName.getText().toString();
                SaveData sd = new SaveData();
                sd.execute(dishName);
            }
        });
    }

    class SaveData extends AsyncTask<String, Long, Long> {
        @Override
        protected Long doInBackground(String... strings) {
            FamilyMenu familyMenu = new FamilyMenu();
            familyMenu.menuName = strings[0];
            long result = familyMenuDbHelper.insertData(familyMenu);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (-1 == result) {
                Toast.makeText(FamilyMenuOperatorActivity.this, "保存失败", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(FamilyMenuOperatorActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                etDishName.setText("");
            }
            super.onPostExecute(result);
        }
    }
}
