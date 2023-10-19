package com.chrinovicmm.tolobelacongo.dependanciesIncluded

import android.content.Context
import com.chrinovicmm.tolobelacongo.util.GoogleAuthUiHelper
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provedesSignedClient(@ApplicationContext context: Context): SignInClient{
        return Identity.getSignInClient(context)
    }

    @Provides
    fun providesGoogleAuthSignInClient(
        signInClient: SignInClient,
        @ApplicationContext context: Context
    ): GoogleAuthUiHelper{
        return GoogleAuthUiHelper(context, signInClient)
    }


    @Provides
    fun provideBlogRef() = Firebase.firestore.collection("Blogs")

    @Provides
    fun proviideStorageRef() = Firebase.storage.reference
}