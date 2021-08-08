package ru.nblackie.dictionary.impl.di.internal

import androidx.navigation.fragment.NavHostFragment
import dagger.Module
import dagger.Provides
import ru.nblackie.core.viewmodel.ViewModelProviderFactory
import ru.nblackie.coredb.impl.db.DictionaryDao
import ru.nblackie.coredi.PerFeature
import ru.nblackie.dictionary.R
import ru.nblackie.dictionary.impl.data.db.DictionaryDataBase
import ru.nblackie.dictionary.impl.data.db.DictionaryDataBaseImpl
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapper
import ru.nblackie.dictionary.impl.data.remote.DictionaryApiMapperImpl
import ru.nblackie.dictionary.impl.data.repository.DictionaryRepositoryImpl
import ru.nblackie.dictionary.impl.domain.interactor.DictionaryInteractor
import ru.nblackie.dictionary.impl.domain.interactor.DictionaryInteractorImpl
import ru.nblackie.dictionary.impl.domain.repository.DictionaryRepository
import ru.nblackie.dictionary.impl.presentation.dictionary.DictionaryViewModel
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
    fun provideNavHostFragment(): NavHostFragment = NavHostFragment.create(R.navigation.dictionary)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideApiMapper(api: RemoteDictionaryApi): DictionaryApiMapper =
        DictionaryApiMapperImpl(api)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideDataBase(dao: DictionaryDao): DictionaryDataBase =
        DictionaryDataBaseImpl(dao)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideRepository(
        apiMapper: DictionaryApiMapper,
        dataBase: DictionaryDataBase
    ): DictionaryRepository = DictionaryRepositoryImpl(apiMapper, dataBase)

    @JvmStatic
    @Provides
    @PerFeature
    fun provideDictionaryInteractor(repository: DictionaryRepository): DictionaryInteractor =
        DictionaryInteractorImpl(repository)

    @JvmStatic
    @Provides
    fun provideDictionaryViewModelProviderFactory(interactor: DictionaryInteractor):
            ViewModelProviderFactory<DictionaryViewModel> =
        ViewModelProviderFactory { DictionaryViewModel(interactor) }

    @JvmStatic
    @Provides
    fun provideSearchViewModelProviderFactory(interactor: DictionaryInteractor):
            ViewModelProviderFactory<SearchViewModel> =
        ViewModelProviderFactory { SearchViewModel(interactor) }
}