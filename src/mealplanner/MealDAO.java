package mealplanner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MealDAO extends DAO{
    private final String CREATE_MEALS_SEQUENCE = "CREATE SEQUENCE IF NOT EXISTS meals_sequence START 1 INCREMENT 1 CACHE 1";
    private final String CREATE_MEALS_TABLE = "CREATE TABLE IF NOT EXISTS meals\n" +
            "(meal_id INTEGER, category VARCHAR(40) NOT NULL, meal VARCHAR(40) NOT NULL)";
    private final String INSERT_MEAL = "INSERT INTO meals(meal_id, category, meal) VALUES(NEXTVAL('meals_sequence'), '%s', '%s')  RETURNING meal_id";

    private final String SELECT_ALL = "SELECT * FROM meals";
    private final String SELECT_CATEGORY = "SELECT * FROM meals WHERE category='%s'";
    private final String SELECT_MEAL_OPTIONS = "SELECT meal, meal_id FROM meals WHERE category='%s' ORDER BY meal";

    IngredientsDAO ingredientsDAO;
    public MealDAO() throws SQLException {
        super.run(CREATE_MEALS_SEQUENCE);
        super.run(CREATE_MEALS_TABLE);
        ingredientsDAO = new IngredientsDAO();
    }

    public List<Meal> findAll() throws SQLException {
        return findByCategory("all");
    }

    public List<Meal> findByCategory(String category) throws SQLException {
        ArrayList<Meal> meals = new ArrayList<>();
        String query;
        if (category.equalsIgnoreCase("all")){
            query = SELECT_ALL;
        } else {
          query = SELECT_CATEGORY.formatted(category);
        }
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Meal meal = new Meal(resultSet.getString("category"), resultSet.getString("meal"), resultSet.getInt("meal_id"));
            List<String> ingredients = ingredientsDAO.findBy(meal.mealID);
            meal.setIngredients(ingredients);
            meals.add(meal);
        }
        preparedStatement.close();
        return meals;
    }

    public Map<String, Integer> findMealOptions(String category) throws SQLException {
        Map<String, Integer> options = new TreeMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MEAL_OPTIONS.formatted(category));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            options.put(resultSet.getString("meal"), resultSet.getInt("meal_id"));
        }
        return options;
    }
    public void add(Meal meal) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MEAL.formatted(meal.category, meal.name));
        ResultSet resultSet = preparedStatement.executeQuery();
        int mealId = -1;
        if (resultSet.next()) {
            mealId =  resultSet.getInt(1);
        }
        preparedStatement.close();
        for (String ingredient: meal.ingredients){
            ingredientsDAO.add(ingredient, mealId);
        }
    }

    @Override
    public void close() throws SQLException {
        super.close();
        ingredientsDAO.close();
    }

}
