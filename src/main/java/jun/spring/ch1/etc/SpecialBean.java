package jun.spring.ch1.etc;

import jun.spring.ch1.etc.annotation.SpecialAnnotation;

@SpecialAnnotation
public class SpecialBean {
    public SpecialBean() {
        System.out.println("Special Bean 생성");
    }
}
