package valuetype;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
