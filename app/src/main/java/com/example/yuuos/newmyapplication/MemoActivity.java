package com.example.yuuos.newmyapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

// 元に戻すよう
// git checkout .

// 登録用
// git add .
// git commit -m "ここになにかこめんとを書く"

//登録したやつをgithubに送るよう
//git push origin master

public class MemoActivity extends AppCompatActivity {

    static final String APP_NAME = "MEMO";

    final String APP_MEMO_KEY = "MEMO_KEY";
    ArrayList<String> memoNameList;
    ListView memoNameListView;
    ArrayAdapter<String> arrayAdapter;
    Toolbar toolbar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG", "Destoryed");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("DEBUG", "Resumed");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean successToLoadData = false;
        memoNameList = loadList(this, APP_MEMO_KEY);

        memoNameListView = findViewById(R.id.list_view_memo_name);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, memoNameList);
        memoNameListView.setAdapter(arrayAdapter);

        memoNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter)memoNameListView.getAdapter();

                String item = (String)adapter.getItem(position);
                adapter.remove(item);
                adapter.add(item);
            }
        });

        memoNameListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter)memoNameListView.getAdapter();

                String item = (String)adapter.getItem(position);
                adapter.remove(item);
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPopupDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemoActivity.this);
        alertDialogBuilder.setTitle("タイトルを入力");
        alertDialogBuilder.setIcon(R.drawable.ic_add_circle_yellow_24dp);
        alertDialogBuilder.setCancelable(false);

        LayoutInflater layoutInflater = LayoutInflater.from(MemoActivity.this);
        final View inputMemoNameView = layoutInflater.inflate(R.layout.input_memo_name, null);

        alertDialogBuilder.setView(inputMemoNameView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button saveMemoNameButton = inputMemoNameView.findViewById(R.id.button_save_memoname);
        saveMemoNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText editText = inputMemoNameView.findViewById(R.id.edit_text_memoname);

                String memoName = editText.getText().toString();

                if (TextUtils.isEmpty(memoName)) {
                    Snackbar.make(v, "タイトルが入力されていません", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {


                    boolean addedNameExist = memoNameList.contains(memoName);
                    // 重複がないとき
                    if (!addedNameExist) {
                        memoNameList.add(memoName);
                        arrayAdapter.notifyDataSetChanged();

                        saveList(MemoActivity.this, APP_MEMO_KEY, memoNameList);


                        memoNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                                Object clickItemObj = adapterView.getAdapter().getItem(index);
                                Toast.makeText(getApplicationContext(), "You clicked " + clickItemObj.toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                        alertDialog.hide();
                    } else {
                        Snackbar.make(v, "そのタイトルは既に存在します", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
            }

        });


        Button cancelMemoNameButton = inputMemoNameView.findViewById(R.id.button_cancel_memoname);
        cancelMemoNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }


    // 設定値 ArrayList<String> を保存（Context は Activity や Application や Service）
    public static void saveList(Context ctx, String key, ArrayList<String> list) {
        JSONArray jsonAry = new JSONArray();
        for(int i=0; i<list.size(); i++) {
            jsonAry.put(list.get(i));
        }
        SharedPreferences prefs = ctx.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, jsonAry.toString());
        Log.d("DEBUG", jsonAry.toString());
        editor.apply();
    }

    // 設定値 ArrayList<String> を取得（Context は Activity や Application や Service）
    public static ArrayList<String> loadList(Context ctx, String key) {
        ArrayList<String> list = new ArrayList<String>();
        SharedPreferences prefs = ctx.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        String strJson = prefs.getString(key, ""); // 第２引数はkeyが存在しない時に返す初期値
        if(!strJson.equals("")) {
            try {
                JSONArray jsonAry = new JSONArray(strJson);
                for(int i=0; i<jsonAry.length(); i++) {
                    list.add(jsonAry.getString(i));
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
}
