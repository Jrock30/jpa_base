package hellojpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자.
 * 예를 들어 주민등록번호도 기본 키로 적절하기 않다.
 * 권장: Long형 + 대체키 + 키 생성전략 사용 (AUTO_INCREMENT, SEQ, UUID ..)
 *
 *
 */

@Entity // @Entity 가 붙은 클래스는 JPA가 관리, 엔티티라 한다. JPA를 사용해서 테이블과 매핑할 클래스는 @Entity가 필수
//@Table(name = "USER")  이렇게 하면 user로 테이블명 명시해서 쿼리나감
// 하이버네이트가 시퀀스명을 자동으로 생성하기 때문에 generator 시퀀스명을 맵핑 시킬 때 사용
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 5)
// 테이블 전략 ( 모든 데이터베이스에 적용가능, 단점 -> 성능, 아래 GenerationType.Table로 변경 ), 시퀀스 전용 테이블이 생성 됨
//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = “MEMBER_SEQ", allocationSize = 50)
public class Member {

    @Id // PK, 직접 할당할 경우 @Id만 사용
    /**
     * IDENTITY(주로 mysql, postgres ... , 데이터베이스에 위임), SEQUENCE(주로 오라클, DB시퀀스 오브젝트 사용), AUTO(자동지정);
     * 하이버네이트가 시퀀스명을 자동으로 생성하기 때문에 generator 시퀀스명을 맵핑 시킬 때 사용
     *
     * IDENTITY
     *   - NULL 값이 들어가야 디비에서 AUTO_INCREMENT와 같은 작업을 해.
     *   - **** PK 값이 DB에 들어가야 값을 알 수 있음. (commit 내지는 persist(entity) -> 이 때 쿼리가 날라감)
     *   - 위의 시점에서만 영속성으로 ID(PK) 값을 알 수 있다.
     *   - 모아서 INSERT 하기에 불가능한 측면이 있다.
     *
     * SEQUENCE
     *   - *** 1부터 시작하고 1씩 증가 시킴 ( 이 또한 DB로 조회를 해야 값을 알 수 있다., persist(entity) )
     *   - *** 이 때는 commit 할 때 insert가 날라간다.
     *   - 쿼리를 모았다가 write 가 가능 ( 버퍼링 가능 )
     *   - 그래서 위에 allocationSize 사이즈를 원하는 숫자만큼 메모리에 올려 놓고 갯수 만큼 사용
     *   - allocationSize = 50'으로 설정하면, (처음 시점에) 애플리케이션 시점 JPA가 메모리에 미리 1~50개를 확보하여 next_val이 51이 되기 전까지는 DB 서버 통신을 하지 않는다.
     *   - 시퀀스 번호 손실을 막으려면 사이즈를 1로 하자.
     *
     */
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동할당, 기본 값 AUTO
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    /**
     * unique, length ... -> ddl 생성만 도와줌
     * JPA 실행 로직에는 영향을 주지 않음.
     * 객체는 username DB 컬럼명은 name
     * updateble = false -> 변경 된 데이터 update 시 쿼리 생성 X
     * insertable = false -> 변경 된 데이터 insert 시 쿼리 생성 X
     * nullable = false -> not null
     * unique = true -> 이름 생성이 힘들어(랜덤 유니크키, @Table에서 걸면 이름 줄 수 있음)
     */
    @Column(name = "name", nullable = false)
    private String username;

    // precision, scale(DDL), precision=19, scale=2
    //BigDecimal 타입에서 사용한다(BigInteger도 사용할 수 있다). precision은 소수점을 포함한 전체 자 릿수를, scale은 소수의 자릿수 다. 참고로 double, float 타입에는 적용되지 않는다. 아주 큰 숫자나 정 밀한 소수를 다루어야 할 때만 사용한다.
    @Column()
    private BigDecimal age;

    // @Enumerated Enum 을 쓸 수 있고 -> varchar
    // EnumType.STRING -> Enum의 네임 등록
    // EnumType.ORDINAL -> Enum의 순서 등록 (BASE), 이 것을 쓰면 헷갈려짐 NAME인 STRING이 필수
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // @TemporalType -> DATE(날짜), TIME(시간), TIMESTAMP(날짜시간)
    // 요즘은 (JAVA8) @TemporalType를 쓰지않고 LocalDate, LocalDateTime을 그냥 쓰면 된다.
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // 요즘은 (JAVA8) @TemporalType를 쓰지않고 LocalDate, LocalDateTime을 그냥 쓰면 된다.
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;

    // String, char[], java.sql.CLOB -> (CLOB)
    // byte[], java.sql.BLOB -> (BLOB)
    @Lob
    private String description;

    // DB의 컬럼이랑 매핑하지 않음, 메모리에서만 사용
    @Transient
    private int temp;

    /**
     * JPA 내부적으로 리플렉션 같은 것을 사용하기 때문에 그냥 생성자를 쓰면 에러 발생
     * 기본 생성자가 하나 있어야함. public 또는 protected
     * final 클래스, enum, interface, inner 클래스 사용 안됨
     * 저장할 필드에 final 사용 안됨
     */
    public Member() {
    }

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

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
