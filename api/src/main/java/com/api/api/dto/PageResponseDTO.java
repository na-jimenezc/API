package com.api.api.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public class PageResponseDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private boolean first;
    private boolean last;

    public PageResponseDTO(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.first = page.isFirst();
        this.last = page.isLast();
    }

    public PageResponseDTO() {
    }

    public List<T> getContent() { 
        return content; 
    }
    
    public void setContent(List<T> content) { 
        this.content = content; 
    }

    public int getTotalPages() { 
        return totalPages; 
    }
    
    public void setTotalPages(int totalPages) { 
        this.totalPages = totalPages; 
    }

    public long getTotalElements() { 
        return totalElements; 
    }
    
    public void setTotalElements(long totalElements) { 
        this.totalElements = totalElements; 
    }

    public int getSize() { 
        return size; 
    }
    
    public void setSize(int size) { 
        this.size = size; 
    }

    public int getNumber() { 
        return number; 
    }
    
    public void setNumber(int number) { 
        this.number = number; 
    }

    public boolean isFirst() { 
        return first; 
    }
    
    public void setFirst(boolean first) { 
        this.first = first; 
    }

    public boolean isLast() { 
        return last; 
    }
    
    public void setLast(boolean last) { 
        this.last = last; 
    }
}