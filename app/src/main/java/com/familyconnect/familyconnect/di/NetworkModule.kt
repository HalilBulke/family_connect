package com.familyconnect.familyconnect.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.familyconnect.familyconnect.login.UserToken
import com.familyconnect.familyconnect.login.LoginRepository
import com.familyconnect.familyconnect.login.NetworkLoginRepository
import com.familyconnect.familyconnect.login.LoginApiService
import com.familyconnect.familyconnect.register.NetworkRegisterRepository
import com.familyconnect.familyconnect.register.RegisterApiService
import com.familyconnect.familyconnect.register.RegisterRepository
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

private const val BASE_URL = "http://localhost:8080"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(converterFactory: Converter.Factory, httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthorizationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .header("Authorization", UserToken.getToken())
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideLogin(apiService: LoginApiService): LoginRepository {
        return NetworkLoginRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRegister(apiService: RegisterApiService): RegisterRepository {
        return NetworkRegisterRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideRegisterApiService(retrofit: Retrofit): RegisterApiService {
        return retrofit.create(RegisterApiService::class.java)
    }
}