package mealplanner;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class PlanDAO extends DAO{
    private final String CREATE_PLAN_TABLE = "CREATE TABLE IF NOT EXISTS plan\n" +
            "(week_day VARCHAR(40) NOT NULL, category VARCHAR(40) NOT NULL, meal_id INTEGER NOT NULL)";
    private final String SELECT_ALL = "SELECT  plan.week_day, plan.category, plan.meal_id, meals.meal FROM plan JOIN meals ON plan.meal_id=meals.meal_id";
    private final String CHECK_IF_EMPTY = "SELECT COUNT(1) WHERE EXISTS (SELECT * FROM plan)";
    private final String UPDATE_ENTRY = "UPDATE plan\n" +
            "SET meal_id = %d\n" +
            "WHERE week_day = '%s' AND category = '%s'";
    private final String INSERT_ENTRY =  "INSERT INTO plan (week_day, category, meal_id) VALUES ('%s', '%s', %d)";
    private final  String SELECT_COUNT_INGREDIENTS_IN_PLAN = "SELECT ingredient, COUNT(ingredient) FROM ingredients JOIN plan ON plan.meal_id=ingredients.meal_id GROUP BY ingredient";
    public PlanDAO() throws SQLException {
        super.run(CREATE_PLAN_TABLE);

    }

    public void savePlan(List<DailyEntry> plan) throws SQLException {
        if (isEmpty()){
            for (DailyEntry dailyEntry: plan){
                insert(dailyEntry);
            }
        } else {
            for (DailyEntry dailyEntry: plan){
                update(dailyEntry);
            }
        }
    }

    public void update(DailyEntry entry) throws SQLException {
        super.run(UPDATE_ENTRY.formatted(entry.mealId, entry.weekDay, entry.category));
    }

    public void insert(DailyEntry entry) throws  SQLException {
        super.run(INSERT_ENTRY.formatted(entry.weekDay, entry.category, entry.mealId));
    }

    public List<DailyEntry> readPlan() throws SQLException {
        List<DailyEntry> plan = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            DailyEntry entry = new DailyEntry(resultSet.getString("week_day"), resultSet.getString("category"), resultSet.getInt("meal_id"), resultSet.getString("meal"));
            plan.add(entry);
        }
        return plan;
    }

    public void saveToFile(String fileName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_INGREDIENTS_IN_PLAN);
        ResultSet resultSet = preparedStatement.executeQuery();

        try (PrintWriter printWriter = new PrintWriter(fileName)){
            while (resultSet.next()){
                String ingredientName = resultSet.getString("ingredient");
                int N = resultSet.getInt("count");
                if (N == 1) {
                    printWriter.println(ingredientName);
                } else {
                    printWriter.println("%s x%d".formatted(ingredientName, N));
                }
            }

        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

    }
    public boolean isEmpty() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_IF_EMPTY);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getInt("count") == 0;
        }
        return true;
    }

    private final List<String> weekDays = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    private final List<String> categories = List.of("breakfast", "lunch", "dinner");
}

class DailyEntry {
    String weekDay;
    String category;
    int mealId;

    String meal;

    @Override
    public String toString() {

        //('Monday', 'lunch', -1)
        return "('%s', '%s', %d)".formatted(weekDay, category, mealId);
    }

    public DailyEntry(String weekDay, String category, int mealId, String meal) {
        this.weekDay = weekDay;
        this.category = category;
        this.mealId = mealId;
        this.meal = meal;
    }
}