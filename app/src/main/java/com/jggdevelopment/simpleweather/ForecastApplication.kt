package com.jggdevelopment.simpleweather

import android.app.Application
import androidx.preference.PreferenceManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jggdevelopment.simpleweather.data.db.ForecastDatabase
import com.jggdevelopment.simpleweather.data.network.*
import com.jggdevelopment.simpleweather.data.provider.LocationProvider
import com.jggdevelopment.simpleweather.data.provider.LocationProviderImpl
import com.jggdevelopment.simpleweather.data.provider.UnitProvider
import com.jggdevelopment.simpleweather.data.provider.UnitProviderImpl
import com.jggdevelopment.simpleweather.data.repository.ForecastRepository
import com.jggdevelopment.simpleweather.data.repository.ForecastRepositoryImpl
import com.jggdevelopment.simpleweather.ui.weather.viewmodel.WeatherResponseViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { DarkSkyWeatherApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl() }
        bind() from provider { WeatherResponseViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false)
    }
}