package com.company.telegrambot.generetor;

import com.company.telegrambot.entity.CampusFacility;
import com.company.telegrambot.service.CampusFacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GeneratorFacility {


    private final CampusFacilityService campusFacilityService;


    public void createGymFacility() {
        var facility = new CampusFacility("Gym facility",
                "AgACAgIAAxkDAAIHv2cksJu9BIEJU9ZeVvB_lD8iAuaAAAJ84TEb5VEgSbsEhPbSgpjWAQADAgADeQADNgQ",
                "North Hall floor 2, room 5", "@username");
        campusFacilityService.addFacility(facility);
    }

    public void createTherapistFacility() {
        var facility = new CampusFacility("Therapist facility",
                "AgACAgIAAxkDAAIHwGcksOZnOA2EO1AGds24andeblxhAAKZ4TEb5VEgSVMqoFbnsiCCAQADAgADeAADNgQ",
                "North Hall floor 2, room 5", "@username");
        campusFacilityService.addFacility(facility);
    }
}
