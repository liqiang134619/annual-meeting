package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.dao.CompanyDao;
import com.luopan.annualmeeting.entity.Company;
import com.luopan.annualmeeting.service.ICompanyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lujw on 2019/1/16.
 */
@Service
public class CompanyService implements ICompanyService {

  @Autowired
  private CompanyDao companyDao;

  @Override
  public Company findById(Long id) {
    return companyDao.findById(id);
  }

  @Override
  public Company findByCompanyId(Long companyId) {
    return companyDao.findByCompanyId(companyId);
  }

  @Override
  public List<Company> findAll() {
    return companyDao.findAll();
  }

}
