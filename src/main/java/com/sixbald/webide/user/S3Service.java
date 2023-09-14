package com.sixbald.webide.user;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sixbald.webide.exception.ErrorCode;
import com.sixbald.webide.exception.GlobalException;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@NoArgsConstructor
public class S3Service {

    private AmazonS3 s3Client;
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;


    @PostConstruct //의존성 주입 시점 @Value 어노테이션의 값이 설정되어 있지 않아서 사용
    public void setS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey); // accessKey, secretKey를 이용해 자격증명 객체를 얻음

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)) // 자격증명을 통해 s3 Client를 가져옴
                .withRegion(this.region) //region설정
                .build();

    }

    public String upload(MultipartFile file) {

        String uploadFileName = getUuidFileName(file.getOriginalFilename());
        try{
            //업로드 하기 위해 사용되는 함수
            s3Client.putObject(new PutObjectRequest(bucket, uploadFileName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); //외부에 공개할 이미지이므로, public read권한 추가
            return s3Client.getUrl(bucket, uploadFileName).toString(); // 업로드한 후 해당 URL을 DB에 저장할 수 있도록 컨트롤러로 URL반환
        }catch(IOException e){
            log.debug("file upload fail", e);
            throw new GlobalException(ErrorCode.S3BUCKET_ERROR);
        }
    }

    public String deleteFile(String imageUrl){
        String result = "success";
        try {
            // https://webide-six.s3.ap-northeast-2.amazonaws.com/73f613f7-62b9-428b-b91e-7d11ea75278f.jpeg
            // s3에 저장되는 이미지 url : 73f613f7-62b9-428b-b91e-7d11ea75278f.jpeg
            String filePath = getFileName(imageUrl);
            boolean isObjectExist = s3Client.doesObjectExist(bucket, filePath);
            log.debug("기존에 파일이 존재하나? : {}", isObjectExist);
            log.debug("삭제 전 파일명 : {}", filePath);

            if (isObjectExist) {
                log.debug("기존파일 s3에 존재함");
                s3Client.deleteObject(bucket, filePath);
            } else {
                result = "file not found";
            }
        }catch (Exception e){
            log.debug("Delete File faild", e);
            throw new GlobalException(ErrorCode.S3BUCKET_ERROR);
        }
        return result;
    }

    /**
     * 존재하는 객체 key명 추출
     */
    private String getFileName(String imageUrl) {
        int idx = imageUrl.lastIndexOf("/");
        return imageUrl.substring(idx + 1);
    }

    /**
     * UUID 파일명 반환
     */
    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

}
