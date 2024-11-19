package com.example.taskflow

import com.example.taskflow.repositories.PriorityRepository
import com.example.taskflow.repositories.TaskRepository
import com.example.taskflow.sources.PrioritySources
import com.example.taskflow.sources.TaskSources
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
    fun provideTaskSources(): TaskSources {
        return TaskSources()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository {
        return TaskRepository(provideTaskSources())
    }

    @Provides
    @Singleton
    fun providePrioritySources(): PrioritySources {
        return PrioritySources()
    }

    @Provides
    @Singleton
    fun providePriorityRepository(): PriorityRepository {
        return PriorityRepository(providePrioritySources())
    }
}
