package com.genebio.nextprot.aop;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.genebio.nextprot.aop.annotation.NotNullArg;

/**
 * @author Daniel Teixeira
 * @version $Revision$, $Date$, $Author$
 */
@Aspect
public class ServiceValidation {

	/**
	 * Checks that a notnull argument is not null
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.genebio.nextprot.service.PublicationService.*(..))")
	public Object checkNotNullArgs(ProceedingJoinPoint pjp) throws Throwable {

		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
		Object[] arguments = pjp.getArgs();

		for (int i = 0; i < annotations.length; i++) {
			for (Annotation parameterAnnotation : annotations[i]) {
				if (parameterAnnotation.annotationType().equals(NotNullArg.class)) {
					if (arguments[i] == null)
						throw new IllegalArgumentException("Argument " + i + " is null on " + methodSignature.getName());
				}
			}
		}

		return pjp.proceed(arguments);
	}
}