package com.jumper.jit.aspect.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/*自定义文件验证器:在hibernate和spring分别执行一次!!!!*/
public class MultipartFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private ValidFile validFile;
    @Autowired
    private MultipartProperties multipartProperties;

    private void addMessage(String message, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        //逃避hibernate验证
        if (file == null && this.validFile.escapeFromHibernate()) return true;
        if (!validFile.allowEmpty()) {
            if (file.isEmpty()) {
                addMessage("文件不能为空", context);
                return false;
            }
        }
        String[] endWiths = validFile.endWith();
        if (endWiths.length > 0) {
            String fileName = file.getOriginalFilename();
            if (validFile.ignoreCase()) fileName = fileName.toLowerCase();
            if (Arrays.stream(endWiths).noneMatch(fileName::endsWith)) {
                addMessage("文件类型只支持:" + Arrays.toString(endWiths), context);
                return false;
            }
        }

        long sysUploadAllowedMaxFileSize = multipartProperties.getMaxFileSize().toBytes();
        long maxAllowedFileSize = sysUploadAllowedMaxFileSize > validFile.max() ? validFile.max() : sysUploadAllowedMaxFileSize;
        if (file.getSize() > maxAllowedFileSize) {
            addMessage("超过最大文件限制:" + (maxAllowedFileSize) / (1024 * 1024) + "M", context);
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.validFile = constraintAnnotation;
    }
}
