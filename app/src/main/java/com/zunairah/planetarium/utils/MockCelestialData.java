package com.zunairah.planetarium.utils;

import com.zunairah.planetarium.models.CelestialObject;

import java.util.ArrayList;
import java.util.List;

public class MockCelestialData {

    public static List<CelestialObject> generateMockCelestialObjects() {
        List<CelestialObject> objects = new ArrayList<>();

        // Add the Sun
        objects.add(new CelestialObject("sun", "Sun", CelestialObject.Type.SUN,
                -26.7f, 180.0, 0.0, "#FFFF00"));

        // Add the Moon
        objects.add(new CelestialObject("moon", "Moon", CelestialObject.Type.MOON,
                -12.6f, 190.0, 10.0, "#C0C0C0"));

        // Add planets
        objects.add(new CelestialObject("mercury", "Mercury", CelestialObject.Type.PLANET,
                -0.4f, 200.0, 5.0, "#8C7853"));
        objects.add(new CelestialObject("venus", "Venus", CelestialObject.Type.PLANET,
                -4.1f, 210.0, -10.0, "#FFC649"));
        objects.add(new CelestialObject("mars", "Mars", CelestialObject.Type.PLANET,
                -2.6f, 220.0, 15.0, "#CD5C5C"));
        objects.add(new CelestialObject("jupiter", "Jupiter", CelestialObject.Type.PLANET,
                -2.2f, 230.0, -5.0, "#D8CA9D"));
        objects.add(new CelestialObject("saturn", "Saturn", CelestialObject.Type.PLANET,
                0.5f, 240.0, 20.0, "#FAD5A5"));
        objects.add(new CelestialObject("uranus", "Uranus", CelestialObject.Type.PLANET,
                5.7f, 250.0, -15.0, "#4FD0E7"));
        objects.add(new CelestialObject("neptune", "Neptune", CelestialObject.Type.PLANET,
                7.8f, 260.0, 25.0, "#4B70DD"));

        // Add bright stars with realistic data
        addBrightStars(objects);

        // Add many more stars to fill the entire sky
        addMediumStars(objects);
        addFaintStars(objects);
        addVeryFaintStars(objects);

        return objects;
    }

    private static void addBrightStars(List<CelestialObject> objects) {
        // Brightest stars in the sky with approximate coordinates
        objects.add(new CelestialObject("sirius", "Sirius", CelestialObject.Type.STAR,
                -1.46f, 101.287, -16.716, "A"));
        objects.add(new CelestialObject("canopus", "Canopus", CelestialObject.Type.STAR,
                -0.74f, 95.988, -52.696, "A"));
        objects.add(new CelestialObject("arcturus", "Arcturus", CelestialObject.Type.STAR,
                -0.05f, 213.915, 19.182, "K"));
        objects.add(new CelestialObject("vega", "Vega", CelestialObject.Type.STAR,
                0.03f, 279.234, 38.784, "A"));
        objects.add(new CelestialObject("capella", "Capella", CelestialObject.Type.STAR,
                0.08f, 79.172, 45.998, "G"));
        objects.add(new CelestialObject("rigel", "Rigel", CelestialObject.Type.STAR,
                0.13f, 78.634, -8.202, "B"));
        objects.add(new CelestialObject("procyon", "Procyon", CelestialObject.Type.STAR,
                0.34f, 114.825, 5.225, "F"));
        objects.add(new CelestialObject("betelgeuse", "Betelgeuse", CelestialObject.Type.STAR,
                0.50f, 88.793, 7.407, "M"));
        objects.add(new CelestialObject("achernar", "Achernar", CelestialObject.Type.STAR,
                0.46f, 24.429, -57.237, "B"));
        objects.add(new CelestialObject("hadar", "Hadar", CelestialObject.Type.STAR,
                0.61f, 210.956, -60.373, "B"));
        objects.add(new CelestialObject("altair", "Altair", CelestialObject.Type.STAR,
                0.77f, 297.696, 8.868, "A"));
        objects.add(new CelestialObject("aldebaran", "Aldebaran", CelestialObject.Type.STAR,
                0.85f, 68.980, 16.509, "K"));
        objects.add(new CelestialObject("antares", "Antares", CelestialObject.Type.STAR,
                1.09f, 247.352, -26.432, "M"));
        objects.add(new CelestialObject("spica", "Spica", CelestialObject.Type.STAR,
                1.04f, 201.298, -11.161, "B"));
        objects.add(new CelestialObject("pollux", "Pollux", CelestialObject.Type.STAR,
                1.14f, 116.329, 28.026, "K"));
        objects.add(new CelestialObject("fomalhaut", "Fomalhaut", CelestialObject.Type.STAR,
                1.16f, 344.413, -29.622, "A"));
        objects.add(new CelestialObject("deneb", "Deneb", CelestialObject.Type.STAR,
                1.25f, 310.358, 45.280, "A"));
        objects.add(new CelestialObject("regulus", "Regulus", CelestialObject.Type.STAR,
                1.35f, 152.093, 11.967, "B"));
        objects.add(new CelestialObject("adhara", "Adhara", CelestialObject.Type.STAR,
                1.50f, 104.656, -28.972, "B"));
        objects.add(new CelestialObject("castor", "Castor", CelestialObject.Type.STAR,
                1.57f, 113.650, 31.888, "A"));
        objects.add(new CelestialObject("gacrux", "Gacrux", CelestialObject.Type.STAR,
                1.63f, 187.791, -57.113, "M"));
        objects.add(new CelestialObject("bellatrix", "Bellatrix", CelestialObject.Type.STAR,
                1.64f, 81.283, 6.350, "B"));
        objects.add(new CelestialObject("elnath", "Elnath", CelestialObject.Type.STAR,
                1.68f, 81.573, 28.608, "B"));
        objects.add(new CelestialObject("miaplacidus", "Miaplacidus", CelestialObject.Type.STAR,
                1.68f, 138.300, -69.717, "A"));
        objects.add(new CelestialObject("alnilam", "Alnilam", CelestialObject.Type.STAR,
                1.69f, 84.053, -1.202, "B"));
        objects.add(new CelestialObject("regor", "Regor", CelestialObject.Type.STAR,
                1.75f, 125.628, -47.336, "O"));
        objects.add(new CelestialObject("alnair", "Alnair", CelestialObject.Type.STAR,
                1.74f, 332.058, -46.961, "B"));
        objects.add(new CelestialObject("alioth", "Alioth", CelestialObject.Type.STAR,
                1.77f, 193.507, 55.960, "A"));
        objects.add(new CelestialObject("alnitak", "Alnitak", CelestialObject.Type.STAR,
                1.79f, 85.190, -1.943, "O"));
        objects.add(new CelestialObject("dubhe", "Dubhe", CelestialObject.Type.STAR,
                1.79f, 165.932, 61.751, "K"));
    }

    private static void addMediumStars(List<CelestialObject> objects) {
        // Medium brightness stars (magnitude 2.0-3.0) - distributed across the sky
        String[] spectralTypes = {"O", "B", "A", "F", "G", "K", "M"};
        String[] starNames = {
                "Mirfak", "Algol", "Almach", "Hamal", "Sheratan", "Mesarthim", "Menkar", "Zaurak",
                "Acamar", "Cursa", "Rigel Kent", "Toliman", "Agena", "Menkent", "Theta Cen", "Muhlifain",
                "Aspidiske", "Suhail", "Markeb", "Regor", "Avior", "Naos", "Turais", "Aludra",
                "Wezen", "Adhara", "Furud", "Azmidiske", "Muliphein", "Mirzam", "Arneb", "Nihal",
                "Mintaka", "Hatsya", "Meissa", "Tabit", "Thabit", "Cursa", "Zaurak", "Beid",
                "Keid", "Angetenar", "Acamar", "Menkar", "Kaffaljidhma", "Botein", "Hamal", "Sheratan",
                "Mesarthim", "Almach", "Mirach", "Alpheratz", "Caph", "Schedar", "Gamma Cas", "Ruchbah",
                "Segin", "Mirach", "Almach", "Triangulum", "Metallah", "Mothallah", "Ras Algethi",
                "Kornephoros", "Zeta Her", "Eta Her", "Pi Her", "Rho Her", "Tau Her", "Chi Her",
                "Rasalgethi", "Kornephoros", "Marfik", "Marsic", "Maasym", "Kajam", "Muliphein",
                "Wazn", "Zaniah", "Syrma", "Khambalia", "Rijl al Awwa", "Heze", "Zaniah", "Porrima",
                "Auva", "Minelauva", "Vindemiatrix", "Zavijava", "Zaniah", "Syrma", "Khambalia",
                "Seginus", "Nekkar", "Izar", "Muphrid", "Alkalurops", "Princeps", "Asellus Primus",
                "Asellus Secundus", "Asellus Tertius", "Theta Boo", "Upsilon Boo", "Tau Boo",
                "Rho Boo", "Sigma Boo", "Delta Boo", "Mu Boo", "Lambda Boo", "Kappa Boo",
                "Iota Boo", "Eta Boo", "Zeta Boo", "Epsilon Boo", "Gamma Boo", "Beta Boo",
                "Alpha Boo", "Arcturus", "Nekkar", "Seginus", "Izar", "Muphrid", "Alkalurops"
        };

        // Generate stars with magnitude 2.0-3.0 distributed across the sky
        for (int i = 0; i < starNames.length && i < 100; i++) {
            String name = starNames[i];
            String spectralType = spectralTypes[i % spectralTypes.length];

            // Generate magnitude between 2.0 and 3.0
            float magnitude = 2.0f + (float) (Math.random() * 1.0);

            // Generate RA covering full range (0-360 degrees)
            double ra = Math.random() * 360.0;

            // Generate Dec with realistic distribution (more stars near celestial equator)
            double dec = generateRealisticDeclination();

            objects.add(new CelestialObject("medium_star_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }
    }

    private static void addFaintStars(List<CelestialObject> objects) {
        // Faint stars (magnitude 3.0-4.5) - many more to fill the sky
        String[] spectralTypes = {"O", "B", "A", "F", "G", "K", "M"};
        String[] starNames = {
                "HD1", "HD2", "HD3", "HD4", "HD5", "HD6", "HD7", "HD8", "HD9", "HD10",
                "SAO1", "SAO2", "SAO3", "SAO4", "SAO5", "SAO6", "SAO7", "SAO8", "SAO9", "SAO10",
                "HIP1", "HIP2", "HIP3", "HIP4", "HIP5", "HIP6", "HIP7", "HIP8", "HIP9", "HIP10",
                "TYC1", "TYC2", "TYC3", "TYC4", "TYC5", "TYC6", "TYC7", "TYC8", "TYC9", "TYC10",
                "GSC1", "GSC2", "GSC3", "GSC4", "GSC5", "GSC6", "GSC7", "GSC8", "GSC9", "GSC10",
                "2MASS1", "2MASS2", "2MASS3", "2MASS4", "2MASS5", "2MASS6", "2MASS7", "2MASS8",
                "USNO1", "USNO2", "USNO3", "USNO4", "USNO5", "USNO6", "USNO7", "USNO8", "USNO9",
                "PPM1", "PPM2", "PPM3", "PPM4", "PPM5", "PPM6", "PPM7", "PPM8", "PPM9", "PPM10",
                "FK51", "FK52", "FK53", "FK54", "FK55", "FK56", "FK57", "FK58", "FK59", "FK60",
                "HR1", "HR2", "HR3", "HR4", "HR5", "HR6", "HR7", "HR8", "HR9", "HR10",
                "BD1", "BD2", "BD3", "BD4", "BD5", "BD6", "BD7", "BD8", "BD9", "BD10",
                "CD1", "CD2", "CD3", "CD4", "CD5", "CD6", "CD7", "CD8", "CD9", "CD10",
                "CPD1", "CPD2", "CPD3", "CPD4", "CPD5", "CPD6", "CPD7", "CPD8", "CPD9", "CPD10",
                "AG1", "AG2", "AG3", "AG4", "AG5", "AG6", "AG7", "AG8", "AG9", "AG10",
                "ADS1", "ADS2", "ADS3", "ADS4", "ADS5", "ADS6", "ADS7", "ADS8", "ADS9", "ADS10",
                "WDS1", "WDS2", "WDS3", "WDS4", "WDS5", "WDS6", "WDS7", "WDS8", "WDS9", "WDS10",
                "STF1", "STF2", "STF3", "STF4", "STF5", "STF6", "STF7", "STF8", "STF9", "STF10",
                "STT1", "STT2", "STT3", "STT4", "STT5", "STT6", "STT7", "STT8", "STT9", "STT10",
                "BU1", "BU2", "BU3", "BU4", "BU5", "BU6", "BU7", "BU8", "BU9", "BU10",
                "HU1", "HU2", "HU3", "HU4", "HU5", "HU6", "HU7", "HU8", "HU9", "HU10"
        };

        // Generate 200 faint stars distributed across the entire sky
        for (int i = 0; i < 200; i++) {
            String name = starNames[i % starNames.length] + "_" + (i / starNames.length + 1);
            String spectralType = spectralTypes[i % spectralTypes.length];

            // Generate magnitude between 3.0 and 4.5
            float magnitude = 3.0f + (float) (Math.random() * 1.5);

            // Generate RA covering full range (0-360 degrees)
            double ra = Math.random() * 360.0;

            // Generate Dec with realistic distribution
            double dec = generateRealisticDeclination();

            objects.add(new CelestialObject("faint_star_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }
    }

    private static void addVeryFaintStars(List<CelestialObject> objects) {
        // Very faint stars (magnitude 4.5-6.0) - fill the entire sky
        String[] spectralTypes = {"O", "B", "A", "F", "G", "K", "M"};

        // Generate 500 very faint stars to completely fill the sky
        for (int i = 0; i < 500; i++) {
            String name = "Star_" + (i + 1000);
            String spectralType = spectralTypes[i % spectralTypes.length];

            // Generate magnitude between 4.5 and 6.0 (naked eye limit)
            float magnitude = 4.5f + (float) (Math.random() * 1.5);

            // Generate RA covering full range (0-360 degrees)
            double ra = Math.random() * 360.0;

            // Generate Dec with realistic distribution
            double dec = generateRealisticDeclination();

            objects.add(new CelestialObject("very_faint_star_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }

        // Add additional stars in specific regions to ensure complete sky coverage
        addPolarStars(objects);
        addEquatorialStars(objects);
        addSouthernStars(objects);
        addNorthernStars(objects);
    }

    private static void addPolarStars(List<CelestialObject> objects) {
        // Add stars near the celestial poles
        String[] spectralTypes = {"A", "F", "G", "K", "M"};

        // North polar region (Dec > 60°)
        for (int i = 0; i < 30; i++) {
            String name = "NorthPolar_" + i;
            String spectralType = spectralTypes[i % spectralTypes.length];
            float magnitude = 4.0f + (float) (Math.random() * 2.0);
            double ra = Math.random() * 360.0;
            double dec = 60.0 + Math.random() * 30.0; // 60° to 90°

            objects.add(new CelestialObject("north_polar_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }

        // South polar region (Dec < -60°)
        for (int i = 0; i < 30; i++) {
            String name = "SouthPolar_" + i;
            String spectralType = spectralTypes[i % spectralTypes.length];
            float magnitude = 4.0f + (float) (Math.random() * 2.0);
            double ra = Math.random() * 360.0;
            double dec = -60.0 - Math.random() * 30.0; // -60° to -90°

            objects.add(new CelestialObject("south_polar_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }
    }

    private static void addEquatorialStars(List<CelestialObject> objects) {
        // Add stars near the celestial equator (-30° to +30°)
        String[] spectralTypes = {"O", "B", "A", "F", "G", "K", "M"};

        for (int i = 0; i < 100; i++) {
            String name = "Equatorial_" + i;
            String spectralType = spectralTypes[i % spectralTypes.length];
            float magnitude = 3.5f + (float) (Math.random() * 2.5);
            double ra = Math.random() * 360.0;
            double dec = -30.0 + Math.random() * 60.0; // -30° to +30°

            objects.add(new CelestialObject("equatorial_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }
    }

    private static void addSouthernStars(List<CelestialObject> objects) {
        // Add stars in southern hemisphere (-60° to -30°)
        String[] spectralTypes = {"B", "A", "F", "G", "K", "M"};

        for (int i = 0; i < 50; i++) {
            String name = "Southern_" + i;
            String spectralType = spectralTypes[i % spectralTypes.length];
            float magnitude = 4.0f + (float) (Math.random() * 2.0);
            double ra = Math.random() * 360.0;
            double dec = -60.0 + Math.random() * 30.0; // -60° to -30°

            objects.add(new CelestialObject("southern_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }
    }

    private static void addNorthernStars(List<CelestialObject> objects) {
        // Add stars in northern hemisphere (+30° to +60°)
        String[] spectralTypes = {"A", "F", "G", "K", "M"};

        for (int i = 0; i < 50; i++) {
            String name = "Northern_" + i;
            String spectralType = spectralTypes[i % spectralTypes.length];
            float magnitude = 4.0f + (float) (Math.random() * 2.0);
            double ra = Math.random() * 360.0;
            double dec = 30.0 + Math.random() * 30.0; // +30° to +60°

            objects.add(new CelestialObject("northern_" + i, name, CelestialObject.Type.STAR,
                    magnitude, ra, dec, spectralType));
        }
    }

    /**
     * Generate realistic declination distribution
     * More stars near celestial equator, fewer near poles
     */
    private static double generateRealisticDeclination() {
        // Use a distribution that favors mid-latitudes
        double random = Math.random();

        if (random < 0.4) {
            // 40% of stars between -30° and +30° (equatorial region)
            return -30.0 + Math.random() * 60.0;
        } else if (random < 0.7) {
            // 30% of stars between -60° and -30° or +30° and +60°
            if (Math.random() < 0.5) {
                return -60.0 + Math.random() * 30.0; // Southern
            } else {
                return 30.0 + Math.random() * 30.0; // Northern
            }
        } else {
            // 30% of stars in polar regions
            if (Math.random() < 0.5) {
                return -90.0 + Math.random() * 30.0; // South polar
            } else {
                return 60.0 + Math.random() * 30.0; // North polar
            }
        }
    }
}
