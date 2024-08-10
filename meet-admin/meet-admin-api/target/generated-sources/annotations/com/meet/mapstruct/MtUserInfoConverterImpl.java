package com.meet.mapstruct;

import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import com.meet.vo.MtUserInfoVo;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-10T14:09:30+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_421 (Oracle Corporation)"
)
public class MtUserInfoConverterImpl implements MtUserInfoConverter {

    @Override
    public void entityToDTO(MtUserInfo entity, MtUserInfoDTO dto) {
        if ( entity == null ) {
            return;
        }

        dto.setDescription( entity.getDescription() );
        dto.setIsDeleted( entity.getIsDeleted() );
        dto.setCreateDate( entity.getCreateDate() );
        dto.setLastUpdateDate( entity.getLastUpdateDate() );
        dto.setCreateBy( entity.getCreateBy() );
        dto.setLastUpdateBy( entity.getLastUpdateBy() );
        dto.setId( entity.getId() );
        dto.setName( entity.getName() );
        dto.setSex( entity.getSex() );
        dto.setPhone( entity.getPhone() );
        dto.setEmail( entity.getEmail() );
        dto.setBirthday( entity.getBirthday() );
        dto.setEducation( entity.getEducation() );
        dto.setIdentity( entity.getIdentity() );
        dto.setAddres( entity.getAddres() );
        dto.setStatus( entity.getStatus() );
    }

    @Override
    public void entityToVO(MtUserInfo entity, MtUserInfoVo vo) {
        if ( entity == null ) {
            return;
        }

        vo.setDescription( entity.getDescription() );
        vo.setIsDeleted( entity.getIsDeleted() );
        vo.setCreateDate( entity.getCreateDate() );
        vo.setLastUpdateDate( entity.getLastUpdateDate() );
        vo.setCreateBy( entity.getCreateBy() );
        vo.setLastUpdateBy( entity.getLastUpdateBy() );
        vo.setId( entity.getId() );
        vo.setName( entity.getName() );
        vo.setSex( entity.getSex() );
        vo.setPhone( entity.getPhone() );
        vo.setEmail( entity.getEmail() );
        vo.setBirthday( entity.getBirthday() );
        vo.setEducation( entity.getEducation() );
        vo.setIdentity( entity.getIdentity() );
        vo.setAddres( entity.getAddres() );
        vo.setStatus( entity.getStatus() );
    }

    @Override
    public void DTOToEntity(MtUserInfoDTO dto, MtUserInfo entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDescription( dto.getDescription() );
        entity.setIsDeleted( dto.getIsDeleted() );
        entity.setCreateDate( dto.getCreateDate() );
        entity.setLastUpdateDate( dto.getLastUpdateDate() );
        entity.setCreateBy( dto.getCreateBy() );
        entity.setLastUpdateBy( dto.getLastUpdateBy() );
        entity.setId( dto.getId() );
        entity.setName( dto.getName() );
        entity.setSex( dto.getSex() );
        entity.setPhone( dto.getPhone() );
        entity.setEmail( dto.getEmail() );
        entity.setBirthday( dto.getBirthday() );
        entity.setEducation( dto.getEducation() );
        entity.setIdentity( dto.getIdentity() );
        entity.setAddres( dto.getAddres() );
        entity.setStatus( dto.getStatus() );
    }

    @Override
    public void entityListToDTOList(List<MtUserInfo> entity, List<MtUserInfoDTO> dto) {
        if ( entity == null ) {
            return;
        }

        dto.clear();
        for ( MtUserInfo mtUserInfo : entity ) {
            dto.add( mtUserInfoToMtUserInfoDTO( mtUserInfo ) );
        }
    }

    protected MtUserInfoDTO mtUserInfoToMtUserInfoDTO(MtUserInfo mtUserInfo) {
        if ( mtUserInfo == null ) {
            return null;
        }

        MtUserInfoDTO mtUserInfoDTO = new MtUserInfoDTO();

        mtUserInfoDTO.setDescription( mtUserInfo.getDescription() );
        mtUserInfoDTO.setIsDeleted( mtUserInfo.getIsDeleted() );
        mtUserInfoDTO.setCreateDate( mtUserInfo.getCreateDate() );
        mtUserInfoDTO.setLastUpdateDate( mtUserInfo.getLastUpdateDate() );
        mtUserInfoDTO.setCreateBy( mtUserInfo.getCreateBy() );
        mtUserInfoDTO.setLastUpdateBy( mtUserInfo.getLastUpdateBy() );
        mtUserInfoDTO.setId( mtUserInfo.getId() );
        mtUserInfoDTO.setName( mtUserInfo.getName() );
        mtUserInfoDTO.setSex( mtUserInfo.getSex() );
        mtUserInfoDTO.setPhone( mtUserInfo.getPhone() );
        mtUserInfoDTO.setEmail( mtUserInfo.getEmail() );
        mtUserInfoDTO.setBirthday( mtUserInfo.getBirthday() );
        mtUserInfoDTO.setEducation( mtUserInfo.getEducation() );
        mtUserInfoDTO.setIdentity( mtUserInfo.getIdentity() );
        mtUserInfoDTO.setAddres( mtUserInfo.getAddres() );
        mtUserInfoDTO.setStatus( mtUserInfo.getStatus() );

        return mtUserInfoDTO;
    }
}
