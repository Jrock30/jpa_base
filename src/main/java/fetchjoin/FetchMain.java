package fetchjoin;

import jpamapping.Member;
import jpamapping.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;

public class FetchMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setName("회원1");
            member1.setAge(10);
            member1.changeTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("회원2");
            member2.setAge(15);
            member2.changeTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("회원3");
            member3.setAge(15);
            member3.changeTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();


            String query = "select m From Member m";
            List<Object[]> resultList = em.createQuery(query).getResultList();

            for (Object[] o : resultList) {
//                System.out.println(Arrays.toString(o));
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.close();
        }
        emf.close();

    }
}
