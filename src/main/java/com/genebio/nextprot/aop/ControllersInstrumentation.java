package com.genebio.nextprot.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Aspect responsible to instrument the actions of the users
 * @author Daniel Teixeira
 * @version $Revision$, $Date$, $Author$
 */
@Aspect
@ManagedResource(objectName = "nextprot:name=ControllersInstrumentation", description="My Managed Bean", log=true,
logFile="jmx.log", persistPeriod=200, persistLocation="/tmp", persistName="bar")
public class ControllersInstrumentation {
	
	private static final Log LOGGER = LogFactory.getLog(ControllersInstrumentation.class);
	
	private boolean enableInstrumentation = true;
	
	/**
	 * Logs information
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	//
	@Around("execution(* com.genebio.nextprot.controllers.*.*(..))")
	public Object logInformaton(ProceedingJoinPoint pjp) throws Throwable {
		
		if(enableInstrumentation){

			MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			Object[] arguments = pjp.getArgs();

			Authentication a = SecurityContextHolder.getContext().getAuthentication();
		    UserDetails currentUserDetails = (UserDetails) a.getPrincipal();

			
			long start = System.currentTimeMillis();
			Object result =  pjp.proceed(arguments);
			StringBuilder sb = new StringBuilder();
			
			sb.append("class=" +  methodSignature.getDeclaringType().getSimpleName() + ";");
			sb.append("method=" +  methodSignature.getName() + ";");
			sb.append("timeStart=" +  System.currentTimeMillis() + ";");
			sb.append("timeElapsed=" +  (System.currentTimeMillis() - start) + ";");
			
			sb.append("user=" + currentUserDetails.getUsername());
			sb.append("role=" + currentUserDetails.getAuthorities().iterator().next().getAuthority());
			
			System.out.println(sb.toString());
			
			//LOGGER.info(s);
			return result;

		}else {
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