package es.joseljg.ejemplo1mysql2324;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.joseljg.ejemplo1mysql2324.clases.ConfiguracionDB;
import es.joseljg.ejemplo1mysql2324.clases.Producto;
import es.joseljg.ejemplo1mysql2324.recyclerview.ProductoViewHolder;
import es.joseljg.ejemplo1mysql2324.utilidades.ImagenesBlobBitmap;

public class MostrarDetallesProductos extends AppCompatActivity {

    private EditText edt_detalles_idp;
    private EditText edt_detalles_nombrep;
    private EditText edt_detalles_cantidad;
    private EditText edt_detalles_preciop;

    private ImageView img_detalles_imagenp;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    private Producto p;

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
        setContentView(R.layout.activity_mostrar_detalles_productos);

        //----------------------------------------------------------------
        edt_detalles_idp = (EditText) findViewById(R.id.edt_detalles_idp);
        edt_detalles_cantidad = (EditText) findViewById(R.id.edt_detalles_cantidadp);
        edt_detalles_nombrep = (EditText) findViewById(R.id.edt_detalles_nombrep);
        edt_detalles_preciop = (EditText) findViewById(R.id.edt_detalles_preciop);
        img_detalles_imagenp = (ImageView) findViewById(R.id.img_detalles_imagenp);
        //----------------------------------------------------------------
        Intent intent = getIntent();
        if(intent != null)
        {
            p = (Producto) intent.getSerializableExtra(ProductoViewHolder.EXTRA_DETALLES_PRODUCTO);
            //---------------------- cargo la foto  ----------------------------------------
            byte[] fotobinaria = (byte[]) intent.getByteArrayExtra(ProductoViewHolder.EXTRA_DETALLES_IMAGEN_PRODUCTO);
            Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobinaria, ConfiguracionDB.ancho_imagen,ConfiguracionDB.alto_imagen);
            img_detalles_imagenp.setImageBitmap(fotobitmap);
        }
        else{
            p = new Producto();
        }
        //-----------------------------------------------------------------
        edt_detalles_idp.setText(p.getIdProducto());
        edt_detalles_nombrep.setText(p.getNombre());
        edt_detalles_cantidad.setText(String.valueOf(p.getCantidad()));
        edt_detalles_preciop.setText(String.valueOf(p.getPrecio()));
    }

    //----------------------------------------------------
    public void borrar_producto(View view)
    {
        borrar_fotodb(p.getIdProducto());
        borrar_productodb(p.getIdProducto());
    }

    private void borrar_productodb(String idProducto) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/eliminar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos eliminados")) {
                            Toast.makeText(MostrarDetallesProductos.this, "datos eliminados", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MostrarDetallesProductos.this, "Error no se puede eliminar el dato", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesProductos.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idProducto",idProducto);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesProductos.this);
        requestQueue.add(request);
    }

//----------------------------------------------------------------------------
    private void borrar_fotodb(String idProducto) {
    StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/eliminar_foto.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("datos eliminados")) {
                        Toast.makeText(MostrarDetallesProductos.this, "datos eliminados", Toast.LENGTH_SHORT).show();
                      //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
                      //  finish();
                    } else {
                        Toast.makeText(MostrarDetallesProductos.this, "Error no se puede eliminar el dato", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MostrarDetallesProductos.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
        }
    }
    ){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Map<String,String>params=new HashMap<>();
            params.put("idProducto",idProducto);
            return params;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesProductos.this);
    requestQueue.add(request);
}

    private void insertarFotodb(String idProducto, ImageView imgNuevopFoto) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/insertar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("foto insertada")) {
                            Toast.makeText(MostrarDetallesProductos.this, "registrado correctamente", Toast.LENGTH_SHORT).show();
                     //       startActivity(new Intent(getApplicationContext(), MainActivity.class));
                     //       finish();
                        } else {
                            Toast.makeText(MostrarDetallesProductos.this, "Error no se subir la foto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesProductos.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("idProducto",idProducto);
                imgNuevopFoto.buildDrawingCache();
                Bitmap foto_bm = imgNuevopFoto.getDrawingCache();
                byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
                String fotostring = ImagenesBlobBitmap.byte_to_string(fotobytes);
                params.put("foto",fotostring);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesProductos.this);
        requestQueue.add(request);
    }

    //---------------------------------------------------
    public void editar_producto(View view)
    {
        String id = String.valueOf(edt_detalles_idp.getText());
        String nombre = String.valueOf(edt_detalles_nombrep.getText());
        int cantidad = Integer.valueOf(String.valueOf(edt_detalles_cantidad.getText()));
        double precio = Double.valueOf(String.valueOf(edt_detalles_preciop.getText()));
        Producto p1 = new Producto(id,nombre,cantidad,precio);

        if(imagen_seleccionada != null) {
            borrar_fotodb(p.getIdProducto());
            insertarFotodb(p.getIdProducto(),img_detalles_imagenp);
        }
        editar_productodb(p1);

    }

    private void editar_productodb(Producto p1) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/actualizar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos actualizados")) {
                            Toast.makeText(MostrarDetallesProductos.this, "actualizado correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MostrarDetallesProductos.this, "Error no se puede actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesProductos.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idProducto",p1.getIdProducto());
                params.put("nombre",p1.getNombre());
                params.put("cantidad",String.valueOf(p1.getCantidad()));
                params.put("precio",String.valueOf(p1.getPrecio()));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesProductos.this);
        requestQueue.add(request);
    }
    private void editar_fotodb(Producto p1, ImageView img_detalles_imagenp) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/actualizar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos actualizados")) {
                            Toast.makeText(MostrarDetallesProductos.this, "actualizado correctamente", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            //finish();
                        } else {
                            Toast.makeText(MostrarDetallesProductos.this, "Error no se puede actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MostrarDetallesProductos.this,String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("idProducto",p1.getIdProducto());
                img_detalles_imagenp.buildDrawingCache();
                Bitmap foto_bm = img_detalles_imagenp.getDrawingCache();
                byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
                String fotostring = ImagenesBlobBitmap.byte_to_string(fotobytes);
                params.put("foto",fotostring);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MostrarDetallesProductos.this);
        requestQueue.add(request);
    }
    //--------------------------------------------------------------------------



    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------

    public void cambiar_imagen(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                img_detalles_imagenp.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}