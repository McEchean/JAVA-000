package com.github.zibuyu28.router;

import com.github.zibuyu28.router.endpoint.EndpointProviderFactory;
import com.github.zibuyu28.router.endpoint.EndpointsProvider;
import com.github.zibuyu28.router.endpoint.EtcdConfigEndpointsProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

@Component
public class RandomEndpointRouter implements HttpEndpointRouter {
    private static final Logger log = LoggerFactory.getLogger(RandomEndpointRouter.class);

//    public static class F {
//        private static RandomEndpointRouter INSTANCE = new RandomEndpointRouter();
//    }
//
//    public static RandomEndpointRouter getInstance() {
//        return F.INSTANCE;
//    }


    @Autowired
    private EndpointProviderFactory providerFactory;

    @Override
    public String getEndPoint() throws Exception {
        List<String> endpoint = providerFactory.getProvider().availableEndpoints();
        if(endpoint.size() == 0) {
            throw new RuntimeException("no available endpoint");
        }
        Random random = new Random();
        int index = random.nextInt(endpoint.size());
        log.debug("random index {}", index);
        return endpoint.get(index);
    }
}
