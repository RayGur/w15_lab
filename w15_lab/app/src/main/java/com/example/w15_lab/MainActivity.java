package com.example.w15_lab;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button queryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryButton = findViewById(R.id.queryButton);
        queryButton.setOnClickListener(v -> makeApiCall());
    }

    private void makeApiCall() {
        String url = "https://tools-api.italkutalk.com/java/lab12";
        Request request = new Request.Builder()
                .url(url)
                .build();

        UnsafeOkHttpClient.getUnsafeOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showDialog("Error", "Failed to fetch data: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();
                    Gson gson = new Gson();
                    Data data = gson.fromJson(jsonString, Data.class);

                    runOnUiThread(() -> showDialog("Response", data.toString()));
                } else {
                    runOnUiThread(() -> showDialog("Error", "Server returned error: " + response.code()));
                }
            }
        });
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}