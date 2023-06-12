package com.stan.analengine.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// User controller

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/all")
  public String hello() {
    return "All users";
  }
  @PostMapping("/login")
  public User login(@RequestBody User user) {
    User foundUser = userRepository.findByUsername(user.getUsername());
    if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
      return foundUser;
    } else {
      throw new RuntimeException("Password or Username incorrect");
    }
  }

  @PostMapping("/create")
  public String createUser(@RequestBody User user) {
    userRepository.save(user);
    return "User created successfully";
  }

  @DeleteMapping("/{username}")
  public String deleteUser(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    userRepository.deleteById(user.getId());
    return "User deleted successfully";
  }

  @GetMapping("")
  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

}