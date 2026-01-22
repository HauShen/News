package com.hau.news.services;

import com.hau.news.requestBodies.UserRequestBody;
import com.hau.news.requestBodies.UserUpdatedRequestBody;
import com.hau.news.responseBodies.UserResponseBody;
import com.hau.news.responseBodies.UserUpdatedResponseBody;

public interface UserService {
     UserResponseBody createUser(UserRequestBody userRequestBody);
     UserResponseBody getUserByIdOrThrow(String userId);
     UserUpdatedResponseBody updateUserDetailsById(String userId, UserUpdatedRequestBody userUpdatedRequestBody);
     String deleteUserById(String userId);
}
