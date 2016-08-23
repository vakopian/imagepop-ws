package com.imagepop;

import com.imagepop.domain.User;
import com.imagepop.domain.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {


    @Autowired
    private UserRepository repository;


    @Test
    public void userTest() {

        List<String> names = Arrays.asList("Jack", "Chloe", "Kim", "David", "Michelle", "Hannah");
        List<User> users = new ArrayList<>();
        List<User> singleUser = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            User newUser = new User();
            newUser.setFirstName(names.get(i));
            newUser.setId(i + 1);
            newUser.setEmail(names.get(i) + "@imagepop.com");
            if (i == 0) {
                singleUser.add(newUser);
            }
            this.repository.save(newUser);
            users.add(newUser);
        }

        Assert.assertEquals(singleUser.get(0).toString(), this.repository.findByEmail("Jack@imagepop.com").toString());
        Assert.assertEquals(users.toString(), this.repository.findAll().toString());
        Assert.assertEquals(users.get(0).toString(), this.repository.findOne(1L).toString());
    }

    @Test
    public void contextLoads() {

    }

}
