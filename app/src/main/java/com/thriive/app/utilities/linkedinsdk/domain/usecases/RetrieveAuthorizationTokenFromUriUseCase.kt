package com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.usecases

import android.net.Uri
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.abstracts.UseCase
import com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.utils.LinkedInConst

class RetrieveAuthorizationTokenFromUriUseCase : UseCase<Uri, String?>() {
    override fun run(input: Uri?): String? {
        return input?.getQueryParameter(LinkedInConst.RESPONSE_TYPE_VALUE)
    }
}