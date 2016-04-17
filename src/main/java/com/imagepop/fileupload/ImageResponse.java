package com.imagepop.fileupload;

/**
 * Created by ananth on 3/20/16.
 */
class ImageResponse {

    private final long imageId;
    private final Image.Status status;
    private final String preview;
    private final String original;
    private final String[] popped;
    private final String enhancement;

    ImageResponse(long imageId, Image.Status status, String preview, String original, String[] popped, String enhancement) {
        this.imageId = imageId;
        this.status = status;
        this.preview = preview;
        this.original = original;
        this.popped = popped;
        this.enhancement = enhancement;
    }

    public static ImageResponse fromImage(Image image, ImageService service) {
        return new ImageResponse(image.getId(), image.getStatus(), service.getPreview(image), service.getUploaded(image), service.getPopped(image), service.getEnhancement(image));
    }

    public long getImageId() {
        return imageId;
    }

    public Image.Status getStatus() {
        return status;
    }

    public String getPreview() {
        return preview;
    }

    public String getOriginal() {
        return original;
    }

    public String[] getPopped() {
        return popped;
    }

    public String getEnhancement() {
        return enhancement;
    }
}
