package com.meet.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import com.meet.pub.entity.R;
import com.meet.vo.MtUserInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户基本信息表 Mapper 接口
 * </p>
 *
 * @author huangjiayi
 * @since 2022-11-26
 */
public interface MtUserInfoMapper extends BaseMapper<MtUserInfo> {

    /**
     * 单条查询
     * @return
     */
    MtUserInfoVo queryOne(@Param("dto") MtUserInfoDTO dto);
}
