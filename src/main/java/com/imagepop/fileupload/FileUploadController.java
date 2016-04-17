package com.imagepop.fileupload;

import com.imagepop.domain.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/com/imagepop/fileupload/")
public class FileUploadController {

    @Autowired
    protected ImageService service;

    @CrossOrigin
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public @ResponseBody ImageResponse startFileUpload() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            Image image = service.initializeNewImage(((CurrentUser) auth.getPrincipal()).getUsername());
            if (image != null) {
                return ImageResponse.fromImage(image, service);
            }
        }
        throw new Exception("Unable to initialize new image.");
    }

    @CrossOrigin
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public @ResponseBody
    ImageResponse handleFileUpload(@RequestParam("fileId") long fileId,
                                   @RequestParam("image") MultipartFile image,
                                   RedirectAttributes redirectAttributes) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            Image img = service.acceptFileUpload(((CurrentUser) auth.getPrincipal()).getUsername(), fileId, image.getBytes());
            if (img != null) {
                redirectAttributes.addFlashAttribute("message", "Success uploading file " + image.getName());
                return ImageResponse.fromImage(img, service);
            }
        }
        throw new Exception("Unable to accept image upload.");
    }
}
