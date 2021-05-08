package com.example.testnotificationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    EditText firstName,secondName,email,password;
    Button signUpButton,goToLogin,goToAddOrUpdateToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName=(EditText)findViewById(R.id.firstName);
        secondName=(EditText)findViewById(R.id.secondName);
        email=(EditText)findViewById(R.id.emailSignUp);
        password=(EditText)findViewById(R.id.passwordSignUp);
        signUpButton=(Button) findViewById(R.id.signUp);
        goToLogin=(Button) findViewById(R.id.GoToLogin);
        goToAddOrUpdateToken=(Button) findViewById(R.id.GoToAddOrUpdateToken);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data="{"+
                        "\"firstName\""   + ":"+  "\""+firstName.getText().toString()+"\","+
                        "\"secondName\""  + ":"+  "\""+secondName.getText().toString()+"\","+
                        "\"email\""       + ":"+  "\""+email.getText().toString()+"\","+
                        "\"password\""    + ":"+   "\""+password.getText().toString()+"\""+
                        "}";
                Submit(data);
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),loginActvtity.class);
                startActivity(i);
            }
        });

        goToAddOrUpdateToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AddOrUpdateToken.class);
                startActivity(i);
            }
        });

    }


    private void Submit(String data) {
        String savedata=data;
        String URL="https://mcc-users-api.herokuapp.com/add_new_user";
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "requestQueue: "+requestQueue);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres = new JSONObject(response);
                    Toast.makeText(getApplicationContext(),objres.toString(),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Server Error ",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public String getBodyContentType(){return "application/json; charset=utf-8";}
            @Override
            public byte[] getBody() throws AuthFailureError {
                try{
                    Log.d("TAG", "savedata: "+savedata);
                    return savedata==null?null:savedata.getBytes("utf-8");
                }catch(UnsupportedEncodingException uee){
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
    }

}