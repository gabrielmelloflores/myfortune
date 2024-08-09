package com.gabrielflores.myfortune.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 * @param <T> Data type
 */
@AllArgsConstructor
@Setter
@Getter
public class PageResponse<T> {

    private List<T> data;

    private Long totalPages;
    private Long totalElements;

    private Integer size;
    private Integer page;
    private Integer elements;

    private Boolean empty;
    private Boolean first;
    private Boolean last;

    public PageResponse(Page<T> page) {
        this.data = page.getContent();

        this.totalPages = Long.valueOf(page.getTotalPages());
        this.totalElements = page.getTotalElements();

        this.size = page.getSize();
        this.page = page.getNumber();
        this.elements = page.getNumberOfElements();

        this.empty = page.isEmpty();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
