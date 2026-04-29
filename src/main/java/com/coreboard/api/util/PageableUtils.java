package com.coreboard.api.util;

import com.coreboard.api.domain.enums.SortBy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable create(
            Integer page,
            Integer size) {


        return PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 18
        );
    }

    public static Pageable createWithSort(
            Integer page,
            Integer size,
            SortBy sortBy,
            String defaultValue) {

        Sort sort = resolveSort(sortBy, defaultValue);

        return PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 18,
                sort
        );
    }

    private static Sort resolveSort(SortBy sortBy, String defaultValue) {
        if (sortBy == null) {
            return Sort.by(Sort.Direction.DESC, defaultValue);
        }

        return switch (sortBy) {
            case VALUE_ASC -> Sort.by(Sort.Direction.ASC, defaultValue);
            case VALUE_DEC   -> Sort.by(Sort.Direction.DESC, defaultValue);
            case MOST_RECENT -> Sort.by(Sort.Direction.DESC, "createdAt");
            case OLDER  -> Sort.by(Sort.Direction.ASC, "createdAt");
            case TITLE_ASC -> Sort.by(Sort.Direction.ASC, "serviceTitle");
        };
    }
}