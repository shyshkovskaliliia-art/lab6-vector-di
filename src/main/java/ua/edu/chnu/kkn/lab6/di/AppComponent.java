package ua.edu.chnu.kkn.lab6.di;

import dagger.Component;
import ua.edu.chnu.kkn.lab6.App;
import ua.edu.chnu.kkn.lab6.service.VectorJsonService;

import javax.inject.Singleton;

@Singleton
@Component(modules = {VectorModule.class})
public interface AppComponent {
    VectorJsonService jsonService();
    void inject(App app);
}