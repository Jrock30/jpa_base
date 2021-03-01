package jpamapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    /**
     * 일대일 관계
     * 일 대 일 어느 테이블 중 하나에서 FK, 유니크 필드를 조인해주면 된다.
     * Member에서 해도 되고 Locker에서 해도 되고
     * 양방향으로 할거면 두개다 만들어주어도 된다.
     * 마찬가지로 외래키를 가지고 있는 곳에 연간관계의 주인을 가지도록 한다. 현재는 Member
     *
     * 반대로 설정할 거면 Member 에 mappedBy를 적용해주면 된다.
     *
     * 비즈니스 적으로 볼 때 현재는 일대일이지만 나중에 일대다가 될 수 있는 환경을 생각하여
     * FK를 잘 설정하도록 하자. 현재는 예제로 보면 회원이 여러개의 라커를 가질 수 있도록 라커에 FK를 주는게 좋을 수 있다.
     * 그러나 속도적인 측면으로 다양하게 데이터를 쉽게 뽑기 위해서는 회원에 FK를 넣는게 나을 수도 있는데 잘 조율하도록 하자.
     */
//    @OneToOne(mappedBy = "locker")
//    private Member member;

}
