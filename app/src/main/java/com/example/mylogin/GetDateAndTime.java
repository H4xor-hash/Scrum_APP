package com.example.mylogin;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDateAndTime {

    Activity activity;
    String url = "https://www.timeapi.io/api/Time/current/coordinate?latitude=41.9028&longitude=12.4964";
    RequestQueue requestQueue;

    public GetDateAndTime(Activity activity) {
        this.activity = activity;

        requestQueue = Volley.newRequestQueue(activity);
    }

    public void getDateTime(VolleyCallBack volleyCallBack){
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    volleyCallBack.onGetDataTime(response.getString("date"),response.getString("time"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(request);
    }

    public interface VolleyCallBack{

        void onGetDataTime(String date , String time);
    }



}
