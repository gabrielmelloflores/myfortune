package com.gabrielflores.myfortune.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Getter
@Setter
@AllArgsConstructor
public class CustomMultipartFile implements MultipartFile {

    private byte[] file;

    @Override
    public String getName() {
        return "customPicture.jpg";
    }

    @Override
    public String getOriginalFilename() {
        return "customPicture.jpg";
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return file == null || file.length == 0;
    }

    @Override
    public long getSize() {
        return file.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(file);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(file);
        }
    }
}
