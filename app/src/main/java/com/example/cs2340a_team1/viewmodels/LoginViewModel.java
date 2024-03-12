package com.example.cs2340a_team1.viewmodels;

import com.example.cs2340a_team1.model.UserData;

public class LoginViewModel {
    private static LoginViewModel instance;
    private final UserData userData;

    public LoginViewModel() {
        userData = new UserData();
        this.updateData("");
    }

    public static synchronized LoginViewModel getInstance() {
        if (instance == null) {
            instance = new LoginViewModel();
        }
        return instance;
    }

    public UserData getUserData() {
        return userData;
    }

    public void updateData(String user) {
        userData.setUser(user);
    }

    public void setMeals(String mealName, int calorieAmt) {
        userData.addMeal(mealName, calorieAmt);
    }
}
