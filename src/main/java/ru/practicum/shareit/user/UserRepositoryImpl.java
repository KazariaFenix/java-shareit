package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailist = new HashSet<>();
    private Long id = 1L;

    @Override
    public User addUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким айди уже существует");
        }
        validationEmail(user.getEmail());
        user = user.toBuilder()
                .id(id++)
                .build();
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с таким айди не существует");
        }
        User oldUser = users.get(user.getId());

        if (!oldUser.getEmail().equals(user.getEmail()) && user.getEmail() != null) {
            validationEmail(user.getEmail());
            emailist.remove(oldUser.getEmail());
            oldUser = oldUser.toBuilder()
                    .email(user.getEmail())
                    .build();
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser = oldUser.toBuilder()
                    .name(user.getName())
                    .build();
        }

        users.put(user.getId(), oldUser);
        return oldUser;
    }

    @Override
    public void deleteUserById(long userId) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("Пользователь с таким айди не существует");
        }
        emailist.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return users.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void validationEmail(String email) {
        if (!addEmail(email)) {
            throw new IllegalArgumentException("Пользователь с такой почтой уже существует");
        }
    }

    private boolean addEmail(String email) {
        return emailist.add(email);
    }
}
