package com.andevcon.hackathon.msft.helpers;

import com.andevcon.hackathon.msft.model.UsersValue;

/**
 * Created by krunalshah on 12/2/15.
 */
public class DataStore {

    private static UsersValue usersValue;

    public static  UsersValue getUsersValue() {
        return usersValue;
    }

    public static void setUsersValue(UsersValue usersValue) {
        usersValue = usersValue;
    }
}
