package es.joseljg.ejemplo1mysql2324;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import java.util.HashMap;
import java.util.Map;

import es.joseljg.ejemplo1mysql2324.clases.ConfiguracionDB;

public class MainActivity extends AppCompatActivity {
    public static final String EMAIL_KEY = "es.joselg.ejemplo1mysql2324.mainactivity.email" ;
    public static final String PASSWORD_KEY = "es.joselg.ejemplo1mysql2324.mainactivity.password" ;
    public static final String SHARED_PREFS ="es.joselg.ejemplo1mysql2324.mainactivity.shared_prefs" ; ;
    // FirebaseDatabase database;
    EditText edt_login_email;
    EditText edt_login_password;
    SharedPreferences sharedpreferences;
  //  private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_login_email = (EditText) findViewById(R.id.edt_login_email);
        edt_login_password = (EditText) findViewById(R.id.edt_login_password);
        //------------------------------------
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
     //   mAuth = FirebaseAuth.getInstance();


    }


    public void insertarProductoConFoto(View view)
    {
        Intent intent = new Intent(this,NuevoProducto_con_foto.class);
        startActivity(intent);
    }
    public void mostrarProductos(View view)
    {
        Intent intent = new Intent(this,MostrarProductosActivity.class);
        startActivity(intent);
    }



    public void entrar(View view)
    {
        String email = String.valueOf(edt_login_email.getText());
        String password = String.valueOf(edt_login_password.getText());
        //-----------------------------------
        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/loguear_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("autenticacion ok")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(EMAIL_KEY, email);
                            editor.putString(PASSWORD_KEY, password);
                            editor.apply();

                            Toast.makeText(MainActivity.this, "se ha logueado correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MostrarProductosActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "error en la autenticacion", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }



    public void registrarse(View view)
    {
        String email = String.valueOf(edt_login_email.getText());
        String password = String.valueOf(edt_login_password.getText());
        //-----------------------------------

        StringRequest request =new StringRequest(Request.Method.POST, ConfiguracionDB.DIRECCION_URL_RAIZ + "/registrar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("registro ok")) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(EMAIL_KEY, email);
                            editor.putString(PASSWORD_KEY, password);
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MostrarProductosActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "error al registrarse", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),String.valueOf(error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String>params=new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }



    //----------------------------------------
    public void salir(View view)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this," ha cerrado sesión ", Toast.LENGTH_LONG).show();
    }
}