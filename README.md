<div align="center">

# TaskFlow Server

[<img src="https://img.shields.io/badge/프로젝트 기간-2025.01.06~2025.02.20-green?style=flat&logo=&logoColor=white" />]()

</div>

## 🔎 TaskFlow


## 💁‍♂️ Team Member 
| 서주원 (PL)  |   나은비(BE)    | 박재학(BE) | 양시훈(BE) | 이규동(BE) | 최효성(INFRA & BE) |
|:---------:|:--------:|:--------:|:--------:|:--------:|:--------:|
| <img src="https://github.com/TaskFlow-CLAP/TaskFlow-Server/blob/develop/.github/image/joowojr.JPG" width="150px" >  | <img src="https://github.com/user-attachments/assets/5c59f742-8f2b-4472-bff4-d8dff350481b" width="150px"> | ![](사진)  | ![](사진)  | ![](사진)  | ![](사진)  |
| [Github](https://github.com/joowojr) | [Github](https://github.com/nano-mm) | [Github](https://github.com/parkjaehak) | [Github](https://github.com/Sihun23) | [Github](https://github.com/starboxxxx) | [Github](https://github.com/hyoseong-Choi) |

## ⚒️ Stack


### Back-end
<img src="https://img.shields.io/badge/Framework-555555?style=for-the-badge">![SpringBoot](https://img.shields.io/badge/springboot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)![spring_data_JPA](https://img.shields.io/badge/spring_data_JPA-%236DB33F?style=for-the-badge&logo=databricks&logoColor=white)![spring_security](https://img.shields.io/badge/spring_security-%236DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white)

<img src="https://img.shields.io/badge/build-555555?style=for-the-badge">![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)

<img src="https://img.shields.io/badge/Test-555555?style=for-the-badge">![junit5](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

<img src="https://img.shields.io/badge/Security-555555?style=for-the-badge">![JWT](https://img.shields.io/badge/json%20web%20tokens-323330?style=for-the-badge&logo=json-web-tokens&logoColor=pink)


### DB
<img src="https://img.shields.io/badge/DB Migration-555555?style=for-the-badge">![Flyway](https://img.shields.io/badge/Flyway-F7B500?style=for-the-badge&logo=flyway&logoColor=white)

### Infra
<img src="https://img.shields.io/badge/CI/CD-555555?style=for-the-badge">![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

### Database
<img src="https://img.shields.io/badge/Database-555555?style=for-the-badge">![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![Elastic Search](https://img.shields.io/badge/Elastic_Search-005571?style=for-the-badge&logo=elasticsearch&logoColor=white)

## 🏗️ Architecture
Coming Soon...

## 📍ERD
![taskflow-erd.png](/.github/image/tf-erd.png)


##  Task Flow 아키텍쳐
![taskflow-architecture.png](/.github/image/tf-architecture.png)

### 🔴 Domain

- 애플리케이션의 **핵심 비즈니스 로직**을 구현하는 계층입니다.
- 모델과 비즈니스 규칙을 정의하여, 애플리케이션의 주요 기능을 수행하며 외부와 의존성이 없습니다.

---

### 🟢Application

- 비지니스 로직 **액션**을 담당하는 서비스 계층입니다.

<aside>
➡️ Inbound Port

- 외부에서 입력을 받는 인커밍 역할 **Usecase**를 담당합니다.
- Inbound adapter로 부터 입력받습니다.
- 입력 유효성 검증을 하지 않고 그 책임은 오직 웹 계층에서 진행합니다.
</aside>

<aside>
➡️ Outbound Port

- adapter의 outbound로 가는 포트의 엳할을 합니다
- Port 인터페이스를 통해 영속성 계층과 의존성 역전을 통해서 데이터를 저장, 조회, 목록조회 등 역할을 수행합니다.
</aside>

---

### 🔵 Adapter

- 외부 요청을 내부로 전달하는 Inbound 어댑터와 내부 요청을 외부로 전달하는 Outbound 어댑터로 나뉩니다.
- 애플리케이션의 핵심 로직과 외부 시스템 간의 통신을 중개하는 역할을 합니다.

<aside>
➡️ Inbound Port

- **Web**
    - 사용자 인터페이스 또는 REST API를 처리하는 컨트롤러
    - 입력 유효성 검증을 진행
</aside>

<aside>
◀️ Outbound Port

- **Persistence**
    - 데이터베이스와의 상호작용을 위한 orm 데이터베이스 매핑 및 repository
- **Api**
    - 외부 서비스와의 통신을 위한 클라이언트

- **Infrastructure**
    - 데이터베이스, 외부 API, 파일 시스템 등 외부 세계와의 연결을 처리
    - 다른 레이어에 대해 의존하지 않습니다.
</aside>



## 🗂️ Package

```
├── 💽 TaskflowApplication
├── 🗂️adapter
│   ├── 🗂️inbound
│   │   └── 📂web
│   │       ├── admin
│   │       ├── auth
│   │       └── dto
│   │          
│   └──  🗂️outbound
│       ├── 📂api
│       ├── 📂infrastructure
│       └── 📂️security
│       └── 📂persistense
│           ├── entity
│           │   ├── common
│           │   ├── log
│           │   ├── member
│           │   ├── notification
│           │   └── task
│           ├── mapper
│           │   └── common
│           └── repository
│               ├── log
│               ├── member
│               ├── notification
│               └── task
├── 🗂️application
│   ├── 🗂️mapper
│   └── 🗂️port
│       ├── 📂inbound
│       └── 📂outbound
├── 🗂️common
│   └── 🗂️annotation
│   ├── 🗂️exception
│   └── 🗂️response
├── 🗂️config
├── 🗂️domain
│   ├── 🗂️model
│       ├── 📂common
│       ├── 📂log
│       ├── 📂member
│       ├── 📂notification
│       └── 📂task
└── 🗂️exception
```

## 📜 Covention
### Code Convetion

| Type | Naming Convention |
|:----:|:---------------:|
| Class | PascalCase |
| Function | camelCase |
| Variable | camelCase |
| DB Table | snake_case |
| ENUM, Constant | PascalCase |

### Prefix
| Prefix | Description |
|:------:|:----------:|
| `feat` | 새로운 기능 구현 |
| `fix` | 버그 수정 |
| `refactor` | 코드 리팩토링 |
| `chore` | 프로젝트 설정 수정 |
| `docs` | 문서 수정 |
| `infra` | 인프라 관련 작업 |
| `hotfix` | 긴급 수정 작업 |

### Branch Naming
<Jira_Issue_Number>

### Commit Message
<Jira_Issue_Number> [Prefix] : <Description>
