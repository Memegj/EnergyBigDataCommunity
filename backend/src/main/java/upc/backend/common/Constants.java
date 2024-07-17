package upc.backend.common;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class Constants {
    public final static int TOKEN_LENGTH = 32;//token字段长度
    public final static String FILES_UPLOAD_DIC = getUploadDirectory("/upload/files");
    public final static String IMGS_UPLOAD_DIC = getUploadDirectory("/upload/images");
    public final static String DATASETS_UPLOAD_DIC = getUploadDirectory("/upload/datasets");
    public final static String CODE_UPLOAD_DIC = getUploadDirectory("/upload/code");
    public final static String LITERATURE_UPLOAD_DIC = getUploadDirectory("/upload/literature");
    public final static String VIDEO_UPLOAD_DIC = getUploadDirectory("/upload/video");
    public final static String PICTURE_UPLOAD_DIC = getUploadDirectory("/upload/picture");


    private static String getUploadDirectory(String filepath) {
        try {
            File targetPath = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!targetPath.exists()) {
                targetPath = new File("");
            }
            File uploadTarget = new File(targetPath.getAbsolutePath(), "static/"+filepath);
            if (!uploadTarget.exists()) {
                uploadTarget.mkdirs();
            }
            return uploadTarget.getAbsolutePath()+"/";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
