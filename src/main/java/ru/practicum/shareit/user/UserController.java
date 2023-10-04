package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable int id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@Valid @PathVariable int id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@Valid @PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
