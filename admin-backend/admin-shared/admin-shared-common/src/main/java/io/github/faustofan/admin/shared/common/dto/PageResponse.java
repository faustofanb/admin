package io.github.faustofan.admin.shared.common.dto;

import java.util.List;

/**
 * Generic pagination response DTO.
 *
 * @param totalElements total number of elements across all pages
 * @param totalPages    total number of pages
 * @param currentPage   current page number (0-based)
 * @param pageSize      number of items per page
 * @param items         list of items on the current page
 * @param <T>           type of items in the list
 */
public record PageResponse<T>(
            long totalElements,
            long totalPages,
            long currentPage,
            long pageSize,
            List<T> items
) {
}

