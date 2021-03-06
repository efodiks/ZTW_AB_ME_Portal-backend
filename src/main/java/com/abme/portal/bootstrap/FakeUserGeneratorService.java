package com.abme.portal.bootstrap;

import com.abme.portal.domain.role.Role;
import com.abme.portal.domain.role.RoleName;
import com.abme.portal.domain.user.User;
import com.abme.portal.domain.role.RoleRepository;
import com.abme.portal.domain.user.UserRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FakeUserGeneratorService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Faker faker;
    private final Random random = new Random();

    private final int FOLLOWING_LOWER_BOUND = 5;
    private final int FOLLOWING_UPPER_BOUND_EXCLUSIVE = 12;


    public void insertFakeUsers(int count) {
        var userAuthority = userAuthority();
        var users = Stream
                .generate(() -> generateUser(userAuthority))
                .limit(count)
                .collect(Collectors.toList());
        userRepository.saveAll(users);
        fillInFollowingAndFollowedByFor(users);
        userRepository.saveAll(users);
    }

    private Role userAuthority() {
        return roleRepository.findByName(RoleName.ROLE_USER);
    }

    public User generateUser(Role userRole) {
        var firstName = faker.name().firstName();
        var firstNameLowerCaseStripped = StringUtils.stripAccents(firstName).toLowerCase();
        var lastName = faker.name().lastName();
        var lastNameLowerCaseStripped = StringUtils.stripAccents(lastName).toLowerCase();

        var username = firstNameLowerCaseStripped + "." + lastNameLowerCaseStripped;
        var email = username + "@example.com";


        return new User()
                .setUuid(UUID.randomUUID())
                .setEmail(email)
                .setPasswordHash(passwordEncoder.encode(faker.internet().password()))
                .setFirstName(firstName)
                .setLastName(lastName)
                .setUsername(username)
                .setProfilePhotoUrl(faker.internet().avatar())
                .setRole(userRole);
    }

    public void fillInFollowingAndFollowedByFor(List<User> users) {
        var usersList = new ArrayList<>(users);

        users.forEach(user -> user.setFollowing(randomUsers(usersList, user)));
    }

    private Set<User> randomUsers(List<User> usersList, User target) {
        Collections.shuffle(usersList, random);
        var numberOfUsers = random.nextInt(FOLLOWING_UPPER_BOUND_EXCLUSIVE) + FOLLOWING_LOWER_BOUND;
        var set = usersList
                .stream()
                .limit(numberOfUsers)
                .collect(Collectors.toSet());
        set.remove(target);
        return set;
    }
}
