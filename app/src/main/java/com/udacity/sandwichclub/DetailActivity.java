package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    // Member variables to store the view objects.
    private ImageView m_SandwichImage;
    private TextView m_AlsoKnownAsText;
    private TextView m_AlsoKnownAsLabel;
    private TextView m_PlaceOfOriginText;
    private TextView m_PlaceOfOriginLabel;
    private TextView m_DescriptionText;
    private TextView m_DescriptionLabel;
    private TextView m_IngredientText;
    private TextView m_IngredientLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Populate the view objects.
        m_SandwichImage = findViewById(R.id.sandwich_image);
        m_AlsoKnownAsText = findViewById(R.id.alsoKnownAs_text);
        m_AlsoKnownAsLabel = findViewById(R.id.alsoKnownAs_label);
        m_PlaceOfOriginText = findViewById(R.id.placeOfOrigin_text);
        m_PlaceOfOriginLabel = findViewById(R.id.placeOfOrigin_label);
        m_DescriptionText = findViewById(R.id.description_text);
        m_DescriptionLabel = findViewById(R.id.description_label);
        m_IngredientText = findViewById(R.id.ingredients_text);
        m_IngredientLabel = findViewById(R.id.ingredients_label);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // Get the sandwich details and parse the result JSON string.
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        // Construct the UI screen.
        populateUI(sandwich);
        Picasso.with(this).load(sandwich.getImage()).into(m_SandwichImage);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        // Set text to alsoKnownAsText
        if (sandwich.getAlsoKnownAs() != null && sandwich.getAlsoKnownAs().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sandwich.getAlsoKnownAs().get(0));

            for (int i = 1; i < sandwich.getAlsoKnownAs().size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(sandwich.getAlsoKnownAs().get(i));
            }
            m_AlsoKnownAsText.setText(stringBuilder.toString());
        } else {
            m_AlsoKnownAsText.setVisibility(View.GONE);
            m_AlsoKnownAsLabel.setVisibility(View.GONE);
        }

        // Set text to placeOfOriginText
        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            m_PlaceOfOriginText.setVisibility(View.GONE);
            m_PlaceOfOriginLabel.setVisibility(View.GONE);
        } else {
            m_PlaceOfOriginText.setText(sandwich.getPlaceOfOrigin());
        }

        // Set text to descriptionText
        if (sandwich.getDescription().isEmpty()) {
            m_DescriptionText.setVisibility(View.GONE);
            m_DescriptionLabel.setVisibility(View.GONE);
        } else {
            m_DescriptionText.setText(sandwich.getDescription());
        }

        // Set text to ingredientText
        if (sandwich.getIngredients() != null && sandwich.getIngredients().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\u2022");
            stringBuilder.append(sandwich.getIngredients().get(0));

            for (int i = 1; i < sandwich.getIngredients().size(); i++) {
                stringBuilder.append("\n");
                stringBuilder.append("\u2022");
                stringBuilder.append(sandwich.getIngredients().get(i));
            }
            m_IngredientText.setText(stringBuilder.toString());
        } else {
            m_IngredientText.setVisibility(View.GONE);
            m_IngredientLabel.setVisibility(View.GONE);
        }
    }
}
