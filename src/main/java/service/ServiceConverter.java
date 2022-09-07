package service;

import entity.User;
import entity.UserSession;

import java.util.ArrayList;
import java.util.List;

public class ServiceConverter {

    public static List<UserSession> convertUsersToUserSessions(List<User> users){
        List<UserSession> userSessions = new ArrayList<>();
        for (User user:users) {
            userSessions.add(new UserSession(user));
        }
        return userSessions;
    }

}
