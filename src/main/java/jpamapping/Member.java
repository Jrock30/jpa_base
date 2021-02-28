package jpamapping;

import javax.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    private int age;


    /**
     * 단방향 연간관계
     * @ManyToOne
     * N(Member) : 1(TEAM) 조인  -> 단방향 객체 연관관계
     * N의 객체에 1의 TEAM을 넣어준다
     *
     * 양방향 연간관계
     * N:1 일 때 1에 N의 객체 리스트가 있으면 된다. Team 객체에 @OneToMany List<Member>
     * 외래키 하나로 양방향 연관관계가 다 있다고 보아야한다.
     * 외래키로 서로의 객체를 가져올 수 있다.
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
     *  연관관계의 주인은 비즈니스적으로 중요한게 아니다.
     *
     */
    @ManyToOne
    @JoinColumn(name = "TEAM_ID") // TEAM_ID 기준으로 조인
    private Team team;

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
