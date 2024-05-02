package com.familyconnect.familyconnect.di

import android.util.Log
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberRepository
import com.familyconnect.familyconnect.addfamilymember.AddFamilyMemberRepositoryImpl
import com.familyconnect.familyconnect.addfamilymember.AddMemberApiService
import com.familyconnect.familyconnect.allProgress.AllProgressApiService
import com.familyconnect.familyconnect.allProgress.AllProgressRepository
import com.familyconnect.familyconnect.allProgress.NetworkAllProgressRepository
import com.familyconnect.familyconnect.calendar.CalendarApiService
import com.familyconnect.familyconnect.calendar.CalenderRepository
import com.familyconnect.familyconnect.calendar.NetworkCalenderRepository
import com.familyconnect.familyconnect.createevent.CreateEventApiService
import com.familyconnect.familyconnect.createevent.CreateEventRepository
import com.familyconnect.familyconnect.createevent.CreateEventRepositoryImpl
import com.familyconnect.familyconnect.createprogress.CreateProgressApiService
import com.familyconnect.familyconnect.createprogress.ProgressRepository
import com.familyconnect.familyconnect.createprogress.ProgressRepositoryImpl
import com.familyconnect.familyconnect.displayfamily.DisplayFamilyRepository
import com.familyconnect.familyconnect.displayfamily.FamilyApiService
import com.familyconnect.familyconnect.displayfamily.GetFamilyRepository
import com.familyconnect.familyconnect.family.CreateFamilyApiService
import com.familyconnect.familyconnect.family.FamilyRepository
import com.familyconnect.familyconnect.family.NetworkFamilyRepository
import com.familyconnect.familyconnect.familyRewards.FamilyRewardsApiService
import com.familyconnect.familyconnect.familyRewards.FamilyRewardsRepository
import com.familyconnect.familyconnect.familyRewards.FamilyRewardsRepositoryImpl
import com.familyconnect.familyconnect.login.LoginApiService
import com.familyconnect.familyconnect.login.LoginRepository
import com.familyconnect.familyconnect.login.NetworkLoginRepository
import com.familyconnect.familyconnect.login.UserToken
import com.familyconnect.familyconnect.maindashboard.DefaultMainDashboardRepository
import com.familyconnect.familyconnect.maindashboard.MainDashboardApiService
import com.familyconnect.familyconnect.maindashboard.MainDashboardRepository
import com.familyconnect.familyconnect.profile.NetworkProfileRepository
import com.familyconnect.familyconnect.profile.ProfileApiService
import com.familyconnect.familyconnect.profile.ProfileRepository
import com.familyconnect.familyconnect.progressGetChild.GetProgressRepository
import com.familyconnect.familyconnect.progressGetChild.NetworkGetProgressRepository
import com.familyconnect.familyconnect.progressGetChild.ProgressApiService
import com.familyconnect.familyconnect.register.NetworkRegisterRepository
import com.familyconnect.familyconnect.register.RegisterApiService
import com.familyconnect.familyconnect.register.RegisterRepository
import com.familyconnect.familyconnect.task.CreateTaskApiService
import com.familyconnect.familyconnect.task.TaskRepository
import com.familyconnect.familyconnect.task.TaskRepositoryImpl
import com.familyconnect.familyconnect.taskGetchild.GetTasksRepository
import com.familyconnect.familyconnect.taskGetchild.NetworkGetTasksRepository
import com.familyconnect.familyconnect.taskGetchild.TaskApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//Enter your local ip as subdomain instead of 192.168.1.2
private const val BASE_URL = "http://192.168.1.83:8000"

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
            Log.d("HEADER-------------------------", UserToken.getToken())
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



    //Create new Task
    @Provides
    @Singleton
    fun provideTaskRepository(createTaskApiService: CreateTaskApiService): TaskRepository {
        return TaskRepositoryImpl(createTaskApiService)
    }

    @Provides
    @Singleton
    fun provideCreateTaskApiService(retrofit: Retrofit): CreateTaskApiService =
        retrofit.create(CreateTaskApiService::class.java)


    //Get Tasks

    @Provides
    @Singleton
    fun provideGetTaskRepository(taskApiService: TaskApiService): GetTasksRepository {
        return NetworkGetTasksRepository(taskApiService)
    }

    @Provides
    @Singleton
    fun provideTaskApiService(retrofit: Retrofit): TaskApiService {
        return retrofit.create(TaskApiService::class.java)
    }

    // Provide the implementation for the TaskRepository
    @Provides
    @Singleton
    fun provideProgressRepository(createProgressApiService: CreateProgressApiService): ProgressRepository {
        return ProgressRepositoryImpl(createProgressApiService)
    }

    // Provide the API service to create tasks
    @Provides
    @Singleton
    fun provideCreateProgressApiService(retrofit: Retrofit): CreateProgressApiService =
        retrofit.create(CreateProgressApiService::class.java)


    @Provides
    @Singleton
    fun provideProgressApiService(retrofit: Retrofit): ProgressApiService {
        return retrofit.create(ProgressApiService::class.java)
    }

    // Family
    @Provides
    @Singleton
    fun provideFamilyRepository(createFamilyApiService: CreateFamilyApiService): FamilyRepository {
        return NetworkFamilyRepository(createFamilyApiService)
    }

    @Provides
    @Singleton
    fun provideCreateFamilyApiService(retrofit: Retrofit): CreateFamilyApiService {
        return retrofit.create(CreateFamilyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDisplayFamilyRepository(familyApiService: FamilyApiService): GetFamilyRepository {
        return DisplayFamilyRepository(familyApiService)
    }

    @Provides
    @Singleton
    fun provideDisplayFamilyApiService(retrofit: Retrofit): FamilyApiService {
        return retrofit.create(FamilyApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAddMemberRepository(addMemberApiService: AddMemberApiService): AddFamilyMemberRepository {
        return AddFamilyMemberRepositoryImpl(addMemberApiService)
    }

    @Provides
    @Singleton
    fun provideAddMemberApiService(retrofit: Retrofit): AddMemberApiService {
        return retrofit.create(AddMemberApiService::class.java)
    }

    //dashboard
    @Provides
    @Singleton
    fun provideMainDashboardRepository(mainDashboardApiService: MainDashboardApiService): MainDashboardRepository {
        return DefaultMainDashboardRepository(mainDashboardApiService)
    }

    @Provides
    @Singleton
    fun provideMainDashboardApiService(retrofit: Retrofit): MainDashboardApiService {
        return retrofit.create(MainDashboardApiService::class.java)
    }

    //profile
    @Provides
    @Singleton
    fun provideProfile(profileApiService: ProfileApiService): ProfileRepository {
        return NetworkProfileRepository(profileApiService)
    }

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    //create event
    @Provides
    @Singleton
    fun provideCreateEvent(createEventApiService: CreateEventApiService): CreateEventRepository {
        return CreateEventRepositoryImpl(createEventApiService)
    }

    @Provides
    @Singleton
    fun provideCreateEventApiService(retrofit: Retrofit): CreateEventApiService {
        return retrofit.create(CreateEventApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCalender(apiService: CalendarApiService): CalenderRepository {
        return NetworkCalenderRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideCalenderApiService(retrofit: Retrofit): CalendarApiService {
        return retrofit.create(CalendarApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFamilyRewardsRepository(apiService: FamilyRewardsApiService): FamilyRewardsRepository {
        return FamilyRewardsRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideFamilyRewardsApiService(retrofit: Retrofit): FamilyRewardsApiService {
        return retrofit.create(FamilyRewardsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAllProgress(apiService: AllProgressApiService): AllProgressRepository {
        return NetworkAllProgressRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideAllProgressApiService(retrofit: Retrofit): AllProgressApiService {
        return retrofit.create(AllProgressApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGetProgress(apiService: ProgressApiService): GetProgressRepository {
        return NetworkGetProgressRepository(apiService)
    }
}