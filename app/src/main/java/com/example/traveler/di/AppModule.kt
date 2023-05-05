package com.example.traveler.di
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.room.Room
import com.example.traveler.Constants.SHARED_PREFERENCES_NAME
import com.example.traveler.Constants.TRACKING_DATABASE_NAME
import com.example.traveler.DB.TrackDAO
import com.example.traveler.DB.TrackingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
    fun provideTrackDao(db: TrackingDatabase): TrackDAO = db.getTrackDao()


}