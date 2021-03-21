package valuetype;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /**
     * 값 타입 (임베디드)
     * @Embeddable 은 정의할 때 적고
     * @Embedded 는 가져다 쓰는 곳에 적는다. 한개는 생략가능
     */
    // 기간 period
    @Embedded
    private Period workPeriod;

    // 주소
    @Embedded
    private Address homeAddress;

    /**
     *  값 타입 컬렉션
     *    - 값 타입을 하나 이상 저장할 때 사용
     *    - @ElementCollection, @CollectionTable 사용
     *    - 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
     *    - 컬렉션을 저장하기 위한 별도의 테이블이 필요함
     *    - 값 타입 컬렉션도 지연 로딩 전략 사용
     *    참고: 값 타입 컬렉션은 영속성 전에(Cascade) + 고아 객체 제 거 기능을 필수로 가진다고 볼 수 있다.
     */
    // 컬렉션 모두 사용 가능하다.
    @ElementCollection // 값 타입 컬렉션은 이 어노테이션 사용, fetch = 기본값 Lazy
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID")) // 생성할 테이블 명, 조인 컬럼 아이디(외래키)
    @Column(name = "FOOD_NAME") // 값이 하나기 떄문에 컬럼명을 명시해도 크게 지장 없다.
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection // 값 타입 컬렉션은 이 어노테이션 사용
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID")) // 생성할 테이블 명, 조인 컬럼 아이디(외래키)
    private List<Address> addressHistory = new ArrayList<>();

    // 주소
    @Embedded
    // 똑같은 임베디드를 사용하려면 @AttributeOverride 를 사용하면 되나 잘 쓰이지는 않을 것이다.
    // 따로 컬럼이 만들어진다.
    // 하나면 @AttributeOverride, 다중 @AttributeOverrides
    @AttributeOverrides({
            @AttributeOverride(name = "city",column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street",column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode",column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<Address> addressHistory) {
        this.addressHistory = addressHistory;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }
}
