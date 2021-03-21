package jpamapping;

import javax.persistence.*;

@Entity
/**
 *
 *  상속관계 매핑
 *  추상클래스로 만들자 (혼자 쓰일 수 없게)
 *  객체의 상속 구조를 만들 수 있고, 결국 다형성을 활용할 수 있다는 이점이 있다.
 *
 *  조인 전략(JOINED)
 *    - 기본적으로 조인 전략이 정석이라고 보자. ( 단점도 크지 않을 뿐더러 사실 커버가 가능함 )
 *    - 조인전략을 사용하지 않으면 Item 테이블에 상속 받는 모든 객체의 컬럼들이 다 생성 됨
 *    - 조인전략을 사용하면 상속받는 객체들의 테이블이 각각 생성됨 그리고 FK는 자동으로 Item Id로 등록됨
 *    장점
 *      - 테이블 정규화
 *      - 외래 키 참조 무결성 제약조건 활용가능
 *      - 저장공간 효율화
 *    단점
 *      - 조회시 조인을 많이사용,성능저하
 *      - 조회 쿼리가 복잡함
 *      - 데이터 저장시 INSERT SQL 2번 호출
 *
 *  @DiscriminatorColumn(name = "DIS_TYPE") 를 넣으면 ITEM 테이블에 DTYPE이 디폴트로 자동으로 들어간다. NAME을 수동으로 바꿔도 됨.
 *  데이터를 넣을 때도 DTYPE이 자동으로 클래스명으로 들어가게 된다. 수동으로 넣고 싶으면 자식에 @DiscriminatorValue("NAME")을 넣으면 된다.
 *
 *
 *  단일 전략(Single_Table)
 *    - 하나의 테이블(ITEM)에 자식 컬럼을 다 때려 박는다. 그래서 ITEM 하나의 테이블만 남는다.
 *    - @DiscriminatorColumn 을 넣지 않아도 기본으로 DYPE이 들어간다. 테이블이 하나이기 때문에 구분 값을 위해 필수로 생성 됨.
 *    장점
 *      - 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
 *      - 조회 쿼리가 단순함
 *    단점
 *      - 자식 엔티티가 매핑한 컬럼은 모두 null 허용
 *      - 단일 테이블에 모든것을 저장하므로 테이블이 커질수있다. 상황에 따라서 조회 성능이 오히려 느려질 수 있다.
 *
 *
 *  구현클래스마다 테이블 전략 (TABLE_PER_CLASS)
 *    - 사실 쓰면 안되는 전략
 *    - 다 따로 따로 테이블이 만들어짐, Item(부모) 테이블이 생기지 않음.
 *    - 데이터는 깔끔하게 들어간다.
 *    - 부모 클래스로 데이터를 찾을 경우 UNION으로 데이터를 찾아서 쿼리가 복잡하게 됨.
 *    장점
 *      - 서브 타입을 명확하게 구분해서 처리할 때 효과적
 *      - not null 제약조건 사용 가능
 *    단점
 *      - 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL 필요)
 *      - 자식 테이블을 통합해서 쿼리하기 어려움
 *
 */
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "DTYPE")
public abstract class Item extends BaseEntity{

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    @Column(insertable=false, updatable=false)
    private String dtype;

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
