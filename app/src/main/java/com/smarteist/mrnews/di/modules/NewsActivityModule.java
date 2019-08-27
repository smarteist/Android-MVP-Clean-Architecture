package com.smarteist.mrnews.di.modules;

import com.smarteist.mrnews.di.scopes.ActivityScoped;
import com.smarteist.mrnews.di.scopes.FragmentScoped;
import com.smarteist.mrnews.util.ChromeTabsUtils.ChromeTabsWrapper;
import com.smarteist.mrnews.views.activities.NewsActivity;
import com.smarteist.mrnews.views.fragments.NewsContracts;
import com.smarteist.mrnews.views.fragments.NewsPresenter;
import com.smarteist.mrnews.views.fragments.NewsFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import io.reactivex.disposables.CompositeDisposable;


/**
 * NewsActivityModule contains an inner abstract module that binds {@link NewsContracts.Presenter}
 * and {@link NewsFragment}
 * This is an alternative to having an abstract NewsActivityModule class with static @Provides methods
 */
@Module(includes = {NewsActivityModule.NewsAbstractModule.class})
public class NewsActivityModule {

    @ActivityScoped
    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @ActivityScoped
    @Provides
    ChromeTabsWrapper providesChromeTabsWrapper(NewsActivity context) {
        return new ChromeTabsWrapper(context);
    }

    @Module
    public abstract class NewsAbstractModule {

        @ActivityScoped
        @Binds
        abstract NewsContracts.Presenter newsPresenter(NewsPresenter presenter);

        @FragmentScoped
        @ContributesAndroidInjector
        abstract NewsFragment newsFragment();

    }
}