package api.fileupload;

/**
 * Created by ananth on 3/20/16.
 */

class Start {
    private final long fileId;

    Start(long fileId) {
        this.fileId = fileId;
    }

    public long getFileId() {
        return fileId;
    }
}
