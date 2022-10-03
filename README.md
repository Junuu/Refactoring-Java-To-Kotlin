# Refactoring-Java-To-Kotlin
로그인 + 게시글로 구성된 자바 프로젝를 리팩토링 및 기능 고도화

### TodoList
- [x] 코틀린으로 테스트 코드 작성
- [x] 도메인 코틀린으로 변경
- [x] Repository 코틀린으로 변경
- [x] Service 코틀린으로 변경
- [ ] DTO 코틀린으로 변경
- [ ] 나머지 부분 코틀린으로 변경
- [ ] 멀티모듈을 도입하여 레이어 분리하기
- [ ] 캐시 도입 (글로벌 캐시)
- [ ] Github Action기반으로 CI/CD 도입 - ktlint, sonarqube
- [ ] 부하 테스트 및 모니터링
- [ ] MySQL Master-Slave 구조 만들어보기(또는 DB를 Mysql대신 PostgresSQL으로 마이그레이션)
- [ ] 선착순 이벤트처럼 순간 많은 접속자가 들어온 상황 재현해서 Message Queue로 해결해보기
- [ ] Blue/Green 배포 및 오토 스케일링으로 트래픽에 따른 서버 조절 (쿠버네티스, nginx, 로드밸런싱 필요할 것 같음)
- [ ] 검색을 위한 Elastic Search 도입해보기
- [ ] DDD 공부하고 적용해보기
- [ ] 스프링 클라우드로 MSA 구축
- [ ] WebFlux + 코루틴 도입(새로운 프로젝트로 진행할 수도 있을 것 같음)