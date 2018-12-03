## 7.3 서비스 추상화 적용
> 서비스 추상화를 통한 **JaxbXmlSqlReader** 개선 및 발전<br>
> JAXB 외에도 XML을 오브젝트로 변환하는 다양한 Object - XML Mapping 기술이 있다. (OXM 서비스 추상화)<br> 
> XML 파일을 클래스패스, 파일, HTTP 프로토콜 등 다양한 곳에서 가져올 수 있다. (Resource 서비스 추상화)<br>

### 7.3.1 OXM 서비스 추상화
> XML과 자바오브젝트를 매핑해서 상호 변환해주는 기술이 OXM 이다.<br>
> Jaxb, Castor XML, JiBX, XmlBeans, Xstream 등.. 의 기술은 모두 XML 을 Object 로 변환하려는 목적을 가지고 있다.<br>
> 여러 기술이 존재한다 -> 서비스 추상화를 이용해 자유롭게 기술을 바꾸어 적용할 수 있다.

#### 7.3.1.1 OXM 서비스 인터페이스
> SqlReader 는 XML을 자바 오브젝트로 변환하는 **Unmarshaller** 를 이용한다. <br>

`Unmarshalling : 컴퓨터 과학에서 언 마샬링은 저장 또는 전송에 사용 된 객체의 표현을 실행 가능 객체의 표현으로 변환하는 프로세스를 의미한다. - wiki`

```java
public interface Unmarshaller {
    //...
    // Source를 통해 제공받은 XML을 자바 오브젝트 트리로 변환해서 루트 오브젝트를 반환한다.
    Object unmarshal(Source source) throws IOException, XmlMappingException;
}
```
- Unmarshaller 인터페이스를 구현한 다섯가지 클래스가 있고 해당 기술에 필요로하는 추가정보를 빈 프로퍼티로 지정할 수 있다. 

#### 7.3.1.2 JAXB 구현 테스트
> JAXB 를 이용하도록 만들어진 Unmarshaller 구현 클래스는 Jaxb2Marshaller 이다.<br>
> XML 설정 파일을 만들고 JAXB 언마샬러를 등록하고 사용할 수 있다.<br>
> OXM 서비스 추상화를 사용하면 어디에서도 JAXB라는 구체적인 기술에 의존하는 부분이 없다.<br>
```xml
<bean id="unmarshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
    <property name="contextPath" value="springbook.user.sqlservice.jaxb"/>
</bean>
```
```java
// 서비스 추상화 전
String contextPath = Sqlmap.class.getPackage().getName();
JAXBContext context = JAXBContext.newInstance(contextPath);
javax.xml.bind.Unmarshaller sqlUnmarshaller = context.createUnmarshaller();
Sqlmap sqlmap = (Sqlmap) sqlUnmarshaller.unmarshal(getClass().getClassLoader().getResourceAsStream("sqlmap.xml"));

// 서비스 추상화 후 (JAXB 기술에 의존하는 부분이 없다)
Source xmlSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("sqlmap.xml"));
Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);
```

#### 7.3.1.3 Castor 구현 테스트
> OXM 서비스 추상화를 이용하면 설정만 바꾸고 애플리케이션 코드는 일관되게 유지할 수 있다.
```java
@Mock
private Unmarshaller mockUnmarshaller;

@Test
public void mockUnmarshallerTest() throws IOException {
    when(mockUnmarshaller.unmarshal((Source) any())).thenReturn(getDefaultSqlMap());
    Sqlmap sqlmap = (Sqlmap) this.mockUnmarshaller.unmarshal(new StreamSource());

    checkSqlList(sqlmap);
}
```

### 7.3.2 OXM 서비스 추상화 적용
> SqlRegistry는 DI 받도록 설정, SqlReader 는 OXM 언마샬러를 이용하여 OxmSqlService 내에 고정한다.<br>
> 기존구조를 유지하고 SQL을 읽는 방법(Reader)을 OXM으로 제한하여 사용성을 극대화 하는 방법이 목적이다. <br>

#### 7.3.2.1 멤버 클래스를 참조하는 통합 클래스
> SqlReader 타입의 의존 오브젝트를 사용하되 스태틱 멤버 클래스로 내장해 자신만 사용하도록 한다.
> 밖에서는 하나처럼 보이지만 내부에서는 두개의 오브젝트가 결합되어 낮은 결합도와 높은 응집도를 만족할 수 있다.
> 이 구조를 선택한 이유는 서비스 구조의 최적화로 빈의 설정과 등록이 단순해지고 쉽게 사용할 수 있다.


![7.7 OxmSqlReader - OxmSqlService(토비의 스프링 3.1)](https://github.com/YounHyunJun/TobySpringExample/blob/master/img/7-7img.PNG)
- 자바의 스태틱 멤버 클래스를 사용하여 OxmSqlService와 OxmSqlReader는 구조적으로 강하게 결합되어 있지만 논리적으로 명확히 분리되는 구조가 되었다.
```java
public class OxmSqlSErvice implements SqlService {
    // final 이므로 변경 불가능, 강하게 결합하여 하나의 빈으로 등록되고 한 번에 설정함.
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader(); 
    // private 멤버 클래스로 정의, OxmSqlService 만 사용 가능
    private class OxmSqlReader implements SqlReader {}
}
```
- OxmSqlReader 는 marshaller 와 sqlmapFile 정보를 제공받아야 한다.
- 실제로 개발자 입장에서 많은 빈을 등록하는 것은 부담스럽기 때문에 멤버 클래스를 소유한 강한 결합 구조로 만들면 장점이 될 수 있다.
    - OxmSqlService로 등록한 빈의 프로퍼티 일부는 OxmSqlReader 프로퍼티의 창구역할을 한다.
    
![7.8 멤버 클래스를 참조하는 통합 클래스(토비의 스프링 3.1)](https://github.com/YounHyunJun/TobySpringExample/blob/master/img/7-8img.PNG)

- OxmSqlReader 의 경우 2개의 프로퍼티가 필요하기 때문에 JdbcTemplate의 Datasource와 다르다.
- 순서를 알지 못하므로 미리 오브젝트를 만들어 두고 각 수정자 메소드에서는 DI 받은 값을 넘겨준다.

#### 7.3.2.2 위임을 이용한 BaseSqlService 재사용
> BaseSqlService <-> OxmSqlService 간에 핵심 메소드(loadSql(), getSql()) 구현코드가 중복되는 것은 꺼림찍하다.
> OxmSqlService를 설정과 기본 구성을 변경해주기 위한 어댑터 같은 개념으로 BaseSqlService 앞에두는 설계를 택한다.
> OxmSqlService의 외형적인 틀은 유지하고 기능 구현은 BaseSqlService로 위임한다. 

![7.9 위임을 통한 BaseSqlService의 재사용(토비의 스프링3.1)](https://github.com/YounHyunJun/TobySpringExample/blob/master/img/7-9img.PNG)

```java
public class OxmSqlService implements SqlService {
    private final BaseSqlService baseSqlService = new BaseSqlService();
    
    @PostConstruct()
    public void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);
        
        this.baseSqlService.loadSql();
    }
    
    public String getSql(String key) {
        return this.baseSqlService.getSql(key);
    }
}
```

### 7.3.3 리소스 추상화
> XML 파일 이름은 외부에서 지정할 수 있지만 클래스패스에 존재하는 파일로 위치가 제한되는 단점이 있다.
> HTTP, FTP 프로토콜이나 서블릿 컨텍스트의 상대적인 폴더의 리소스는 가져올 수 없을까?
> 여러 가지 종류의 리소스를 접근할 수 있는 통일된 방법이 필요하다. 

#### 7.3.3.1 리소스
> 외부의 리소스 정보가 필요할 때 이용하는 추상화 인터페이스
> Resource 는 스프링에서 서비스를 제공해주는 빈이 아니라 단순한 정보를 가진 값으로 취급된다. 

```java
public interface Resource extends InputStreamSource {
    // 리소스의 존재나 읽기 가능한지 여부를 확인 가능, 입력 스트림이 열려있는지 확인 가능
    boolean isExists();
    boolean isReadable();
    boolean isOpen();
    
    //..
    
    // 리소스 이름과 부가정보 확인 가능
    long lastModified();
    String getFilename();
    String getDescription();
}

public interface InputStreamSource {
    InputStream getInputStream() throws IOException;
}
```

#### 7.3.3.2 리로스 로더
> 스프링에서 문자열로 정의된 프로퍼티를 실제 Resource 타입 오브젝트로 변환해주는 인터페이스 <br>
> 접두어가 의미하는 위치와 방법을 이용해 리소스를 읽어온다. <br>

![7-1 ResourceLoader 가 처리하는 접두어의 예](https://github.com/YounHyunJun/TobySpringExample/blob/master/img/7.1table.PNG)

```java
public interface ResourceLoader {
    String CLASSPATH_URL_PREFIX = "classpath:";
    Resource getResource(String path);
    ClassLoader getClassLoader();
}
```

#### 7.3.3.3 Resource를 이용해 XML 파일 가져오기
> String 타입을 Resource 타입으로 바꾼다. <br>
> Resource 타입은 실제 소스가 어떤 것이든 상관없이 getInputStream() 메소드를 이용해 스트림으로 데이터를 가져올 수 있다. <br>

```java
private class OxmSqlReader implements SqlReader {
    // Resource 구현 클래스인 ClassPathResource를 이용해 classPath 경로의 파일을 가져온다.
    private Resource sqlmap = new ClassPathResource("sqlmap.xml");
    
    public void setSqlmap(Resource sqlmap){
        this.sqlmap = sqlmap;
    }
    
    public void read(SqlRegistry sqlRegistry){
        try {
            // 리소스의 종류에 상관없이 스트림으로 가져올 수 있다.
            Source source = new StreamSource(sqlmap.getInputStream())   
        }
        //...
    }
}
```
