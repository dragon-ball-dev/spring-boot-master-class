package com.springmasterclass.study.aspect;

import com.springmasterclass.study.anotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(distributedLock)")
    public Object handleDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 1. Phân tích cú pháp SpEL để lấy tên Lock Key động
        String lockKey = parseSpelKey(distributedLock.key(), method, args);

        // 2. Lấy đối tượng RLock từ RedissonClient
        RLock lock = redissonClient.getLock(lockKey);
        boolean isAcquired = false;

        log.info("[DistributedLock] Đang thử xin Lock cho key: {} (Wait: {}s, Lease: {}s)",
                lockKey, distributedLock.waitTime(), distributedLock.leaseTime());

        try {
            // 3. Thử lấy khóa trong khoảng thời gian waitTime
            isAcquired = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!isAcquired) {
                log.warn("[DistributedLock] Không thể lấy Lock cho key: {}. Hệ thống đang quá tải!", lockKey);
                throw new IllegalStateException("Hệ thống bận, vui lòng thử lại sau!");
            }

            log.info("[DistributedLock] Lấy Lock thành công cho key: {}", lockKey);

            // 4. Cho phép method gốc (business logic) được thực thi
            return joinPoint.proceed();

        } finally {
            // 5. Giải phóng khóa an toàn trong khối finally
            if (isAcquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[DistributedLock] Đã giải phóng Lock thành công cho key: {}", lockKey);
            }
        }
    }

    /**
     * Phương thức phân tích biểu thức SpEL (Spring Expression Language)
     * để trích xuất giá trị tham số động (Ví dụ: "#productId" -> "101")
     */
    private String parseSpelKey(String spelKey, Method method, Object[] args) {
        if (spelKey == null || spelKey.trim().isEmpty()) {
            return method.getName();
        }

        String[] parameterNames = nameDiscoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();

        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }

        return parser.parseExpression(spelKey).getValue(context, String.class);
    }
}
