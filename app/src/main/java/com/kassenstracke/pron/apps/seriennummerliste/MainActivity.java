package com.kassenstracke.pron.apps.seriennummerliste;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainListView = (ListView)findViewById(R.id.mainListView);
        String[] nos = new String[] {"123", "234"};
        ArrayList<String> noList = new ArrayList<String>();
        noList.addAll(Arrays.asList(nos));

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, noList);

        listAdapter.add("345");

        mainListView.setAdapter(listAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                try{
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    Log.e("onCreate", "Scanner not found!", anfe);
                }
            }
        });

        //setContentView(R.layout.main);
        //try {
        //    Button scanner = (Button)findViewById(R.id.scanner);
        //    scanner.setOnClickListener(new View.OnClickListener() {
        //        public void onClick(View v) {
        //            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        //            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        //            startActivityForResult(intent, 0);
        //        }
        //    });
        //
        //    Button scanner2 = (Button)findViewById(R.id.scanner2);
        //    scanner2.setOnClickListener(new View.OnClickListener() {
        //        public void onClick(View v) {
        //            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        //            // intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
        //            startActivityForResult(intent, 0);
        //        }
        //    });
        //} catch (ActivityNotFoundException anfe) {
        //    Log.e("onCreate", "Scanner not found", anfe);
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_clear_list){
            listAdapter.clear();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.action_close){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0) {
            if(resultCode == RESULT_OK){
                String contents = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                listAdapter.add(contents);
                // Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                // toast.setGravity(Gravity.TOP, 25, 400);
                // toast.show();
            } else if(resultCode==RESULT_CANCELED){
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        }
    }
}
