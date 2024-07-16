## 브랜치 흐름도
![image](https://github.com/user-attachments/assets/f1616311-9930-4dc5-972b-33ef6c2f5a17)
<br><br>

## 로컬에서 개인 브랜치 생성하기
local workspace에 'feature'라는 이름으로 브랜치 생성 <br>
<code>git branch -b feature</code> 
<br><br>

## 로컬에서 브랜치 작업후 원격저장소에 반영하기
로컬 브랜치가 있는 폴더에서 개인작업을 마친 후 공동 저장소에 반영한다.
<br><br>

### feature 브랜치 작업 후 develop 브랜치에 반영하기
1. <code>git checkout feature</code>  - feature 브랜치로 전환
2. workspace에서 작업
3. <code>git add .</code>
4. <code>git commit -m "message"</code>
5. <code>git push origin feature</code>  - 원격저장소 feature 브랜치에 반영
6. <code>git checkout develop</code>  - 브랜치 전환
7. <code>git pull origin develop</code>  - 원격저장소 develop의 최신 정보를 로컬에 업데이트
8. <code>git merge feature</code>  - develop에 feature 브랜치 작업 반영
9. <code>git push origin develop</code>  - 원격저장소 develop에 반영
<br><br>

### develop브랜치 작업을 feature/2 브랜치에 반영하기
1. <code>git checkout feature/2</code> - feature/2브랜치로 전환
2. <code>git pull origin develop</code> - develop브랜치의 최신 변경사항을 feature/2브랜치에 병합
<br><br>

### main브랜치에 develop브랜치 작업 반영하기
1. <code> git checkout develop</code> - develop브랜치로 전환
2. <code>git add .</code>
3. <code>git commit -m "message"</code>
4. <code>git pull origin develop</code> - 원격저장소 develop의 최신 정보를 로컬에 업데이트
5. <code>git checkout main</code> - main브랜치로 전환
6. <code>git pull origin main</code> - 원격저장소 main의 최신 정보를 로컬에 업데이트
7. <code>git merge develop</code> - main브랜치에 develop브랜치를 병합
8. <code>git push origin main</code> - main브랜치의 변경 사항을 원격 저장소에 반영
<br><br>

## CI/CD 흐름도
![image](https://github.com/user-attachments/assets/16fb6acd-baac-45c3-82da-7b7fa38e2814)
