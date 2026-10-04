package com.khushnuma.space2026.service;

import com.khushnuma.space2026.dto.*;
import com.khushnuma.space2026.exception.InvalidDateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class SpaceService {

    private final RestClient restClient;
    private final String apiKey;

    public SpaceService(
            RestClient.Builder restClientBuilder,
            @Value("${nasa.api.key}") String apiKey) {

        this.restClient = restClientBuilder.build();
        this.apiKey = apiKey;
    }


    // =========================
    // APOD
    // =========================

    public ApodApiResponse getApod() {

        List<ApodResponse> nasaResponse = restClient.get()
                .uri(
                        "https://science.nasa.gov/wp-json/wp/v2/apod-basic?api_key={key}&date={date}",
                        apiKey,
                        LocalDate.now()
                )
                .retrieve()
                .body(new ParameterizedTypeReference<List<ApodResponse>>() {});

        ApodResponse apod = nasaResponse.get(0);

        return new ApodApiResponse(
                apod.date(),
                apod.title(),
                apod.explanation(),
                apod.url(),
                apod.hdurl(),
                apod.media_type()
        );
    }


    // =========================
    // ASTEROIDS
    // =========================

    public List<AsteroidApiResponse> getAsteroids(
            LocalDate startDate,
            LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException(
                    "End date cannot be before start date"
            );
        }

        NeoResponse nasaResponse = restClient.get()
                .uri(
                        "https://api.nasa.gov/neo/rest/v1/feed?start_date={startDate}&end_date={endDate}&api_key={key}",
                        startDate,
                        endDate,
                        apiKey
                )
                .retrieve()
                .body(NeoResponse.class);

        return nasaResponse.near_earth_objects()
                .values()
                .stream()
                .flatMap(List::stream)
                .map(asteroid -> {

                    CloseApproachData approach =
                            asteroid.close_approach_data().get(0);

                    return new AsteroidApiResponse(
                            asteroid.id(),
                            asteroid.name(),
                            asteroid.potentially_hazardous_asteroid(),

                            asteroid.estimated_diameter()
                                    .kilometers()
                                    .estimated_diameter_min(),

                            asteroid.estimated_diameter()
                                    .kilometers()
                                    .estimated_diameter_max(),

                            approach.close_approach_date(),

                            approach.relative_velocity()
                                    .kilometers_per_hour(),

                            approach.miss_distance()
                                    .kilometers()
                    );
                })
                .toList();
    }


    // =========================
    // NASA IMAGE LIBRARY
    // =========================

    public NasaImageResponse getImages(String query) {

        return restClient.get()
                .uri(
                        "https://images-api.nasa.gov/search?q={query}&media_type=image",
                        query
                )
                .retrieve()
                .body(NasaImageResponse.class);
    }


    // =========================
    // SOLAR FLARES
    // =========================

    public List<SolarFlare> getSolarFlares(
            LocalDate startDate,
            LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException(
                    "End date cannot be before start date"
            );
        }

        return restClient.get()
                .uri(
                        "https://ccmc.gsfc.nasa.gov/DONKI-API/get/FLR?startDate={startDate}&endDate={endDate}&api_key={key}",
                        startDate,
                        endDate,
                        apiKey
                )
                .retrieve()
                .body(
                        new ParameterizedTypeReference<List<SolarFlare>>() {}
                );
    }


    // =========================
    // SPACEX LAUNCHES
    // =========================

    public List<SpaceXLaunchResponse> getSpaceXLaunches() {

        LaunchLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("lsp__name", "SpaceX")
                        .queryParam("limit", 20)
                        .queryParam("ordering", "-net")
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);

        return response.results();
    }


    // =========================
    // SPACEX UPCOMING
    // =========================

    @Cacheable("spacexUpcoming")
    public List<SpaceXLaunchResponse> getUpcomingSpaceXLaunches() {

        LaunchLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("lsp__name", "SpaceX")
                        .queryParam("net__gte", Instant.now().toString())
                        .queryParam("limit", 20)
                        .queryParam("ordering", "net")
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);

        return response.results();
    }


    // =========================
    // SPACEX PAGINATED LAUNCHES
    // =========================

    @Cacheable(
            value = "spacexLaunches",
            key = "#page + '-' + #size"
    )
    public LaunchLibraryResponse getSpaceXLaunches(
            int page,
            int size) {

        int offset = (page - 1) * size;

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("lsp__name", "SpaceX")
                        .queryParam("limit", size)
                        .queryParam("offset", offset)
                        .queryParam("ordering", "-net")
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);
    }


    // =========================
    // SPACEX LAUNCH BY ID
    // =========================

    public SpaceXLaunchResponse getSpaceXLaunch(
            String id) {

        LaunchLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);

        return response.results().get(0);
    }


    // =========================
    // SPACEX ROCKETS
    // =========================

    public RocketLibraryResponse getSpaceXRockets(
            int page,
            int size) {

        int offset = (page - 1) * size;

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launcher_configurations/")
                        .queryParam("limit", size)
                        .queryParam("offset", offset)
                        .queryParam("manufacturer__name", "SpaceX")
                        .build())
                .retrieve()
                .body(RocketLibraryResponse.class);
    }


    // =========================
    // SPACEX LAUNCHPADS
    // =========================

    @Cacheable(
            value = "spacexLaunchpads",
            key = "#page + '-' + #size"
    )
    public LaunchpadLibraryResponse getSpaceXLaunchpads(
            int page,
            int size) {

        int offset = (page - 1) * size;

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/pads/")
                        .queryParam("limit", size)
                        .queryParam("offset", offset)
                        .build())
                .retrieve()
                .body(LaunchpadLibraryResponse.class);
    }


    // =========================
    // SPACEX LAUNCHPAD BY ID
    // =========================

    public LaunchpadLibraryResponse.Launchpad getSpaceXLaunchpad(
            String id) {

        LaunchpadLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/pads/")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .body(LaunchpadLibraryResponse.class);

        return response.results().get(0);
    }


    // =========================
    // SPACEX HISTORY
    // =========================

    public List<SpaceXLaunchResponse> getSpaceXLaunchHistory() {

        LaunchLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("lsp__name", "SpaceX")
                        .queryParam(
                                "net__lte",
                                Instant.now().toString()
                        )
                        .queryParam("limit", 20)
                        .queryParam("ordering", "-net")
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);

        return response.results();
    }


    // =========================
    // SPACEX BY STATUS
    // =========================

    public List<SpaceXLaunchResponse> getSpaceXLaunchesByStatus(
            String status) {

        LaunchLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("lsp__name", "SpaceX")
                        .queryParam("status__name", status)
                        .queryParam("limit", 20)
                        .queryParam("ordering", "-net")
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);

        return response.results();
    }


    // =========================
    // SPACEX DATE RANGE
    // =========================

    public List<SpaceXLaunchResponse> getSpaceXLaunchesByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException(
                    "End date cannot be before start date"
            );
        }

        LaunchLibraryResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("ll.thespacedevs.com")
                        .path("/2.3.0/launches/")
                        .queryParam("lsp__name", "SpaceX")
                        .queryParam(
                                "net__gte",
                                startDate.atStartOfDay().toString()
                        )
                        .queryParam(
                                "net__lte",
                                endDate.plusDays(1)
                                        .atStartOfDay()
                                        .toString()
                        )
                        .queryParam("limit", 50)
                        .queryParam("ordering", "net")
                        .build())
                .retrieve()
                .body(LaunchLibraryResponse.class);

        return response.results();
    }
}