package es.joseljg.ejemplo1mysql2324;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.joseljg.ejemplo1mysql2324.clases.ConfiguracionDB;
import es.joseljg.ejemplo1mysql2324.clases.Producto;
import es.joseljg.ejemplo1mysql2324.recyclerview.ListaProductosAdapter;

public class MostrarProductosActivity extends AppCompatActivity {

    public  ArrayList<Producto> productos;
    public  ListaProductosAdapter adapter;
    private RecyclerView rv_productos;
    private EditText edt_buscar_productos;

    protected void onStart() {
        super.onStart();
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        String email = sharedpreferences.getString(MainActivity.EMAIL_KEY, null);
        String password = sharedpreferences.getString(MainActivity.PASSWORD_KEY, null);
        if(email== null || password==null)
        {
            Toast.makeText(getApplicationContext(), "debes loguearte", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos);
        //--------------------------------------------------------
        rv_productos = (RecyclerView) findViewById(R.id.rv_productos1);
        edt_buscar_productos = (EditText) findViewById(R.id.edt_buscar_productos);
        productos = new ArrayList<Producto>();

        //-----------------------------------------------------------
        adapter = new ListaProductosAdapter(this,productos);
        rv_productos.setAdapter(adapter);
        mostrarProductos();

        //-------------------------------------------------------------
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            rv_productos.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            // In portrait
            rv_productos.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void mostrarProductos() {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ+ "/mostrar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        productos.clear();
                        try {

                                JSONObject jsonObject = new JSONObject(response);
                                String exito=jsonObject.getString("exito");
                                JSONArray jsonArray =jsonObject.getJSONArray("productos");

                                if (exito.equals("1")){
                                    for (int i=0;i<jsonArray.length();i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String idProducto = object.getString("idProducto");
                                        String nombre = object.getString("nombre");
                                        int cantidad = Integer.valueOf(object.getString("cantidad"));
                                        double precio = Double.valueOf(object.getString("precio"));
                                        Producto p1 = new Producto(idProducto, nombre, cantidad, precio);
                                        productos.add(p1);
                                    }
                                    adapter.setProductos(productos);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(MostrarProductosActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i("mysql1","error al pedir los datos");
            }
        }
        ){


        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarProductosActivity.this);
        requestQueue.add(request);
    }

    //---------------------------------------------------------------------------------
    public void buscarProductos(View view) {

        String texto = String.valueOf(edt_buscar_productos.getText());
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ+ "/mostrar_.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            productos.clear();
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                String exito=jsonObject.getString("exito");
                                JSONArray jsonArray =jsonObject.getJSONArray("productos");

                                if (exito.equals("1")){
                                    for (int i=0;i<jsonArray.length();i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String idProducto = object.getString("idProducto");
                                        String nombre = object.getString("nombre");
                                        int cantidad = Integer.valueOf(object.getString("cantidad"));
                                        double precio = Double.valueOf(object.getString("precio"));
                                        Producto p1 = new Producto(idProducto, nombre, cantidad, precio);
                                        if(p1.getNombre().contains(texto)) {
                                            productos.add(p1);
                                        }
                                    }
                                    adapter.setProductos(productos);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MostrarProductosActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.i("mysql1","error al pedir los datos");
                }
            }
            ){


            };
            RequestQueue requestQueue = Volley.newRequestQueue(MostrarProductosActivity.this);
            requestQueue.add(request);
        }
}