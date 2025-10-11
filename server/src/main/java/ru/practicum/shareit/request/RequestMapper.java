package ru.practicum.shareit.request;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    public ItemRequest toItemRequest(RequestDto request);

    public ReqResponseDto toReqResponseDto(ItemRequest request);

    public List<ReqResponseFullDto> toFullDtoList(List<ItemRequest> requestsByAuthor);

    public List<ReqResponseDto> toReqResponseDtoList(List<ItemRequest> allRequests);

    public ReqResponseFullDto toFullDto(ItemRequest request);
}
