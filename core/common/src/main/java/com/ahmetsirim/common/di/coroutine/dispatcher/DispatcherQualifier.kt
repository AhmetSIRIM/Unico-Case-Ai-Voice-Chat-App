package com.ahmetsirim.common.di.coroutine.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DispatcherQualifier(val dispatcherTypeEnum: DispatcherTypeEnum)