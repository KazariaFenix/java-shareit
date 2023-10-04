package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user);

    User updateUser(User user);

    void deleteUserById(long userId);

    User getUserById(long userId);

    List<User> getUsers();
}
