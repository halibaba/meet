package com.meet.mapstruct;

import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import org.mapstruct.Mapper;

/**
 * @author Administrator
 */
public interface MarketSmsTargetedUserBaseBeanMapper {

	@Mapper(componentModel = "spring")
	interface MarketSmsTargetedUserSaveBeanMapper extends
			BaseObjectMapper<MtUserInfoDTO, MtUserInfo> {
	}
}
