package com.bnwzy.smartclassesspringbootweb.pojo.dto;

public class DifyReportCommentDTO {
    private DifyReportCommentDTO.Inputs inputs;
    private String response_mode;
    private String user;

    public DifyReportCommentDTO.Inputs getInputs() {
        return inputs;
    }

    public void setInputs(DifyReportCommentDTO.Inputs inputs) {
        this.inputs = inputs;
    }

    public String getResponse_mode() {
        return response_mode;
    }

    public void setResponse_mode(String response_mode) {
        this.response_mode = response_mode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static class Inputs {
        private DifyReportCommentDTO.FileInput studentReport;
        private DifyReportCommentDTO.FileInput moBan;

        public FileInput getMoBan() {
            return moBan;
        }

        public void setMoBan(FileInput moBan) {
            this.moBan = moBan;
        }

        public DifyReportCommentDTO.FileInput getStudentReport() {
            return studentReport;
        }

        public void setStudentReport(DifyReportCommentDTO.FileInput studentReport) {
            this.studentReport = studentReport;
        }
    }

    public static class FileInput {
        private String type;
        private String transfer_method;
        private String url;
        private String upload_file_id;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTransfer_method() {
            return transfer_method;
        }

        public void setTransfer_method(String transfer_method) {
            this.transfer_method = transfer_method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUpload_file_id() {
            return upload_file_id;
        }

        public void setUpload_file_id(String upload_file_id) {
            this.upload_file_id = upload_file_id;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "type='" + type + '\'' +
                    ", transfer_method='" + transfer_method + '\'' +
                    ", url='" + url + '\'' +
                    ", upload_file_id='" + upload_file_id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DifyGenerateQuestionDTO{" +
                "inputs=" + inputs +
                ", response_mode='" + response_mode + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
