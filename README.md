## 안드로이드 세미나

- 강의 시간: 월요일 17:30
- 강의자: 정해찬(@qdrptd)



## 과제 0

### 안드로이드 스튜디오 설치하기
- 설치가 완료되어 Hello World!가 표시된 화면을 캡쳐해서 첨부해 주세요.

<img width="714" height="1467" alt="image" src="https://github.com/user-attachments/assets/9f0c25c8-4734-44f1-a0ee-56c7948b2e00" />


### Kotlin과 친해지기
- Kotlin 공식 문서를 읽고, 다른 언어에서 보지 못했던 문법이나 새로 알게 된 문법이 있다면 작성해 주세요.
- Kotlin 언어의 장점이 무엇인지도 검색해서 찾아 주세요. (gpt가 잘 합니다.)

**1. 널 안정성(Null Safety)**

  - null 문제를 문법 차원에서 방지 (? 연산자, !! 등).
  
  - ```var name: String? = null (nullable)```
  
  - ```val len = name?.length (null-safe call)```

**2. 데이터 클래스(Data Class)**

  - Java의 POJO에 비해 훨씬 짧게 작성 가능.
   ```kotlin
  data class User(val name: String, val age: Int)
   ```

**3. 람다와 고차 함수 지원**

  - 함수형 프로그래밍 지원.
  
  - ```list.filter { it > 10 }.map { it * 2 }```

**4. 확장 함수(Extension Functions)**

  - 기존 클래스를 수정하지 않고 메서드 추가 가능.
  
   ```kotlin
  fun String.addExcited(): String = this + "!!!"
  println("Hello".addExcited()) // Hello!!!
   ```

**5. 코루틴(Coroutines)**

  - 비동기/병렬 처리 간단하게 지원.
  
  - suspend 함수와 launch 블록으로 쉽게 비동기 실행 가능.

**6. 스마트 캐스트(Smart Cast)**

  - 타입 검사 후 자동 형변환.
   ```kotlin
  if (obj is String) {
      println(obj.length) // 자동으로 String으로 캐스팅됨
  }
   ```

### PR 날리기
- 본 레포지토리를 Fork한 후, assignment-0 브랜치를 파서 과제를 완료해 주세요.
- 이후 your-repository/assignment-0 -> seminar-2025-android-assignment/main으로 PR을 날려 주세요.
