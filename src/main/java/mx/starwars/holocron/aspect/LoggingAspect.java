package mx.starwars.holocron.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspecto de log que intercepta la ejecución de métodos dentro del paquete
 * mx.starwars.holocron
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("execution(* mx.starwars.holocron..*(..))")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut() && !execution(* mx.starwars.holocron.repository..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("-> Entrando: {} con argumentos  = {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        logger.info("<- Saliendo: {} con resultado = {}", joinPoint.getSignature(), result);
        return result;
    }
}