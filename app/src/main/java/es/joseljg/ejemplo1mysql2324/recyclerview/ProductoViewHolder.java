package es.joseljg.ejemplo1mysql2324.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.joseljg.ejemplo1mysql2324.MostrarDetallesProductos;
import es.joseljg.ejemplo1mysql2324.MostrarProductosActivity;
import es.joseljg.ejemplo1mysql2324.R;
import es.joseljg.ejemplo1mysql2324.clases.Producto;
import es.joseljg.ejemplo1mysql2324.utilidades.ImagenesBlobBitmap;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String EXTRA_DETALLES_PRODUCTO = "es.joseljg.ejemplo1mysql2324.productoviewholder.detalles_producto" ;
    public static final String EXTRA_DETALLES_IMAGEN_PRODUCTO = "es.joseljg.ejemplo1mysql2324.productoviewholder.detalles_producto_imagen";

    // atributos
    private TextView txt_item_nombre;
    private TextView txt_item_cantidad;
    private TextView txt_item_precio;
    private ImageView img_item_producto;
    //-------------------------------------
    private ListaProductosAdapter lpa;
    private Context contexto;

    public TextView getTxt_item_cantidad() {
        return txt_item_cantidad;
    }

    public void setTxt_item_cantidad(TextView txt_item_cantidad) {
        this.txt_item_cantidad = txt_item_cantidad;
    }

    public TextView getTxt_item_precio() {
        return txt_item_precio;
    }

    public void setTxt_item_precio(TextView txt_item_precio) {
        this.txt_item_precio = txt_item_precio;
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ProductoViewHolder(@NonNull View itemView, ListaProductosAdapter listaProductosAdapter) {
        super(itemView);
        txt_item_nombre = (TextView) itemView.findViewById(R.id.txt_item_nombre);
        txt_item_cantidad = (TextView) itemView.findViewById(R.id.txt_item_cantidad);
        txt_item_precio = (TextView) itemView.findViewById(R.id.txt_item_precio);
        img_item_producto = (ImageView) itemView.findViewById(R.id.img_item_producto);
        //-----------------------------------------------------------------------------
        lpa = listaProductosAdapter;
        itemView.setOnClickListener(this);
    }

    public ImageView getImg_item_producto() {
        return img_item_producto;
    }

    public void setImg_item_producto(ImageView img_item_producto) {
        this.img_item_producto = img_item_producto;
    }

    public TextView getTxt_item_nombre() {
        return txt_item_nombre;
    }

    public void setTxt_item_nombre(TextView txt_item_nombre) {
        this.txt_item_nombre = txt_item_nombre;
    }

    public ListaProductosAdapter getLpa() {
        return lpa;
    }

    public void setLpa(ListaProductosAdapter lpa) {
        this.lpa = lpa;
    }

    @Override
    public void onClick(View view) {
        int posicion = getLayoutPosition();
        Producto p = lpa.getProductos().get(posicion);
        Intent intent = new Intent(lpa.getContexto(), MostrarDetallesProductos.class);
        intent.putExtra(EXTRA_DETALLES_PRODUCTO,p);
        img_item_producto.buildDrawingCache();
        Bitmap foto_bm = img_item_producto.getDrawingCache();
        byte[] fotobytes = ImagenesBlobBitmap.bitmap_to_bytes_png(foto_bm);
        intent.putExtra(EXTRA_DETALLES_IMAGEN_PRODUCTO,fotobytes );
        //intent.putExtra(EXTRA_POSICION_CASILLA, posicion);

        lpa.getContexto().startActivity(intent);
      //  ((MostrarProductosActivity)lpa.getContexto()).finish();

    }
}
