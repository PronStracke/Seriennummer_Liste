package com.kassenstracke.pron.apps.seriennummerliste;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    private int iTyp = 0;
    private String sTyp = "Liste";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainListView = (ListView) findViewById(R.id.mainListView);
        String[] nos = new String[]{"123", "234"};
        ArrayList<String> noList = new ArrayList<String>();
        noList.addAll(Arrays.asList(nos));

        listAdapter = new ArrayAdapter<>(this, R.layout.simplerow, noList);

        listAdapter.add("345");

        mainListView.setAdapter(listAdapter);

        listAdapter.clear();

        TextView myTitel = (TextView) findViewById(R.id.textView);
        myTitel.setText(sTyp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                try {
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        if (id == R.id.action_clear_list) {
            listAdapter.clear();
            return true;
        }

        if (id == R.id.action_set_wareneingang) {
            iTyp = getResources().getInteger(R.integer.typ_wareneingang);
            sTyp = getResources().getString(R.string.typ_wareneingang);
            TextView myTitel = (TextView) findViewById(R.id.textView);
            myTitel.setText(sTyp);
            return true;
        }

        if (id == R.id.action_set_lieferschein) {
            iTyp = getResources().getInteger(R.integer.typ_lieferschein);
            sTyp = getResources().getString(R.string.typ_lieferschein);
            TextView myTitel = (TextView) findViewById(R.id.textView);
            myTitel.setText(sTyp);
            return true;
        }

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}


        if (id == R.id.action_do_print) {
            // Do some printing...
            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
            String jobName = this.getString(R.string.app_name) + " Document";
            printManager.print(jobName, new MyPrintDocumentAdapter(this), null);
            return true;
        }

        if (id == R.id.action_close) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                listAdapter.add(contents);
                // Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                // toast.setGravity(Gravity.TOP, 25, 400);
                // toast.show();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kassenstracke.pron.apps.seriennummerliste/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.kassenstracke.pron.apps.seriennummerliste/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
        Context context;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalpages = 1;

        public MyPrintDocumentAdapter(Context context) {
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal, LayoutResultCallback callback,
                             Bundle extras) {
            myPdfDocument = new PrintedPdfDocument(context, newAttributes);

            pageHeight = newAttributes.getMediaSize().getHeightMils() / 1000 * 72; // 1/72 inch
            pageWidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

            if (cancellationSignal.isCanceled()) {
                callback.onLayoutCancelled();
                return;
            }
            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder =
                        new PrintDocumentInfo.Builder("print_output.pdf")
                                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                                .setPageCount(totalpages);
                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal, WriteResultCallback callback) {
            for (int i = 0; i < totalpages; i++) {
                if (pageInRange(pages, i)) {
                    PdfDocument.PageInfo newPage =
                            new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();
                    PdfDocument.Page page = myPdfDocument.startPage(newPage);

                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    drawPage(page, i);
                    myPdfDocument.finishPage(page);
                }
            }
            try {
                myPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }
            callback.onWriteFinished(pages);

        }

        private boolean pageInRange(PageRange[] pageRanges, int page) {
            for (int i = 0; i < pageRanges.length; i++) {
                if ((page >= pageRanges[i].getStart()) && (page <= pageRanges[i].getEnd()))
                    return true;
            }
            return false;
        }

        private void drawPage(PdfDocument.Page page, int pagenumber)
        {
            Canvas canvas = page.getCanvas();

            pagenumber++;

            int titleBaseLine = 72;
            int leftMargin = 54;
            int aktuelleZeile = titleBaseLine;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText(sTyp, leftMargin, titleBaseLine, paint);
            aktuelleZeile+=40;
            paint.setTextSize(20);
            canvas.drawText("Seriennummern:", leftMargin, aktuelleZeile, paint);
            aktuelleZeile+=25;
            paint.setTextSize(14);
            for (int i=0; i<listAdapter.getCount(); i++){
                canvas.drawText(listAdapter.getItem(i), leftMargin, aktuelleZeile, paint);
                aktuelleZeile+=20;
            }
        }
    }
}
