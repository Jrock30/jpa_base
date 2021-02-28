package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        /**
         * 영속성 컨텍스트
         *
         * 엔티티를 영구 저장하는 환경
         * EntityManager.persist(entity)
         * 디비에 저장한다는 것이 아니라 영속성 컨텍스를 통해서 엔티티를 영속화 한다는 뜻(영속성 컨텍스트에 저장)
         * 영속성 컨텍스트는 논리적인 개념이다.(엔티티 매니저를 통해 영속성 컨텍스트에 접근, 엔티티매니저 1 : 1 영속성 컨텍스트)
         * 어플리케이션과 디비 사이에 중간 계층이 있다고 이해
         *
         * 이점
         * 1차캐시
         *   - find -> 영속성 컨텍스트 -> DB    --> 한 트랜잭션 안에서만 작동하니 큰 이점은 없으나, 가끔 복잡한 로직에서만 도움 됨
         * 동일성(identity)보장
         *   - 영속엔티티의 같은 데이터를 == 비교하면 동일성 보장(자바 컬렉션 조회에서 == 비교 하듯이)
         *   - 1차 캐시로 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공
         * 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
         *   - em.persist(member) 할 적에 INSERT 쿼리를 보내는 것이 아닌 tx.commit(); 할 때 FLUSH가 되면서 INSERT 쿼리를 보낸다.
         *   - 영속 컨텍스트(entityManager) 안에 쓰기 지연 SQL 저장소에 쿼리를 먼저 쌓아둠. (1차 캐시에도 데이터 저장)
         * 변경감지(Dirty Checking), 지연로딩(Laze Loading)
         *   - 데이터를 find 한 다음에 객체의 값을 수정해주기만 해도 수정된 데이터가 커밋이 된다.
         *   - flush() -> 엔티티와 스냅샷 비교(1차캐시 비교, 1차캐시 내부에 최초시점의 스냅샷을 떠 놓음) -> 쓰기지연 SQL저장소(SQL 생성) -> flush -> commit
         *   - 변경을 체크해서 update 쿼리를 날리거나, insert를 날리거나 하는 분기를 둘 필요가 없음.
         *   - on duplicate key update (X)
         *
         * 플러쉬
         *   - em.flush() -> 직접호출 ( 쿼리를 미리 보고 싶을 때 활용 )
         *   - 트랜잭션 커밋  -> 자동호출
         *   - JPQL 호출   -> 자동호출
         *   - 영속성 컨텍스트를 비우지 않음
         *   - 영속성 컨텍스트의 변경내용 데이터베이스에 동기화
         *   - 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화 하면 됨.
         *
         * 준 영속성
         *   - em.detach(entity)  -> 특정 엔티티만 준영속 상태로 전환
         *   - em.clear()         -> 영속성 컨텍스트를 완전히 초기화(테스트 케이스 작성할 때 도움됨)
         *   - em.close()         -> 영속성 컨텍스트를 중료
         *
         */

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
             * 객체생성(비영속상태)
             */
            Member member1 = new Member();
            member1.setUsername("A");
            member1.setRoleType(RoleType.USER);

            Member member2 = new Member();
            member2.setUsername("B");
            member2.setRoleType(RoleType.USER);

            Member member3 = new Member();
            member3.setUsername("C");
            member3.setRoleType(RoleType.USER);

            Member member4 = new Member();
            member4.setUsername("D");
            member4.setRoleType(RoleType.USER);

            // e.g. allocationSize = 50
            // DB SEQ = 1       |   1
            // DB SEQ = 51      |   2
            // DB SEQ = 51      |   3
            // allocationSize 시퀀스 사이즈를 원하는 숫자만큼 메모리에 올려 놓고 갯수 만큼 사용 *** 시퀀스 방법 ***
            // allocationSize = 50'으로 설정하면, (처음 시점에) 애플리케이션 시점 JPA가 메모리에 미리 1~50개를 확보하여 next_val이 51이 되기 전까지는 DB 서버 통신을 하지 않는다.
            em.persist(member1);//1, 51
            em.persist(member2);//Memory
            em.persist(member3);//Memory
            em.persist(member4);//Memory
            // .... 51을 만나는 순간 next call 이 호출됨 Hibernate: call next value for MEMBER_SEQ

            System.out.println("member1 = " + member1.getId());
            System.out.println("member2 = " + member2.getId());
            System.out.println("member3 = " + member3.getId());

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
             * JPQL을 날릴 때는 무조건 flush()가 발생함. 그래서 객체를 persist 하고 커밋을 하지 않아도 flush가 발동해서 조회가 됨
             *
             */
//            List<Member> result = em.createQuery("SELECT m FROM Member AS m", Member.class)
//                    .getResultList();
//            List<Member> result = em.createQuery("SELECT m FROM Member AS m", Member.class)
//                    .setFirstResult(5) // 페이지네이션(오라클이면 rownum, MYSQL 이면 limit offset 디비에 따라 바뀜)
//                    .setMaxResults(8)
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member = " + member.getName());
//            }

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
             *  이러면 저장이 된다.(이 때 디비에 저장되는 것은 아님, 쿼리가 나가는 타이밍은 아님)
             *  객체를 저장한 상태(영속 상태)
             *  영속성 컨텍스트 안에 들어가서 관리
             */
//            em.persist(member);
//            em.detach(member); // 회원 엔티티를 영속성 컨텍스트에서 분리, 준영속 상태
//            em.remove(member); // 객체를 삭제한 상태(삭제)

//            em.flush() -> 쿼리 날림(수동)

            /**
             * 트랜잭션 커밋
             * 이 때 디비에 쿼리가 날라감
             */
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
