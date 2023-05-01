package com.example.traveler.di
import android.content.Context
import androidx.room.Room
import com.example.traveler.Constants.TRACKING_DATABASE_NAME
import com.example.traveler.DB.TrackingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTrackingDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TrackingDatabase::class.java,
        TRACKING_DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideTrackDao(db: TrackingDatabase) = db.getTrackDao()

}