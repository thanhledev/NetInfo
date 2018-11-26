package de.uniba.mobi.netinfo.services;

import de.uniba.mobi.netinfo.opencellid.OpenCellIdHelper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class OpenCellIdService extends Service {

    // handler
    private Handler sHandler;

    // runnable
    private final Runnable sendData = new Runnable(){
        public void run(){
            try {
                //prepare and send the data here..
                final String mRequestBody = OpenCellIdHelper.getInstance().getJsonBody();

                JsonObjectRequest jsonRequest = new JsonObjectRequest
                        (Request.Method.POST, OpenCellIdHelper.apiServer, null, response -> {
                            // the response is already constructed as a JSONObject!
                            try {
                                String status = response.getString("status");
                                int balance = response.getInt("balance");
                                double lat = response.getDouble("lat");
                                double lon = response.getDouble("lon");
                                int accuracy = response.getInt("accuracy");
                                String address = response.getString("address");
                                Toast.makeText(OpenCellIdService.this, "lat: " + lat + ", lon: " + lon + "\nAddress: " + address, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> error.printStackTrace()) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }
                };

                Volley.newRequestQueue(OpenCellIdService.this).add(jsonRequest);
                sHandler.postDelayed(this, 30000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // constructor
    public OpenCellIdService() {
        sHandler = new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "OpenCellIdService Started", Toast.LENGTH_SHORT).show();

        // start handler
        sHandler.post(sendData);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sHandler.removeCallbacks(sendData);
        Toast.makeText(this, "OpenCellIdService Destroyed", Toast.LENGTH_SHORT).show();
    }
}
