package com.xp.store.utils.Pagination;

import com.xp.store.dto.ClienteExpoDTO;
import com.xp.store.model.Cliente;
import com.xp.store.model.Produto;
import org.springframework.data.jpa.domain.Specification;

public class OfMeasureSpecification {
    public static Specification<Cliente> withCustomFilter(String filterBy, String filter) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(filterBy)),
                    "%" + filter.toLowerCase() + "%"
            );
        };
    }
    public static Specification<Cliente> withCustomFilterClient(String filterBy, String filter) {
        return (root, query, criteriaBuilder) -> {
             return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(filterBy)),
                   "%" + filter.toLowerCase() + "%"
             );
        };
    }
    public static Specification<Produto> withCustomFilterProduto(String filterBy, String filter) {
        return (root, query, criteriaBuilder) -> {
             return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(filterBy)),
                   "%" + filter.toLowerCase() + "%"
             );
        };
    }
}