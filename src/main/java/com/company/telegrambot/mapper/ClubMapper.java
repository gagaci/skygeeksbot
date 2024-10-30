package com.company.telegrambot.mapper;

import com.company.telegrambot.dto.club.CreateClubRequest;
import com.company.telegrambot.entity.Club;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING, unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {ClubMapper.class})
public interface ClubMapper {

    Club toEntity(final CreateClubRequest request);
}
