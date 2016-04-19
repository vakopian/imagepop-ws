package com.imagepop.fileupload;

/**
 * Created by ananth on 3/20/16.
 */
class ImageResponse {

    private final long imageId;
    private final String name;
    private final Image.Status status;
    private final String preview;
    private final String original;
    private final String[] popped;
    private final String enhancement;
    private final int size;

    ImageResponse(long imageId, String name, Image.Status status, String preview, String original, String[] popped, String enhancement, int size) {
        this.imageId = imageId;
        this.name = name;
        this.status = status;
        this.preview = preview;
        this.original = original;
        this.popped = popped;
        this.enhancement = enhancement;
        this.size = size;
    }

    public static ImageResponse fromImage(Image image, ImageService service) {
        return new ImageResponse(image.getId(), image.getName(), image.getStatus(), service.getPreview(image), service.getUploaded(image), service.getPopped(image), service.getEnhancement(image), image.getSize());
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

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
