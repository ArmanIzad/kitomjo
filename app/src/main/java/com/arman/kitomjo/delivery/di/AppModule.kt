package com.arman.kitomjo.delivery.di

import com.arman.kitomjo.delivery.data.OfferRepositoryImpl
import com.arman.kitomjo.delivery.domain.repository.OfferRepository
import com.arman.kitomjo.delivery.domain.usecase.CalculateDeliveryTimeUseCase
import com.arman.kitomjo.delivery.domain.usecase.DefaultDeliveryCostStrategy
import com.arman.kitomjo.delivery.domain.usecase.DefaultOfferStrategy
import com.arman.kitomjo.delivery.domain.usecase.DeliveryCostUseCase
import com.arman.kitomjo.delivery.domain.usecase.OfferUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDeliveryRepository(): OfferRepository {
        return OfferRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providesDeliveryUseCase(): OfferUseCase {
        return DefaultOfferStrategy
    }

    @Provides
    @Singleton
    fun providesDeliveryCostUseCase(): DeliveryCostUseCase {
        return DefaultDeliveryCostStrategy
    }

    @Provides
    @Singleton
    fun providesCalculateDeliveryTimeUseCase(): CalculateDeliveryTimeUseCase {
        return CalculateDeliveryTimeUseCase()
    }
}