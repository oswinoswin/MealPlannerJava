package mealplanner;

import java.sql.SQLException;
import java.util.*;

public class MealMenu {
    Scanner scanner;
    List<Meal> meals;
    Set<String> mealTypes = Set.of("breakfast", "lunch","dinner");
    List<String> weekDays = List.of("Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
    List<DailyEntry> plan;
//    DAO DAO;
    MealDAO mealDAO;
    PlanDAO planDAO;

    public MealMenu() throws SQLException {
        scanner = new Scanner(System.in);
        mealDAO = new MealDAO();
        planDAO = new PlanDAO();
        if (planDAO.isEmpty()) {
            plan = new ArrayList<>();
        } else {
            plan = planDAO.readPlan();
        }
        try {
            meals = mealDAO.findAll();
        } catch (SQLException e){
//            System.out.println(e.getMessage());
            meals = new ArrayList<>();
        }
    }

    public void displayMenu() throws SQLException {
        boolean exit = false;
        while (!exit){
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            switch (scanner.nextLine()){
                case "add": {
                    add();
                    break;
                }
                case "show": {
                    show();
                    break;
                }
                case "plan": {
                    plan();
                    break;
                }
                case "save": {
                    save();
                    break;
                }
                case "exit": {
                    exit = true;
//                    DAO.close();
                    mealDAO.close();
                    System.out.println("Bye!");
                    break;
                }
                default: break;
            }
        }

    }

    private void save() throws SQLException {
        if (plan.isEmpty()){
            System.out.println("Unable to save. Plan your meals first.");
        } else {
            System.out.println("Input a filename:");
            String fileName = scanner.next();
            planDAO.saveToFile(fileName);
            System.out.println("Saved!");
        }

    }

    private void readMealOption(Map<String, Integer> mealOptions, String category, String weekDay){
        mealOptions.keySet().forEach(System.out::println);
        System.out.println("Choose the %s for %s from the list above:".formatted(category, weekDay));
        String meal = scanner.nextLine().strip();
        while (! mealOptions.containsKey(meal)){
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            meal = scanner.nextLine().strip();
        }
        plan.add(new DailyEntry(weekDay, category, mealOptions.get(meal), meal));
    }
    private void plan() throws SQLException {
        plan = new ArrayList<>();

        Map<String, Integer> breakfastOptions = mealDAO.findMealOptions("breakfast");
        Map<String, Integer> lunchOptions = mealDAO.findMealOptions("lunch");
        Map<String, Integer> dinnerOptions = mealDAO.findMealOptions("dinner");
        for(String day: weekDays){
            System.out.println(day);

            readMealOption(breakfastOptions, "breakfast", day);
            readMealOption(lunchOptions, "lunch", day);
            readMealOption(dinnerOptions, "dinner", day);

            System.out.println("Yeah! We planned the meals for %s.".formatted(day));
            System.out.println();
        }

        planDAO.savePlan(plan);

        //wypisać jakkolwiek
        int i = 0;
        for (String day: weekDays){
            System.out.println(day);
            System.out.println("Breakfast: " + plan.get(i++).meal);
            System.out.println("Lunch: " + plan.get(i++).meal);
            System.out.println("Dinner: " + plan.get(i++).meal);
            System.out.println();

        }

    }

    private void show() throws SQLException {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = readCategory();
        List<Meal> mealsInCategory = mealDAO.findByCategory(category);
        if (mealsInCategory.isEmpty()){
            System.out.println("No meals found.");
            return;
        }
        System.out.println("Category: " + category);
        mealsInCategory.forEach(System.out::println);
        System.out.println();
    }

    private void add() throws SQLException {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = readCategory();
        String name = readName();
        Meal meal = new Meal(category, name);
        System.out.println("Input the ingredients:");
        ArrayList<String> ingredients = readIngredients();
        meal.setIngredients(ingredients);

        meals.add(meal);
        mealDAO.add(meal);
        System.out.println("The meal has been added!");
    }

    private ArrayList<String> readIngredients() {
        String ingredients = scanner.nextLine();
        ArrayList<String> ingredientsList = new ArrayList<>();
        for (var ingredient: ingredients.split(",")){
            ingredient = ingredient.strip();
            if (!ingredient.matches("[a-zA-Z ]+")){
                System.out.println("Wrong format. Use letters only!");
                ingredientsList = null;
                return readIngredients();
            }else {
                ingredientsList.add(ingredient);
            }
        }

        return ingredientsList;
    }

    private String readName() {
        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();
        while (!name.matches("[a-zA-Z ]+") || name.isEmpty()){
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }
        return name;
    }

    String readCategory(){

        String category = scanner.nextLine();
        while (!mealTypes.contains(category) || category.isEmpty()){
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }
        return category;
    }

}


