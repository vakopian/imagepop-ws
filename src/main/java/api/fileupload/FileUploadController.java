package api.fileupload;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class FileUploadController {
    private final String API_PATH = "/api/fileupload/";
    private final String FILE_UPLOAD_PATH = "/dev/null";
    private final AtomicLong fileId = new AtomicLong();

    @RequestMapping(value = API_PATH + "start", method = RequestMethod.POST)
    public
    @ResponseBody
    Start getFileId() {
        return new Start(fileId.incrementAndGet()); // Currently just use a member variable to get the next available
        // file number.
    }

    @RequestMapping(value = API_PATH + "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    UploadedFile handleFileUpload(@RequestParam("fileId") long fileId,
                                  @RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        String uploadPath = FILE_UPLOAD_PATH + "/" + file.getName() + fileId;
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath)));
            FileCopyUtils.copy(file.getInputStream(), stream);
            stream.close();
            redirectAttributes.addFlashAttribute("message", "Success uploading file " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadedFile(fileId, uploadPath);
    }
}