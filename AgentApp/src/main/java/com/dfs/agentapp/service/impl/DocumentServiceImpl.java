package com.dfs.agentapp.service.impl;

import com.dfs.agentapp.controller.HelperClass;
import com.dfs.agentapp.dto.Document;
import com.dfs.agentapp.dto.DocumentResponse;
import com.dfs.agentapp.dto.UploadDocumentRequest;
import com.dfs.agentapp.dto.common.Request;
import com.dfs.agentapp.model.LkpDocumentType;
import com.dfs.agentapp.model.TblAccountLevel;
import com.dfs.agentapp.model.TblAppUser;
import com.dfs.agentapp.model.TblDocument;
import com.dfs.agentapp.repo.*;
import com.dfs.agentapp.service.CommonService;
import com.dfs.agentapp.service.DocumentService;
import com.dfs.agentapp.util.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class DocumentServiceImpl extends HelperClass implements DocumentService {
    @Value("${file.upload.path}")
    private String parentDirectory;
    @Value("${doc.path}")
    private String docPath;
    @Autowired
    private TblAccountRepo tblAccountRepo;
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
    @Autowired
    private AESencryption aeSencryption;


    @Override
    public HashMap<String, Object> uploadDocument(UploadDocumentRequest uploadDocumentRequest, Request request, BigDecimal userId){
       return uploadDocument(uploadDocumentRequest, request, userId, true);
    }
    @Override
    public HashMap<String, Object> uploadDocument(UploadDocumentRequest uploadDocumentRequest, Request request, BigDecimal userId, boolean isAgent) {
        TblAppUser tblAppUser = isAgent
            ? tblAppUserRepo.findByMobileNumber(aeSencryption.encryptwith256(uploadDocumentRequest.getMobileNumber()))
            : tblAppUserRepo.findByCustomerAccountNo(uploadDocumentRequest.getMobileNumber());;
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
//                            filePath = filePath.replace("\\", "/");
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
