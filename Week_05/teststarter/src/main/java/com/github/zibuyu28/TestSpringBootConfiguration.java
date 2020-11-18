package com.github.zibuyu28;


import com.github.zibuyu28.klass.Klass;
import com.github.zibuyu28.klass.School;
import com.github.zibuyu28.klass.Student;
import com.github.zibuyu28.prop.TestSpringBootPropertiesConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestSpringBootPropertiesConfiguration.class)
public class TestSpringBootConfiguration {

    private final TestSpringBootPropertiesConfiguration props;

    public TestSpringBootConfiguration(TestSpringBootPropertiesConfiguration props) {
        this.props = props;
    }

    @Bean(name = "klass1")
    public Klass klass() {
        Klass klass = new Klass();
        klass.setClassName(props.getClassName());
        return klass;
    }

    @Bean
    public School school() {
         return new School();
    }

    @Bean
    public Student student() {
        return new Student();
    }

}
