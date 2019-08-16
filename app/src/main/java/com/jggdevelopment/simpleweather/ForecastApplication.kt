package com.jggdevelopment.simpleweather

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jggdevelopment.simpleweather.data.db.ForecastDatabase
import com.jggdevelopment.simpleweather.data.db.LocationSearchDatabase
import com.jggdevelopment.simpleweather.data.network.*
import com.jggdevelopment.simpleweather.data.provider.LocationProvider
import com.jggdevelopment.simpleweather.data.provider.LocationProviderImpl
import com.jggdevelopment.simpleweather.data.provider.UnitProvider
import com.jggdevelopment.simpleweather.data.provider.UnitProviderImpl
import com.jggdevelopment.simpleweather.data.repository.ForecastRepository
import com.jggdevelopment.simpleweather.data.repository.ForecastRepositoryImpl
import com.jggdevelopment.simpleweather.data.repository.LocationSearchRepository
import com.jggdevelopment.simpleweather.data.repository.LocationSearchRepositoryImpl
import com.jggdevelopment.simpleweather.ui.adapter.LocationSearchResultsAdapter
import com.jggdevelopment.simpleweather.ui.search.viewmodel.LocationResponseViewModelFactory
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

        // databases
        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { LocationSearchDatabase(instance()) }

        // DAOs
        bind() from singleton { instance<ForecastDatabase>().weatherResponseDao() }
        bind() from singleton { instance<LocationSearchDatabase>().locationResponseDao() }

        // API services
        bind() from singleton { DarkSkyWeatherApiService() }
        bind() from singleton { MapboxApiService() }

        // Network data sources
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<LocationNetworkDataSource>() with singleton { LocationNetworkDataSourceImpl(instance()) }

        // location provider
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }

        // repositories
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance()) }
        bind<LocationSearchRepository>() with singleton { LocationSearchRepositoryImpl(instance(), instance()) }

        // providers
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }

        // view model factories
        bind() from provider { WeatherResponseViewModelFactory(instance(), instance()) }
        bind() from provider { LocationResponseViewModelFactory(instance()) }

        // adapters
        bind() from provider { LocationSearchResultsAdapter(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.settings, false)
    }
}