package ru.nblackie.dictionary.impl.di.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import ru.nblackie.core.api.ResourceManager
import ru.nblackie.core.impl.fragment.ContainerFragment
import ru.nblackie.core.impl.viewmodel.*
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.PerFeature
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.cache.Cache
import ru.nblackie.dictionary.impl.data.cache.CacheImpl
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapper
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapperImpl
import ru.nblackie.dictionary.impl.data.repository.DictionaryRepositoryImpl
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCaseImpl
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.dictionary.impl.presentation.DictionaryStackFragment
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryViewModelNew
import ru.nblackie.dictionary.impl.presentation.core.SharedViewModel
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object DictionaryFeatureModule {

    @Provides
    @PerFeature
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
    fun provideApiMapper(api: RemoteDictionaryApi): DictionaryApiMapper =
        DictionaryApiMapperImpl(api)

    @Provides
    @PerFeature
    fun provideRepository(
        apiMapper: DictionaryApiMapper,
        dao: DictionaryDao
    ): DictionaryRepository = DictionaryRepositoryImpl(apiMapper, dao)

    @Provides
    @PerFeature
    fun provideDictionaryUseCase(repository: DictionaryRepository): DictionaryUseCase =
        DictionaryUseCaseImpl(repository)

    @Provides
    fun provideTempCreator(useCase: DictionaryUseCase): ViewModelAssistedProvideFactory<DictionaryViewModelNew> {
        return ViewModelAssistedProvideFactory<DictionaryViewModelNew> { owner, defaultArgs ->
            SaveStateViewModelProviderFactory(owner, defaultArgs) {
                DictionaryViewModelNew(useCase, it)
            }
        }
    }

    @Provides
    fun provideViewModelProviderFactory(useCase: DictionaryUseCase):
        ViewModelProviderFactory<SharedViewModel> =
        ViewModelProviderFactory { SharedViewModel(useCase) }
}