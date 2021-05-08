package com.example.testnotificationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddOrUpdateToken extends AppCompatActivity {
    RequestQueue requestQueue;
    EditText email,password,getToken;
    Button AddOrUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_token);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        getToken=(EditText)findViewById(R.id.Token);
        AddOrUpdate=(Button) findViewById(R.id.AddOrUpdate);

        AddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data="{"+
                        "\"email\""       + ":"+  "\""+email.getText().toString()+"\","+
                        "\"password\""    + ":"+   "\""+password.getText().toString()+"\""+
                        "\"reg_token\""    + ":"+   "\""+getToken.getText().toString()+"\""+
                        "}";
                // post method
                Submit(data);
            }
        });

        getRegToken();
    }
    private void getRegToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.w("TAG","Failed to get token");
                    return;
                }
                String token =task.getResult();
                Log.d("TAG","Token: " + token);
            }
        });
    }

    private void Submit(String data) {
        String savedata = data;
        String URL="https://mcc-users-api.herokuapp.com/add_reg_token";
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        Log.d("TAG", "requestQueue: " + requestQueue);
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Get response on JsonObject
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