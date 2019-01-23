package com.anime.reminderoli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity {
    @BindView(R.id.admin_iduser)
    Spinner spinnerIduser;
    @BindView(R.id.admin_idoli)
    Spinner spinnerIdoli;
    @BindView(R.id.admin_noPol)
    EditText anoPol;
    @BindView(R.id.admin_kmawal)
     EditText akmAwal;
    @BindView(R.id.admin_kmservice)
    EditText akmService;
    @BindView(R.id.admin_namamobil)
            EditText anamaMobil;
    @BindView(R.id.admin_jenismobil)
            EditText ajenisMobil;
    @BindView(R.id.admin_input_selesai)
            EditText aInput;
    @BindView(R.id.loading)
    ProgressBar loading;

    ArrayList<Mobil> listMobil;
    ArrayList<String> listIdMobil;
    ArrayAdapter<String> listAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        listMobil = new ArrayList<>();
        listIdMobil = new ArrayList<>();
        listMobil = getIntent().getParcelableExtra("Mobil");


        spinnerIduser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                String noPol = anoPol.getText().toString().trim();
                String kmAwal = akmAwal.getText().toString().trim();
                String kmService = akmService.getText().toString().trim();
                String namaMobil = anamaMobil.getText().toString().trim();
                String jenisMobil = ajenisMobil.getText().toString().trim();

                if (TextUtils.isEmpty(noPol)){
                    anoPol.setError("Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(kmAwal)){
                    akmAwal.setError("Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(kmService)){
                    akmService.setError("Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(namaMobil)){
                    anamaMobil.setError("Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(jenisMobil)){
                    ajenisMobil.setError("Tidak Boleh Kosong");
                }
            }
        });

        LoadData();


    }
    void LoadAdmin(String noPol, String kmAwal, String kmService, String namaMobil, String jenisMobil){

        String URL2 = "reminder.96.lt/setMobil.php?nopol=";
        URL2 += noPol;
        URL2 += "&kmawal" + kmAwal;
        URL2 += "&kmservice"+ kmService;
        URL2 += "&namamobil"+ namaMobil;
        URL2 += "&jenismobil"+ jenisMobil;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Log.d("TAg", response);
                if (response.equals("Terupdate")){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
    });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    void LoadData() {
        String URL = "http://reminder.96.lt/getUser.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> userData = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        User user = new User(obj);
                        Log.d("TAG", "onResponse: " + user.getIdUser());


                        userData.add(user.getIdUser());
                        Log.d("TAg", "onResponse: " + userData.get(i));


                    }
                    listAdapter = new ArrayAdapter<>(AdminActivity.this, R.layout.spinner_item, userData);
                    listAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
                    spinnerIduser.setAdapter(listAdapter);
                    loadOli();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    void loadOli() {

        String URL = "http://reminder.96.lt/getMobil.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<String> oliData = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray array1 = obj.getJSONArray("data");
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj2 = array1.getJSONObject(i);
                        Mobil oli = new Mobil(obj2);
                        Log.d("TAG", "onResponse: " + oli.getId_oli());
                        oliData.add(oli.getId_oli());
                        Log.d("TAG", "onResponse: " + oliData.get(i));
                    }
                    listAdapter = new ArrayAdapter<>(AdminActivity.this, R.layout.spinner_item, oliData);
                    listAdapter.setDropDownViewResource(R.layout.spinner_item_drop);
                    spinnerIdoli.setAdapter(listAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
}
}
