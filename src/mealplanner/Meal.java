package mealplanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meal {
    String category;
    String name;
    int mealID;
    ArrayList<String> ingredients;
    public Meal(String category, String name) {
        this.category = category;
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public Meal(String category, String name, int mealID) {
        this.category = category;
        this.name = name;
        this.mealID = mealID;
        this.ingredients = new ArrayList<>();
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients.addAll(ingredients);
    }
    public void addIngredient(String ingredient){
        this.ingredients.add(ingredient);
    }




    @Override
    public String toString() {
//        return "\nCategory: " + category + '\n' +
           return     "\nName: " + name + '\n' +
                "Ingredients:\n" + String.join("\n", ingredients);
    }
}
