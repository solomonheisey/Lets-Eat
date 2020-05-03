package com.example.lets_eat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RecipeView extends AppCompatActivity {
    private TextView jsonRecipe;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_recipe_view);

        jsonRecipe = findViewById(R.id.jsonRecipe);

        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

     private void jsonParse() {
        String url = "https://www.themealdb.com/api/json/v1/1/random.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject recipe = jsonArray.getJSONObject(i);

                                String thumbnail = recipe.getString("strMealThumb");
                                String strMeal = recipe.getString("strMeal");
                                String instructions = recipe.getString("strInstructions");
                                jsonRecipe.setText(strMeal + "\n\n");
                                jsonRecipe.append("Ingredients:\n");

                                for(int j = 1; j <= 20; j++) {
                                    String tempIngredient = recipe.getString("strIngredient" + j);
                                    String tempMeasure = recipe.getString("strMeasure" + j);
                                    if (!tempIngredient.equals("") && !tempIngredient.equals("null"))
                                        jsonRecipe.append(tempIngredient);
                                    else
                                        break;

                                    if (!tempMeasure.equals("") && !tempMeasure.equals("null") && !tempMeasure.equals(" "))
                                        jsonRecipe.append(" (" + tempMeasure + ")\n");
                                }


                                jsonRecipe.append("\nInstructions: \n");
                                jsonRecipe.setTypeface(null, Typeface.NORMAL);
                                jsonRecipe.append(instructions);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
