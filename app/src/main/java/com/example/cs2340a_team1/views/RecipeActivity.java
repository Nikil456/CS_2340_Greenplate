package com.example.cs2340a_team1.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs2340a_team1.R;
import com.example.cs2340a_team1.model.CookbookData;
import com.example.cs2340a_team1.model.FirebaseUtil;
import com.example.cs2340a_team1.model.IngredientData;
import com.example.cs2340a_team1.model.RecipeData;
import com.example.cs2340a_team1.model.SortAlpha;
import com.example.cs2340a_team1.model.SortQuant;
import com.example.cs2340a_team1.model.UserData;
import com.example.cs2340a_team1.viewmodels.CookbookViewModel;
import com.example.cs2340a_team1.viewmodels.UserViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Set;

public class RecipeActivity extends AppCompatActivity {
    private TextView recipeList;
    private HashMap<String, Integer> recipes;
    private HashMap<String, Boolean> isBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_screen);
        Button toInputMealScreenButton = findViewById(R.id.toInputMealScreenButton);
        Button toHomeScreenButton = findViewById(R.id.toHomeScreenButton);
        Button toIngredientScreenButton = findViewById(R.id.toRecipeScreenButton);
        Button toShoppingListScreenButton = findViewById(R.id.toShoppingListScreenButton);
        Button toPersonalInfoScreenButton = findViewById(R.id.toPersonalInfoScreenButton);
        Button newRecipeButton = findViewById(R.id.newRecipeBtn);
        Button sortAlphaButton = findViewById(R.id.sortAlphaBtn);
        Button sortQuantButton = findViewById(R.id.sortQuantBtn);
        Button updateButton = findViewById(R.id.updateBtn);
        recipeList = findViewById(R.id.recipeList);
        recipes = new HashMap<>();
        isBlue = new HashMap<>();

        toInputMealScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeActivity.this, InputMealActivity.class);
            startActivity(intent);
        });

        toShoppingListScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeActivity.this, ShoppingList.class);
            startActivity(intent);
        });

        toHomeScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        toIngredientScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeActivity.this, IngredientsActivity.class);
            startActivity(intent);
        });
        toPersonalInfoScreenButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });
        newRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeActivity.this, NewRecipeActivity.class);
            startActivity(intent);
        });

        updateButton.setOnClickListener(v -> {
            CookbookViewModel model = CookbookViewModel.getInstance();
            CookbookData data = model.getData();
            UserViewModel user = UserViewModel.getInstance();
            for (RecipeData recipe : data.getRecipes()) {
                for (String ingName : recipe.getIngredientSet()) {
                    int quant = recipe.getQuantity(ingName);
                    if (user.getUserData().getShoppingList().containsKey(ingName)) {
                        if (user.getUserData().getShoppingList().get(ingName).second < quant) {
                            user.getUserData().addShopping(new IngredientData(ingName,
                                    user.getUserData().getShoppingList().get(ingName).first.getCalories()), quant);
                        }
                    } else {
                        user.getUserData().addShopping(new IngredientData(ingName, "100"), quant);
                    }
                }
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(user.getUserData().getUser());
            ref.setValue(user);
        });

        sortAlphaButton.setOnClickListener(v -> {
            Set<String> names = recipes.keySet();
            names = new SortAlpha().sort(names, recipes);
            recipeList.setText("");
            TableLayout btnContainer = findViewById(R.id.btnContainer);
            btnContainer.removeAllViews();
            for (String name : names) {
                Button button = new Button(getApplicationContext());
                button.setText("View Recipe");
                button.setOnClickListener(x -> {
                    Intent intent = new Intent(RecipeActivity.this, DisplayRecipeActivity.class);
                    intent.putExtra("recipe",
                            CookbookViewModel.getInstance().getData().getRecipe(name));
                    startActivity(intent);
                });
                Spannable word =
                        new SpannableString(name + "\t\t-\t\t" + recipes.get(name) + "\n");
                if (isBlue.get(name)) {
                    word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    button.setVisibility(View.VISIBLE);
                } else {
                    word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    button.setVisibility(View.INVISIBLE);
                }
                recipeList.append(word);
                btnContainer.addView(button);
            }
        });

        sortQuantButton.setOnClickListener(v -> {
            Set<String> names = recipes.keySet();
            names = new SortQuant().sort(names, recipes);
            recipeList.setText("");
            TableLayout btnContainer = findViewById(R.id.btnContainer);
            btnContainer.removeAllViews();
            for (String name : names) {
                Button button = new Button(getApplicationContext());
                button.setText("View Recipe");
                button.setOnClickListener(x -> {
                    Intent intent = new Intent(RecipeActivity.this, DisplayRecipeActivity.class);
                    intent.putExtra("recipe",
                            CookbookViewModel.getInstance().getData().getRecipe(name));
                    startActivity(intent);
                });
                Spannable word =
                        new SpannableString(name + "\t\t-\t\t" + recipes.get(name) + "\n");
                if (isBlue.get(name)) {
                    word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    button.setVisibility(View.VISIBLE);
                } else {
                    word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    button.setVisibility(View.INVISIBLE);
                }
                recipeList.append(word);
                btnContainer.addView(button);
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CookbookViewModel model = CookbookViewModel.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref1 =
                database.getReference(UserViewModel.getInstance().getUserData().getUser());

        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUtil.loadFromFirebase(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference = database.getReference("recipes");
        TableLayout btnContainer = findViewById(R.id.btnContainer);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                    model.getData().clearRecipes();
                    recipeList.setText("");
                    for (String name : map.keySet()) {
                        boolean hasIngredients = true;
                        UserData data = UserViewModel.getInstance().getUserData();
                        int quant = 0;
                        RecipeData recipe = new RecipeData();
                        recipe.setName(name);
                        HashMap<String, Long> ingredient = (HashMap<String, Long>) map.get(name);
                        for (String ingName : ingredient.keySet()) {
                            System.out.println(data.getIngredients().keySet());
                            quant += ingredient.get(ingName);
                            recipe.addIngredient(ingName, Math.toIntExact(ingredient.get(ingName)));
                            if (!data.getIngredients().containsKey(ingName)) {
                                hasIngredients = false;
                            }
                        }

                        model.getData().addRecipe(recipe);
                        Spannable word =
                                new SpannableString(name + "\t\t-\t\t" + quant + "\n");
                        Button button = new Button(getApplicationContext());
                        button.setText("View Recipe");
                        button.setOnClickListener(v -> {
                            Intent intent = new Intent(RecipeActivity.this,
                                    DisplayRecipeActivity.class);
                            intent.putExtra("recipe", recipe);
                            startActivity(intent);
                        });
                        if (hasIngredients) {
                            word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            button.setVisibility(View.VISIBLE);
                        } else {
                            word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            button.setVisibility(View.INVISIBLE);
                        }
                        recipeList.append(word);
                        recipes.put(name, quant);
                        isBlue.put(name, hasIngredients);


                        btnContainer.addView(button);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}