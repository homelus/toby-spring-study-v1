## 6.8 트랜잭션 지원 테스트
### 6.8.1 선언적 트랜잭션과 트랜잭션 전파 속성
#### 1. 선언적 트랜잭션
- AOP를 이용해 코드 외부에서 트랜잭션의 기능을 부여해주고 속성을 지정할 수 있는 방법

> * Annotation
> ```java
> <tx:annotation-driven/>
>  
> @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
> ```

> * 네임스페이스
> ```xml
> <aop:config>
>     <aop:advisor advice-ref="transactionAdvice" pointcut="bean(*Service)"/>
> </aop:config>
> 
> <tx:advice id="transactionAdvice">
>     <tx:attributes>
>         <tx:method name="get*" read-only="true" />
>         <tx:method name="*"/>
>     </tx:attributes>
> </tx:advice>
> ```

#### 2. 프로그램에 의한 트랜잭션
```java
DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
txDefinition.setReadOnly(true);
TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

userService.deleteAll();

userService.add(users.get(0));
userService.add(users.get(1));

transactionManager.commit(txStatus);
```

***
* 어떤 메소드가 다른 트랜잭션의 일부로 참여하려면 매번 복사해서 참여해야 하기 때문에 중복되는 코드가 발생함.
    * EX) 이벤트 신청 작업에 사용자 추가 작업이 종속 될 경우.
* 선언적 트랜잭션은 트랜잭션 전파 기법을 이용해 불필요한 코드 중복이 일어나지 않는다. 

### 6.8.2 트랜잭션 동기화와 테스트
#### 1. 트랜잭션 매니저와 트랜잭션 동기화
* 트랜잭션 추상화 기술의 핵심은 <b>트랜잭션 매니저</b>와 <b>트랜잭션 동기화</b> 이다.
