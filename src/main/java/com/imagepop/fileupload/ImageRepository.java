package com.imagepop.fileupload;

import com.imagepop.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by matt on 4/16/16.
 */
public interface ImageRepository extends CrudRepository<Image, Long> {

    List<Image> findByUser(User user);

}
