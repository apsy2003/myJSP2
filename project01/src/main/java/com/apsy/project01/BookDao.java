package com.apsy.project01;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/*�����Ϳ� �����ϴ� Ŭ�������� ����*/
/*�������� �����͸� �����ϴ� Ŭ������� �����ؼ� �ڹ� ��(Java bean)���� ����ؼ� ����*/
@Repository
public class BookDao {
	/* sqlSessionTemplate ��ü�� ����� �� �ְ� */
    @Autowired
    SqlSessionTemplate sqlSessionTemplate;	
	/* ���� XML�� �����Ű�� ���ؼ� SqlSessionTemplate ��ü�� ��� ������ ���� */
    
    public int insert(Map<String, Object> map) {
        return this.sqlSessionTemplate.insert("book.insert", map);
    }   

    public Map<String, Object> selectDetail(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("book.select_detail", map);
    }
    
    public int update(Map<String, Object> map) {
        return this.sqlSessionTemplate.update("book.update", map);
    }    

    public int delete(Map<String, Object> map) {
        return this.sqlSessionTemplate.delete("book.delete", map);
    }    
    
}