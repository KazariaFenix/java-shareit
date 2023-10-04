package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public User updateUser(User user, long userId) {
        user.setId(userId);
        return userRepository.updateUser(user);
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }
}
