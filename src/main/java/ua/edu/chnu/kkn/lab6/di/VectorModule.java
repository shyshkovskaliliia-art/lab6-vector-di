package ua.edu.chnu.kkn.lab6.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import ua.edu.chnu.kkn.lab6.model.VectorUInt;

import javax.inject.Singleton;

@Module
public class VectorModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Provides
    VectorUInt provideDefaultVector() {
        return new VectorUInt(5, 1);
    }
}