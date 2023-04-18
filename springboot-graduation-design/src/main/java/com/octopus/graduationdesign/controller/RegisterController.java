package com.octopus.graduationdesign.controller;

import com.octopus.graduationdesign.pojo.dao.User;
import com.octopus.graduationdesign.properties.OProperties;
import com.octopus.graduationdesign.service.RegisterService;
import com.octopus.graduationdesign.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/do_register")
    public R doRegister(@RequestBody User user) {
        int res = registerService.addUser(user);
        if (res != OProperties.HTTP_SUCCESS) {
            return R.error(res);
        }
        return R.ok();
    }
}
