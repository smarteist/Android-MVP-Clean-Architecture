# Android-MVP-Dagger2
This repository contains a detailed sample application that uses MVP as its presentation layer pattern. **The app aims to be extremely flexible to creating variants for automated and manual testing.** Also, the project implements and follows the guidelines presented in Google Sample [MVP+dagger2+dagger-android](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/).

Essential dependencies are Dagger2 with Dagger-android, RxJava2 with RxAndroid, Room, Retrofit and Espresso. Other noteworthy dependencies would be Mockito, Chrome CustomTabs, Picasso and Guava.
## App Demo
Mr. News is an app that displays news headlines from all around the world. A fixed number of headlines are continuously being fetched realtime. If offline, the app displays the most recent loaded headlines and offers the possibility of saving headlines for further reading when back online.

![content](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/scrolling.gif)
![content](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/archiving.gif)
![content](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/open_tab.gif)
## Presentation Layer
MVP pattern is used to facilitate automated testing and improve the separation of concerns in presentation logic.

As shown in the below diagram, the View layer is as passive as possible. The Presenter handles most of the logic, cancelling any dependancy between the View Layer and the Model Layer - at the cost of having a relation between the `Presenter` and its corresponding `View` through the use of `Contracts`. The model layer is completely isolated and centralized through the repository pattern.

![Presentation](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/presentation_layer_diagram.png)

## Model Layer
The model layer is structured on repository pattern so that the presenter has no clue on the origins of the data. 

The repository handles data interactions and transactions from two main data sources - local and remote:
- `NewsRemoteDataSource` defined by a REST API consumed with [Retrofit](http://square.github.io/retrofit)
- `NewsLocalDataSource` defined by a SQL database consumed with [Room](https://developer.android.com/topic/libraries/architecture/room)

There are two main use-cases, online and offline. In the online use-case data is first being fetched from the `NewsRemoteDataSource` and the repository data is refreshed. In case of failure,  `NewsLocalDataSource` is queried. As for the offline use-case, `NewsLocalDataSource` has priority.

When data is being retrieved (from any source), every response is propagated through callbacks all the way to the `NewsPresenter` that handles them accordingly.

The same way as the Presenter-View relation depends entirely on interfaces defined in `NewsContract`, decoupling is reinforced within the Model layer (entirely consisted by `NewsRepository`). Therefore, lower level components (which are the data sources: `NewsRemoteDataSource` and `NewsLocalDatasource`) are decoupled through `NewsDataSource` interface. Also, through their dependence on the same interface, these data sources are interchangeable.

In this manner, the project respects the DIP (Dependency Inversion Principle) as both low and high level modules depend on abstractions.

### Reactive approach
It is extremely important to note that this project has a low level of reactiveness, it might barely dream to the possibilities of a effective reactive approach.
Nevertheless, the app was intended to have a flexible and efficient testing capability, rather than a fully reactive build.

Even in this case, you will be able to notice RxJava's benefits when data is being retrieved by `NewsRemoteDataSource` from the REST client ([News API](https://newsapi.org/)):
- Threading is much easier, with no need for the dreaded `AsyncTasks`.
- Error handling is straightforward and comfortable.
- Any reactive process is immediately stopped in certain situations of the apps' life cycle with the help of `Disposable`.

## Dependency Injection
Dagger2 is used to externalize the creation of dependencies from the classes that use them. Android specific helpers are provided by `Dagger-Android` and the most significant advantage is that they generate a subcomponent for each `Activity` through a new code generator.
Such subcomponent is:
```java
@ActivityScoped
@ContributesAndroidInjector(modules = NewsModule.class)
abstract NewsActivity newsActivity(); 
```
The below diagram illustrates the most significant relations between components and modules. You can also get a quick glance on how annotations help us define custom Scopes in order to properly handle classes instantiation.

![Dependecy](https://github.com/catalinghita8/android-mvp-dagger2/blob/master/readme_pics/dagger_dependency_graph_diagram.png)
_Note: The above diagram might help you understand how Dagger-android works. Also, only essential components/modules/objects are included here, this is suggested by the "…"_
## Testing
The apps' components are easy to test due to the project's structure and also due to DI achieved through Dagger. Unit tests are conducted with the help of Mockito and Instrumentation tests with the help of Espresso. 

In order to synchronize the background tasks with the testing environment provided by Espresso, a custom implementation of `IdlingResource` has been integrated into the app. I have given more details on this topic in [this medium article](https://medium.com/@catalinghita8/integrate-espresso-idling-resources-in-your-app-to-build-flexible-ui-tests-c779e24f5057).

## Strong points
- Possess high flexibility to create variants for automated and manual testing.
- Possess lightweight structure due to its presentation layer pattern.
- Is scalable and easy to expand.
## Weak points
- Maintenance effort could be lower and scalability could be better - even though the app has a solid structure and complies to some of the SOLID principles, it cannot be considered as part of Clean Architecture, mainly because the Presenter contains most of the logic, therefore substituting Interactors (use-cases).
- Decoupling level could be higher - even though MVP presentation pattern reduces coupling, the relation between `Presenter` and its corresponding  `View` still remains through the use of `Contracts`.
- Possess medium complexity - other approaches might lower complexity and increase efficiency.

# Final notes:
- The app is not a polished ready-to-publish product, it acts as a boilerplate project or as a starting point for android enthusiasts out there.
- Using this project as your starting point and expanding it is also encouraged, as at this point it is very easy to add new modules.
