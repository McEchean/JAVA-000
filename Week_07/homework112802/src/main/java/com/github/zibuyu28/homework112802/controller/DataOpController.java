package com.github.zibuyu28.homework112802.controller;

import com.github.zibuyu28.homework112802.dao.User;
import com.github.zibuyu28.homework112802.service.UserOpImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataOpController {

    @Autowired
    private UserOpImpl userOpImpl;

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getDataByID(@PathVariable("id") int id) {
        User user = userOpImpl.getUserById(id);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        try {
            userOpImpl.addUser(user);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity("ok",HttpStatus.OK);

    }
}
