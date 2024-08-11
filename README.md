# 멋쟁이 사자처럼 12기 중앙 해커톤 
<img src="https://github.com/user-attachments/assets/591d445b-42ff-4847-9848-96691696c10e" width="1000" />
 

## wholeRest Web Project ~~(On Progress)~~
>**팀명** : 대문자 I들    
>**개발 기간** : 20240705 - 20240807  
>**배포된 주소** : https://wholerest.site  
>**프로젝트 소개** : [GoogleDrive](https://drive.google.com/file/d/1oT7_dztIgNlCxKRJ2CCLz4HhVG7WUeeY/view?usp=drive_link)  
>**API 명세서** : [Notion 페이지 보기](https://noisy-factory-102.notion.site/WholeRest-API-2872e7cccb8144c1ba4aab52cd637f80)

## 팀원소개 
### PM / Design 
🎨 비주얼디자인 전공 유서진 

### Backend  
|<img src="https://avatars.githubusercontent.com/u/108880488?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/113746577?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/163099474?v=4" width="150" height="150"/>|
|:-:|:-:|:-:|
|[@Oyisbe](https://github.com/Oyisbe) 컴퓨터공학과 김온유  |[@kimjy0117](https://github.com/kimjy0117) 소프트웨어학과 김주영 |[@E2YunJeong](https://github.com/E2YunJeong) 전자공학과 이윤정 | 

### Frontend
|<img src="https://avatars.githubusercontent.com/u/162791828?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/147697405?v=4" width="150" height="150"/>|
|:-:|:-:|
|[@soojinsong](https://github.com/soojinsong) 글로벌비지니스어학부 송수진 |[@kimgabin321](https://github.com/kimgabin321)소프트웨어학과 김가빈 |


## 시작 가이드
#### Backend Requirements 
- **Spring Framework**: 3.3.1
- **JDK**: 21
- **Build Tool**: Gradle
- **Database** : Amazon RDS (배포 환경 : Mysql) / H2 (개발 환경)

**IntelliJ IDEA** : MainApplication Run 버튼 실행 or 아래 커맨드를 통해 빌드 & 실행
```
git clone https://github.com/wholeRest/wholeRest_be.git
cd wholeRest_be
./gradlew build
./gradlew bootRun
```

#### Frontend Requirements 
작성중.. 

## Stacks
#### Design (UI/UX) 
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)  ![Styled Components](https://img.shields.io/badge/Styled%20Components-DB7093?style=for-the-badge&logo=styled-components&logoColor=white)

#### Environment
![VS Code](https://img.shields.io/badge/VS%20Code-007ACC?style=for-the-badge&logo=visual-studio-code&logoColor=white)  ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)  ![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)    ![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)

#### Frontend 
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)  ![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)  ![Vite](https://img.shields.io/badge/Vite-4B32C3?style=for-the-badge&logo=vite&logoColor=white)  ![Material-UI](https://img.shields.io/badge/Material--UI-0081CB?style=for-the-badge&logo=material-ui&logoColor=white) ![axios](https://img.shields.io/badge/axios-007ACC?style=for-the-badge&logo=axios&logoColor=white)


#### Backend
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)  ![Spring Security](https://img.shields.io/badge/Spring%20Security-4A5B6D?style=for-the-badge&logo=spring-security&logoColor=white)  ![Spring Cloud AWS](https://img.shields.io/badge/Spring%20Cloud%20AWS-6DB33F?style=for-the-badge&logo=spring-cloud&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-5D8AA8?style=for-the-badge&logo=spring-data&logoColor=white)  ![Spring Mail](https://img.shields.io/badge/Spring%20Mail-9BCA8E?style=for-the-badge&logo=spring&logoColor=white)  ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)  


#### Communication
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)  ![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)

## 화면 구성 
<img src="https://github.com/user-attachments/assets/9a8116cc-f10b-445a-8881-4cbe0d06207e" width="500"/> 
<br>
<br>

주요 기능 1 : **메인페이지** (오늘의 포춘쿠키 및 인기글 리스트 제공)  

<br>
<img src="https://github.com/user-attachments/assets/411d633d-ee50-432c-a76d-827725124aac" width="500"/>
<br>
<br>
<img src="https://github.com/user-attachments/assets/be335f03-54f0-48b9-86ed-9f283655b894" width="500"/> 
<br>
<br>

주요 기능 2 : **마이페이지** (MUIX DatePicker를 통한 구간 별 일정 기록)

<img src="https://github.com/user-attachments/assets/90478f9f-fd42-41ab-a7a5-2f281951142b" width="500"/><img src="https://github.com/user-attachments/assets/6d40c04a-3fed-442a-9192-3069d034738a" width="250"/>
<br>
<br>

주요 기능 3 : **마이페이지** (Today's Diary 를 통한 상세한 감정 및 AWS S3 BUCKET 을 통한 이미지 업로드 기능 구현
<br><br>

## erd 설계
![image](https://github.com/user-attachments/assets/f97d42e3-208e-43ad-9609-84766578eaad)
<br><br>

## 깃 협업 브랜치 흐름
![image](https://github.com/user-attachments/assets/5c1a26ac-af17-4610-af23-3e3ee9f4e906)
<br><br>

## CI / CD 흐름
<img src="https://github.com/user-attachments/assets/88cf8c77-6528-4f53-9577-270e4da35a22" width="650" />


