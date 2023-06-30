package mealplanner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientsDAO extends DAO{
    public final String CREATE_SEQUENCE = "CREATE SEQUENCE IF NOT EXISTS ingredients_sequence START 1 INCREMENT 1 CACHE 1";
    public final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ingredients" +
            "(ingredient_id INTEGER NOT NULL, meal_id INTEGER NOT NULL, ingredient VARCHAR(40) NOT NULL)";
    public final String INSERT_INGREDIENT = "INSERT INTO ingredients(ingredient_id, ingredient, meal_id) VALUES(NEXTVAL('ingredients_sequence'), '%s', '%d')";
    public final String SELECT_BY_MEAL = "SELECT * FROM ingredients WHERE meal_id=%d";
    public IngredientsDAO() throws SQLException {
        super.run(CREATE_SEQUENCE);
        super.run(CREATE_TABLE);
    }
    public void add(String ingredient, int mealID) throws SQLException {
        super.run(INSERT_INGREDIENT.formatted(ingredient, mealID));
    }

    public List<String> findBy(int mealID) throws SQLException {
        ArrayList<String> ingredients = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_MEAL.formatted(mealID));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            ingredients.add(resultSet.getString("ingredient"));
        }
        return ingredients;
    }
}
