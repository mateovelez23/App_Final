package com.mateo_velez.app_final;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity {

    private String correoR,contraseñaR,nombreR, foto,log;
    int duration = Toast.LENGTH_SHORT;
    private TextView tvcorreo,tvnombre;
    private ImageView imagen_perfil;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvcorreo=(TextView) findViewById(R.id.tNombre);
        tvnombre=(TextView) findViewById(R.id.tvCorreo);
        imagen_perfil=(ImageView) findViewById(R.id.imagen_perfil);

        Bundle extras= getIntent().getExtras();

        if (extras!=null){
            log=extras.getString("log");
            if (log.equals("registro")){
                correoR=extras.getString("correo");
                contraseñaR=extras.getString("contraseña");
                foto=extras.getString("foto");
                nombreR=extras.getString("nombre");
            }
            else {
                correoR=extras.getString("correo");
                foto=extras.getString("foto");
                nombreR=extras.getString("nombre");
            }
        }

        if (foto!=null) {
            loadImageFromUrl(foto);
        }
        else
            Toast.makeText(getApplicationContext(),"Su cuenta no tiene foto", Toast.LENGTH_SHORT).show();

        if (correoR!=null)
            tvcorreo.setText("Correo: "+correoR);

        tvnombre.setText("Nombre: "+nombreR);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void loadImageFromUrl(String foto) {
        Picasso.with(this).load(foto).placeholder(R.mipmap.ic_launcher)
                .into(imagen_perfil, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuperfil,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        Intent intent;

        switch (id){

            case R.id.mPrincipal:
                if(log.equals("facebook")){
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("nombre",nombreR);
                    intent.putExtra("foto",foto);
                    intent.putExtra("log",log);
                    startActivity(intent);
                }
                else if(log.equals("google")){
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("nombre",nombreR);
                    intent.putExtra("foto",foto);
                    intent.putExtra("log",log);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("nombre",nombreR);
                    intent.putExtra("contraseña",contraseñaR);
                    intent.putExtra("log",log);
                    startActivity(intent);
                }
                break;

            case R.id.mCerrar:

                if(log.equals("facebook")){
                    intent=new Intent(this,LoginActivity.class);
                    LoginManager.getInstance().logOut();// cerrar sesion en facebook
                    intent.putExtra("log",log);
                    Toast.makeText(getApplicationContext(),"Saliendo de facebook", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
                else if(log.equals("google")){
                    signOut(); //cerrar sesion en google
                    intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("log",log);
                    startActivity(intent);
                    finish();
                }
                else {
                    intent=new Intent(this,LoginActivity.class);
                    intent.putExtra("correo",correoR);
                    intent.putExtra("contraseña",contraseñaR);
                    intent.putExtra("nombre",nombreR);
                    intent.putExtra("log",log);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;

        }

        return super.onOptionsItemSelected(item);

    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Context context = getApplicationContext();
                        CharSequence text = "Saliendo de google";
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
    }
}
