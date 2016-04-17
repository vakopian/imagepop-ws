package com.imagepop.fileupload;

import com.imagepop.domain.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/com/imagepop/fileupload/")
public class FileUploadController {

    @Autowired
    protected ImageService service;

    @CrossOrigin
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public @ResponseBody ImageResponse startFileUpload(@RequestParam("name") String name) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            Image image = service.initializeNewImage(((CurrentUser) auth.getPrincipal()).getUsername(), name);
            if (image != null) {
                return ImageResponse.fromImage(image, service);
            }
        }
        throw new Exception("Unable to initialize new image.");
    }

    @CrossOrigin
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public @ResponseBody
    ImageResponse handleFileUpload(@RequestParam("imageId") long imageId,
                                   @RequestParam("image") MultipartFile image,
                                   RedirectAttributes redirectAttributes) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            Image img = service.acceptFileUpload(((CurrentUser) auth.getPrincipal()).getUsername(), imageId, image.getBytes());
            if (img != null) {
                redirectAttributes.addFlashAttribute("message", "Success uploading file " + image.getName());
                return ImageResponse.fromImage(img, service);
            }
        }
        throw new Exception("Unable to upload image.");
    }

    @CrossOrigin
    @RequestMapping(value = "get_images", method = RequestMethod.POST)
    public @ResponseBody
    List<ImageResponse> getUserImages() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            List<Image> images = service.getUserImages(((CurrentUser) auth.getPrincipal()).getUsername());
            List<ImageResponse> responses = new ArrayList<>();
            for (Image image : images) {
                responses.add(ImageResponse.fromImage(image, service));
            }
            return responses;
        }
        throw new Exception("Unable to get uploaded images.");
    }
}
