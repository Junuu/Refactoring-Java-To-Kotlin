package anthill.Anthill.db.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter
public class Address{
    private String zipCode;
    private String streetNameAddress;
    private String detailAddress;

    @Builder
    public Address(String zipCode, String streetNameAddress, String detailAddress) {
        this.zipCode = zipCode;
        this.streetNameAddress = streetNameAddress;
        this.detailAddress = detailAddress;
    }
}
