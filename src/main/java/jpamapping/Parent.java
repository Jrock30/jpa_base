package jpamapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id @GeneratedValue
    private Long id;
    private String name;

    /**
     * 영속성 전이 : CASCADE
     *   - 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들도 싶을 때
     *   - 예: 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.
     *   - 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
     *   - 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함 을 제공할 뿐
     *   - 옵션은 ALL 또는 PERSIST(저장할떄만) 만 사용하도록
     *   - 하나의 부모가 자식을 관리할 때 의미있음(게시판이나, 첨부파일 경로 데이터 등)
     *   - Child의 소유자가 하나 일 때만 사용하도록 하자. 현재는 Parent에서만 사용하는데 Member와 같이 다른 것도 소유하게 되면 사용하면 안됨.
     *   - 즉 하나에 완전히 종속적 일 때와(단일소유자), 라이프사이클이 유사할 때 사용.
     *
     * 고아객체
     *   - 고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티 를 자동으로 삭제
     *   - orphanRemoval = true
     *   - persist를 부모 하나만 해도 같이 영속성에 자식이랑 들어감
     *   - 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
     *   - 영속성과 마찬가지로 참조하는 곳이 하나일 때 사용해야함!, 특정 엔티티가 개인소유 할 때 사용
     *   - 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다.
     *          따라서 고아 객체 제거 기능을 활성화 하면,
     *          부모를 제거할 때 자식도 함께 제거된다. 이것은 CascadeType.REMOVE처럼 동작한다.
     *   - orphanRemoval = true 는 조심해서 쓰도록 하자. Delete가 되니까!!
     *
     * 영속성 전이 + 고아 객체, 생명주기
     *   - CascadeType.ALL + orphanRemovel=true
     *   - 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
     *   - 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명 주기를 관리할 수 있음
     *   - 도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용
     *
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
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

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public List<Child> getChildList() {
        return childList;
    }
}
