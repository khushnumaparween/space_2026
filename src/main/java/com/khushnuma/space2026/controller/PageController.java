package com.khushnuma.space2026.controller;

import com.khushnuma.space2026.dto.ApodApiResponse;
import com.khushnuma.space2026.dto.AsteroidApiResponse;
import com.khushnuma.space2026.dto.LaunchLibraryResponse;
import com.khushnuma.space2026.dto.NasaImageResponse;
import com.khushnuma.space2026.dto.SolarFlare;
import com.khushnuma.space2026.dto.SpaceXLaunchResponse;
import com.khushnuma.space2026.service.SpaceService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PageController {

    private final SpaceService spaceService;

    public PageController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }


    // =========================
    // HOME
    // =========================

    @GetMapping("/")
    public String home() {
        return "index";
    }


    // =========================
    // APOD
    // =========================

    @GetMapping("/apod")
    public String apod(Model model) {

        ApodApiResponse apod = spaceService.getApod();

        model.addAttribute("apod", apod);

        return "apod";
    }


    // =========================
    // ASTEROIDS
    // =========================

    @GetMapping("/asteroids")
    public String asteroids(
            @RequestParam(defaultValue = "2026-10-01")
            LocalDate startDate,

            @RequestParam(defaultValue = "2026-10-02")
            LocalDate endDate,

            Model model) {

        List<AsteroidApiResponse> asteroids =
                spaceService.getAsteroids(startDate, endDate);

        model.addAttribute("asteroids", asteroids);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "asteroids";
    }


    // =========================
    // NASA IMAGES
    // =========================

    @GetMapping("/images")
    public String images(
            @RequestParam(defaultValue = "galaxy")
            String query,

            Model model) {

        NasaImageResponse response =
                spaceService.getImages(query);

        model.addAttribute("response", response);
        model.addAttribute("query", query);

        return "images";
    }


    // =========================
    // SOLAR FLARES
    // =========================

    @GetMapping("/solar-flares")
    public String solarFlares(
            @RequestParam(defaultValue = "2026-10-01")
            LocalDate startDate,

            @RequestParam(defaultValue = "2026-10-02")
            LocalDate endDate,

            Model model) {

        List<SolarFlare> flares =
                spaceService.getSolarFlares(startDate, endDate);

        model.addAttribute("flares", flares);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "solar-flares";
    }


    // =========================
    // SPACEX LAUNCHES
    // =========================

    @GetMapping("/spacex")
    public String spacex(
            @RequestParam(defaultValue = "1")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            Model model) {

        LaunchLibraryResponse response =
                spaceService.getSpaceXLaunches(page, size);

        model.addAttribute("launches", response.results());
        model.addAttribute("count", response.count());
        model.addAttribute("next", response.next());
        model.addAttribute("previous", response.previous());
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "spacex";
    }


    // =========================
    // UPCOMING SPACEX LAUNCHES
    // =========================

    @GetMapping("/spacex/upcoming")
    public String upcomingSpaceX(Model model) {

        List<SpaceXLaunchResponse> launches =
                spaceService.getUpcomingSpaceXLaunches();

        model.addAttribute("launches", launches);

        return "spacex-upcoming";
    }


    // =========================
    // SPACEX HISTORY
    // =========================

    @GetMapping("/spacex/history")
    public String spacexHistory(Model model) {

        List<SpaceXLaunchResponse> launches =
                spaceService.getSpaceXLaunchHistory();

        model.addAttribute("launches", launches);

        return "spacex-history";
    }



    @GetMapping("/spacex/mission/{id}")
    public String spacexMission(
            @PathVariable String id,
            Model model) {

        SpaceXLaunchResponse launch =
                spaceService.getSpaceXLaunch(id);

        model.addAttribute("launch", launch);

        return "spacex-mission";
    }
}