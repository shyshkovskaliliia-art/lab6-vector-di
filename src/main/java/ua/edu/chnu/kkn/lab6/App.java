package ua.edu.chnu.kkn.lab6;

import ua.edu.chnu.kkn.lab6.di.DaggerAppComponent;
import ua.edu.chnu.kkn.lab6.model.VectorUInt;
import ua.edu.chnu.kkn.lab6.service.VectorJsonService;

import javax.inject.Inject;

public class App {
    @Inject
    VectorJsonService jsonService;

    public static void main(String[] args) {
        App app = new App();
        DaggerAppComponent.create().inject(app);
        app.run();
    }

    private void run() {
        System.out.println("===== LAB6 - VectorUInt with Dagger DI =====");
        System.out.println();

        VectorUInt v = new VectorUInt(3, 5);
        System.out.println("Created vector: " + v);

        String json = jsonService.toJson(v);
        System.out.println("JSON: " + json);

        System.out.println();
        System.out.println("Number of vectors created: " + VectorUInt.getNumVec());
    }
}