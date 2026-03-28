package ru.job4j.social.mappers;

import jakarta.validation.Valid;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.model.User;

@Validated
@Mapper(componentModel = "spring")
public interface UserWithPostsMapper {
    @Mapping(target = "userName", source = "fullName")
    @Mapping(target = "userId", source = "id")
    UserWithPostsDto getModelFromEntity(@Valid  User user);

    @InheritInverseConfiguration
    User getEntityFromDto(UserWithPostsDto userWithPostsDto);
}
