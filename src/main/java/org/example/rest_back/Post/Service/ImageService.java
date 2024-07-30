package org.example.rest_back.Post.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Transactional
@Service
public class ImageService {

    private final AmazonS3 amazonS3; // S3와의 상호작용을 처리
    private final String S3_bucket; // S3의 버킷 이름을 저장

    @Autowired
    public ImageService(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String S3_bucket) {
        this.amazonS3 = amazonS3;
        this.S3_bucket = S3_bucket;
    }

    // 파일 업로드 (S3에 저장된 URL 반환, DB에 저장하기 위해 사용)
    // S3에 파일 업로드, 업로드 된 파일의 URL 반환 여기에 합치기
    // convertToFile()가 IOException를 던질 수 있음 -> throws IOException를 추가하여 예외처리
    public String uploadFileToS3(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename(); // 업로드된 파일의 원래 이름을 originalFileName에 저장

        // originalFileName이 null이 아닌지 확인
        // 만약 null이면 예외 발생
        if (originalFileName == null) {
            throw new IllegalArgumentException("파일 이름이 null입니다.");
        }

        String uuid = UUID.randomUUID().toString(); // 중복 방지를 위해 고유한 UUID 생성

        /* originalFileName가 null이 아님을 보장받은 상태에서 새로운 파일명 생성
         *  원래 파일 이름의 모든 공백을 _로 변경, UUID를 붙여 고유한 파일 이름 생성 */
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = dirName + "/" + uniqueFileName; // 파일의 고유한 전체 경로 생성 (디렉토리 명 + 고유한 파일 이름)
        File uploadFile = convertToFile(multipartFile); // MultipartFile을 File객체로 변환

        /* 변환된 파일을 S3에 업로드
         *       - PutObjectRequest을 사용하여 파일을 S3 버킷에 업로드
         *       - 업로드된 파일의 접근 권한을 PublicRead로 설정
         *       - PublicRead : 누구나 인터넷을 통해 해당 파일에 접근 가능 (읽기 권한 부여)
         *                      주로 이미지, 비디오, 공공 데이터 파일 등에 사용
         *  S3에 업로드된 파일의 URL을 uploadImageUrl에 저장 */
        amazonS3.putObject(new PutObjectRequest(S3_bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String uploadImageUrl = amazonS3.getUrl(S3_bucket,fileName).toString();

        removeTempFile(uploadFile); // 로컬에 저장된 임시파일 삭제
        return uploadImageUrl; // S3에 업로드된 파일의 URL 반환
    }

    // 파일 변환 (MultipartFile -> File)
    public File convertToFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("파일 이름이 null입니다.");
        }
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");
        /* uploadFileToS3()와 동일 */

        File convertFile = new File(uniqueFileName); // 고유한 파일 이름으로(위에서 만든 새로운 파일 이름) 새로운 File 객체 생성
        // 새로운 파일 생성에 성공한다면
        if (convertFile.createNewFile()) {
            // 파일을 쓰기 위한 스트림을 열고, 업로드된 파일의 내용을 새로운 파일에 작성
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
                // 파일 변환 중 오류 발생 시 에러메세지 출력
            } catch (IOException e) {
                System.out.println("파일 변환 중 오류 발생 : " + e.getMessage());
            }
            // File 객체로 변환된 파일을 반환
            return convertFile;
        }
        else{
            // 파일 변환에 실패 시 예외처리
            throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
        }
    }

    // 임시 파일(로컬에 저장된 파일) 삭제
    public void removeTempFile(File file) {
        if (file.delete()) {
            System.out.println("파일 삭제 성공");
        } else {
            System.out.println("파일 삭제 실패");
        }
    }

    // S3 파일 삭제
    public void deleteFileInS3(String fileName) {
        try {
            /* URL 디코딩을 통해 원래의 파일 이름을 가져옴
             *  이 때 매개변수로 받는 fileName = DB에 저장된 이미지 URL을 String타입으로 변환한 것.
             *  이에 관한 것은 postService의 deletePost함수에서 확인 가능 */
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            amazonS3.deleteObject(S3_bucket, decodedFileName); // S3 버킷에서 해당 파일 삭제
            System.out.println("Deleting file from S3: " + decodedFileName); // 파일 삭제 후 콘솔창에 출력
        } catch (UnsupportedEncodingException e) {
            // 디코딩 중 오류 발생 시 예외처리
            System.out.println("Error while decoding the file name: " + e.getMessage());
        }
    }
}
