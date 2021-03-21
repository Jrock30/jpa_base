package jpaquery;

import valuetype.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * JPA를 사용하면 엔티티 객체를 중심으로 개발 • 문제는 검색 쿼리
 * 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
 * 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검 색 조건이 포함된 SQL이 필요
 * JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
 * SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY,
 * HAVING, JOIN 지원
 * JPQL은 엔티티 객체를 대상으로 쿼리 SQL은 데이터베이스 테이블을 대상으로 쿼리
 *
 * 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
 * SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
 * JPQL을 한마디로 정의하면 객체 지향 SQL
 *
 *
 */
public class JpaQueryMain {
    public static void main(String[] args) {
        System.out.println("JpaQueryMain.main");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

//        String jpql = "select m From Member m where m.name like '%hello%'";
//
//        List<Member> result = em.createQuery(jpql, Member.class).getResultList();

        /**
         * Criteria
         *   - 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
         *   - JPQL 빌더 역할
         *   - JPA 공식 기능
         *   - 단점: 너무 복잡하고 실용성이 없다. Criteria 대신에 QueryDSL 사용 권장
         */
        //Criteria 사용 준비
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = cb.createQuery(Member.class);
        //루트 클래스 (조회를 시작할 클래스)
        Root<Member> m = query.from(Member.class);
        //쿼리 생성
        CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
        List<Member> resultList = em.createQuery(cq).getResultList();

        /**
         * QuesyDSL
         *   - 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
         *   - JPQL 빌더 역할
         *   - 컴파일 시점에 문법 오류를 찾을 수 있음 동적쿼리 작성 편리함
         *   - 단순하고 쉬움
         *   - 실무 사용 권장
         */
        //JPQL
        //select m from Member m where m.age > 18
//        JPAFactoryQuery query = new JPAQueryFactory(em);
//        QMember m = QMember.member;
//        List<Member> list = query.selectFrom(m)
//                .where(m.age.gt(18))
//                .orderBy(m.name.desc())  .fetch();

        /**
         * 네이티브 SQL 소개
         */
        String sql = "SELECT ID, AGE, TEAM_ID, NAME FROM MEMBER WHERE NAME = 'kim'";
        List<Member> resultList2 = em.createNativeQuery(sql, Member.class).getResultList();

    }
}
