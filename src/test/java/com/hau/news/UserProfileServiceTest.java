package com.hau.news;

import com.hau.news.models.roles.Role;
import com.hau.news.models.UserProfile;
import com.hau.news.repositories.UserRepository;
import com.hau.news.requestbodies.UserRequestBody;
import com.hau.news.requestbodies.UserUpdatedRequestBody;
import com.hau.news.responsebodies.UserResponseBody;
import com.hau.news.responsebodies.UserUpdatedResponseBody;
import com.hau.news.serviceimpls.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private UserRepository fakeUserRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private UserProfile expectedUserProfile = new UserProfile("123","John",12, Role.READER);
    private UserProfile expectedUpadtedUserProfile = new UserProfile("123","John",13, Role.READER);
    private UserResponseBody expectedUserResponseBody = new UserResponseBody(expectedUserProfile);
    private UserUpdatedResponseBody expectedUserUpdatedResponseBody =  new UserUpdatedResponseBody(expectedUpadtedUserProfile);
    private UserRequestBody fakeUserRequestBody = new UserRequestBody("John",12, Role.READER);
    private UserUpdatedRequestBody fakeUpdatedUserRequestBody = new UserUpdatedRequestBody("John",13,Role.READER);



    @Test
    public void createUserShouldInsertCorrectly(){
//        Mockito.when(fakeUserRepository.save(Mockito.any(User.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(fakeUserRepository.save(any(UserProfile.class))).thenReturn(expectedUserProfile);
        UserResponseBody actualUserResponseBody = userServiceImpl.createUser(fakeUserRequestBody);
        assertThat(actualUserResponseBody)
                .usingRecursiveComparison()
                .ignoringFields("userId")
                .isEqualTo(expectedUserResponseBody);
    }
    @Test
    public void getUserByIdShouldGetCorrectUser(){
        Mockito.when(fakeUserRepository.findByUserId("123")).thenReturn(expectedUserProfile);
        UserResponseBody actualUserResponseBody = userServiceImpl.getUserByIdOrThrow("123");
        assertThat(actualUserResponseBody)
                .usingRecursiveComparison()
                .ignoringFields("userId")
                .isEqualTo(expectedUserResponseBody);
    }

    @Test
    public void getUserByIdOrThrow_whenNotFound_shouldThrowException() {
        Mockito.when(fakeUserRepository.findByUserId("999")).thenReturn(null);
        org.junit.jupiter.api.Assertions.assertThrows(
                com.hau.news.models.exceptions.UserNotFoundException.class,
                () -> userServiceImpl.getUserByIdOrThrow("999")
        );
    }

    @Test
    public void updateUserDetailsByIdShouldEditCorrectly(){
        Mockito.when(fakeUserRepository.findByUserId("123")).thenReturn(expectedUserProfile);
        UserUpdatedResponseBody actualUserUpdatedResponseBody =  userServiceImpl.updateUserDetailsById("123",fakeUpdatedUserRequestBody);
        assertThat(actualUserUpdatedResponseBody)
                .usingRecursiveComparison()
                .isEqualTo(expectedUserUpdatedResponseBody);

    }

    @Test
    public void deleteUserByUserIdShouldDeleteCorrectly(){
        String actualDeleteUser = userServiceImpl.deleteUserById("123");
        Mockito.verify(fakeUserRepository,Mockito.times(1)).deleteById("123");
        assertThat("User with Id 123 is deleted")
                .isEqualTo(actualDeleteUser);

    }

}
