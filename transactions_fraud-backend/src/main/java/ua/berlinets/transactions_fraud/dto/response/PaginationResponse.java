package ua.berlinets.transactions_fraud.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse<T> {
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private List<T> content;
}
