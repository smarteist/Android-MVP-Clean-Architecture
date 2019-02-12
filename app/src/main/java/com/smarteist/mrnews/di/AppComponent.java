package com.smarteist.mrnews.di;



import com.smarteist.mrnews.Application;
import com.smarteist.mrnews.data.source.remote.NewsService;
import com.smarteist.mrnews.di.modules.AppModule;
import com.smarteist.mrnews.di.modules.ActivityBindingModule;
import com.smarteist.mrnews.di.modules.NewsRepositoryModule;
import com.smarteist.mrnews.di.modules.UtilityModule;
import com.smarteist.mrnews.di.scopes.ApplicationScoped;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * This is the root Dagger component.
 * {@link AndroidSupportInjectionModule}
 * is the module from Dagger.Android that helps with the generation
 * and location of subcomponents, which will be in our case, activities
 */
@ApplicationScoped
@Component(modules = {
        AndroidSupportInjectionModule.class, //!IMPORTANT
        AppModule.class,
        NewsRepositoryModule.class,
        UtilityModule.class,
        ActivityBindingModule.class
})
public interface AppComponent extends AndroidInjector<Application> {

    NewsService getNewsService();

    // we can now do DaggerAppComponent.builder().application(this).build().inject(this),
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(android.app.Application application);

        AppComponent build();
    }
}