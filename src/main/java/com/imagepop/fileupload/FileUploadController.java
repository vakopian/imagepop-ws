package com.imagepop.fileupload;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class FileUploadController {
    private final String API_PATH = "/com/imagepop/fileupload/";
    private final String FILE_UPLOAD_PATH = "/dev/null";
    private final AtomicLong fileId = new AtomicLong();

    @CrossOrigin
    @RequestMapping(value = API_PATH + "start", method = RequestMethod.POST)
    public
    @ResponseBody
    Start getFileId() {
        return new Start(fileId.incrementAndGet()); // Currently just use a member variable to get the next available
        // file number.
    }

    @CrossOrigin
    @RequestMapping(value = API_PATH + "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    UploadedFile handleFileUpload(@RequestParam("fileId") long fileId,
                                  @RequestParam("image") MultipartFile image,
                                  RedirectAttributes redirectAttributes) {
        String uploadPath = FILE_UPLOAD_PATH + "/" + image.getName() + fileId;
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