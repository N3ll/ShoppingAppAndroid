package org.projects.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {


    ProductsAdapter adapter;
    ListView listView;
    ArrayList<ProductsInfo> bag = new ArrayList<ProductsInfo>();
    String product;
    String number;

    static MyDialogFragment dialog;
    static Context context;


    public ProductsAdapter getMyAdapter() {
        return adapter;
    }

    public ArrayList<ProductsInfo> getBag() {
        return this.bag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.context = this;

        listView = (ListView) findViewById(R.id.list);
        adapter = new ProductsAdapter(this, bag);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addField = (EditText) findViewById(R.id.addField);
                product = addField.getText().toString();

                ProductsInfo p = new ProductsInfo(product, number, false);
                bag.add(p);
                addField.setText("");
                getMyAdapter().notifyDataSetChanged();
            }
        });

        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                number = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                number = "1";
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("bag", bag);
        EditText addField = (EditText) findViewById(R.id.addField);
        outState.putString("product", addField.getText().toString());

        Spinner addNumber = (Spinner) findViewById(R.id.spinner1);
        outState.putInt("spinner", addNumber.getSelectedItemPosition());
    }

    //this is called when our activity is recreated, but
    //AFTER our onCreate method has been called
    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        Log.i("log", "in onRestoreInstanceState");
        super.onRestoreInstanceState(savedState);
        EditText addProduct = (EditText) findViewById(R.id.addField);
        this.product = savedState.getString("product");
        addProduct.setText(this.product);

        Spinner addNumber = (Spinner) findViewById(R.id.spinner1);
        addNumber.setSelection(savedState.getInt("spinner"));

        this.bag.addAll((ArrayList) savedState.getParcelableArrayList("bag"));
        for (ProductsInfo p : this.bag) {
            Log.i("log", p.toString());
        }
        getMyAdapter().notifyDataSetChanged();
    }

    public void clearShoppingList(View view) {
        dialog = new MyDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("bag", bag);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "MyFragment");
    }

    public void handleDialogClose(DialogInterface dialog) {
        getMyAdapter().notifyDataSetChanged();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyDialog extends MyDialogFragment {


        @Override
        protected void positiveClick() {
            ArrayList<ProductsInfo> bag = getArguments().getParcelableArrayList("bag");
            bag.clear();

            Toast toast = Toast.makeText(context,
                    "Your list was deleted!" +
                            "" +
                            "", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        protected void negativeClick() {
            //Here we override the method and can now do something
            Toast toast = Toast.makeText(context,
                    "Phiu! Your list is save", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
