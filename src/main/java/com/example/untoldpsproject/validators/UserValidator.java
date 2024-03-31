package com.example.untoldpsproject.validators;

import com.example.untoldpsproject.constants.UserConstants;
import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.dtos.UserDtoIds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    public boolean nameValidator(String name) throws Exception {
        Pattern pattern = Pattern.compile("[a-zA-Z]+-?[a-zA-Z]*$");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        } else {
            matcher.reset();
            throw new Exception(UserConstants.NAME_NOT_MATCH);
        }
    }
    public boolean emailValidator(String email) throws Exception{
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        if(matcher.find()){
            matcher.reset();
            return matcher.find();
        }else{
            matcher.reset();
            throw new Exception(UserConstants.EMAIL_NOT_MATCH);
        }
    }
    public boolean passwordValidator(String password) throws Exception{
        if(password != null && password.length() >= 8 && password.length() <= 15){
            return true;
        }else{
            throw  new Exception(UserConstants.PASSWORD_NOT_MATCH);
        }
    }

    public boolean userDtoValidator(UserDto userDto) throws Exception{
        if(nameValidator(userDto.getFirstName()) && nameValidator(userDto.getLastName())&&
                emailValidator(userDto.getEmail()) && passwordValidator(userDto.getPassword())){
            return true;
        } else {
            return false;
        }
    }
}
