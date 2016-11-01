package org.projects.shoppinglist;

import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nelly on 9/15/16.
 */
public class ProductsAdapter extends ArrayAdapter<ProductsInfo> {
    Context context;
    ArrayList<ProductsInfo> products;
    ProductsInfo lastDeletedProduct;
    int lastDeletedPosition;

    public ProductsAdapter(Context context, ArrayList<ProductsInfo> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    public ProductsInfo getLastDeletedProduct() {
        return this.lastDeletedProduct;
    }

    public int getLastDeletedPosition() {
        return this.lastDeletedPosition;
    }

    @Override
    public View getView(final int position, View convertView,final ViewGroup parent) {
        // Get the data item for this position
        final ProductsInfo product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product, parent, false);
        }


        final CheckBox productChecked = (CheckBox) convertView.findViewById(R.id.product_checked);
        productChecked.setText(product.getName() + " " + product.getNumber());

        productChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productChecked.isChecked()) {
                    productChecked.setPaintFlags(productChecked.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    System.out.println("box checked " + position);
                    products.get(position).setIsCrossed(true);
                    System.out.println(products.get(position).toString());
                } else {
                    productChecked.setPaintFlags(productChecked.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    products.get(position).setIsCrossed(false);
                }
            }
        });

        if (product.getIsCrossed()) {
            productChecked.setChecked(true);
            productChecked.setPaintFlags(productChecked.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        final Button btn = (Button) convertView.findViewById(R.id.btnDel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lastDeletedPosition = position;
                lastDeletedProduct = products.get(lastDeletedPosition);
                products.remove(position);
                notifyDataSetChanged();


                System.out.println("Parent "+parent);
                Snackbar snackbar = Snackbar
                        .make(parent, "Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                products.add(getLastDeletedPosition(), getLastDeletedProduct());
                                System.out.println("Product "+getLastDeletedProduct().toString());
                                System.out.println("Position "+getLastDeletedPosition());
                                System.out.println("Parent "+parent);
                                notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                snackbar.show();
            }
        });

        return convertView;
    }


}
