package ru.nblackie.dictionary.impl.di.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import ru.nblackie.core.fragment.ContainerFragment
import ru.nblackie.core.viewmodel.*
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.PerFeature
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapper
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapperImpl
import ru.nblackie.dictionary.impl.data.repository.DictionaryRepositoryImpl
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCase
import ru.nblackie.dictionary.impl.domain.usecase.DictionaryUseCaseImpl
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.dictionary.impl.presentation.DictionaryStackFragment
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryViewModel
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryViewModelNew
import ru.nblackie.dictionary.impl.presentation.search.SearchViewModel
import ru.nblackie.remote.impl.dictionary.RemoteDictionaryApi

/**
 * @author tatarchukilya@gmail.com
 */
@Module
internal object DictionaryFeatureModule {

    @JvmStatic
    @Provides
    @PerFeature
    fun provideContainerFragment(): ContainerFragment = DictionaryStackFragment.newInstance()

    @JvmStatic
    @Provides
    @PerFeature
    fun provideNavHostFragment(): NavHostFragment = NavHostFragment.create(R.navigation.dictionary)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideApiMapper(api: RemoteDictionaryApi): DictionaryApiMapper =
        DictionaryApiMapperImpl(api)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideRepository(
        apiMapper: DictionaryApiMapper,
        dao: DictionaryDao
    ): DictionaryRepository = DictionaryRepositoryImpl(apiMapper, dao)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideDictionaryInteractor(repository: DictionaryRepository): DictionaryUseCase =
        DictionaryUseCaseImpl(repository)

    @JvmStatic
    @Provides
    fun provideDictionaryViewModelProviderFactory(useCase: DictionaryUseCase):
            ViewModelProviderFactory<DictionaryViewModel> =
        ViewModelProviderFactory { DictionaryViewModel(useCase) }

    @JvmStatic
    @Provides
    fun provideSearchViewModelProviderFactory(useCase: DictionaryUseCase):
            ViewModelProviderFactory<SearchViewModel> =
        ViewModelProviderFactory { SearchViewModel(useCase) }

    @JvmStatic
    @Provides
    fun provideTempCreator(useCase: DictionaryUseCase): ViewModelAssistedProvideFactory<DictionaryViewModelNew> {
        return ViewModelAssistedProvideFactory<DictionaryViewModelNew> { owner, defaultArgs ->
            SaveStateViewModelProviderFactory(owner, defaultArgs) {
                DictionaryViewModelNew(useCase, it)
            }
        }
    }
}