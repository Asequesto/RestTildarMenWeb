package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.ReviewDto;
import kz.tildarmen.TildarMen.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.profileImageUrl", target = "profileImageUrl")
    ReviewDto toDto(Review review);

    List<ReviewDto> toDtoList(List<Review> reviews);

}
