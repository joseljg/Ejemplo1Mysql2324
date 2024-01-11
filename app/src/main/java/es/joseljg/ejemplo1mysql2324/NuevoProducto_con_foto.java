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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.joseljg.ejemplo1mysql2324.clases.ConfiguracionDB;
import es.joseljg.ejemplo1mysql2324.clases.Producto;
import es.joseljg.ejemplo1mysql2324.utilidades.ImagenesBlobBitmap;

public class NuevoProducto_con_foto extends AppCompatActivity {

    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    private EditText edt_nuevo_idp;
    private EditText edt_nuevo_nombrep;
    private EditText edt_nuevo_cantidadp;
    private EditText edt_nuevo_preciop;

    ImageView img_nuevop_foto;

    @Override
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
        setContentView(R.layout.activity_nuevo_producto_con_foto);
        edt_nuevo_idp = (EditText) findViewById(R.id.edt_nuevo_idp_foto);
        edt_nuevo_nombrep = (EditText) findViewById(R.id.edt_nuevo_nombrep_foto);
        edt_nuevo_cantidadp = (EditText) findViewById(R.id.edt_nuevo_cantidadp_foto);
        edt_nuevo_preciop = (EditText) findViewById(R.id.edt_nuevo_preciop_foto);
        img_nuevop_foto = (ImageView) findViewById(R.id.img_nuevop_foto);
        //-------------------------------------------------------------------------

    }


    public void insertarProducto_foto(View view)
    {
        String identificador = String.valueOf(edt_nuevo_idp.getText()).trim();
        String nombre = String.valueOf(edt_nuevo_nombrep.getText()).trim();
        int cantidad = Integer.valueOf(String.valueOf(edt_nuevo_cantidadp.getText()).trim());
        double precio = Double.valueOf(String.valueOf(edt_nuevo_preciop.getText()).trim());
        Producto p1 = new Producto(identificador,nombre,cantidad,precio);
        //------------------------------------------------------------------------
        insertarProductodb(p1);

        //---------------------- codigo para añadir la foto al MYSQL ------------------------------
             if(imagen_seleccionada != null) {
                 insertarFotodb(p1.getIdProducto(),img_nuevop_foto);
             }

    }

    //-----------------------------------------------------------------------------------------------------------------

    private void insertarProductodb(Producto p1) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/insertar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos insertados")) {
                            Toast.makeText(NuevoProducto_con_foto.this, "registrado correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(NuevoProducto_con_foto.this, "Error no se puede registrar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NuevoProducto_con_foto.this,error.getMessage(),Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(NuevoProducto_con_foto.this);
        requestQueue.add(request);
    }
    //-----------------------------------------------------------------------------------------------------------------
    private void insertarFotodb(String idProducto, ImageView imgNuevopFoto) {
      StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/insertar_foto.php",
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         if (response.equalsIgnoreCase("foto insertada")) {
                             Toast.makeText(NuevoProducto_con_foto.this, "registrado correctamente", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(), MainActivity.class));
                             finish();
                         } else {
                             Toast.makeText(NuevoProducto_con_foto.this, "Error no se subir la foto", Toast.LENGTH_SHORT).show();
                         }
                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(NuevoProducto_con_foto.this,error.getMessage(),Toast.LENGTH_SHORT).show();
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


         RequestQueue requestQueue = Volley.newRequestQueue(NuevoProducto_con_foto.this);
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
                img_nuevop_foto.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}