package com.imagepop.fileupload;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping(value = "/com/imagepop/fileupload/")
public class FileUploadController {
    private final AtomicLong fileId = new AtomicLong();

    private String fileUploadPath;;

    public FileUploadController() {
        fileUploadPath = System.getProperty("user.dir") + "/uploads";
        File uploadDir = new File(fileUploadPath);
        if (!uploadDir.exists()) {
            System.out.println("Creating uploads directory");
            boolean success = uploadDir.mkdir();
            if (!success) {
                System.err.println("Unable to create upload directory");
            }
        }
    }

    @CrossOrigin
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public
    @ResponseBody
    Start getFileId() {
        // Currently just use a member variable to get the next available file number
        return new Start(fileId.incrementAndGet());
    }

    @CrossOrigin
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    UploadedFile handleFileUpload(@RequestParam("fileId") long fileId,
                                  @RequestParam("image") MultipartFile image,
                                  RedirectAttributes redirectAttributes) {
        String uploadPath = fileUploadPath + "/" + image.getName() + fileId;
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath)));
            FileCopyUtils.copy(image.getInputStream(), stream);
            stream.close();
            redirectAttributes.addFlashAttribute("message", "Success uploading file " + image.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadedFile(fileId, uploadPath);
    }
}