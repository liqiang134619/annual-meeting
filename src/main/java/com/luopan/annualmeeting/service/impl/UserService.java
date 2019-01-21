package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.Role;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.UserDao;
import com.luopan.annualmeeting.entity.Company;
import com.luopan.annualmeeting.entity.User;
import com.luopan.annualmeeting.entity.vo.UserCompanyVO;
import com.luopan.annualmeeting.entity.vo.UserVO;
import com.luopan.annualmeeting.service.IUserService;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public RespMsg login(UserVO userVO) {
    User user = userDao.findByUsernameAndPassword(userVO.getUsername(), userVO.getPassword());
    if (user == null) {
      return ResultUtil.error(ErrCode.USER_NOT_FOUND);
    }
    if (!Role.ADMIN.equalsIgnoreCase(user.getRole())) {
      return ResultUtil.error(ErrCode.USER_NO_PERMISSION);
    }
    Company company = redisUtil.hGet(RedisKey.COMPANY_MAP, user.getCompanyId(), Company.class);
    if (company == null) {
      return ResultUtil.error(ErrCode.COMPANY_NOT_FOUND);
    }
    UserCompanyVO userCompanyVO = new UserCompanyVO().setCompanyId(company.getId())
        .setCompanyName(company.getName());
    return ResultUtil.success(userCompanyVO);
  }

}
