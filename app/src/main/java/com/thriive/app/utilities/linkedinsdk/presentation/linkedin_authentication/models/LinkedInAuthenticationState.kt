package com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.presentation.linkedin_authentication.models

import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases.retrieve_access_token.models.LinkedInAccessTokenInfo
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases.retrieve_access_token.models.errors.RetrieveAccessTokenError
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases.retrieve_basic_profile.models.LinkedInBasicProfileInfo
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases.retrieve_basic_profile.models.error.RetrieveBasicProfileInfoError

sealed class LinkedInAuthenticationState {
    object Loading : LinkedInAuthenticationState()
    class AccessTokenRetrievedSuccessfully(val accessTokenInfo: LinkedInAccessTokenInfo) : LinkedInAuthenticationState()
    class FailedToRetrieveAccessToken(val accessTokenRetrievalError: RetrieveAccessTokenError) : LinkedInAuthenticationState()
    class BasicProfileRetrievedSuccessfully(val profileInfo: LinkedInBasicProfileInfo) : LinkedInAuthenticationState()
    class FailedToRetrieveProfile(val accessTokenRetrievalError: RetrieveBasicProfileInfoError) : LinkedInAuthenticationState()
}