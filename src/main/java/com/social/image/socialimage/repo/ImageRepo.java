package com.social.image.socialimage.repo;

import com.social.image.socialimage.domain.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends PagingAndSortingRepository<Image, Long> {
    public Image findByName(String name);
}
