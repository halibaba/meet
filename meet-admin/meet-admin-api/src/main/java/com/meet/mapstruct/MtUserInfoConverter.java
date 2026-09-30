package com.meet.mapstruct;

import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import com.meet.vo.MtUserInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MtUserInfoConverter {

    MtUserInfoConverter INSTANCE = Mappers.getMapper(MtUserInfoConverter.class);

    @Mapping(target = "username", ignore = true)
    void entityToDTO(MtUserInfo entity, @MappingTarget MtUserInfoDTO dto);

    @Mapping(target = "username", ignore = true)
    void entityToVO(MtUserInfo entity, @MappingTarget MtUserInfoVo vo);

    void DTOToEntity(MtUserInfoDTO dto, @MappingTarget MtUserInfo entity);

    void entityListToDTOList(List<MtUserInfo> entity, @MappingTarget List<MtUserInfoDTO> dto);
}
