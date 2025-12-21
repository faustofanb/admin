package io.github.faustofan.admin.shared.common.dto;

/**
 * DTO for pagination requests.
 *
 * @param page          the page number (0-based)
 * @param size          the number of items per page
 * @param sortBy        the field to sort by
 * @param sortDirection the direction of sorting ("asc" or "desc")
 */
public record PageRequestDto(
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection
) {
}
