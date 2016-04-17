package com.imagepop.domain;

import com.imagepop.domain.User;
import com.imagepop.domain.UserRepository;
import com.imagepop.fileupload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 4/16/16.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final String ENHANCEMENT_NAME = "enhancement";

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ImageRepository imageRepo;

    private final String fileUploadPath;
    private final String previewPath;
    private final String poppedPath;

    public ImageServiceImpl() {
        String currentDir = System.getProperty("user.dir");
        fileUploadPath = currentDir + "/uploads";
        previewPath = currentDir + "/previews";
        poppedPath = currentDir + "/popped";
        String[] paths = {fileUploadPath, previewPath, poppedPath};
        for (String path : paths) {
            File f = new File(path);
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    System.err.println("Unable to create directory " + path);
                }
            }
        }
    }

    private static String imagePathToBase64(String path) throws IOException {
        return Base64Utils.encodeToString(FileCopyUtils.copyToByteArray(new File(path)));
    }

    private void thumbnail(com.imagepop.fileupload.Image i, int longestScaledDim) throws IOException {
        BufferedImage image = ImageIO.read(new File(getUploadPath(i)));
        double longestDim = Math.max(image.getWidth(), image.getHeight());
        double scale = longestScaledDim / longestDim;
        BufferedImage thumbnail = new BufferedImage((int) (scale * image.getWidth()), (int) (scale * image.getHeight()), image.getType());
        Graphics2D g = thumbnail.createGraphics();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);
        g.drawImage(image, scaleTransform, null);
        ImageIO.write(thumbnail, "jpeg", new File(getPreviewPath(i)));
    }

    private String getUploadPath(com.imagepop.fileupload.Image image) {
        return fileUploadPath + File.separator + image.getId();
    }

    private String getPreviewPath(com.imagepop.fileupload.Image image) {
        return previewPath + File.separator + image.getId();
    }

    public String getUploaded(com.imagepop.fileupload.Image image) {
        if (image.getStatus() == com.imagepop.fileupload.Image.Status.UPLOADED || image.getStatus() == com.imagepop.fileupload.Image.Status.POPPED) {
            try {
                return imagePathToBase64(getUploadPath(image));
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public String getPreview(com.imagepop.fileupload.Image image) {
        if (image.getStatus() == com.imagepop.fileupload.Image.Status.UPLOADED || image.getStatus() == com.imagepop.fileupload.Image.Status.POPPED) {
            try {
                return imagePathToBase64(getPreviewPath(image));
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public String[] getPopped(com.imagepop.fileupload.Image image) {
        if (image.getStatus() == com.imagepop.fileupload.Image.Status.POPPED) {
            try {
                File poppedDir = new File(poppedPath + File.separator + image.getId() + File.separator);
                List<String> images = new ArrayList<String>();
                for (File f : poppedDir.listFiles()) {
                    if (!f.getName().equals(ENHANCEMENT_NAME)) {
                        images.add(imagePathToBase64(f.getAbsolutePath()));
                    }
                }
                return images.toArray(new String[images.size()]);
            } catch (IOException e) {
                e.printStackTrace();
                return new String[0];
            }
        } else {
            return new String[0];
        }
    }

    public String getEnhancement(com.imagepop.fileupload.Image image) {
        if (image.getStatus() == com.imagepop.fileupload.Image.Status.POPPED) {
            try {
                return imagePathToBase64(poppedPath + File.separator + image.getId() + File.separator + ENHANCEMENT_NAME);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    @Transactional
    public com.imagepop.fileupload.Image initializeNewImage(String email) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            com.imagepop.fileupload.Image image = new com.imagepop.fileupload.Image(user);
            imageRepo.save(image);
            return image;
        }
        return null;
    }

    @Override
    @Transactional
    public com.imagepop.fileupload.Image acceptFileUpload(String email, Long imageId, byte[] bytes) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            com.imagepop.fileupload.Image image = imageRepo.findOne(imageId);
            if (image != null && image.getUser().equals(user)) {
                String uploadPath = getUploadPath(image);
                BufferedOutputStream stream = null;
                try {
                    stream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath)));
                    FileCopyUtils.copy(bytes, stream);
                    thumbnail(image, 250);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                image.setStatus(com.imagepop.fileupload.Image.Status.UPLOADED);
                // start poppping process
                imageRepo.save(image);
                return image;
            }
        }
        return null;
    }
}
