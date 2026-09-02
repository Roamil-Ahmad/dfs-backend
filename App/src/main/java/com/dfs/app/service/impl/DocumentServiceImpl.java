package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.Document;
import com.dfs.app.dto.DocumentResponse;
import com.dfs.app.dto.UploadDocumentRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.model.LkpDocumentType;
import com.dfs.app.model.TblAccountLevel;
import com.dfs.app.model.TblAppUser;
import com.dfs.app.model.TblDocument;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.DocumentService;
import com.dfs.app.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DocumentServiceImpl extends HelperClass implements DocumentService {
    @Value("${file.upload.path}")
    private String parentDirectory;
    @Value("${doc.path}")
    private String docPath;
    @Autowired
    private TblAppUserRepo tblAppUserRepo;
    @Autowired
    private TblAccountLevelRepo tblAccountLevelRepo;
    @Autowired
    private TblDocumentRepo tblDocumentRepo;
    @Autowired
    private LkpDocumentTypeRepo lkpDocumentTypeRepo;
    @Autowired
    private CommonService commonService;

    @Override
    public HashMap<String, Object> uploadDocument(UploadDocumentRequest uploadDocumentRequest, Request request, BigDecimal userId) {
        TblAppUser tblAppUser = tblAppUserRepo.findByAccountNo(uploadDocumentRequest.getMobileNumber());
        if (tblAppUser == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.USER_NOT_FOUND.getResponseCode());
        }
        TblAccountLevel tblAccountLevel = tblAccountLevelRepo.findByAccountLevelCode(uploadDocumentRequest.getAccountLevelCode());
        if (tblAccountLevel == null) {
            throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_LEVEL_NOT_FOUND.getResponseCode());

        }
        // Real multipart parts win when the caller sent them; base64 stays supported so existing
        // clients keep working during the rollout.
        List<MultipartFile> files = uploadDocumentRequest.getFiles() != null
                        && !uploadDocumentRequest.getFiles().isEmpty()
                ? uploadDocumentRequest.getFiles()
                : convertBase64FilesToMultiPart(uploadDocumentRequest.getDocuments());
        DocumentResponse documentResponse = uploadFiles(tblAppUser, tblAccountLevel, files, userId);
        if(documentResponse!=null){
            return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), documentResponse);
        }else {
            return commonService.getResponse(GenericResponseCode.FAILED_TO_UPLOAD_DOC.getResponseCode(), documentResponse);
        }

    }

    private List<MultipartFile> convertBase64FilesToMultiPart(List<Document> documents) {
        List<MultipartFile> multipartFiles=new ArrayList<>();
        if (documents == null) {
            return multipartFiles;
        }
        documents.forEach(d->{
            multipartFiles.add(Base64MultipartFile.fromBase64(d.getBase64(), d.getDocFileName(), d.getContentType()));
        });
        return multipartFiles;
    }

    private DocumentResponse uploadFiles(TblAppUser tblAppUser, TblAccountLevel tblAccountLevel, List<MultipartFile> files, BigDecimal userId) {
        DocumentResponse documentResponse = null;
        HashMap<String, Boolean> upload = new HashMap<>();
        String uploadDir = parentDirectory;
        for (MultipartFile file : files) {
            try {
                byte[] fileData = file.getBytes();

                if (isFileInfected(fileData)) {
                    throw new CustomDataNotFoundException(GenericResponseCode.FILE_IS_INFECTED.getResponseCode());

                } else {
                    Path parentPath = Paths.get(uploadDir);
                    Path newDirectoryPath = parentPath.resolve(String.valueOf(tblAppUser.getAppUserId()));
                    if (!Files.exists(newDirectoryPath)) {
                        Files.createDirectories(newDirectoryPath);
                    }

                    String originalFileName = file.getOriginalFilename();
                    String fileNameWithoutExtension = getFileName(originalFileName);
                    LkpDocumentType lkpDocumentType = lkpDocumentTypeRepo.findByDocumentTypeCode(fileNameWithoutExtension);
                    String fileNameToSave = file.getOriginalFilename();
                    if (lkpDocumentType != null) {


                        fileNameToSave = getFileNameWithLevel(tblAccountLevel.getAccountLevelCode(), fileNameToSave);
                        if (fileNameToSave != null) {
                            String fileExtension = fileNameToSave.substring(fileNameToSave.lastIndexOf("."));
                            String filePath = newDirectoryPath + File.separator + fileNameToSave;
                            file.transferTo(new File(filePath));
                            String viewPath = getDocumentViewPath(tblAppUser.getAppUserId(), fileNameToSave);
                            TblDocument tblDocument = tblDocumentRepo.findByDocumentTypeIdAndAppUserIdAndAccountLevelId(lkpDocumentType.getDocumentTypeId(),tblAppUser.getAppUserId(),tblAccountLevel.getAccountLevelId());
                            if(tblDocument!=null){
                                tblDocument.setIsActive("N");
                                tblDocument.setLastupdatedate(new Date());
                                tblDocument.setLastupdateuser(userId);
                                tblDocumentRepo.saveAndFlush(tblDocument);
                            }
                            tblDocument=new TblDocument();
                            tblDocument.setCreatedate(new Date());
                            tblDocument.setCreateuser(userId);
                            tblDocument.setDocumentExt(fileExtension);
                            filePath=filePath.replace(docPath,"");
                            filePath = filePath.replace("\\", "/");
                            tblDocument.setDocumentPath(filePath);
                            tblDocument.setIsActive("Y");
                            tblDocument.setTblAccountLevel(tblAccountLevel);
                            tblDocument.setLkpDocumentType(lkpDocumentType);
                            tblDocument.setTblAppUser(tblAppUser);
                            tblDocumentRepo.saveAndFlush(tblDocument);
                            upload.put(originalFileName, true);
                        }
                    } else {
                        upload.put(originalFileName, false);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();

                upload = null;
            } finally {
                if (upload != null && !upload.isEmpty()) {
                    documentResponse=new DocumentResponse();
                    documentResponse.setDocStatus(upload);
                }
            }

        }
        return documentResponse;
    }

    private boolean isFileInfected(byte[] fileData) {
        // Convert byte array to String for simple pattern matching
        String content = new String(fileData);

        // Check if the content contains the word "virus"
        return content.toLowerCase().contains("virus");
    }

    private String getFileNameWithLevel(String accountLevelCode, String fileName) {

        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        fileName = accountLevelCode + "_" + date + "_" + fileName;
        return fileName;
    }

    private String getFileName(String originlName) {
        // Get the full filename
        if (originlName != null && originlName.contains(".")) {
            return originlName.substring(0, originlName.lastIndexOf('.')); // Extract filename without extension

        } else {
            return null;
        }
    }

    private String getDocumentViewPath(long appUserId, String fileNameToSave) {
        return parentDirectory + appUserId + Constants.BACK_SLASH + fileNameToSave;

    }

}
