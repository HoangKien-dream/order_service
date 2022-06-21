package com.example.orderservice.specification;

import com.example.orderservice.entity.OrderDetail;
import com.example.orderservice.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;

public class OrderSpecification implements Specification<Order> {
    private SearchCriteria searchCriteria;

    public OrderSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true);
        switch (searchCriteria.getOperator()) {
            case EQUALS:
                return criteriaBuilder.equal(
                        root.get(searchCriteria.getKey()),
                        searchCriteria.getValue());
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case GREATER_THAN_OR_EQUALS:
                if (root.get(searchCriteria.getKey()).getJavaType() == LocalDate.class) {
                    return criteriaBuilder.greaterThanOrEqualTo(
                            root.get(searchCriteria.getKey()), (LocalDate) searchCriteria.getValue());
                }
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case LESS_THAN:
                return criteriaBuilder.lessThan(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case LESS_THAN_OR_EQUALS:
                if (root.get(searchCriteria.getKey()).getJavaType() == LocalDate.class) {
                    return criteriaBuilder.lessThanOrEqualTo(
                            root.get(searchCriteria.getKey()), (LocalDate) searchCriteria.getValue());
                }
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get(searchCriteria.getKey()),
                        String.valueOf(searchCriteria.getValue()));
            case JOIN:
                Join<OrderDetail, Product> orderDetailProductJoin = root.join("orderDetails").join("product");
                // hoặc tìm trong bảng product bản ghi có name giống với giá trị
                return criteriaBuilder.like(orderDetailProductJoin.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
//            case JOIN_PHONE:
//                Join<User, Order> userOrderJoin = root.join("user");
//                return criteriaBuilder.like(userOrderJoin.get("phone"), "%" + searchCriteria.getValue() + "%");
        }
        return null;
    }
}
