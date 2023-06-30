package mealplanner;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) throws SQLException {

    MealMenu menu = new MealMenu();
    menu.displayMenu();

  }

  public static void addMealsForTest() throws SQLException {
    MealDAO mealDAO = new MealDAO();
    Meal meal = new Meal("lunch", "salad");
    meal.addIngredient("lettuce");
    meal.addIngredient("tomato");
    mealDAO.add(meal);

    meal = new Meal("lunch", "carbonara");
    meal.addIngredient("eggs");
    meal.addIngredient("spaghetti");
    meal.addIngredient("bacon");
    mealDAO.add(meal);

    meal = new Meal("dinner", "spaghetti");
    meal.addIngredient("spaghetti");
    meal.addIngredient("tomato sauce");
    meal.addIngredient("basil");
    mealDAO.add(meal);

    meal = new Meal("dinner", "caserole");
    meal.addIngredient("potato");
    meal.addIngredient("bacon");
    meal.addIngredient("tomatoes");
    meal.addIngredient("sour cream");
    meal.addIngredient("basil");
    mealDAO.add(meal);

    meal = new Meal("breakfast", "toasts");
    meal.addIngredient("bread");
    meal.addIngredient("cheese");
    meal.addIngredient("ketchup");
    mealDAO.add(meal);

    meal = new Meal("breakfast", "scrambled eggs");
    meal.addIngredient("eggs");
    meal.addIngredient("butter");
    mealDAO.add(meal);

    mealDAO.close();

  }
}