package com.thriive.app.utilities.linkedinsdkutil.linkedinsdk.domain.abstracts

abstract class UseCase<in Input, Output> {

    abstract fun run(input: Input?): Output
}