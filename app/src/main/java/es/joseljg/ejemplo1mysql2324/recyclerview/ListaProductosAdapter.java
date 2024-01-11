package es.joseljg.ejemplo1mysql2324.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import es.joseljg.ejemplo1mysql2324.MostrarProductosActivity;
import es.joseljg.ejemplo1mysql2324.R;
import es.joseljg.ejemplo1mysql2324.clases.ConfiguracionDB;
import es.joseljg.ejemplo1mysql2324.clases.Producto;
import es.joseljg.ejemplo1mysql2324.utilidades.ImagenesBlobBitmap;
import es.joseljg.ejemplo1mysql2324.utilidades.ImagenesFirebase;


public class ListaProductosAdapter extends RecyclerView.Adapter<ProductoViewHolder> {
    // atributos
    private Context contexto = null;
    private ArrayList<Producto> productos = null;
    private LayoutInflater inflate = null;


    public ListaProductosAdapter(Context contexto, ArrayList<Producto> productos ) {
        this.contexto = contexto;
        this.productos = productos;
        inflate =  LayoutInflater.from(this.contexto);
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ArrayList<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflate.inflate(R.layout.item_rv_productos,parent,false);
        ProductoViewHolder evh = new ProductoViewHolder(mItemView,this);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto p = this.getProductos().get(position);
        //----------------------------------------------------------------------
        holder.getTxt_item_nombre().setText("nombre: " + p.getNombre());
        holder.getTxt_item_cantidad().setText("cantidad: " + String.valueOf(p.getCantidad()));
        holder.getTxt_item_precio().setText("precio: " + String.valueOf(p.getPrecio()));
        //----------- codigo para mostrar la foto del producto -----------------------
        String idProducto = p.getIdProducto();
        descargarImagen(idProducto, holder.getImg_item_producto(), contexto);
    }

    private void descargarImagen(String idProducto, ImageView img_foto, Context contexto) {
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ+ "/mostrar_foto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String exito=jsonObject.getString("exito");
                            JSONArray jsonArray =jsonObject.getJSONArray("productos_fotos");
                            if (exito.equals("1")){
                                int cuantos = jsonArray.length();
                                if (cuantos > 0) {
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    String idProducto = object.getString("idProducto");
                                    String foto = object.getString("foto");
                                    byte[] fotobyte = ImagenesBlobBitmap.string_to_byte(foto);
                                    Bitmap fotobitmap = ImagenesBlobBitmap.bytes_to_bitmap(fotobyte, ConfiguracionDB.ancho_imagen,ConfiguracionDB.alto_imagen);
                                    img_foto.setImageBitmap(fotobitmap);
                                }
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
                Log.i("mysql1","error al pedir la foto");
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
        RequestQueue requestQueue = Volley.newRequestQueue(contexto);
        requestQueue.add(request);
    }


    @Override
    public int getItemCount() {
        return this.productos.size();
    }

    public void addproducto(Producto productoAñadido) {
        productos.add(productoAñadido);
       notifyDataSetChanged();
    }
}
