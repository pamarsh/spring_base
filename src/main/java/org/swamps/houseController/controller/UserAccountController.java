package org.swamps.houseController.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.swamps.houseController.DuplicateUserException;
import org.swamps.houseController.security.Utils;
import org.swamps.houseController.service.AttemptingToChangeUserRoles;
import org.swamps.houseController.service.UserAccountService;
import org.swamps.houseController.dto.UserAccountDto;

import java.security.Principal;
import java.util.List;



@RestController
@RequestMapping(value = "/users")
public class UserAccountController {

    @Autowired
    UserAccountService userAccountService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<UserAccountDto> getAccounts() {
        return userAccountService.getAccounts();
    }


    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public UserAccountDto getAccount(@PathVariable String userName, Principal principle) throws UserUnortherisedException {
        if ( principle.getName().equals(userName) || hasRole("ROLE_ADMIN")) {
            return userAccountService.findUserAccount(userName);
        }
        throw new UserUnortherisedException();
    }


    public static boolean hasRole(String role) {
        return Utils.hasRole(role);

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserAccountDto addAccount(@RequestBody UserAccountDto account) throws DuplicateUserException {
        return userAccountService.addAccount(account);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public UserAccountDto changeAccount(@PathVariable String userName, @RequestBody UserAccountDto account) throws org.swamps.houseController.service.AttemptingToChangeUserRoles {
        if (hasRole("ROLE_ADMIN")) {
            return userAccountService.changeUserAccountByAdmin(account);
        } else {
            return userAccountService.changeUserAccountByUser(account);
        }
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserAccountDto deleteAccount(@PathVariable("userName") String userName) {
        UserAccountDto dto = userAccountService.findUserAccount(userName);
        if (dto != null) {
            userAccountService.deleteAccount(dto);
        }
        return dto;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUserException.class)
    public void duplicateAccount() {
    }


    @ExceptionHandler(UserUnortherisedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public void userNotAuthorized() {
    }

    @ExceptionHandler(AttemptingToChangeUserRoles.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public void invalidChangeOfRoles() {

    }


}
