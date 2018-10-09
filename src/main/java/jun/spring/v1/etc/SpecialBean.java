package jun.spring.v1.etc;

import jun.spring.v1.etc.annotation.SpecialAnnotation;

@SpecialAnnotation
public class SpecialBean {
    public SpecialBean() {
        System.out.println("Special Bean 생성");
    }
}
