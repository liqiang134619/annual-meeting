package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Company;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by lujw on 2019/1/16.
 */
@Mapper
@Repository
public interface CompanyDao {

  Company findById(long id);

  Company findByCompanyId(long companyId);

  List<Company> findAll();

}
