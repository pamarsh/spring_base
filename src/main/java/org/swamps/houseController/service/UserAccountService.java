package org.swamps.houseController.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swamps.houseController.DuplicateUserException;
import org.swamps.houseController.counters.Counted;
import org.swamps.houseController.domain.AccountAuthority;
import org.swamps.houseController.domain.AccountAuthorityBuilder;
import org.swamps.houseController.domain.UserAccount;
import org.swamps.houseController.domain.UserAccountBuilder;
import org.swamps.houseController.domain.repositories.AccountAuthoritiesRepository;
import org.swamps.houseController.domain.repositories.AccountRepository;
import org.swamps.houseController.dto.UserAccountDto;
import org.swamps.houseController.dto.UserAccountDtoBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserAccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountAuthoritiesRepository accountAuthoritiesRepository;

    public List<UserAccountDto> getAccounts() {
        List<UserAccountDto> accounts = new LinkedList<>();

        for (UserAccount userAccount : accountRepository.findAll()) {
            List<AccountAuthority> roles = getUserRoles(userAccount.getUserId());
            accounts.add(translate(userAccount, roles));
        }
        return accounts;
    }

    public UserAccountDto findUserAccount(String userName) {
        UserAccount account = accountRepository.findByUserId(userName);
        List<AccountAuthority> roles = getUserRoles(userName);
        return translate(account, roles);
    }

    private List<AccountAuthority> getUserRoles(String userName) {
        return accountAuthoritiesRepository.findByUserId(userName);
    }

    @Counted(value = "users.deleterequest")
    public UserAccountDto addAccount(UserAccountDto account) throws DuplicateUserException {
        if (null != accountRepository.findByUserId(account.getUserId())) throw new DuplicateUserException();

        List<AccountAuthority> roles = new ArrayList<>();
        if (  account.getRoles().isEmpty()) {
            account.getRoles().add("USER");
        }

        List<AccountAuthority> accountAuthorities = new ArrayList<>();
        for (String role : account.getRoles()) {
            accountAuthorities.add(new AccountAuthorityBuilder()
                    .withUserId(account.getUserId()).withAuthority(role).create());
        }

        UserAccount storedAccount = accountRepository.save(UserAccountBuilder.emptyAccount()
                .withUserId(account.getUserId())
                .withFirstName(account.getFirstName())
                .withLastName(account.getLastName())
                .withEmailAddress(account.getEmailAddress())
                .withPassword(account.getPassword())
                .withRoles(accountAuthorities)
                .create());

        return translate(storedAccount, roles);
    }

    @Counted(value = "users.deleterequest")
    public void deleteAccount(UserAccountDto account) {
        accountRepository.delete(account.getId());
    }

    private UserAccountDto translate(UserAccount userAccount, List<AccountAuthority> roles) {
        final UserAccountDtoBuilder accountDtoBuilder = UserAccountDtoBuilder.emptyUser()
                .withId(userAccount.getId())
                .withUserId(userAccount.getUserId())
                .withFirstName(userAccount.getFirstName())
                .withLastName(userAccount.getLastName())
                .withEmailAddress(userAccount.getEmailAddress())
                .withPassword(userAccount.getPassword());

        for (AccountAuthority role : roles) {
            accountDtoBuilder.withRole(role.getAuthority());
        }
        return accountDtoBuilder.createUserAccountDto();
    }

    @Transactional
    public UserAccountDto changeUserAccountByAdmin(UserAccountDto account) {
        UserAccount userAccount = accountRepository.findByUserId(account.getUserId());
        userAccount.setEmailAddress(account.getEmailAddress());

        userAccount.setFirstName(account.getFirstName());

        if ( account.getLastName() != null) {
            userAccount.setLastName(account.getLastName());
        }
        if ( account.getPassword() != null ) {
            userAccount.setPassword(account.getPassword());
        }
        UserAccount updatedUserAccount = accountRepository.save(userAccount);
        List<AccountAuthority> roles = changeUserRoles(account.getUserId(), account.getRoles());
        return translate(updatedUserAccount, roles);
    }

    @Transactional
    public UserAccountDto changeUserAccountByUser(UserAccountDto account) throws AttemptingToChangeUserRoles {
        if ( rolesChanged(account)) {
            throw new AttemptingToChangeUserRoles();
        }

        UserAccount userAccount = accountRepository.findByUserId(account.getUserId());
        userAccount.setEmailAddress(account.getEmailAddress());
        userAccount.setFirstName(account.getFirstName());
        userAccount.setLastName(account.getLastName());
        if ( account.getPassword() != null) {
        userAccount.setPassword(account.getPassword());
        }
        UserAccount updatedUserAccount = accountRepository.save(userAccount);
        return translate(updatedUserAccount, accountAuthoritiesRepository.findByUserId(account.getUserId()));
    }

    private boolean rolesChanged(UserAccountDto account) {
        final List<AccountAuthority> currentRoles = accountAuthoritiesRepository.findByUserId(account.getUserId());
        if ( currentRoles.size() != account.getRoles().size()) {
            return true;
        }
        for (String currentRole : account.getRoles()) {
            boolean found = false;
            for (AccountAuthority role : currentRoles) {
                if ( currentRole.equals(role.getAuthority())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public List<AccountAuthority> changeUserRoles(String userName, List<String> userRoles) {
        final List<AccountAuthority> currentRoles = accountAuthoritiesRepository.findByUserId(userName);
        List<AccountAuthority> newUserRoles = new ArrayList<>();
        accountAuthoritiesRepository.delete(currentRoles);

        for ( String role: userRoles) {
            AccountAuthority newRole = new AccountAuthorityBuilder()
                    .withUserId(userName)
                    .withAuthority(role)
                    .create();

            newUserRoles.add(newRole);
            accountAuthoritiesRepository.save(newRole);
        }
        return newUserRoles;
    }
}
