package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.SystemDictionaryItem;
import com.payease.p2p.base.domain.UserFile;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.mapper.UserFileMapper;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.UserFileQueryObject;
import com.payease.p2p.base.service.IUserFileService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liuxiaoming on 2017/7/5.
 */
@Service
public class UserFileServiceImpl implements IUserFileService {

    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void apply(String fileName) {
        UserFile userFile = new UserFile();

        userFile.setApplier(UserContext.getCurrent());
        System.out.println(UserContext.getCurrent());
        userFile.setApplyTime(new Date());
        userFile.setImage(fileName);
        userFile.setState(UserFile.STATE_NORMAL);
        this.userFileMapper.insert(userFile);
    }

    @Override
    public List<UserFile> listUserFilesByHasSelectType(Boolean hasTypes) {
        return this.userFileMapper.listUserFilesByHasSelectType(UserContext.getCurrent().getId(),hasTypes);
    }

    @Override
    public void selectTypes(Long[] ids, Long[] fileTypes) {
        if(ids != null && fileTypes !=null && ids.length == fileTypes.length){
            for (int i = 0; i<ids.length ; i++){
                UserFile uf = this.userFileMapper.selectByPrimaryKey(ids[i]);
                if(uf.getFileType() == null){
                    SystemDictionaryItem type = new SystemDictionaryItem();
                    type.setId(fileTypes[i]);
                    uf.setFileType(type);
                    this.userFileMapper.updateByPrimaryKey(uf);
                }
            }
        }
    }

    @Override
    public PageResult query(UserFileQueryObject qo) {
        int count = this.userFileMapper.queryForCount(qo);
        if(count>0){
            List<UserFile> list = this.userFileMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());

        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public List<UserFile> queryForList(UserFileQueryObject qo) {
        return this.userFileMapper.query(qo);
    }

    @Override
    public void audit(Long id, String remark, int score, int state) {
         //得到UserFile对象，判断状态
        UserFile userFile = this.userFileMapper.selectByPrimaryKey(id);
        if(userFile!=null && userFile.getState() == UserFile.STATE_NORMAL){
            //设置UserFile 相关属性
            userFile.setAuditor(UserContext.getCurrent());
            userFile.setAuditTime(new Date());
            userFile.setState(state);
            userFile.setRemark(remark);
            if(state == UserFile.STATE_AUDIT){
                //如果审核通过累加用户的风控分数
                userFile.setScore(score);
                Userinfo userinfo = this.userInfoService.get(userFile.getApplier().getId());
                userinfo.setAuthScore(userinfo.getAuthScore() + score);
                this.userInfoService.update(userinfo);
            }
            this.userFileMapper.updateByPrimaryKey(userFile);
        }



    }
}
