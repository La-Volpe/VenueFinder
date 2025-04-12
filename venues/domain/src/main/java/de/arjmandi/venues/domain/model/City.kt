package de.arjmandi.venues.domain.model

data class CityBounds(
    val latMin: Double,
    val latMax: Double,
    val lonMin: Double,
    val lonMax: Double,
)

sealed class City(
    val displayName: String,
    val coordinates: List<Location>,
    val cityBounds: CityBounds,
) {
    data object Helsinki : City(
        displayName = "Helsinki",
        coordinates =
            listOf(
                Location(60.169418, 24.931618, "Kamppi Chapel of Silence"),
                Location(60.169818, 24.932906, "Narinkka Square"),
                Location(60.170005, 24.935105, "Kamppi Shopping Centre"),
                Location(60.169108, 24.936210, "Tennispalatsi (Finnkino)"),
                Location(60.168355, 24.934869, "Helsinki Bus Station"),
                Location(60.167560, 24.932562, "Helsinki Music Centre"),
                Location(60.168254, 24.931532, "Sanomatalo"),
                Location(60.169012, 24.930341, "Kiasma Museum of Contemporary Art"),
                Location(60.170085, 24.929569, "Helsinki Central Railway Station"),
            ),
        cityBounds =
            CityBounds(
                latMin = 59.7915698748,
                latMax = 60.8408511915,
                lonMin = 23.5863109041,
                lonMax = 26.2992496490479,
            ),
    )

    data object Hamburg : City(
        displayName = "Hamburg",
        coordinates =
            listOf(
                Location(53.551086, 9.993682, "Rathausmarkt"),
                Location(53.546268, 9.984619, "St. Michael's Church"),
                Location(53.543764, 9.966287, "Reeperbahn"),
                Location(53.552555, 9.995708, "Binnenalster"),
                Location(53.545560, 9.989580, "Speicherstadt"),
                Location(53.558572, 9.927821, "Altona"),
                Location(53.565411, 10.001984, "Außenalster"),
                Location(53.550341, 10.006653, "Deichtorhallen"),
                Location(53.556574, 9.970122, "Planten un Blomen"),
            ),
        CityBounds(
            latMin = 53.39507,
            latMax = 53.964403,
            lonMin = 8.419907,
            lonMax = 10.325959,
        ),
    )

    data object Vienna : City(
        displayName = "Vienna",
        coordinates =
            listOf(
                Location(48.208174, 16.373819, "Stephansplatz"),
                Location(48.206453, 16.361330, "MuseumsQuartier"),
                Location(48.203072, 16.369124, "Naschmarkt"),
                Location(48.208889, 16.372500, "St. Stephen's Cathedral"),
                Location(48.214341, 16.357444, "Rathaus"),
                Location(48.198674, 16.371918, "Belvedere Palace"),
                Location(48.220600, 16.379900, "Prater"),
                Location(48.199910, 16.371800, "Karlskirche"),
                Location(48.210033, 16.363449, "Hofburg Palace"),
            ),
        CityBounds(
            latMin = 48.117903,
            latMax = 48.322667,
            lonMin = 16.18183,
            lonMax = 16.577514,
        ),
    )

    data object Tallinn : City(
        displayName = "Tallinn",
        coordinates =
            listOf(
                Location(59.437000, 24.753600, "Town Hall Square"),
                Location(59.436100, 24.744500, "Toompea Castle"),
                Location(59.438500, 24.761500, "Kadriorg Palace"),
                Location(59.440000, 24.745000, "Alexander Nevsky Cathedral"),
                Location(59.435000, 24.752000, "St. Olaf's Church"),
                Location(59.433000, 24.743000, "Kiek in de Kök"),
                Location(59.437500, 24.745500, "Viru Gate"),
                Location(59.439000, 24.750000, "Tallinn City Museum"),
                Location(59.436500, 24.749000, "Estonian History Museum"),
            ),
        CityBounds(
            latMin = 59.351801,
            latMax = 59.59148,
            lonMin = 24.550348,
            lonMax = 24.926284,
        ),
    )

    data object Stockholm : City(
        displayName = "Stockholm",
        coordinates =
            listOf(
                Location(59.329323, 18.068581, "Sergels Torg"),
                Location(59.325117, 18.071093, "Gamla Stan"),
                Location(59.327500, 18.067500, "Royal Palace"),
                Location(59.334591, 18.063240, "Djurgården"),
                Location(59.342000, 18.059000, "Skansen"),
                Location(59.329000, 18.065000, "Kungsträdgården"),
                Location(59.331000, 18.070000, "Stockholm City Hall"),
                Location(59.336000, 18.080000, "Vasa Museum"),
                Location(59.337000, 18.065000, "Nationalmuseum"),
            ),
        CityBounds(
            latMin = 59.2272840554,
            latMax = 59.4402429413,
            lonMin = 17.7605130429,
            lonMax = 18.2011814732,
        ),
    )

    companion object {
        val all: List<City> = listOf(Helsinki, Hamburg, Vienna, Tallinn, Stockholm)
    }
}
