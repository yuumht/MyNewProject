package com.example.yuuos.newmyapplication;

import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    List<String> memoNameList = new ArrayList<String>();

                    boolean addedNameExist = false;

                    ListView memoNameListView = MemoActivity.this.findViewById(R.id.list_view_memo_name);


                    if (memoNameListView != null) {
                        ListAdapter listAdapter = memoNameListView.getAdapter();

                        if (listAdapter != null) {
                            int itemCount = listAdapter.getCount();
                            for (int i = 0; i < itemCount; i++) {
                                Object item = listAdapter.getItem(i);


                                if (item != null) {
                                    if (item instanceof String) {

                                        String itemString = (String) item;
                                        // 同じがどうかチェック
                                        if (itemString.equalsIgnoreCase(memoName)) {
                                            addedNameExist = true;
                                        }

                                        memoNameList.add(((String) item));
                                    }
                                }



                            }
                        }
                    }

                    // 重複がないとき
                    if (!addedNameExist) {
                        memoNameList.add(memoName);

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, memoNameList);
                        memoNameListView.setAdapter(arrayAdapter);

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
}
