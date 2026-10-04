package com.khushnuma.space2026.controller;

import com.khushnuma.space2026.dto.*;
import com.khushnuma.space2026.service.SpaceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import com.khushnuma.space2026.dto.SpaceXLaunchResponse;

@RestController
@RequestMapping("/api/space")
public class SpaceController {

    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/test")
    public String test() {
        return "Space2026 is working!";
    }

    @GetMapping("/apod")
    public ApodApiResponse getApod() {
        return spaceService.getApod();
    }

    @GetMapping("/asteroids")
    public List<AsteroidApiResponse> getAsteroids(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return spaceService.getAsteroids(startDate, endDate);
    }

    @GetMapping("/images")
    public NasaImageResponse getImages(
            @RequestParam String query) {

        return spaceService.getImages(query);
    }

    @GetMapping("/solar-flares")
    public List<SolarFlare> getSolarFlares(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return spaceService.getSolarFlares(startDate, endDate);
    }

//    @GetMapping("/spacex/launches")
//    public List<SpaceXLaunchResponse> getSpaceXLaunches() {
//        return spaceService.getSpaceXLaunches();
//    }


    @GetMapping("/spacex/upcoming")
    public List<SpaceXLaunchResponse> getUpcomingSpaceXLaunches() {
        return spaceService.getUpcomingSpaceXLaunches();
    }

    @GetMapping("/spacex/launches")
    public LaunchLibraryResponse getSpaceXLaunches(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return spaceService.getSpaceXLaunches(page, size);
    }

    @GetMapping("/spacex/launches/{id}")
    public SpaceXLaunchResponse getSpaceXLaunch(
            @PathVariable String id) {

        return spaceService.getSpaceXLaunch(id);
    }


    @GetMapping("/spacex/rockets")
    public RocketLibraryResponse getSpaceXRockets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return spaceService.getSpaceXRockets(page, size);
    }


    @GetMapping("/spacex/launchpads")
    public LaunchpadLibraryResponse getSpaceXLaunchpads(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        return spaceService.getSpaceXLaunchpads(page, size);
    }


    @GetMapping("/spacex/launchpads/{id}")
    public LaunchpadLibraryResponse.Launchpad getSpaceXLaunchpad(
            @PathVariable String id) {

        return spaceService.getSpaceXLaunchpad(id);
    }



    @GetMapping("/spacex/history")
    public List<SpaceXLaunchResponse> getSpaceXLaunchHistory() {

        return spaceService.getSpaceXLaunchHistory();
    }


    @GetMapping("/spacex/launches/status")
    public List<SpaceXLaunchResponse> getSpaceXLaunchesByStatus(
            @RequestParam String status) {

        return spaceService.getSpaceXLaunchesByStatus(status);
    }




    @GetMapping("/spacex/launches/range")
    public List<SpaceXLaunchResponse> getSpaceXLaunchesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return spaceService.getSpaceXLaunchesByDateRange(
                startDate,
                endDate
        );
    }
}