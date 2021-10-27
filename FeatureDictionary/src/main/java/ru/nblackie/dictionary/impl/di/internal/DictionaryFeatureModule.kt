package ru.nblackie.dictionary.impl.di.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
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

    @Provides
    @PerFeature
    fun provideRemoteSearchUseCase(
        remoteRepository: RemoteRepository,
        dbRepository: DbRepository,
        resourceManager: ResourceManager
    ): RemoteSearchUseCase {
        return RemoteSearchUseCaseImpl(remoteRepository, dbRepository, CacheImpl(), resourceManager)
    }

    @Provides
    @PerFeature
    fun provideDbSearchUseCase(repository: DbRepository): DbSearchUseCase = DbSearchUseCaseImpl(repository)

    @Provides
    @PerFeature
    fun provideRemoteCountUseCase(repository: RemoteRepository): RemoteCountUseCase {
        return RemoteCountUseCaseImpl(repository)
    }

    @Provides
    @PerFeature
    fun provideDeleteTranslationUseCase(repository: DbRepository): DeleteTranslationUseCase {
        return DeleteTranslationUseCaseImpl(repository)
    }

    @Provides
    @PerFeature
    fun provideAddTranslationUseCase(repository: DbRepository): AddTranslationsUseCase {
        return AddTranslationsUseCaseImpl(repository)
    }

    @Provides
    fun provideViewModelProviderFactory(
        multiSourceUseCase: RemoteSearchUseCase,
        dbSearch: DbSearchUseCase,
        remoteCountUseCase: RemoteCountUseCase,
        deleteTranslationsUseCase: DeleteTranslationUseCase,
        addTranslationsUseCase: AddTranslationsUseCase
    ):
        ViewModelProviderFactory<SharedViewModel> = ViewModelProviderFactory({
        SharedViewModel(
            multiSourceUseCase,
            dbSearch,
            remoteCountUseCase,
            deleteTranslationsUseCase,
            addTranslationsUseCase
        )
    })
}