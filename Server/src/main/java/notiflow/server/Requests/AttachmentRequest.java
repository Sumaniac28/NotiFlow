package notiflow.server.Requests;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Arrays;

public class AttachmentRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultipartFile[] images;
    private MultipartFile[] pdfs;
    private MultipartFile[] docs;
    private MultipartFile[] other;

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }

    public MultipartFile[] getPdfs() {
        return pdfs;
    }

    public void setPdfs(MultipartFile[] pdfs) {
        this.pdfs = pdfs;
    }

    public MultipartFile[] getDocs() {
        return docs;
    }

    public void setDocs(MultipartFile[] docs) {
        this.docs = docs;
    }

    public MultipartFile[] getOther() {
        return other;
    }

    public void setOther(MultipartFile[] other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "AttachmentRequest{" +
                "images=" + Arrays.toString(images) +
                ", pdfs=" + Arrays.toString(pdfs) +
                ", docs=" + Arrays.toString(docs) +
                ", other=" + Arrays.toString(other) +
                '}';
    }
}
