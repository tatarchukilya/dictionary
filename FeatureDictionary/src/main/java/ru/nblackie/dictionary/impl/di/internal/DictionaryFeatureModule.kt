package ru.nblackie.dictionary.impl.di.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.fragment.ContainerFragment
import ru.nblackie.core.impl.viewmodel.ViewModelProviderFactory
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.PerFeature
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.cache.Cache
import ru.nblackie.dictionary.impl.data.cache.CacheImpl
import ru.nblackie.dictionary.impl.data.repository.db.DbRepositoryImpl
import ru.nblackie.dictionary.impl.data.repository.remote.RemoteRepositoryImpl
import ru.nblackie.dictionary.impl.domain.repository.DbRepository
import ru.nblackie.dictionary.impl.domain.repository.RemoteRepository
import ru.nblackie.dictionary.impl.domain.usecase.UseCase
import ru.nblackie.dictionary.impl.domain.usecase.create.AddTranslationsUseCase
import ru.nblackie.dictionary.impl.domain.usecase.create.AddTranslationsUseCaseImpl
import ru.nblackie.dictionary.impl.domain.usecase.delete.DeleteTranslationUseCase
import ru.nblackie.dictionary.impl.domain.usecase.delete.DeleteTranslationUseCaseImpl
import ru.nblackie.dictionary.impl.domain.usecase.reed.DbSearchUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.DbSearchUseCaseImpl
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteCountUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteCountUseCaseImpl
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteSearchUseCase
import ru.nblackie.dictionary.impl.domain.usecase.reed.RemoteSearchUseCaseImpl
import ru.nblackie.dictionary.impl.presentation.DictionaryStackFragment
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object DictionaryFeatureModule {

    @Provides
    fun provideCache(): Cache<String> = CacheImpl()

    @Provides
    @PerFeature
    fun provideContainerFragment(): ContainerFragment = DictionaryStackFragment.newInstance()

    @Provides
    @PerFeature
    fun provideNavHostFragment(): NavHostFragment =
        NavHostFragment.create(R.navigation.navigation_dictionary)

    @Provides
    @PerFeature
    fun provideRemoteRepo(api: RemoteDictionaryApi): RemoteRepository = RemoteRepositoryImpl(api)

    @Provides
    @PerFeature
    fun provideDbRepo(dao: DictionaryDao): DbRepository = DbRepositoryImpl(dao)

    @IntoMap
    @Provides
    @PerFeature
    @UseCaseClassKey(RemoteSearchUseCase::class)
    fun provideRemoteSearchUseCase(
        remoteRepository: RemoteRepository,
        dbRepository: DbRepository,
        resourceManager: ResourceManager
    ): UseCase {
        return RemoteSearchUseCaseImpl(remoteRepository, dbRepository, CacheImpl(), resourceManager)
    }

    @IntoMap
    @Provides
    @PerFeature
    @UseCaseClassKey(DbSearchUseCase::class)
    fun provideDbSearchUseCase(repository: DbRepository): UseCase = DbSearchUseCaseImpl(repository)

    @IntoMap
    @Provides
    @PerFeature
    @UseCaseClassKey(RemoteCountUseCase::class)
    fun provideRemoteCountUseCase(repository: RemoteRepository): UseCase {
        return RemoteCountUseCaseImpl(repository)
    }

    @IntoMap
    @Provides
    @PerFeature
    @UseCaseClassKey(DeleteTranslationUseCase::class)
    fun provideDeleteTranslationUseCase(repository: DbRepository): UseCase {
        return DeleteTranslationUseCaseImpl(repository)
    }

    @IntoMap
    @Provides
    @PerFeature
    @UseCaseClassKey(AddTranslationsUseCase::class)
    fun provideAddTranslationUseCase(repository: DbRepository): UseCase {
        return AddTranslationsUseCaseImpl(repository)
    }

    @Provides
    fun provideViewModelProviderFactory():
        ViewModelProviderFactory<SharedViewModel> = ViewModelProviderFactory({
        SharedViewModel(HashMap())
    })
}