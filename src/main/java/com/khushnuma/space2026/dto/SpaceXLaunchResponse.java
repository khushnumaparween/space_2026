package com.khushnuma.space2026.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpaceXLaunchResponse(
        String id,
        String name,

        @JsonProperty("net")
        String launchDate,

        Image image,

        LaunchStatus status,

        @JsonProperty("launch_service_provider")
        LaunchServiceProvider launchServiceProvider,

        Rocket rocket,

        Mission mission
) {

        public record Image(
                String id,
                String name,

                @JsonProperty("image_url")
                String imageUrl
        ) {}

        public record LaunchStatus(
                String name,
                String abbrev
        ) {}

        public record LaunchServiceProvider(
                String name,
                String abbrev
        ) {}

        public record Rocket(
                RocketConfiguration configuration
        ) {}

        public record RocketConfiguration(
                String name,
                String full_name
        ) {}

        public record Mission(
                String name,
                String description,
                String type
        ) {}
}