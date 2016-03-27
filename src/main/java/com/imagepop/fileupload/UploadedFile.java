package com.imagepop.fileupload;

/**
 * Created by ananth on 3/20/16.
 */
class UploadedFile {
    private final long fileId;
    private final String imagePath;
    private String status;

    UploadedFile(long fileId, String imagePath) {
        this.fileId = fileId;
        this.imagePath = imagePath;
        this.status = "PROCESSING";
    }

    public long getFileId() {
        return fileId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getStatus() {
        return status;
    }
}
