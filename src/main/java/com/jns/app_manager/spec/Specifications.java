package com.jns.app_manager.spec;

import com.jns.app_manager.entity.CarePlan;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class Specifications {

    public static Specification<CarePlan> byUserId(UUID id) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), id);
    }

    public static Specification<CarePlan> byClientId(UUID id) {
        return (root, query, cb) -> cb.equal(root.get("client").get("id"), id);
    }

    public static Specification<CarePlan> byUserOrClientId(UUID id) {
        return Specification.where(byUserId(id)).or(byClientId(id));
    }

    public static Specification<CarePlan> titleContainsIgnoreCase(String title) {
        return (root, query, cb) -> cb.like(
                cb.lower(root.get("title")),
                "%" + title.toLowerCase() + "%"
        );
    }
}