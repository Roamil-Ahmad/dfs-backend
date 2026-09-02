package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadDocumentRequest {
    private String mobileNumber;
    private String accountLevelCode;
    private List<Document>  documents;

    /**
     * Documents received as real multipart parts.
     *
     * <p>Preferred over {@link #documents} when present. Base64 in a JSON body costs roughly ten
     * times the file size in heap once it has been buffered, parsed into UTF-16 strings, logged
     * and decoded; a multipart part is streamed and decoded once.</p>
     *
     * <p>Not part of the JSON contract, so it is never serialised.</p>
     */
    @JsonIgnore
    private List<MultipartFile> files;

}
