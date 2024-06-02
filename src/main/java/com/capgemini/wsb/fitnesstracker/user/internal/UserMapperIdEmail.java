package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Component;

@Component
class UserMapperIdEmail {


    UserDtoIdEmail toDtoIdEmail(User user) {
        return new UserDtoIdEmail(user.getId(),
                user.getEmail());
    }



    User toEntity(UserDto userDto) {
        return new User(
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.birthdate(),
                        userDto.email());
    }

}
