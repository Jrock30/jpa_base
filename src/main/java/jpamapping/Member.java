package jpamapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    private int age;


    /**
     * 단방향 연간관계
     *
     * @ManyToOne N(Member) : 1(TEAM) 조인  -> 단방향 객체 연관관계
     * N의 객체에 1의 TEAM을 넣어준다
     * <p>
     * 양방향 연간관계
     * N:1 일 때 1에 N의 객체 리스트가 있으면 된다. Team 객체에 @OneToMany List<Member>
     * 외래키 하나로 양방향 연관관계가 다 있다고 보아야한다.
     * 외래키로 서로의 객체를 가져올 수 있다.
     * <p>
     * <p>
     * 연관관계의 주인
     * - 객체의 두 관계중 하나를 연관관계의 주인으로 지정
     * - 연관관계의 주인만이 외래 키를 관리(등록, 수정) 주인이 아닌쪽은 읽기만 가능
     * - 주인이 아닌쪽은 읽기만 가능
     * - 주인은 mappedBy 속성 사용 X
     * - 주인이 아니면 mappedBy 속성으로 주인 지정
     * - 외래키가 있는 곳을 주인으로 정해라(Member.team)
     * - N:1 -> 1에 mappedBy를 걸어라
     * <p>
     * 연관관계의 주인은 비즈니스적으로 중요한게 아니다.
     */
    // 아래를 Team자체를 지우고 Team 객체에서의 기준으로 조인하면 하면 일 대 다(1:N) 연간관계
    // 다대일을 쓰도록 하자. 필요하면 다대일 양방향으로
    // 외래키를 해당 테이블이 아닌 연관관계 테이블에서 관리하는 것이 더 헷갈리다.
    // 아래처럼하면 읽기전용으로 일대다 양방향으로 되긴하지만 억지스럽다(JPA 공식 스펙에 있는 것은 아님). 다대일 단방향 양방향을 사용하도록 하자
    // @ManyToOne
    // @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    // @ManyToOne 은 스펙상 mappedBy를 지원하지 않음. 혼란을 야기
    /**
     *  지연로딩, 즉시로딩
     *    - 실무에서는 애지간하면 지연로딩을 사용하는 것이 좋다.
     *    - FetchType.LAZY 지연로딩 ( 프록시로 가져옴 )
     *    - FetchType.EAGER 즉시로딩 ( 한번에 조인해서 다가져옴 ), 많아지면 속도 제약이 생길 수 있다.
     *    - 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
     *    - 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다. ( 쿼리 한개를 실행했는데 객체에 따라 N개의 쿼리가 추가 된다 )
     *    - @ManyToOne, @OneToOne은 기본이 즉시 로딩 -> LAZY로 설정
     *    - @OneToMany, @ManyToMany는 기본이 지연 로딩
     *    - 모든 연관관계에 지연 로딩을 사용해라!, 실무에서 즉시 로딩을 사용하지 마라!
     *    - JPQL fetch 조인이나, 엔티티 그래프 기능을 사용해라!
     */
    //
    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
//    @ManyToOne(fetch = FetchType.EAGER) // 즉시로딩
    @JoinColumn(name = "TEAM_ID") // TEAM_ID 기준으로 조인
    private Team team;

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
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    /**
     * 다대다
     *
     * 관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
     * 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야함
     * 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능
     * 편리해 보이지만 실무에서 사용X
     * 연결 테이블이 단순히 연결만 하고 끝나지 않음
     * 주문시간, 수량 같은 데이터가 들어올 수 있음
     * 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
     * @ManyToMany -> @OneToMany, @ManyToOne
     *
     */
//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT") // 연결 테이블 명 기재
//    private List<Product> products = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
