package com.epam.specimenbase.catalog.ports;

import com.epam.specimenbase.catalog.domain.GetUserDetails;
import com.epam.specimenbase.catalog.domain.LogInUser;
import com.epam.specimenbase.catalog.domain.RegisterUser;

public interface UseCaseFactory {

    RegisterUser registerUser();

    LogInUser logInUser();

    GetUserDetails getUserDetails();

}
