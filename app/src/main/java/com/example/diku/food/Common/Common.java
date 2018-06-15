package com.example.diku.food.Common;

import com.example.diku.food.Module.FoodList;
import com.example.diku.food.Module.Order;
import com.example.diku.food.Module.Request;
import com.example.diku.food.Module.User;

/**
 * Created by Diku on 16-05-2018.
 */

public class Common {

    public static User currentUser;

    public static final String DELETE="Delete";
    public static String convertCodeToStatus(String status) {

        if (status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On my way";


        else

            return "Shipped";


    }

}


