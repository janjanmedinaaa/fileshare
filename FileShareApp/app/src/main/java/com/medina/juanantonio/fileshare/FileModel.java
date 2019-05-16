package com.medina.juanantonio.fileshare;

public class FileModel {
    String fileName, fileType, fileLMD, fileIcon, fileSize, fileSource;
    Boolean isFolder;

    public FileModel(String fileName, String fileType, String fileLMD, String fileSize, String fileSource, Boolean isFolder) {
        this.fileName = fileName;
        this.fileType = fileType.toUpperCase();
        this.fileLMD = fileLMD;
        this.fileSize = fileSize;
        this.fileSource = fileSource;
        this.isFolder = isFolder;
        this.fileIcon = getFileIconImage(fileType.toUpperCase(), isFolder);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLMD() {
        return fileLMD;
    }

    public void setFileLMD(String fileLMD) {
        this.fileLMD = fileLMD;
    }

    public String getFileIcon() {
        return fileIcon;
    }

    public void setFileIcon(String fileIcon) {
        this.fileIcon = fileIcon;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Boolean folder) {
        isFolder = folder;
    }

    public String getFileSource() {
        return fileSource;
    }

    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
    }

    public String getFileIconImage(String fileType, Boolean isFolder) {
        String fileIcon;

        if(isFolder) return "ic_folder_white";

        switch(fileType) {
            case "APK":
                fileIcon = "ic_android_white";
                break;
            case "JPEG":
                fileIcon = "ic_image_white";
                break;
            case "JPG":
                fileIcon = "ic_image_white";
                break;
            case "PNG":
                fileIcon = "ic_image_white";
                break;
            case "PDF":
                fileIcon = "ic_pdf_white";
                break;
            case "MP3":
                fileIcon = "ic_music_white";
                break;
            case "MPEG":
                fileIcon = "ic_videos_white";
                break;
            case "MP4":
                fileIcon = "ic_videos_white";
                break;
            case "AVI":
                fileIcon = "ic_videos_white";
                break;
            case "GIF":
                fileIcon = "ic_gif_white";
                break;
            case "HTML":
                fileIcon = "ic_webpage_white";
                break;
            case "PHP":
                fileIcon = "ic_webpage_white";
                break;
            case "PREVIOUS":
                fileIcon = "ic_back_white";
                break;
            case "ZIP":
                fileIcon = "ic_zip_white";
                break;
            case "TXT":
                fileIcon = "ic_txt_white";
                break;
            case "SVG":
                fileIcon = "ic_svg_white";
                break;
            case "PPT":
                fileIcon = "ic_ppt_white";
                break;
            case "PPTX":
                fileIcon = "ic_ppt_white";
                break;
            case "DOC":
                fileIcon = "ic_docx_white";
                break;
            case "DOCX":
                fileIcon = "ic_docx_white";
                break;
            case "XLSX":
                fileIcon = "ic_excel_white";
                break;
            case "CSV":
                fileIcon = "ic_excel_white";
                break;
            case "XLS":
                fileIcon = "ic_excel_white";
                break;
            case "XML":
                fileIcon = "ic_xml_white";
                break;
            case "JS":
                fileIcon = "ic_js_white";
                break;
            case "JSON":
                fileIcon = "ic_json_white";
                break;
            case "SQL":
                fileIcon = "ic_sql_white";
                break;
            case "MD":
                fileIcon = "ic_md_white";
                break;
            default:
                fileIcon = "ic_default_file_white";

        }

        return fileIcon;
    }
}
