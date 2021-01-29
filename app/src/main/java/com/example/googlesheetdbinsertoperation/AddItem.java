package com.example.googlesheetdbinsertoperation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddItem extends AppCompatActivity implements View.OnClickListener{

    EditText editTextItemName,editTextBrand, editTextPrice;
    Button buttonAddItem;
    String link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextItemName = (EditText)findViewById(R.id.et_item_name);
        editTextBrand = (EditText)findViewById(R.id.et_brand);
        editTextPrice = findViewById(R.id.et_price);

        buttonAddItem = (Button)findViewById(R.id.btn_add_item);
        buttonAddItem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==buttonAddItem){
            addItemToSheet();
        }
    }

    //This is the part where data is transafeered from Your Android phone to Sheet by using HTTP Rest API calls

    private void addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String name = editTextItemName.getText().toString().trim();
        final String brand = editTextBrand.getText().toString().trim();
        final String price = editTextPrice.getText().toString().trim();


        // Main Google App Script link

        link = "https://script.google.com/macros/s/AKfycbxFX6S1q34ZVLtpmEty9gQf_Lmnxw8TN_7puF82knd0k-obDx64/exec";

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(AddItem.this, Html.fromHtml(response),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("itemName",name);
                parmas.put("brand",brand);
                parmas.put("price",price);

                return parmas;
            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }
}