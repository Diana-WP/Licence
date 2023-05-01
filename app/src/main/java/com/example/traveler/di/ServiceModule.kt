package com.example.traveler.di

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.traveler.Constants
import com.example.traveler.R
import com.example.traveler.TrackingService
import com.example.traveler.UI.TrackingActivity
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
//The object created by this provider function will have a scope that matches the lifecycle of a service.
    @ServiceScoped
//indicates that this function is a provider function that will create and provide the dependency.
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext app: Context
    ) = getFusedLocationProviderClient(app)


    @RequiresApi(Build.VERSION_CODES.M)
    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ) = PendingIntent.getActivity(
        app,
        0,
        Intent(app, TrackingActivity::class.java).also{
            it.action = Constants.ACTION_SHOW_TRACKING
        },
        PendingIntent.FLAG_IMMUTABLE
    )



    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app:Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.bike)
        .setContentTitle("Travel App")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)
}