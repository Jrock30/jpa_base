package jpamapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMappingMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //저장
//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("member1");

            /**
             *
             * 아래의 em.flush() 과정을 하지 않게 되면 1차 캐시(영속 컨텍스트에만 들어가 있기 때문에 값을 가져올 수 없다)
             * 그러므로 add를 해주는게 객체 지향적으로 맞다.
             * 연관관계 두개 모두에 등록
             * TEST 케이스에도 데이터가 안 맞을수가 있다.
             * 결론은 양방향 할 떄는 양쪽에 모두 값을 셋팅해주는게 맞다.
             *
             * 헷갈리고 실수할수 있으니 연관관계 주인의 세터(세터보다는 의미있는 이름을 넣자)에서 등록하자. 그리고 **아래의 코드는 주석
             * 1에 넣어도 되고, N에 넣어도되니 상황에따라 하자
             *
             * 무한루프를 조심하자 (서로가 서로를 부르다 보면 발생할 수 있다.)
             * toString(), lombok(toString옵션사용 하지 말거나 중복제거), JSON 생성라이브러리(컨트롤러에서는 엔티티를 반환하지 말라, DTO로 변환해서 반환)
             *
             */
//            team.getMembers().add(member); // ** 이걸 셋터에 적용
            // 연간관계의 주인에 값 설정
//            member.changeTeam(team);
//            em.persist(member);

            // member.changeTeam(team); 대신 아래처럼 해도된다. (1에 넣어도 되고, N에 넣어도되니 상황에따라 하자)
//            team.addMember(member);

            // 쿼리를 미리 날려서 확인하고 싶을 떄 사용 ( 테스트 할 때 )
//            em.flush();
//            em.close();

            // 양방향 연간관계
//            Member findMember = em.find(Member.class, member.getId());
//            List<Member> members = findMember.getTeam().getMembers();
//
//            for (Member m : members) {
//                System.out.println("m = " + m.getName());
//            }


            /**
             *  조인전략
             *  자식(무비) 테이블에 정상적으로 데이터가 들어가고 부모(아이템) 테이블에도 정상적으로 등록된다.
             *
             */
//            Movie movie = new Movie();
//            movie.setDirector("aaaa1111");
//            movie.setActor("bbbbb111");
//            movie.setName("비열한거리111");
//            movie.setPrice(10000);
//
//            em.persist(movie);
//
//            // 영속성 컨텍스트 클리어
//            em.flush();
//            em.clear();
//
//            // 자식을 부르면 Movice inner join Item 으로 조인해서 데이터를 가져옴
//            Item findMovie = em.find(Movie.class, movie.getId());
//            System.out.println("findMovie = " + findMovie.getDtype());

            /**
             * 프록시 (JPA 말고 라이브러리가(하이버네이트) 구현하기 나름)
             *   - em.find(): 데이터베이스를 통해서 실제 엔티티 객체 조회
             *   - em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시)
             *   - 실제 클래스를 상속 받아서 만들어짐
             *   - 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨 (이론상)
             *   - 프록시 객체는 실제 객체의 참조(target)를 보관
             *   - 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출
             *   - 프록시 객체는 처음 사용할 때 한번만 초기화 .getName()
             *   - 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님, 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
             *   - 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함 (== 비 교 실패, 대신 instance of 사용)
             *   - member.getName() -> 프록시(가짜객체) -> 영속성 컨텍스트 -> DB -> 실제 엔티티 생성 Member
             *   - 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해 도 실제 엔티티 반환
             *     (반대도 똑같다, 프록시로 먼저찾고 영속성에 올려있는 상태면 find로 해도 영속성에 올라와 있는 프록시가 찾아짐)
             *   - 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생 --> em.detach(Entity), em.close() em.clear() 한 후 객체를 부를 떄 이 문제 발생.
             *     (하이버네이트는 org.hibernate.LazyInitializationException 예외를 터트림)
             *
             * 프록시 확인
             *   - 프록시 인스턴스의 초기화 여부 확인
             *     - PersistenceUnitUtil.isLoaded(Object entity)
             *     - emf.getPersistenceUnitUtil.isLoaded(entity)
             *   - 프록시 클래스 확인
             *     - entity.getClass().getName() 출력(..javasist.. or HibernateProxy...)
             *   - 프록시 강제 초기화
             *     - Hibernate.initialize(entity);
             *     - JPA 표준은 강제 초기화 없음, 강제 호출: member.getName()
             *
             * 참고 - 하이버네이트가 업데이트가 되면서, 트랜잭션이 살아있으면 em.close()를 호출해도 완전히 닫히지 않은 읽기 가능 상태가 된다.
             */
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("name");
//
//            em.persist(member);
//
//            em.flush();
//            em.clear();

            // 여기는 이 때 쿼리를 날린다.
//            Member findMember = em.find(Member.class, member.getId());

            // 이 떄 쿼리를 날리지 않는다. (em.getReference)
//            Member findMember = em.getReference(Member.class, member.getId());
//            // 프록시 클래스 (가짜클래스)
//            System.out.println("findMember = " + findMember.getClass());
//            // 객체를 가져올 때 쿼리를 달린다.
//            System.out.println("findMember = " + findMember.getId());
//            // 객체를 가져올 때 쿼리를 달린다.
//            System.out.println("findMember = " + findMember.getName());
//
//            Member findMember2 = em.find(Member.class, member.getId());
            // true 가 나온다. find를 먼저 하면 실 객체가 영속성에 올라가고 그 뒤 호출된 getReference는 영속성의 실객체를 가져온다
            // 반대도 동일하다 프록시를 먼저 부르면 영속성에 프록시가 올라가고 find는 그 프록시를 가져온다
//            System.out.println("findMember = " + (findMember.getClass() == findMember2.getClass());

            /**
             * 지연로딩, 즉시로딩
             *
             */
//            Team team = new Team();
//            team.setName("team");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("member");
//            em.persist(member);
//
//            em.flush();
//            em.clear();

            // 즉시로딩을 하면 이 때 getTeam() 까지 객체를 다 가져온다(fetch = FetchType.EAGER), 반면 LAZY는 지연로딩 프록시 초기화를 함.
//            Member m = em.find(Member.class, member.getId());

            // 팀은 프록시로 가져옴 지연로딩으로 인해 (fetch = FetchType.LAZY)
//            System.out.println("m = " + m.getTeam().getClass());

            // 지연로딩 - 이 떄 쿼리가 나감 ( 프록시 초기화, 지연로딩을 하면 프록시로 가져온다. ), getTeam() 이아니고 getTeam().getName() 메서드를 가져올 떄 (필드)
//            m.getTeam().getName();

            /**
             * 즉시로딩의 문제점 ( e.g. JPQL )
             * 실행하면 셀럭트 쿼리가 두번 나감
             * 멤버 안에 즉시로딩을 찾게 되면 걸려있는 일대다 다대다로 되어 있는 것은 따로 따로 조회를 다함.
             */
//            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();


            /**
             * 영속성 전이 : CASCADE
             */

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            /**
             * 고아객체 삭제
             */
            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }
        emf.close();
    }
}
