## 1.5 스프링의 IoC
### 1.5.1 오브젝트 팩토리를 이용한 스프링 IoC
#### DaoFactory 를 스프링에서 사용하도록 변경
##### Bean : 스프링이 제어권을 가지고 직접 만들고 관계를 부여하는 오브젝트

> #### 빈 팩토리 vs 애플리케이션 컨텍스트
>       
> 1. 빈 팩토리<br>
> 빈을 생성하고 관계를 설정하는 기본 기능에 초점<br>
> 설정 정보를 자바 코드로 작성          
>  
> 2. 애플리케이션 컨텍스트<br> 
> 애플리케이션 전반에 걸쳐 모든 구성요소의 제어작업을 담당하는 IoC엔진에 초점<br>
> 설정 정보를 담고있는 무언가를 가져와 이를 활용
>
> ＊ 예제 SpringDaoFactory, SpringUserDaoTest.annotationConfigApplicationContextTest

### 애플리케이션 컨텍스트 동작 방식
> #### DaoFactory<br> 
> DAO 오브젝트(UserDao)를 생성하고 DB 생성 오브젝트(ConnectionMaker) 와 관계를 <span style="color:red">**직접**</span> 맺어주는 역할
> #### ApplicationContext
> 애플리케이션에서 <span style="color:red">**IoC를 적용해**</span> 관리할 모든 오브젝트에 대한 생성과 관계설정을 담당하는 역할.
> 직접 오브젝트를 생성하고 관계를 맺어주는 코드는 없지만 생성정보와 연관관계 정보를 별도의 설정정보를 통해 얻음.
><br>
> Application 동작 예제 : SpringApplicationContextTest - applicationContextTest


#### ※ 용어 정리

| 이름   | 설명 |
|-|-|
|빈|스프링이 IoC 방식으로 관리하는 오브젝트<br>(스프링이 직접 그 생성과 제어를 담당하는 오브젝트)|
|빈 팩토리|빈을 등록하고, 생성하고, 조회해 돌려주고, 구 외에 부가적인 빈을 관리하는 기능을 담당하는 오브젝트       |
|애플리케이션 컨텍스트|빈 팩토리를 확장한 IoC 컨테이너. 기본적인 기능은 빈 팩토리와 동일하나 각종 부가 서비스를 추가로 제공|
|설정정보/설정 메타정보|애플리케이션 컨텍스트/빈 팩토리가 IoC를 적용하기 위해 사용하는 메타정보|
|컨테이너/IoC 컨테이너|IoC 방식으로 빈을 관리한다는 의미에서 애플리케이션 컨텍스트나 빈 팩토리를 컨테이너라고 부름|
|스프링 프레임워크|IoC 컨테이너, 애플리케이션 컨텍스트를 포함해 스프링이 제공하는 모든 기능을 통틀어 제공해주는 프레임워크
|

## 1.6 싱글톤 레지스트리와 오브젝트 스코프

##### DaoFactory와 ApplicationContext 를 통해 만들어진 객체는 중요한 차이점이 있다.

> 오브젝트의 동일성과 동등성
> 동일성 비교 연산 "=="  (메모리상에 존재)
> 동등성 비교 연산 "equals"  (값만 같은 경우)

DaoFactory 의 경우 동등한 빈이 조회되지만 ApplicationContext 의 경우 동일한 빈이 조회된다. (참조 : SpringUserDaoSingletonTest)
그 이유는 애플리케이션 컨텍스트는 싱글톤을 저장하고 관리하는 싱글톤 레지스트리이기 때문이다.
스프링이 사용하는 싱글톤은 디자인 패턴에서 나오는 싱글톤 패턴과 비슷하지만 구현 방법은 확연히 다르다. (참조 : UserDaoSingletonAntiExample)
그렇다면 싱글톤 레지스트리란?
스프링 컨테이너에서 싱글톤을 직접 생성하고 관리하고 공급하는 역할을 한다. 그래서 컨테이너에서 private 생성자가 아닌 평범한 자바 클래스를 사용해서 만들고 관리한다. 이런 역할을 해주는 오브젝트를 싱글톤 레지스트리라고 한다.
하지만 싱글톤은 멀티스레드 환경에서 동시에 접근할 수 있기 때문에 상태 관리에 주의를 기울여야 한다. (참조 : UserDaoSingletonSharedAntiExample)

#### 스프링의 빈 스코프
스프링이 관리하는 오브젝트에는 적용되는 범위가 존재하고 이것을 빈의 스코프라고 한다. (참조 : beanScopeTest)

## 1.7 의존관계 주입
#### 제어의 역전과 의존관계 주입

IoC 는 느슨하게 정의되어 폭넓게 사용되는 용어. IoC 컨테이너는 스프링이 제공하는 기능의 특징을 명확히 설명하지 못함.
스프링이 제공하는 IoC 방식의 핵심을 짚어주는 의존관계 주입이라는 이름이 등장. (IoC 컨테이너 -> DI 컨테이너)

> 의존관계란?
> 만약 A, B 라는 두 클래스가 있다면, 그리고 A가 B에 의존(사용)한다면 B가 변하면 A에 영향을 미친다는 의미 (반면 B는 A에 영향을 미치지 않음 - 방향성이 있음)

> 의존관계 주입
> 구체적인 의존 오브젝트 (런타임 시에 의존관계를 맺는 대상) 와 그것을 사용할 주체, 보통 클라이언트라고 부르는 오브젝트를 런타임 시에 연결해주는 작업.<br>
> ※ 다음 세가지를 반드시 충족
> 1. 인터페이스에만 의존 (런타임 시점의 의존관계는 드러나지 않음)
> 2. 런타임 시점의 의존관계는 컨테이너나 팩토리 같은 제 3의 존재가 결정
> 3. 의존관계는 사용할 오브젝트에 대한 레퍼런스를 외부에서 제공(주입)해줌으로써 만들어진다.

참조 : UserDaoV3_2Interface, UserDaoV3_3RelationSeparation, UserDaoTestV3_RelationSeparation

###### **의존관계 주입의 핵심은 설계 시점에 알지 못했던 두 오브젝트의 관계를 맺도록 도와주는 제 3의 존재가 있다는 것.**
==애플리케이션 컨텍스트, 빈 팩토리, IoC 컨테이너 등은 모두 외부에서 오브젝트 사이의 런타임 관계를 맺어주는 제 3의 존재==

#### 의존관계 검색

의존관계를 맺는 방법이 외부(제 3의 존재)로부터의 주입이 아니라 스스로 검색을 이용하는 방법

참조 : UserDaoV4_DependencyLookup

#### 의존관계 주입의 응용
Dao 가 DB를 얼마나 많이 연결해서 사용하는지 파악하고 싶다면 어떻게 할 것인가?

> userDao 내부에 makeConnection 메소드를 호출하는 부분에 일일이 카운팅 메소드를 추가한다면 낭비이다.
중요한 건 DB 연결횟수를 세는 건 Dao 의 관심사항이 아님.
해결 방법 : DAO <-> DB 커넥션 사이에 연결횟수를 카운팅하는 오브젝트를 더 추가
UserDao -> CountingConnectionMaker -> DConnectionMaker

참조 : CountingConnectionMakers, CountingDaoFactory, UserDaoConnectionCountingTest

#### 메소드를 이용한 의존관계 주입

##### 수정자 메소드를 이용한 주입
생성자를 사용하는 것 보다 수정자 메소드를 더 많이 사용함.

##### 일반 메소드를 이용한 주입
여러개의 파라미터를 전달하고 싶을 때 이용

참조 : UserDaoV5_MethodInjection, DaoFactoryV3_MethodInjection

## XML 을 이용한 설정

의존정보를 일일이 자바 코드로 만들어주는게 번거로울 수 있음.(대부분 틀에박힌 구조)
또한 DI 구성이 바뀔 때 마다 자바코드를 수정하고 클래스를 다시 컴파일 하는 것도 귀찮은 작업임.
XML 을 지원함.

#### XML 설정 방법
하나의 @Bean 메소드를 통해 얻을 수 있는 빈의 DI 정보는 세가지
> 빈의 이름 : @Bean 메소드 이름 - getBean() 에서사용
> 빈의 클래스 : 빈 오브젝트를 어떤 클래스를 이용해서 만들지 결정
> 빈의 의존 오브젝트 : 빈의 생성자나 수정자 메소드를 통해 의존 오브젝트를 넣어줄 수 있음. (하나 이상일 수 있음)

※ 대응 항목 표

||자바 코드 설정정보|XML 설정 정보|
|--------|--------|---- |
|빈 설정 파일|@Configuration|`<beans>`|
|빈의 이름|@Bean methodName|`<bean id="methodName">`|
|빈의 클래스|return new BeanClass()|class="a.b.c...BeanClass"|

참조 : applicationContext.xml

#### XML 을 이용하는 애플리케이션 컨텍스트
참조 : SpringUserDaoTest - genericXmlApplicationContextTest

#### DataSource 인터페이스로 변환
DB 커넥션을 가져오는 추상화된 DataSource 인터페이스가 존재한다.
참조 : UserDaoV6_DataSource

##### XML 설정 방식으로 변경
참조 : applicationContextV2_DataSource



