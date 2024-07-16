## 로컬에서 개인 브랜치 생성하기
local workspace에 'feature'라는 이름으로 브랜치 생성 <br>
<code>git branch feature</code> 

## 로컬에서 브랜치 작업후 원격저장소에 반영하기
로컬 브랜치가 있는 폴더에서 개인작업을 마친 후 공동 저장소에 반영한다.

1. <code>git checkout feature</code>  - main에서 feature 브랜치로 전환
2. workspace에서 작업
3. <code>git commit -m "message"</code>
4. <code>git push origin feature</code>  - 원격저장소 feature 브랜치에 반영
5. <code>git checkout develop</code>  - 브랜치 전환
6. <code>git pull</code>  - 원격저장소 develop의 최신 정보를 로컬에 업데이트 시키기
7. <code>git merge feature</code>  - develop에 feature 브랜치 작업 반영
8. <code>git push origin develop</code>  - 원격저장소 develop에 반영

## 브랜치 흐름도 (참고)
![image](https://github.com/user-attachments/assets/f1616311-9930-4dc5-972b-33ef6c2f5a17)
 <br>

## CI/CD 흐름도 (참고)
![image](https://github.com/user-attachments/assets/16fb6acd-baac-45c3-82da-7b7fa38e2814)

