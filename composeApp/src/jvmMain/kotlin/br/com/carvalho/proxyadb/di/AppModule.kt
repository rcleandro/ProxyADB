package br.com.carvalho.proxyadb.di

import br.com.carvalho.proxyadb.data.AdbRepositoryImpl
import br.com.carvalho.proxyadb.data.NetworkRepositoryImpl
import br.com.carvalho.proxyadb.domain.AdbRepository
import br.com.carvalho.proxyadb.domain.CheckAdbAvailabilityUseCase
import br.com.carvalho.proxyadb.domain.DisableProxyUseCase
import br.com.carvalho.proxyadb.domain.EnableProxyUseCase
import br.com.carvalho.proxyadb.domain.GetLocalIpUseCase
import br.com.carvalho.proxyadb.domain.NetworkRepository
import br.com.carvalho.proxyadb.presentation.ProxyViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::AdbRepositoryImpl) bind AdbRepository::class
    singleOf(::NetworkRepositoryImpl) bind NetworkRepository::class

    factoryOf(::EnableProxyUseCase)
    factoryOf(::DisableProxyUseCase)
    factoryOf(::CheckAdbAvailabilityUseCase)
    factoryOf(::GetLocalIpUseCase)

    viewModelOf(::ProxyViewModel)
}
