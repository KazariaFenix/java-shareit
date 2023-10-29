package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User addUser(User user) {
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is null");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, long userId) {
        Optional<User> oldUser = userRepository.findById(userId);
        user = user.toBuilder()
                .id(userId)
                .build();
        if (user.getEmail() == null) {
            user = user.toBuilder()
                    .email(oldUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User Not Found")).getEmail())
                    .build();
        }
        if (user.getName() == null) {
            user = user.toBuilder()
                    .name(oldUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User Not Found")).getName())
                    .build();
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
