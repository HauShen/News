package com.hau.news.services;

import com.hau.news.requestbodies.UserRequestBody;
import com.hau.news.requestbodies.UserUpdatedRequestBody;
import com.hau.news.responsebodies.UserResponseBody;
import com.hau.news.responsebodies.UserUpdatedResponseBody;

public interface UserService {
     UserResponseBody createUser(UserRequestBody userRequestBody);
     UserResponseBody getUserByIdOrThrow(String userId);
     UserUpdatedResponseBody updateUserDetailsById(String userId, UserUpdatedRequestBody userUpdatedRequestBody);
     String deleteUserById(String userId);
}
