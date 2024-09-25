package com.fdhasna21.githubusers

import android.app.Application
import com.fdhasna21.githubusers.repository.GeneralRepositoryImp
import com.fdhasna21.githubusers.repository.UserRepositoryImp
import com.fdhasna21.githubusers.viewmodel.GeneralActivityViewModel
import com.fdhasna21.githubusers.viewmodel.MainActivityViewModel
import com.fdhasna21.githubusers.viewmodel.UserDetailActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Created by Fernanda Hasna on 24/09/2024.
 * Updated by Fernanda Hasna on 26/09/2024.
 */

class GitApplication : Application() {

    val appModule = module {
        single { GeneralRepositoryImp(get()) }
        single { UserRepositoryImp(get()) }

        viewModel { GeneralActivityViewModel(get()) }
        viewModel { MainActivityViewModel(get()) }
        viewModel { UserDetailActivityViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GitApplication)
            modules(appModule)
        }
    }
}