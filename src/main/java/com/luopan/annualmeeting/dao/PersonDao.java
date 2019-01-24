package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Person;
import com.luopan.annualmeeting.entity.vo.PersonExampleVO;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonVO;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PersonDao {

  int insert(Person person);

  List<Person> findAll(long companyId);

  List<Person> findNoLotteryPeople(long companyId);

  Set<Long> findJoinGrandPrizePersonIds(long companyId);

  long countByExample(PersonExampleVO personExampleVO);

  List<Person> findByExample(PersonExampleVO personExampleVO);

  List<PersonVO> search(PersonSearchVO personSearchVO);

  int updateSelective(Person person);

}
