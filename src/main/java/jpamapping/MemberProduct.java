package jpamapping;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProduct {

    @Id @GeneratedValue
    private Long id;

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
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int count;

    private int price;

    private LocalDateTime orderDateTime;
}
