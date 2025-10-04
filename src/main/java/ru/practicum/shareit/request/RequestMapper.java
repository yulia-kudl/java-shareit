package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ReqResponseDto;
import ru.practicum.shareit.request.dto.ReqResponseFullDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    public ItemRequest toItemRequest(RequestDto request);

    public ReqResponseDto toReqResponseDto(ItemRequest request);

    public List<ReqResponseFullDto> toFullDtoList(List<ItemRequest> requestsByAuthor);

    public List<ReqResponseDto> toReqResponseDtoList(List<ItemRequest> allRequests);
    public ReqResponseFullDto toFullDto(ItemRequest request);
}
