package com.imagepop.fileupload;

import com.imagepop.domain.User;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by matt on 4/16/16.
 */

public interface ImageService {

    Image initializeNewImage(String email);

    Image acceptFileUpload(String email, Long imageId, byte[] bytes);

    String getUploaded(Image image);

    String getPreview(Image image);

    String[] getPopped(Image image);

    String getEnhancement(Image image);

}
