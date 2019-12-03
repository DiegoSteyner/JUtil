package jutil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Classe para mapeamento de colunas de SQL para campos Java
 * 
 * @author Diego Steyner
 */
public @interface JdbcColumnOf 
{
	/**
	 * O nome da coluna na SQL
	 * @return O nome da coluna na SQL
	 */
	public String columnName();

}
