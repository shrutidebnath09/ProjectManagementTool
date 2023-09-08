package com.taskmanager.dal;

import java.lang.reflect.Constructor;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.taskmanager.config.AppConfig;

/**
 * Builder for entity managers
 * 
 */
public class EntityManagerBuilder {

	private static final EntityManagerBuilder entityManagerBuilder = new EntityManagerBuilder();

	private static final String DRIVER = "javax.persistence.jdbc.driver";
	private static final String URL = "javax.persistence.jdbc.url";
	private static final String USER = "javax.persistence.jdbc.user";
	private static final String PASSWORD = "javax.persistence.jdbc.password";
	private static final String DDL_GENERATION = "eclipselink.ddl-generation";
	private static final String DDL_GENERATION_MODE = "eclipselink.ddl-generation.output-mode";

	private final EntityManagerFactory managerFactory;
	private final EntityManager entityManager;

	private EntityManagerBuilder() {
		final AppConfig appConfig = AppConfig.getInstance();
		final Properties properties = getPropertiesFromAppConfig(appConfig);
		this.managerFactory = Persistence.createEntityManagerFactory(
				appConfig.getPersistenceUnitName(), properties);
		this.entityManager = managerFactory.createEntityManager();
	}

	public static EntityManagerBuilder getEntityManager() {
		return entityManagerBuilder;
	}

	@SuppressWarnings("unchecked")
	public <T> T createEntityManager(Class<T> className) {
		try {
			Class<?> classObj = Class.forName(className.getName());
			Constructor<?> constructor = classObj
					.getConstructor(EntityManager.class);
			return (T) constructor.newInstance(entityManager);
		} catch (final Exception exc) {
			return null;
		}
	}

	private Properties getPropertiesFromAppConfig(final AppConfig appConfig) {
		final Properties properties = new Properties();
		properties.put(DRIVER, appConfig.getDriver());
		properties.put(URL, appConfig.getUrl());
		properties.put(USER, appConfig.getUser());
		properties.put(PASSWORD, appConfig.getPassword());
		properties.put(DDL_GENERATION, appConfig.getDdlGeneration());
		properties.put(DDL_GENERATION_MODE, appConfig.getDdlGenerationMode());
		return properties;
	}

}
