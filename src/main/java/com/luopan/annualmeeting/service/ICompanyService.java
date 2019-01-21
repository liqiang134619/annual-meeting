package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.entity.Company;
import java.util.List;

/**
 * Created by lujw on 2019/1/16.
 */
public interface ICompanyService {

  Company findById(Long id);

  Company findByCompanyId(Long companyId);

  List<Company> findAll();

}
