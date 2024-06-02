package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

interface UserRepository extends JpaRepository<User, Long> {



    /**
     * Query searching users by email address. It matches by exact match.
     *
     * @param email email of the user to search
     * @return {@link Optional} containing found user or {@link Optional#empty()} if none matched
     */

    default List<User> findByEmail(String email) {
        return findAll().stream()
                        .filter(user ->  user.getEmail().toLowerCase().contains(email.toLowerCase()))
                        .collect(Collectors.toList());

    }

    default List<User> getUsersOlderThan(String age) {
        int ageInt = Integer.parseInt(age);
        return findAll().stream()
                        .filter(user ->  user.getBirthdate() != null &&
                                Period.between(user.getBirthdate(), LocalDate.now()).getYears() > ageInt)
                        .collect(Collectors.toList());
    }


}
