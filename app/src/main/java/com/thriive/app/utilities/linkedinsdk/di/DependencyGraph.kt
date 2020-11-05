package com.thriive.app.utilities.linkedinsdk.di

import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases.retrieve_access_token.usecases.RetrieveAccessTokenUseCase
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases.retrieve_basic_profile.usecase.RetrieveBasicProfileInfoUseCase
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.utils.Executors
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.utils.RequestHandler
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.presentation.linkedin_authentication.models.LinkedInInitializationInfo
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.presentation.linkedin_authentication.viewmodel.LinkedInAuthenticationViewModelFactory

class DependencyGraph(
        private val initializationInfo: LinkedInInitializationInfo,
        private val requestHandler: RequestHandler,
        private val executors: Executors
) {
    fun providesRetrieveAccessTokenUseCase() = RetrieveAccessTokenUseCase(requestHandler)
    fun providesRetrieveBasicProfileInfoUseCase() = RetrieveBasicProfileInfoUseCase(requestHandler)
    fun providesLinkedInAuthenticationViewModelFactory() = LinkedInAuthenticationViewModelFactory(
            initializationInfo,
            providesRetrieveAccessTokenUseCase(),
            providesRetrieveBasicProfileInfoUseCase(),
            executors
    )
}