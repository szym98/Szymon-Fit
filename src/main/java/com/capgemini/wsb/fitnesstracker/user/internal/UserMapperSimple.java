package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.springframework.stereotype.Component;

@Component
class UserMapperSimple {


    UserDtoSimple toDtoSimple(User user) {
        return new UserDtoSimple(user.getId(),
                user.getFirstName(),
                user.getLastName());
    }




    User toEntity(UserDto userDto) {
        return new User(
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.birthdate(),
                        userDto.email());
    }

}
