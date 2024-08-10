package com.meet.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meet.admin.mapper.MtUserInfoMapper;
import com.meet.admin.service.IMtUserInfoService;
import com.meet.dto.MtUserInfoDTO;
import com.meet.entity.MtUserInfo;
import com.meet.mapstruct.MtUserInfoConverter;
import com.meet.pub.entity.R;
import com.meet.vo.MtUserInfoVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author huangjiayi
 * @since 2022-11-26
 */
@Service
public class MtUserInfoServiceImpl extends ServiceImpl<MtUserInfoMapper, MtUserInfo> implements IMtUserInfoService {

    @Override
    public R queryPage(Page page, MtUserInfoDTO mtUserInfoDto) {
        MtUserInfo mtUserInfo = new MtUserInfo();
        MtUserInfoConverter.INSTANCE.DTOToEntity(mtUserInfoDto, mtUserInfo);
        LambdaQueryWrapper<MtUserInfo> wrapper = new LambdaQueryWrapper<>();
        Page<MtUserInfo> resPage = baseMapper.selectPage(page, wrapper);
        IPage<MtUserInfoVo> convert = resPage.convert(this::getMtUserInfoVo);
        return R.ok().data("data", convert);
    }

    public MtUserInfoVo getMtUserInfoVo(MtUserInfo entity){
        MtUserInfoVo vo = new MtUserInfoVo();
        MtUserInfoConverter.INSTANCE.entityToVO(entity, vo);
        return vo;
    }

    @Override
    public R insert(MtUserInfoDTO dto) {
        MtUserInfo mtUserInfo = new MtUserInfo();
        MtUserInfoConverter.INSTANCE.DTOToEntity(dto, mtUserInfo);
        return R.ok().data("data", this.baseMapper.insert(mtUserInfo));
    }

    @Override
    public R update(MtUserInfoDTO dto) {
        MtUserInfo mtUserInfo = new MtUserInfo();
        MtUserInfoConverter.INSTANCE.DTOToEntity(dto, mtUserInfo);
        UpdateWrapper<MtUserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", mtUserInfo.getId());
        int update = this.baseMapper.update(mtUserInfo,updateWrapper);
        return R.ok().data("data", update);
    }

    @Override
    public R queryOne(MtUserInfoDTO dto) {
        return R.ok().data("data", baseMapper.queryOne(dto));
    }
}
