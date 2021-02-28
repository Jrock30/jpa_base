package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Table(name = "USER")  이렇게 하면 user로 테이블명 명시해서 쿼리나감
public class Member {

    @Id // PK
    private Long id;
//    @Column(name = "username") 이렇게하면 username 으로 컬럼명이 명시되어 쿼리 나감
    private String name;

    //

    /**
     * JPA 내부적으로 리플렉션 같은 것을 사용하기 때문에 그냥 생성자를 쓰면 에러 발생
     * 기본 생성자가 하나 있어야함. (public으로 꼭 할 필요는 없음
     */
    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
