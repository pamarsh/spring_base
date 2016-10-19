package org.swamps.houseController.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.swamps.houseController.DuplicateUserException;
import org.swamps.houseController.dto.EmailClientDetailsMessage;
import org.swamps.houseController.dto.EmailGroupMessage;
import org.swamps.houseController.dto.EmailMessage;
import org.swamps.houseController.service.AttemptingToChangeUserRoles;
import org.swamps.houseController.service.EmailClientService;

@RestController
@RequestMapping(value = "/settings/email")
public class EmailController {

    @Autowired
    EmailClientService emailService;

    @RequestMapping(value = "/service", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    EmailClientDetailsMessage getEmailSettings() throws ResourceDoesNotExistException {
        return emailService.getEmailSettings();
    }


    @RequestMapping(value = "/service", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmailClientDetailsMessage addAccount(@RequestBody EmailClientDetailsMessage serverDetails) throws DuplicateUserException {
        return emailService.addEmailSettings(serverDetails);
    }

    @RequestMapping(value = "/service", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmailClientDetailsMessage changeAccount(@RequestBody EmailClientDetailsMessage emailDetails) throws ResourceDoesNotExistException {
        return emailService.updateServiceDetails(emailDetails);
    }

    @RequestMapping(value = "/service", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAccount() {
        emailService.deleteService();
    }


    @RequestMapping(value = "/group", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmailGroupMessage getEmailGroups() {
        return emailService.getGroups();
    }


    @RequestMapping(value = "/group/{groupName}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmailGroupMessage getEmailGroup(@PathVariable  String groupName) {
        return emailService.getEmailGroup(groupName);
    }


    @RequestMapping(value = "/group", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmailGroupMessage insertGroup(@RequestBody EmailGroupMessage emailGroup)
    {
        return emailService.addGroup(emailGroup);
    }


    @RequestMapping(value = "/test/{groupName}", method =   RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void sendEmail(@PathVariable("groupName") String emailGroup, @RequestBody EmailMessage message) throws ResourceDoesNotExistException {
        emailService.sendMessage(emailGroup, message);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUserException.class)
    public void duplicateEmailSetting() {
    }

    @ExceptionHandler(UserUnortherisedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public void userNotAuthorized() {
    }

    @ExceptionHandler(AttemptingToChangeUserRoles.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public void invalidChangeOfRoles() {

    }

    @ExceptionHandler(ResourceDoesNotExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void resourceNonExistent() {

    }

}
