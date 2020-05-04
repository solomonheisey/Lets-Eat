package com.example.lets_eat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.Objects;


public class RecipeView extends AppCompatActivity {
    private TextView jsonRecipe;
    private RequestQueue mQueue;
    private ImageView recipeImage;
    private TextView recipeTitle;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_recipe_view);

        jsonRecipe = findViewById(R.id.jsonRecipe);
        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);

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
                                if (!thumbnail.equals("") && !thumbnail.equals("null") && !thumbnail.equals(" "))
                                    new DownloadImageTask(recipeImage).execute(thumbnail);

                                String strMeal = recipe.getString("strMeal");
                                String instructions = recipe.getString("strInstructions");
                                recipeTitle.setText(Html.fromHtml("<b>"+ strMeal + "</b><br><br>"));
                                jsonRecipe.setText(Html.fromHtml("<b>Ingredients:</b><br>"));

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


                                jsonRecipe.append(Html.fromHtml("<br><br><b>Instructions:</b><br>"));
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
