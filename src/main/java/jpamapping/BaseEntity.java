package jpamapping;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @MappedSuperclass
 * 맵핑정보만 등록해주는 수퍼클래스
 * 필요한 곳에서 extends 하면 된다.
 * 테이블이 생성되지는 않지만(엔티티가 아님) 필드(컬럼)들만 상속받아 객체지향적으로 사용할 수 있다.
 * 속성만 공유(컬럼, 필드)
 * @Column 도 넣을 수 있다.
 *
 * abstract 클래스 권장
 *
 * entity클래스에서의 extends는 @Entity가 있거나 @MappedSuperclass 가 있어야 상속가능
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "INSERT_MEMBER")
    private String createdBy;
    private LocalDateTime createDate;
    @Column(name = "UPDATE_MEMBER")
    private String lastModifiedBy;
    private LocalDateTime lastmodifiedBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastmodifiedBy() {
        return lastmodifiedBy;
    }

    public void setLastmodifiedBy(LocalDateTime lastmodifiedBy) {
        this.lastmodifiedBy = lastmodifiedBy;
    }
}
