package com.example.crosspuzzleserver.service.spi;

import com.example.crosspuzzleserver.service.dto.CategoryDto;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategories();

}
