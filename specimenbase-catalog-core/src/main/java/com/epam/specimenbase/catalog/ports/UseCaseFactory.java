package com.epam.specimenbase.catalog.ports;

import com.epam.specimenbase.catalog.domain.samples.*;
import com.epam.specimenbase.catalog.domain.users.GetUserDetails;
import com.epam.specimenbase.catalog.domain.users.LogInUser;
import com.epam.specimenbase.catalog.domain.users.RegisterUser;

public interface UseCaseFactory {

    RegisterUser registerUser();

    LogInUser logInUser();

    GetUserDetails getUserDetails();

    ListSamples listSamples();

    AddSample addSample();

    GetSample getSample();

    DeleteSample deleteSample();

    UpdateSample updateSample();
}
