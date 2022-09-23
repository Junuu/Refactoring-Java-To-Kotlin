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
    private String detainAddress;

    @Builder
    public Address(String zipCode, String streetNameAddress, String detainAddress) {
        this.zipCode = zipCode;
        this.streetNameAddress = streetNameAddress;
        this.detainAddress = detainAddress;
    }
}
