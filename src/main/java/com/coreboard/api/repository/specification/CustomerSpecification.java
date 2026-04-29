package com.coreboard.api.repository.specification;

import com.coreboard.api.domain.entity.Customer;
import com.coreboard.api.util.FilterUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CustomerSpecification {

    public static Specification<Customer> byNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("name")), FilterUtils.like(name));
        };
    }

    public static Specification<Customer> byPhoneLike(String phone) {
        return (root, query, cb) -> {
            if (phone == null) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("phone")), FilterUtils.like(phone));
        };
    }

    public static Specification<Customer> byEmailExact(String email) {
        return (root, query, cb) -> {
            if (email == null) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("email")), FilterUtils.exact(email));
        };
    }

    public static Specification<Customer> byCpfExact(String cpf) {
        return (root, query, cb) -> {
            if (cpf == null) {
                return cb.conjunction();
            }

            return cb.like(cb.lower(root.get("cpf")), FilterUtils.exact(cpf));
        };
    }

    public static Specification<Customer> createdAtFrom(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
        };
    }

    public static Specification<Customer> createdAtTo(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }


}
