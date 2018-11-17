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

***
* 어떤 메소드가 다른 트랜잭션의 일부로 참여하려면 매번 복사해서 참여해야 하기 때문에 중복되는 코드가 발생함.
    * EX) 이벤트 신청 작업에 사용자 추가 작업이 종속 될 경우.
* 선언적 트랜잭션은 트랜잭션 전파 기법을 이용해 불필요한 코드 중복이 일어나지 않는다. 

### 6.8.2 트랜잭션 동기화와 테스트

> * 프로그램에 의한 트랜잭션
> ```java
> DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
> txDefinition.setReadOnly(true);
> TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
> 
> userService.deleteAll();
> 
> userService.add(users.get(0));
> userService.add(users.get(1));
> 
> transactionManager.commit(txStatus);
> ```
#### 1. 트랜잭션 매니저와 트랜잭션 동기화
* 트랜잭션 추상화 기술의 핵심은 <b>트랜잭션 매니저</b>와 <b>트랜잭션 동기화</b> 이다.
* 트랜잭션 동기화 덕분에 트랜잭션 전파 속성을 적용할 수 있음.
* 테스트에서는 지금까지 진행했던 작업이 한 곳에서 일어난다.

#### 2. 트랜잭션 매니저를 이용한 테스트용 트랜잭션 제어
* 세 개의 메소드 모두 트랜잭션 전파 속성이 REQUIRED이므로 트랜잭션이 호출되고 메소드가 호출되면 하나의 트랜잭션이 성립한다.

#### 3. 트랜잭션 동기화 검증
* 트랜잭션 속성을 변경해 동기화를 검증해보자
```java
DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
txDefinition.setReadOnly(true);
TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

userService.deleteAll(); // <- 읽기 전용 속성을 위반, 예외가 발생
//...
transactionManager.commit(txStatus);
```

### 4. 롤백 테스트
* 복잡한 데이터를 바탕으로 동작하는 기능을 테스트하려면 DB 데이터와 상태가 매우 중요하다.
* 테스트에 의해 데이터가 바뀐다면 다음 테스트에 영향을 미칠 수 있다.
* 데이터를 조작하여 테스트 후 데이터를 롤백한다면 동시에 여러개의 테스트 진행을 보장한다.
 ```java
@Test
public void transactionRollback() {
    DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
    TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

    try {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }
    finally {
        transactionManager.rollback(txStatus);
    }
}
```

### 6.8.3 테스트를 위한 트랜잭션 애노테이션
#### 1. Transactional
* 테스트에도 @Transactional 을 적용하면 자동적으로 트랜잭션 경계가 설정된다.
```java
@Test
@Transactional(readOnly = true /*or false*/ )
public void transactionAnnotationSyncTest() {
    userService.deleteAll();
    userService.add(users.get(0));
    userService.add(users.get(1));
}
```

#### 2. Rollback
* @Transactional 은 디폴트 속성으로 지정되고 테스트가 끝나면 자동으로 롤백된다.
* 테스트에 트랜잭션은 적용하지만 롤백을 원치않는다면 @Rollback(false)를 적용한다.
```java
@Test
@Transactional
@Rollback(false)
public void transactionAnnotationNotRollbackTest() {
    userService.deleteAll();
    userService.add(users.get(0));
    userService.add(users.get(1));
}
```

#### 3. TransactionConfiguration
* Transactional 은 테스트 클래스에 사용해 모든 메소드에 일괄 적용할 수 있다.
* Rollback 은 메소드 레벨만 적용할 수 있다.
* TransactionConfiguration 을 이용하면 모든 메소드에 트랜잭션을 적용하면서 롤백되지 않고 커밋되도록 할 수 있다.
```java
@ContextConfiguration("/test-applicationContext-v6.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest {
    @Test
    @Rollback
    public void add {}
    //...
}
```

#### 4. NotTransactional과 Propagation.NEVER
* 트랜잭션 설정을 무시하려면 @NotTransactional 을 사용한다.
* 하지만 스프링 3.0에서는 제거 대상이므로 @Transactional(propagation=Propagation.NEVER)을 사용하는 것을 권장한다.

#### 5. 효과적인 DB 테스트
* 테스트 내에서 트랜잭션을 제어하는 네 가지 애노테이션을 잘 활용하면 편리하다.
    * @Transactional, @Rollback, @TransactionConfiguration, @NotTransaction
* DB가 사용되는 통합 테스트는 클래스에 @Transactional을 사용해 롤백테스트로 만드는 게 좋다.