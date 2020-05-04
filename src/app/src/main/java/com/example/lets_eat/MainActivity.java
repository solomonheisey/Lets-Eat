package com.example.lets_eat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "InitialLetsEat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnLetsEat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Let's Eat button has been pressed");
                Toast.makeText(getApplicationContext(), "Recipe Generated!", Toast.LENGTH_SHORT)
                        .show();
                goToRecipeView(v);
            }
        });
    }

    public void goToRecipeView(View view) {
        Intent intent = new Intent(this, RecipeView.class);
        startActivity(intent);
    }
}
