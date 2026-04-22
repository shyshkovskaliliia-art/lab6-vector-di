package ua.edu.chnu.kkn.lab6.service;

import com.google.gson.Gson;
import ua.edu.chnu.kkn.lab6.model.VectorUInt;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorJsonService {
    private final Gson gson;

    @Inject
    public VectorJsonService(Gson gson) {
        this.gson = gson;
    }

    public String toJson(VectorUInt vector) {
        return gson.toJson(vector);
    }

    public VectorUInt fromJson(String json) {
        return gson.fromJson(json, VectorUInt.class);
    }
}