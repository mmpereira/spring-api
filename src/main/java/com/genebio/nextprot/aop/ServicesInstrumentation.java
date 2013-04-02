package com.genebio.nextprot.aop;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Aspect responsible to instrument the actions of the users
 * @author Daniel Teixeira
 * @version $Revision$, $Date$, $Author$
 */
@Aspect
@ManagedResource("com.genebio.nextprot:name=ServicesInstrumentation")
public class ServicesInstrumentation {
	
	private static final Log LOGGER = LogFactory.getLog(ServicesInstrumentation.class);
	
	private boolean enableInstrumentation = true;

	/**
	 * Logs information
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.genebio.nextprot.service.*.*(..))")
	public Object logInformaton(ProceedingJoinPoint pjp) throws Throwable {
		
		if(enableInstrumentation){

			MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
			Object[] arguments = pjp.getArgs();
			
			StringBuilder builder = new StringBuilder();

			int i=0;
			for (Object o : arguments){
				Annotation[] annots = annotations[i];
				boolean foundValue = false;
				for(Annotation a : annots){
					if(!foundValue && a.annotationType().equals(Value.class)){
						Value v = (Value) a;
						builder.append(v.value());
						foundValue = true;
					}
				}

				i++;
				if(!foundValue){
					builder.append("arg" + i);
				}

				builder.append("=" + ((o!=null) ? o.toString() : "null"));
				builder.append(";");
			}

			long start = System.currentTimeMillis();
			Object result =  pjp.proceed(arguments);
			StringBuilder sb = new StringBuilder();
			
			sb.append("class=" +  methodSignature.getDeclaringType().getSimpleName() + ";");
			sb.append("method=" +  methodSignature.getName() + ";");
			sb.append("time=" +  (System.currentTimeMillis() - start) + ";");
			sb.append("method=" +  methodSignature.getName() + ";");
			sb.append(builder.substring(0, builder.length()-1));
			
			System.out.println(sb.toString());
			//LOGGER.info(s);
			return result;

		} else {
			
			return pjp.proceed();

		}

	
		
	}
	
	
	
	@ManagedAttribute
	public boolean getInstrumentationEnabled(){
		return enableInstrumentation;
	}
	
	@ManagedAttribute
	public void setInstrumentationEnabled(boolean enableInstrumentation){
		this.enableInstrumentation = enableInstrumentation;
	}
}