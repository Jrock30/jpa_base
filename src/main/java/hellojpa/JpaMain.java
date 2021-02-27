package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        /**
         *
         * 모든 JPA 에서는 트랜잭션 단위가 엄청 중요하다. 모든작업은 트랜잭션 안에서 작업을 해야한다.
         * EntityManagerFactory 설정 -> persistence.xml 에서 등록한 <persistence-unit name="hello">
         * EntityManagerFactory 는 어플리케이션 실행시점에 하나만 만들어야한다.
         * *** 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
         *
         * JPA 정석 으로 사용해 본다 ( 스프링을 쓰면 자동으로 다 해줌)
         *
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        /**
         *  엔티티 매니저를 꺼낸다
         *  사용자가 디비를 통해 데이터를 꺼낼 때 EntityManager를 항상 만들어주아야한다
         *  *** 엔티티 매니저는 쓰레드간에 공유하지 않는다(사용하고 버려야한다)
         *  *** JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
         */
        EntityManager em = emf.createEntityManager();

        /**
         * 트랜잭션 가져와서 시작
         */
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /**
         * 엔티티 객체 멤버를 만들어준다.
         */

        try {
            /**
             * INSERT
             */
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");

            /**
             * SELECT
             * find -> 객체를 대신 저장해준다. (자바 컬렉션처럼 이해하자), SELECT 쿼리
             */
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());

            /**
             * JPA 입장에서는 테이블을 대상으로 쿼리를 만들지 않고 객체를 대상으로 만든다.
             * 그래서 밑에 JPQL 자세히 보면 Member
             * SELECT * FROM MEMBER --> 안먹힘
             * SELECT m FROM Member AS m --> 먹힘
             * 주석은 JPQL로 들어감
             * JPA 는 SQL을 추상화한 JPQL 이라는 객체지향쿼리 언어제공
             * SQL 과 문법유사, SELECT, FROM ,WHERE, GROUP BY, HAVING, JOIN 지원
             * JPQL은 엔티티 객체를 대상으로 쿼리, SQL은 데이터베이스 테이블을 대상으로 쿼리
             * 데이터베이스에 종속되지 않는 JPQL 대표적으로 아래와 같이 디비에 따라 바뀐다.
             * 페이지네이션(오라클이면 rownum, MYSQL 이면 limit offset 디비에 따라 바뀜)
             * 한마디로 객체지향 SQL
             */
//            List<Member> result = em.createQuery("SELECT m FROM Member AS m", Member.class)
//                    .getResultList();
            List<Member> result = em.createQuery("SELECT m FROM Member AS m", Member.class)
                    .setFirstResult(5) // 페이지네이션(오라클이면 rownum, MYSQL 이면 limit offset 디비에 따라 바뀜)
                    .setMaxResults(8)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getName());
            }

            /**
             * DELETE
             */
//            em.remove(findMember);

            /**
             * UPDATE
             * 객체를 바꾸었을 뿐인데 UPDATE 쿼리가 나간다.
             * JPA 통해서 엔티티를 가져오면 JPA가 관리를 한다.
             * 최종 COMMIT 하는 시점에 데이터가 바뀌면 체크를 하여 UPDATE를 날린다.
             */
//            findMember.setName("HelloJPA");

            /**
             *  이러면 저장이 된다.
             */
//            em.persist(member);

            // 트랜잭션 커밋
            tx.commit();
        } catch (Exception e) {
            // Exception 발생시 rollback
            tx.rollback();
        } finally {
            // 작업 수행 후 닫아줌 (엔티티가 데이터 커넥션을 물고 있으니 꼭 닫아줘야한다)
            em.close();
        }
        emf.close();
    }
}
