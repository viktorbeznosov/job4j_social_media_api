package ru.job4j.social.mappers;

import jakarta.validation.Valid;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.validation.annotation.Validated;
import ru.job4j.social.dto.PostDto;
import ru.job4j.social.dto.UserWithPostsDto;
import ru.job4j.social.model.Post;
import ru.job4j.social.model.User;

@Validated
@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto getModelFromEntity(@Valid Post post);

    Post getEntityFromDto(PostDto postDto);
}
