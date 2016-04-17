package com.imagepop.fileupload;

import java.util.List;

/**
 * Created by matt on 4/16/16.
 */

public interface ImageService {

    Image initializeNewImage(String email, String name);

    Image acceptFileUpload(String email, Long imageId, byte[] bytes);

    String getUploaded(Image image);

    String getPreview(Image image);

    String[] getPopped(Image image);

    String getEnhancement(Image image);

    List<Image> getUserImages(String email);

}
