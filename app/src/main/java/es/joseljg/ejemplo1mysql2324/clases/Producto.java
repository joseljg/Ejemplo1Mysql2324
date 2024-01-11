package es.joseljg.ejemplo1mysql2324.clases;

import java.io.Serializable;
import java.util.Objects;

public class Producto implements Serializable {
    // atributos
    private String idProducto;
    private String nombre;
    private int cantidad;
    private double precio;

    public Producto(String idProducto, String nombre, int cantidad, double precio)
    {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Producto() {
        this.idProducto = "";
        this.nombre = "";
        this.cantidad = 0;
        this.precio = 0.0;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return idProducto.equals(producto.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto='" + idProducto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
