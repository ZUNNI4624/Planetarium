package com.zunairah.planetarium.data.repositories;

import android.content.Context;

import com.zunairah.planetarium.models.CelestialObject;
import com.zunairah.planetarium.utils.MockCelestialData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CelestialRepository {
    private final Context context;
    private final Executor executor;

    public CelestialRepository(Context context) {
        this.context = context;
        this.executor = Executors.newCachedThreadPool();
    }

    public interface DataCallback {
        void onDataLoaded(List<CelestialObject> celestialObjects);
        void onError(Exception e);
    }

    public void loadCelestialData(DataCallback callback) {
        executor.execute(() -> {
            try {
                // Load mock celestial data
                List<CelestialObject> mockObjects = MockCelestialData.generateMockCelestialObjects();
                callback.onDataLoaded(mockObjects);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
}
