package com.example.crosspuzzleserver.service;


import com.example.crosspuzzleserver.domain.Category;
import com.example.crosspuzzleserver.repository.CategoryRepository;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.service.dto.CategoryDto;
import com.example.crosspuzzleserver.service.spi.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CrossWordsRepository crossWordsRepository;

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(
                category -> categoryToCategoryDto(category, crossWordsRepository.countByCategories(category))).toList();
    }

    private CategoryDto categoryToCategoryDto(Category category, long count) {
        return CategoryDto.builder()
                .id(category.getId().toHexString())
                .name(category.getName())
                .koreanName(category.getKoreanName())
                .puzzleCount(count)
                .build();
    }
}
