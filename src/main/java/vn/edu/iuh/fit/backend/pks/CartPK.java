package vn.edu.iuh.fit.backend.pks;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CartPK implements Serializable {
    private long customer;
    private long product;
}
