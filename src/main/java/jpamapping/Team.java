package jpamapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    /**
     * 단방향 연간관계
     * @ManyToOne
     * N(Member) : 1(TEAM) 조인  -> 단방향 객체 연관관계
     * N의 객체에 1의 TEAM을 넣어준다
     *
     * 양방향 연간관계
     * N:1 일 때 1에 N의 객체 리스트가 있으면 된다. Team 객체에 @OneToMany List<Member>
     * mappedBy = "team" 팀 객체의 변수를 넣어준다.
     * 외래키 하나로 양방향 연관관계가 다 있다고 보아야한다.
     * 외래키로 서로의 객체를 가져올 수 있다.
     *
     *
     * ** 객체의 연관관계 mappedBy
     * 객체의 양방향 관계는 사실 양방향 관계가 아니라 서로 다른 단 뱡향 관계 2개다.
     * 객체를 양방향으로 참조하려면 단방향 연관관계를 2개 만들어 야 한다.
     * A -> B (a.getB()) B -> A (b.getA())
     *
     *
     * ** 테이블의 연간관계
     * 테이블은 외래 키 하나로 두 테이블의 연관관계를 관리
     * MEMBER.TEAM_ID 외래 키 하나로 양방향 연관관계 가짐(양쪽으로 조인할 수 있다.)
     *  SELECT *
     *  FROM MEMBER M
     *  JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
     *
     *  SELECT *
     *  FROM TEAM T
     *  JOIN MEMBER M ON T.TEAM_ID = M
     *
     *
     * 연관관계의 주인
     *  - 객체의 두 관계중 하나를 연관관계의 주인으로 지정
     *  - 연관관계의 주인만이 외래 키를 관리(등록, 수정) 주인이 아닌쪽은 읽기만 가능
     *  - 주인이 아닌쪽은 읽기만 가능
     *  - 주인은 mappedBy 속성 사용 X
     *  - 주인이 아니면 mappedBy 속성으로 주인 지정
     *  - 외래키가 있는 곳을 주인으로 정해라(Member.team)
     *  - N:1 -> 1에 mappedBy를 걸어라
     *
     *
     * 양방향 매핑정리
     *   - 단방향 매핑만으로도 이미 연관관계 매핑은 완료 ( 양방향을 먼저 생각하지 말라 )
     *   - 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능이 추 가된 것 뿐
     *   - JPQL에서 역방향으로 탐색할 일이 많음
     *   - 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 됨(테이블에 영향을 주지 않음)
     *   - 비즈니스 로직을 기준으로 연관관계의 주인을 선택하면 안됨
     *   - 연관관계의 주인은 외래 키의 위치를 기준으로 정해야함
     *
     * 객체는 가급적으로 단방향이 좋긴하다.
     */
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();  // 관례 초기화를 해줌 ( 널포인트가 뜨지 않게 )

    public void addMember(Member member) {
        member.setTeam(this);
        members.add(member);
    }

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }


}
