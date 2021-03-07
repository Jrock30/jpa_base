package valuetype;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 임베디드 타입
 *  - 임베디드 타입은 엔티티의 값일 뿐이다.
 *  - 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
 *  - 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
 *  - 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래 스의 수가 더 많음
 *
 *
 * 값 타입 (임베디드)
 * @Embeddable 은 정의할 때 적고
 * @Embedded 는 가져다 쓰는 곳에 적는다. 한개는 생략가능
 * 기본 생성자는 만들어 주어야한다.
 *
 * 현업에서 value 타입을 많이 쓰지는 않으나 쓸 때가 있다.
 * 공통으로 관리할 수 있다.(코드, 용어등)
 *
 * @MappedSuperclass 로 상속 을 받는 것 보단
 * @Embeddable 로 위임 하는 것이 더 효율적이다. 상속은 한번 밖에 되지 않는 특성이 있다.
 * 그러나 모든 엔티티에서 공통으로 사용하는 컬럼들이 있을시에는 @MappedSuperclass 를 권장
 *
 * 객체 타입의 한계
 *   - 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함
 *   - 부작용(side effect) 발생
 *   - 값 타입의 실제 인스턴스인 값을 공유하는 것은 위험
 *   - 대신 값(인스턴스)를 복사해서 사용 new Address();
 *   - 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본 타입이 아니라 객체 타입이다.
 *   - 자바 기본 타입에 값을 대입하면 값을 복사한다.
 *   - 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.
 *
 * 불변객체 사용 !!! 값 타입은 불변으로 만들어야한다 !!!!
 *   - 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
 *   - 값 타입은 불변 객체(immutable object)로 설계해야함
 *   - 불변 객체: 생성 시점 이후 절대 값을 변경할 수 없는 객체
 *   - 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨
 *   - 참고: Integer, String은 자바가 제공하는 대표적인 불변 객체
 *
 * 값 타입의 비교
 *   - 동일성(identity) 비교: 인스턴스의 참조 값을 비교, == 사용
 *   - 동등성(equivalence) 비교: 인스턴스의 값을 비교, equals() 사용
 *   - 값 타입은 a.equals(b)를 사용해서 동등성 비교를 해야 함
 *   - 값 타입의 equals() 메소드를 적절하게 재정의(주로 모든 필드 사용)
 *
 * 공유할 사항이 많을 것 같으면 @Embeddable 보다는 @Entity를 사용.
 * 값 타입은 따로 PK를 구성하지 않고 값들과 FK를 가지고 PK를 구성한다.
 *
 *
 */
@Embeddable
public class Address {

    private String city;
    private String street;

    @Column(name = "ZIPCODE")
    private String zipcode;

    // 이렇게 다른 Entity도 들어올 수 있다.
    private Member member;

    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
