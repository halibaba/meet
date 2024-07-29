package com.meet.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import com.meet.pub.entity.R;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author huangjiayi
 * @since 2022-11-26
 */
public interface IMtUserInfoService extends IService<MtUserInfo> {

    /**
     * 分页查询
     * @param page
     * @param mtUserInfoDto
     * @return
     */
    R queryPage(Page page, MtUserInfoDTO mtUserInfoDto);

    /**
     * 新增
     * @return
     */
    R insert(MtUserInfoDTO dto);

    /**
     * 修改
     * @return
     */
    R update(MtUserInfoDTO dto);

    /**
     * 单条查询
     * @return
     */
    R queryOne(MtUserInfoDTO dto);
}
