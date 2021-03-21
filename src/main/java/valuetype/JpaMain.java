package valuetype;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 값 타입 컬렉션의 제약사항
 *   - 값 타입은 엔티티와 다르게 식별자 개념이 없다.
 *   - 값은 변경하면 추적이 어렵다.
 *   - 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고, 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
 *   - 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야 함: null 입력X, 중복 저장X
 *
 * 값 타입 컬렉션 대안
 *   - 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
 *   - 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
 *   - 영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬 렉션 처럼 사용
 *   - EX) AddressEntity
 *
 * 엔티티 타입의 특징 • 식별자O
 * 생명주기관리
 * 공유
 * 값타입의특징
 *   - 식별자X
 *   - 생명 주기를 엔티티에 의존
 *   - 공유하지 않는 것이 안전(복사해서 사용)
 *   - 불변객체로만드는것이안전
 *
 * 값 타입은 정말 값 타입이라 판단될 때만 사용
 * 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안됨
 * 식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것 은 값 타입이 아닌 엔티티
 */
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /**
             * 값 타입 컬렉션
             */

            Member member = new Member();

            member.setUsername("hello");
            member.setHomeAddress(new Address("homcity", "street", "10"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("버거");

            member.getAddressHistory().add(new Address("city1", "street", "10"));
            member.getAddressHistory().add(new Address("city2", "street", "10"));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("=============== START =================");
            Member findMember = em.find(Member.class, member.getId());

            // 불변(immutable) 이므로 새로 넣어주어여 한다. 수정을 원할 시
            // 값 타입은 의존관계를 멤버에 다 맡기는.
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));
            // 불변(immutable) 이므로 새로 넣어주어여 한다. 수정을 원할 시
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");



            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }
        emf.close();
    }
}
