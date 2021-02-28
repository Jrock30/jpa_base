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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");

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
            member.changeTeam(team);
            em.persist(member);

            // member.changeTeam(team); 대신 아래처럼 해도된다. (1에 넣어도 되고, N에 넣어도되니 상황에따라 하자)
//            team.addMember(member);

            // 쿼리를 미리 날려서 확인하고 싶을 떄 사용 ( 테스트 할 때 )
            em.flush();
            em.close();

            // 양방향 연간관계
//            Member findMember = em.find(Member.class, member.getId());
//            List<Member> members = findMember.getTeam().getMembers();
//
//            for (Member m : members) {
//                System.out.println("m = " + m.getName());
//            }


            tx.commit();
        } catch (Exception e) {
            em.close();
        }
        emf.close();
    }
}
