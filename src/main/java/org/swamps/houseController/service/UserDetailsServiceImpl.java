package org.swamps.houseController.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swamps.houseController.domain.AccountAuthority;
import org.swamps.houseController.domain.AccountAuthorityBuilder;
import org.swamps.houseController.domain.UserAccount;
import org.swamps.houseController.domain.UserAccountBuilder;
import org.swamps.houseController.domain.repositories.AccountAuthoritiesRepository;
import org.swamps.houseController.domain.repositories.AccountRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserAccount person = accountRepository.findByUserId(username);

        if (person == null) {
            if ( accountRepository.count() == 0) {
                //createDefaultAdmin();
                return buildDefaultAdmin();
            }
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        return new User(username, person.getPassword(), createGrantedAuthorities(person.getRoles()));
        } catch ( Throwable ex ) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> createGrantedAuthorities(List<AccountAuthority> roles) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (AccountAuthority authority : roles) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return grantedAuthorityList;

    }

    private void createDefaultAdmin() {
        List<AccountAuthority> accountAuthorities = new ArrayList<>();
        accountAuthorities.add(AccountAuthorityBuilder.newAuthority()
                .withAuthority("ROLE_ADMIN")
                .withUserId("admin")
                .create());

        UserAccount useraccount = UserAccountBuilder.emptyAccount()
                .withEmailAddress("")
                .withFirstName("admin")
                .withLastName("admin")
                .withPassword("admin")
                .withUserId("admin")
                .withRoles(accountAuthorities)
                .create();

        accountRepository.save(useraccount);

    }

    private UserDetails buildDefaultAdmin() {

        return new User("admin", "admin", buildAdminAuthorities());
    }

    private Collection<? extends GrantedAuthority> buildAdminAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return grantedAuthorityList;
    }



}
