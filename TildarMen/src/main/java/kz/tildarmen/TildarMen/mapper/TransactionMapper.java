package kz.tildarmen.TildarMen.mapper;

import kz.tildarmen.TildarMen.dto.TransactionDto;
import kz.tildarmen.TildarMen.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "translator.firstName", target = "translatorFirstName")
    @Mapping(source = "translator.lastName", target = "translatorLastName")
    @Mapping(source = "translator.profileImageUrl", target = "profileImageUrl")
    TransactionDto toDto(Transaction transaction);

    List<TransactionDto> toDtoList(List<Transaction> transactions);

}
